package hashnode.cli.commands.list;

import picocli.CommandLine;

@CommandLine.Command(name = "list", description = "List posts, drafts, series or sessions", subcommands = {
        ListSessionsCommand.class
//        ListPostsCommand.class,
//        ListDraftsCommand.class,
//        ListSeriesCommand.class
})
public class ListCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Usage: hashnode-cli list [post|draft|series|sessions]");
    }
}