package hashnode.cli.commands.create;

import picocli.CommandLine;

@CommandLine.Command(name = "list", description = "Create posts, drafts, series or sessions", subcommands = {
    CreateSessionCommand.class,
})
public class CreateCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Usage: hashnode-cli create [post|draft|series|sessions]");
    }
}
