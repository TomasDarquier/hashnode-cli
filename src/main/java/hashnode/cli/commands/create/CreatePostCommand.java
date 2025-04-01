package hashnode.cli.commands.create;

import hashnode.cli.clients.GraphQLQueries;
import hashnode.cli.credentials.ApiKeysManager;
import hashnode.cli.credentials.SessionsManager;
import hashnode.cli.models.Edge;
import hashnode.cli.models.Node;
import hashnode.cli.models.Publications;
import picocli.CommandLine;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@CommandLine.Command(name = "post", description = "Uploads a new post to your blog")
public class CreatePostCommand implements Runnable {

    Scanner sc = new Scanner(System.in);

    @CommandLine.Option(names = {"-t", "--title"}, description = "Post main title", required = true)
    String title;
    @CommandLine.Option(names = {"--subtitle"}, description = "Post subtitle", required = false)
    String subtitle;
    @CommandLine.Option(names = {"-c", "--content"}, description = "Post markdown content", required = true)
    String blogContent;
    @CommandLine.Option(names = {"-b", "--banner"}, description = "Banner Image", required = false)
    String bannerImage;

    @Override
    public void run() {
        try {
            String apiKey = validateSession();
            if(apiKey == null) return;

            Node blog = selectBlog(apiKey);
            if(blog == null) return;

            // TODO Manage series
            boolean useSeries = true;
            String series = selectSeries(blog.url , apiKey);
            if(series == null || series.isBlank()) useSeries = false;

            // Check how to read a file from a relative path
            // Make post
        } catch (BadPaddingException e){
            System.err.println("Wrong password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String selectSeries(String host, String apiKey) {
        //TODO implement method
        return null;
    }

    private Node selectBlog(String apiKey) throws IOException {
        ArrayList<Edge> edges = GraphQLQueries.getPublications(apiKey).edges;

        if (edges.isEmpty()) {
            System.err.println("Your credential is not attached to any blog.\n" +
                    "If you have a blog, check your password or API key.");
            return null;
        }

        if (edges.size() == 1) {
            System.out.printf("You have only one blog.\n%s has been selected by default%n",
                    edges.get(0).node.url);
            return edges.get(0).node;
        }

        System.out.println("Please, select a blog:");
        for (int i = 0; i < edges.size(); i++) {
            System.out.printf("[%d] %s", i + 1, edges.get(i).node.url);
        }

        int selectedOption;
        do {
            selectedOption = sc.nextInt();
            if (selectedOption < 1 || selectedOption > edges.size()) {
                System.err.println("Please select a valid blog.");
            }
        } while (selectedOption < 1 || selectedOption > edges.size());

        return edges.get(selectedOption - 1).node;
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
