package cucucmber.demo.messageboardx.sql;

import cucumber.demo.messageboard.MessageBoard;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SQLFindAllMessagesCommand implements MessageBoard.Command {
    public DataSource ds;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        try(Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {
            List<String> messages = newArrayList();
            ResultSet rs = statement.executeQuery("select message from board order by id asc");
            while(rs.next()) {
                messages.add(rs.getString(1));
            }
            response.ok(messages);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

