package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.persistence.AbstractFindAllMessagesCommandTest;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InMemoryFindAllMessagesCommandTest extends AbstractFindAllMessagesCommandTest {
    private List<String> db = new ArrayList<>();

    @Before public void setup() {
        InMemoryFindAllMessagesCommand command = new InMemoryFindAllMessagesCommand();
        command.db = db;
        this.command = command;
    }

    @Override
    protected void add(String msg) {
        db.add(msg);
    }

    @Override
    protected List<String> actualMessages() {
        return db;
    }
}