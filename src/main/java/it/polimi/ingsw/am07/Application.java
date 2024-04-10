package it.polimi.ingsw.am07;

import it.polimi.ingsw.am07.client.cli.CLI;
import it.polimi.ingsw.am07.client.gui.GUI;
import it.polimi.ingsw.am07.server.Server;

public class Application {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar <jarfile> <cli|gui|server>");
            System.exit(1);
        }

        String option = args[0];

        switch (option) {
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