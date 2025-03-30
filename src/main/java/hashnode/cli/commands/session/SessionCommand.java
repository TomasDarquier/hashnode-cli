package hashnode.cli.commands.session;

import picocli.CommandLine;

@CommandLine.Command(name = "session", description = "Manage credentials and user sessions", subcommands = {
        SessionCreateCommand.class,
//        SessionDelete.class,
        SessionLoginCommand.class,
//        SessionLogout.class
})
public class SessionCommand implements Runnable {
    @Override
    public void run() {
    }
}