package cucucmber.demo.messageboardx.sql;

import org.junit.After;
import org.junit.Before;
import cucumber.demo.messageboard.persistence.AbstractFindAllMessagesCommandTest;

import java.sql.SQLException;
import java.util.List;

public class SQLFindAllMessagesCommandTest extends AbstractFindAllMessagesCommandTest {
    private DB db = new DB();

    @Before
    public void setup() throws SQLException {
        SQLFindAllMessagesCommand command = new SQLFindAllMessagesCommand();
        command.ds = db.pool;
        this.command = command;

        db.create();
    }

    @After
    public void cleanup() throws SQLException {
        db.destroy();
    }

    @Override
    protected void add(String msg) {
        try {
            db.addMessage(msg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<String> actualMessages() {
        return db.getMessages();
    }
}