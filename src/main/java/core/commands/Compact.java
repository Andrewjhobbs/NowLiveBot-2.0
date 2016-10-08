package core.commands;

import core.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import util.Const;
import util.database.Database;
import util.database.calls.Tracker;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Veteran Software by Ague Mort
 */
public class Compact implements Command {

    private String option;

    @Override
    public boolean called(String args, MessageReceivedEvent event) {
        String[] options = new String[]{"on", "off", "help"};

        for (String s : options) { // Iterate through the available options for this command
            if (args != null && !args.isEmpty()) {
                if (optionCheck(args, s)) {
                    this.option = s;
                    return true;
                } else if (args.equals("help")) { // If the help argument is the only argument that is passed
                    return true;
                }
            } else {
                // If there are no passed arguments
                event.getTextChannel().sendMessage(Const.EMPTY_ARGS);
                return false;
            }
        }
        // If all checks fail
        return false;
    }

    @Override
    public void action(String args, MessageReceivedEvent event) {
        Integer intArg = -1;
        switch (args) {
            case "on":
                intArg = 1;
                break;
            case "off":
                intArg = 0;
                break;
            default:
                event.getMessage().getChannel().sendMessage(Const.INCORRECT_ARGS);
                break;
        }

        if (intArg.equals(1) || intArg.equals(0)) {
            try {
                Connection connection = Database.getInstance().getConnection();
                Statement statement = connection.createStatement();

            } catch (IOException | SQLException | PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        event.getTextChannel().sendMessage("Compact mode has been turned " + this.option + ".");
    }

    @Override
    public void help(MessageReceivedEvent event) {

        event.getTextChannel().sendMessage(Const.COMPACT_HELP);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) throws PropertyVetoException, IOException, SQLException {
        Tracker tracker = new Tracker("Compact");
    }

    private boolean optionCheck(String args, String option) {
        return args.toLowerCase().substring(0, option.length()).equals(option);
    }
}
