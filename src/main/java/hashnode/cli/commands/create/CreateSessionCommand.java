package hashnode.cli.commands.create;

import hashnode.cli.credentials.ApiKeysManager;
import hashnode.cli.credentials.SessionsManager;
import picocli.CommandLine;

import java.security.Key;
import java.util.Scanner;

@CommandLine.Command(name = "session", description = "Creates a new blog session")
public class CreateSessionCommand implements Runnable {

    Scanner sc = new Scanner(System.in);

    @CommandLine.Option(names = {"-n", "--name"}, description = "Session name", required = true)
    private String sessionName;

    @CommandLine.Option(names = {"-t", "--token"}, description = "Your Hashnode Personal Access Token", required = true)
    private String apiKey;

    @Override
    public void run() {
        System.out.println("Please, enter a password to encrypt your Access Token (Don't forget it! It will be necessary to login)");
        String password = sc.nextLine();

        try {
            Key key = ApiKeysManager.generateKey(password);
            String encryptedApiKey = ApiKeysManager.encrypt(apiKey,key);
            SessionsManager.createSession(encryptedApiKey, sessionName + ".txt");
            System.out.println("Session created");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}