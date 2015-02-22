package cucucmber.demo.messageboardx.sql;

import cucumber.demo.messageboard.MessageBoard;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLClearCommand implements MessageBoard.Command {
    public DataSource ds;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        try(Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete from board");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
