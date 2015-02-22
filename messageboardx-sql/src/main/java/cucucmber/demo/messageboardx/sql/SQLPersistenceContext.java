package cucucmber.demo.messageboardx.sql;

import org.h2.jdbcx.JdbcConnectionPool;
import cucumber.demo.messageboard.persistence.PersistenceContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQL persistence strategy developed from the contract tests in the messageboard module.
 * These are integration tested with an embedded database and only cover the persistence modules.
 */
public class SQLPersistenceContext extends PersistenceContext {
    public JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:./messageboard", "sa", "");

    {
        findAllMessagesCommand = new SQLFindAllMessagesCommand() {{
            ds = pool;
        }};
        addMessageCommand = new SQLAddMessageCommand() {{
            ds = pool;
        }};
        removeMessageCommand = new SQLRemoveMessageCommand() {{
            ds = pool;
        }};
        clearCommand = new SQLClearCommand() {{
            ds = pool;
        }};

        try(Connection connection = pool.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists board(id int auto_increment primary key, message varchar(255))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

