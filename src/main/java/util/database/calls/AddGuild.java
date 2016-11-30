package util.database.calls;

import core.Main;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static util.database.Database.cleanUp;

/**
 * @author Veteran Software by Ague Mort
 */
public class AddGuild {
    public static final Logger logger = LoggerFactory.getLogger("AddGuild");
    private static List<String> tableList = new ArrayList<>();
    private static Connection connection = Database.getInstance().getConnection();
    private static PreparedStatement pStatement;
    private static PreparedStatement pStmt;
    private static PreparedStatement pSt;
    private static ResultSet result;
    private static String query;
    private static Integer resultInt;

    public synchronized static void action(GuildMessageReceivedEvent event) {

        tableList.add("channel");
        tableList.add("game");
        tableList.add("guild");
        tableList.add("manager");
        tableList.add("notification");
        tableList.add("permission");
        tableList.add("stream");
        tableList.add("tag");
        tableList.add("team");

        for (String s : tableList) {
            try {
                connection = Database.getInstance().getConnection();
                query = "SELECT COUNT(*) AS `count` FROM `" + s + "` WHERE `guildId` = ?";

                if (connection.isClosed()) {
                    connection = Database.getInstance().getConnection();
                }
                pStatement = connection.prepareStatement(query);
                pStatement.setString(1, event.getGuild().getId());
                result = pStatement.executeQuery();

                while (result.next()) {
                    if (result.getInt("count") == 0) {
                        switch (s) {
                            case "guild":
                                connection = Database.getInstance().getConnection();
                                if (connection.isClosed()) {
                                    connection = Database.getInstance().getConnection();
                                }
                                String guildQuery = "INSERT INTO `guild` (`guildId`, `channelId`, `isCompact`, `cleanup`," +
                                        " `emoji`) VALUES (?, ?, 0, 0, ?)";
                                pStmt = connection.prepareStatement(guildQuery);
                                pStmt.setString(1, event.getGuild().getId());
                                pStmt.setString(2, event.getGuild().getId());
                                pStmt.setString(3, ":heart_eyes_cat:");
                                resultInt = pStmt.executeUpdate();
                                break;
                            case "manager":
                                List<String> userIds = new ArrayList<>();
                                // Auto add the guild owner as a manager
                                userIds.add(Main.getJDA().getGuildById(event.getGuild().getId()).getOwner().getUser().getId());
                                // Pull the roles from the guild
                                for (Role role : Main.getJDA().getGuildById(event.getGuild().getId()).getRoles()) {
                                    // Check permissions of each role
                                    if (role.hasPermission(Permission.MANAGE_SERVER) || role.hasPermission(Permission.ADMINISTRATOR)) {
                                        // See if the user in question has the correct role
                                        for (Member member : Main.getJDA().getGuildById(event.getGuild().getId())
                                                .getMembersWithRoles(role)) {
                                            // Add them to the list of authorized managers
                                            if (!userIds.contains(member.getUser().getId())) {
                                                userIds.add(member.getUser().getId());
                                            }
                                        }
                                    }
                                }

                                for (String users : userIds) {
                                    try {
                                        connection = Database.getInstance().getConnection();
                                        query = "INSERT INTO `manager` (`guildId`, `userId`) VALUES (?, ?)";
                                        if (connection.isClosed()) {
                                            connection = Database.getInstance().getConnection();
                                        }
                                        pSt = connection.prepareStatement(query);

                                        pSt.setString(1, event.getGuild().getId());
                                        pSt.setString(2, users);
                                        resultInt = pSt.executeUpdate();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } finally {
                                        cleanUp(pSt, connection);
                                    }

                                    if (Main.debugMode()) {
                                        if (resultInt > 0) {
                                            logger.info("Successfully added manager " + users + " to G:" + event.getGuild
                                                    ().getName() + ":" + event.getGuild().getId() + ".");
                                        } else {
                                            logger.warn("Failed to add manager to my database~");
                                        }
                                    }
                                }
                                break;
                            case "notification":
                                try {
                                    connection = Database.getInstance().getConnection();
                                    query = "INSERT INTO `notification` (`guildId`, `level`) VALUES (?, ?)";
                                    if (connection.isClosed()) {
                                        connection = Database.getInstance().getConnection();
                                    }
                                    pStatement = connection.prepareStatement(query);

                                    pStatement.setString(1, event.getGuild().getId());
                                    pStatement.setInt(2, 0);
                                    pStatement.executeUpdate();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } finally {
                                    cleanUp(pStatement, connection);
                                }
                                break;
                            default:
                                if (Main.debugMode()) {
                                    logger.info("No data to add to this table");
                                }
                                break;
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                cleanUp(result, pStatement, connection);
            }
        }
    }
}