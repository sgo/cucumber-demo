package cucucmber.demo.messageboardx.sql;

import cucumber.demo.messageboard.MessageBoard;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLAddMessageCommand implements MessageBoard.Command {
    public DataSource ds;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        try(Connection connection = ds.getConnection(); PreparedStatement statement = connection.prepareStatement("insert into board (message) values (?)")) {
            statement.setString(1, (String)request.body.get("message"));
            statement.executeUpdate();
            response.ok();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
