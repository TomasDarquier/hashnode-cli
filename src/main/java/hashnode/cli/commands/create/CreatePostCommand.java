package hashnode.cli.commands.create;

import hashnode.cli.clients.GraphQLMutations;
import hashnode.cli.clients.GraphQLQueries;
import hashnode.cli.credentials.ApiKeysManager;
import hashnode.cli.credentials.SessionsManager;
import hashnode.cli.models.Edge;
import hashnode.cli.models.Node;
import hashnode.cli.models.PublishPost;
import hashnode.cli.models.Series;
import picocli.CommandLine;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;

@CommandLine.Command(name = "post", description = "Uploads a new post to your blog")
public class CreatePostCommand implements Runnable {

    Scanner sc = new Scanner(System.in);

    @CommandLine.Option(names = {"-t", "--title"}, description = "Post main title", required = true)
    String title;
    @CommandLine.Option(names = {"--subtitle"}, description = "Post subtitle", required = false)
    String subtitle;
    @CommandLine.Option(names = {"-c", "--content"}, description = "Post content (file)", required = true)
    String blogContent;
    @CommandLine.Option(names = {"-b", "--banner"}, description = "Banner Image", required = false)
    String bannerImage;

    @Override
    public void run() {
        try {

            boolean subtitleExists = subtitle != null && !subtitle.isEmpty();
            boolean bannerExists = bannerImage != null && !bannerImage.isEmpty();

            String apiKey = validateSession();
            if(apiKey == null) return;

            Node blog = selectBlog(apiKey);
            if(blog == null) return;
            String publicationId = blog.getId();

            String seriesId = selectSeries(blog.getUrl(), apiKey, publicationId);
            boolean seriesExists = seriesId != null && !seriesId.isBlank();

            String content = readMdFile(blogContent);
            if(content == null) return;

            // TODO, add images functionality
            PublishPost post = GraphQLMutations.createNewPost(
                    apiKey,
                    publicationId,
                    title,
                    content,
                    subtitleExists,
                    subtitle,
                    bannerExists,
                    bannerImage,
                    seriesExists,
                    seriesId);

            System.out.println("Post successfully created!");
            System.out.println("Post Info: ");
            System.out.println("Title: " + post.getPost().getTitle());
            if(subtitleExists) System.out.println("Subtitle: " + post.getPost().getSubtitle());
            System.out.println("Url: " + post.getPost().getUrl());
            System.out.println("Read Time in Minutes: " + post.getPost().getReadTimeInMinutes() + "min");
            if(seriesExists) System.out.println("Series: " + post.getPost().getSeries().getName());
            System.out.println("Brief: " + post.getPost().getBrief());
            System.out.println("Published At: " + post.getPost().getPublishedAt());

        } catch (BadPaddingException e){
            System.err.println("Wrong password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readMdFile(String blogContent) {
        Path path = Paths.get(blogContent);
        if (!Files.exists(path)) {
            System.err.println("File does not exist");
            return null;
        }

        try {
            return Files.readString(path);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String selectSeries(String host, String apiKey, String publicationId) {
        try {
            Map<String,String> series = GraphQLQueries.getSeries(apiKey, host.substring(host.indexOf("//")+2));
            int aux = 0;
            String[] seriesNames = series.keySet().toArray(new String[0]);
            if(series.isEmpty()){
                System.out.println("No series found. Please, select an option:");
            }else {
                for(String s : seriesNames){
                    System.out.printf("[%d] %s\n", aux + 1, s);
                    aux++;
                }
            }
            System.out.printf("[%d] %s", aux++ + 1, "Create new Series\n");
            System.out.printf("[%d] %s", aux + 1, "Post blog without Series\n");

            int selectedOption;
            do {
                selectedOption = sc.nextInt();
                sc.nextLine(); //to clean the scanner
                if (selectedOption < 1 || selectedOption > series.size() + 2) {
                    System.err.println("Please select a valid Series.\n");
                }
            } while (selectedOption < 1 || selectedOption > series.size() + 2);

            if(series.isEmpty() ) {
                return selectedOption == 1 ?
                    createSeries(publicationId, apiKey) : null;
            }
            if(selectedOption > series.size()) {
                return selectedOption == series.size() + 1?
                        createSeries(publicationId, apiKey) : null;
            }
            return series.get(seriesNames[selectedOption - 1]);
        } catch (IOException e) {
            System.err.println("Error getting Series\n");
        }
        return null;
    }

    private String createSeries(String publicationId, String apiKey){
        System.out.println("Creating new Series...");
        System.out.println("Please, write de Series name:");
        String name = sc.nextLine().trim();
        String slug = name.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
        System.out.println("Slug: " + slug);
        Series createdSeries;
        try {
            createdSeries = GraphQLMutations.createNewSeries(name, slug, publicationId, apiKey);
            System.out.println(
                    "Series " + createdSeries.getName() +
                    " created at: " +
                    createdSeries.getCreatedAt()
            );
            return createdSeries.getId();
        } catch (IOException e) {
            //TODO
            //throw an exception to stop de blog post process
            System.err.println("Error creating new Series\n");
        }
        return null;
    }

    private Node selectBlog(String apiKey) throws IOException {
        ArrayList<Edge> edges = GraphQLQueries.getPublications(apiKey).getEdges();

        if (edges.isEmpty()) {
            System.err.println("Your credential is not attached to any blog.\n" +
                    "If you have a blog, check your password or API key.");
            return null;
        }

        if (edges.size() == 1) {
            System.out.printf("You have only one blog.\n%s has been selected by default%n\n",
                    edges.get(0).getNode().getUrl());
            return edges.get(0).getNode();
        }

        System.out.println("Please, select a blog:");
        for (int i = 0; i < edges.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, edges.get(i).getNode().getUrl());
        }

        int selectedOption;
        do {
            selectedOption = sc.nextInt();
            if (selectedOption < 1 || selectedOption > edges.size()) {
                System.err.println("Please select a valid blog.");
            }
        } while (selectedOption < 1 || selectedOption > edges.size());

        return edges.get(selectedOption - 1).getNode();
    }

    private String validateSession() throws Exception {
        String[] sessions = Arrays.stream(SessionsManager.getSessions()).map(s -> s.substring(0, s.indexOf(".txt"))).toArray(String[]::new);
        if (sessions.length == 0) {
            System.err.println("No sessions found. Please create a session first.");
            return null;
        }
        System.out.println("Please, choose a session from the following list:");
        for (String session : sessions) {
            System.out.println(session);
        }
        String session = sc.nextLine();
        if(!Arrays.asList(sessions).contains(session)) {
            System.err.println("Invalid session");
            return null;
        }
        System.out.println("Enter the session password");
        String password = sc.nextLine();

        Key key = ApiKeysManager.generateKey(password);
        String encryptedApiKey = SessionsManager.readSession(session + ".txt");
        return ApiKeysManager.decrypt(encryptedApiKey, key);
    }
}
