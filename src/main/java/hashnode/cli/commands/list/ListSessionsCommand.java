package hashnode.cli.commands.list;

import hashnode.cli.credentials.SessionsManager;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "sessions", description = "Get the sessions from disk")
public class ListSessionsCommand implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Sessions: ");
            String[] sessions = SessionsManager.getSessions();
            for (String session : sessions) {
                System.out.println(session.substring(0, session.indexOf(".txt")));
            }
        } catch (
                IOException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        }

    }
}