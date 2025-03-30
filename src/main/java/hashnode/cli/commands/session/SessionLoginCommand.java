package hashnode.cli.commands.session;

import hashnode.cli.credentials.ApiKeysManager;
import hashnode.cli.credentials.SessionsManager;
import picocli.CommandLine;

import java.security.Key;
import java.util.Scanner;

@CommandLine.Command(name = "login", description = "Access to a blog session")
public class SessionLoginCommand implements Runnable {

    Scanner sc = new Scanner(System.in);

    @CommandLine.Option(names = {"-n", "--name"}, description = "Session name", required = true)
    String sessionName;


    @Override
    public void run() {
        System.out.println("Please enter your password:");
        String password = sc.nextLine();

        try {
            Key key = ApiKeysManager.generateKey(password);
            String apiKey = ApiKeysManager.decrypt(
                    SessionsManager.readSession(sessionName + ".txt"),
                    key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
