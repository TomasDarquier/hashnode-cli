package hashnode.cli;

import hashnode.cli.commands.list.ListCommand;
import hashnode.cli.commands.session.SessionCommand;
import hashnode.cli.credentials.SessionsManager;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "hashnode-cli", mixinStandardHelpOptions = true, subcommands = {
        ListCommand.class,
        SessionCommand.class,
//        AddCommand.class,
//        EditCommand.class,
//        DeleteCommand.class
})
public class Main implements Runnable {
    @Override
    public void run() {
        System.out.println("Running hashnode-cli, use --help for more information.");
    }

    public static void main(String[] args) {
        try {
            SessionsManager.createSessionsPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new CommandLine(new Main()).execute(args);
    }

}