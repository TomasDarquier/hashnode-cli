package hashnode.cli.credentials;

import javax.naming.InvalidNameException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SessionsManager {

    private static final Path HOME_PATH = Paths.get(System.getProperty("user.home"));
    private static final Path HASHNODE_PATH = HOME_PATH.resolve("hashnode-cli");
    private static final Path SESSIONS_PATH = HASHNODE_PATH.resolve("sessions");

    public static void createSessionsPath() throws IOException {
        if(!Files.exists(SESSIONS_PATH)) {
            Files.createDirectory(SESSIONS_PATH);
        }
        if(!Files.exists(HASHNODE_PATH)) {
            Files.createDirectory(HASHNODE_PATH);
        }
    }

    public static String[] getSessions() throws IOException {
        try(Stream<Path> paths = Files.list(SESSIONS_PATH)) {
            return paths.
                    map(Path::getFileName).
                    map(Path::toString).
                    filter(s -> s.endsWith(".txt"))
                    .toArray(String[]::new);
        }
    }

    public static String readSession(String file) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(SESSIONS_PATH.resolve(file))) {
            StringBuilder session = new StringBuilder();
            reader.lines().forEach(session::append);
            return session.toString();
        }
    }

    public static void createSession(String session, String file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(SESSIONS_PATH.resolve(file))) {
            writer.write(session);
        }
    }

    public static void deleteSession(String session) throws IOException {
        Files.delete(SESSIONS_PATH.resolve(session));
    }
}