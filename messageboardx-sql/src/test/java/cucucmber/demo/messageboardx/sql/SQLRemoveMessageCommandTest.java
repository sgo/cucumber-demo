package cucucmber.demo.messageboardx.sql;

import org.junit.After;
import org.junit.Before;
import cucumber.demo.messageboard.persistence.AbstractRemoveMessageCommandTest;

import java.sql.SQLException;
import java.util.List;

public class SQLRemoveMessageCommandTest extends AbstractRemoveMessageCommandTest {
    private DB db = new DB();

    @Before
    public void setup() {
        SQLRemoveMessageCommand command = new SQLRemoveMessageCommand();
        command.ds = db.pool;
        this.command = command;

        try {
            db.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        super.setup();
    }

    @After
    public void cleanup() throws SQLException {
        db.destroy();
    }

    @Override
    protected void addMessage(String msg) {
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