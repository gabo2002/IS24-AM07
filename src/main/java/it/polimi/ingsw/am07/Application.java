package it.polimi.ingsw.am07;

import it.polimi.ingsw.am07.client.cli.CLI;
import it.polimi.ingsw.am07.client.gui.GUI;
import it.polimi.ingsw.am07.server.Server;
import it.polimi.ingsw.am07.utils.IdentityManager;

/**
 * Main class of the application.
 */
public class Application {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar <jarfile> <cli|gui|server> [options]");
            System.out.println("Options: -refresh=True|False");
            System.exit(1);
        }

        String option = args[0];
        boolean refresh = false;

        if (args.length > 1) {
            String[] options = args[1].split("=");
            if (options.length == 2 && options[0].equals("-refresh")) {
                refresh = Boolean.parseBoolean(options[1]);
            }
        }

        if (refresh) {
            IdentityManager.clearIdentity();
        }

        switch (option) {
            // lo chiamerà il client handler
            case "cli" -> new CLI().entrypoint();
            case "gui" -> new GUI().entrypoint();
            case "server" -> new Server().entrypoint();
            default -> {
                System.out.println("Usage: java -jar <jarfile> <cli|gui|server>");
                System.exit(1);
            }
        }
    }

}