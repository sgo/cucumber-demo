package cucucmber.demo.messageboardx.sql;

import cucumber.demo.messageboard.MessageBoard;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLRemoveMessageCommand implements MessageBoard.Command {
    public DataSource ds;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        try(Connection connection = ds.getConnection(); PreparedStatement statement = connection.prepareStatement("delete from board where message = ?")) {
            statement.setString(1, (String)request.body.get("message"));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.ok();
    }
}
