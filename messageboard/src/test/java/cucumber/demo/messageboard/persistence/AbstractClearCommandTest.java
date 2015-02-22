package cucumber.demo.messageboard.persistence;

import org.junit.Test;
import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractClearCommandTest {
    protected MessageBoard.Command command;

    @Test
    public void when_db_is_already_empty() {
        command.execute(null, null);
        assertTrue(actualMessages().isEmpty());
    }

    protected abstract List<String> actualMessages();

    @Test
    public void when_db_is_not_empty() {
        add("msg");
        command.execute(null, null);
        assertTrue(actualMessages().isEmpty());
    }

    protected abstract void add(String msg);
}
