package cucucmber.demo.messageboardx.sql;

import org.junit.After;
import org.junit.Before;
import cucumber.demo.messageboard.persistence.AbstractAddMessageCommandTest;

import java.sql.SQLException;
import java.util.List;

public class SQLAddMessageCommandTest extends AbstractAddMessageCommandTest {
    private DB db = new DB();

    @Before
    public void setup() throws SQLException {
        SQLAddMessageCommand command = new SQLAddMessageCommand();
        command.ds = db.pool;
        this.command = command;

        db.create();
    }

    @After
    public void cleanup() throws SQLException {
        db.destroy();
    }

    protected List<String> actualMessages() {
        return db.getMessages();
    }
}