package cucucmber.demo.messageboardx.sql;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class DB {
    public JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");

    public void create() throws SQLException {
        try(Connection connection = pool.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists board(id int auto_increment primary key, message varchar(255))");
        }
    }

    public void destroy() throws SQLException {
        Connection connection = pool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("drop table board");
    }

    public List<String> getMessages() {
        try(Connection connection = pool.getConnection(); Statement statement = connection.createStatement()) {
            List<String> messages = newArrayList();
            ResultSet rs = statement.executeQuery("select message from board order by id asc");
            while(rs.next()) {
                messages.add(rs.getString(1));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMessage(String msg) throws SQLException {
        Connection connection = pool.getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into board (message) values (?)");
        statement.setString(1, msg);
        statement.executeUpdate();
    }
}
