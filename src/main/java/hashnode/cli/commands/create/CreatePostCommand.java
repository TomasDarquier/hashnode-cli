package hashnode.cli.commands.create;

import hashnode.cli.credentials.ApiKeysManager;
import hashnode.cli.credentials.SessionsManager;
import picocli.CommandLine;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Scanner;

@CommandLine.Command(name = "session", description = "Uploads a new post to your blog")
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
    @CommandLine.Option(names = {"-s", "--series"}, description = "Blog series", required = false)
    String series;

    @Override
    public void run() {
        try {
            String apiKey = validateSession();
            if(apiKey == null) {
                return;
            }
            // TODO
            // Create method to get the Publication path and make the user choose one
            // Check how to read a file from a relative path
            // Manage series
            // Make post
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String validateSession() throws Exception {
        String[] sessions = SessionsManager.getSessions();
        if (sessions.length == 0) {
            System.out.println("No sessions found. Please create a session first.");
            return null;
        }
        System.out.println("Please, choose a session from the following list:");
        for (String session : sessions) {
            System.out.println(session);
        }
        String session = sc.nextLine();
        if(!Arrays.asList(sessions).contains(session)) {
            System.out.println("Invalid session");
            return null;
        }
        System.out.println("Enter the session password");
        String password = sc.nextLine();

        Key key = ApiKeysManager.generateKey(password);
        String encryptedApiKey = SessionsManager.readSession(session + ".txt");
        return ApiKeysManager.decrypt(encryptedApiKey, key);
    }
}
