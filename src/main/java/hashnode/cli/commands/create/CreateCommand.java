package hashnode.cli.commands.create;

import picocli.CommandLine;

@CommandLine.Command(name = "create", description = "Create posts, drafts, series or sessions", subcommands = {
        CreateSessionCommand.class,
        CreatePostCommand.class,
})
public class CreateCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Usage: hashnode-cli create [post|draft|series|sessions]");
    }
}
