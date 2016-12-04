package util.database.calls;

import util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static util.database.Database.cleanUp;

/**
 * @author Veteran Software by Ague Mort
 */
public class SetCompact {

    private Connection connection = Database.getInstance().getConnection();
    private PreparedStatement pStatement;

    public SetCompact() {
        setConnection();
    }

    private void setConnection() {
        this.connection = Database.getInstance().getConnection();
    }

    private void setStatement(String query) {
        try {
            if (connection.isClosed() || connection == null) {
                setConnection();
            }
            this.pStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized Boolean action(String guildId, int isCompact) {
        try {
            String query = "UPDATE `guild` SET `isCompact` = ? WHERE `guildId` = ?";

            setStatement(query);
            pStatement.setInt(1, isCompact);
            pStatement.setString(2, guildId);

            if (pStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanUp(pStatement, connection);
        }
        return false;
    }

}
