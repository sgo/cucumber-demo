package cucumber.demo.messageboard.inmemory;

import org.junit.Before;
import cucumber.demo.messageboard.persistence.AbstractRemoveMessageCommandTest;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class InMemoryRemoveMessageCommandTest extends AbstractRemoveMessageCommandTest {
    private List<String> db = newArrayList();

    @Before
    public void setup() {
        InMemoryRemoveMessageCommand command = new InMemoryRemoveMessageCommand();
        command.db = db;
        this.command = command;

        super.setup();
    }

    @Override
    protected void addMessage(String msg) {
        db.add(msg);
    }

    @Override
    protected List<String> actualMessages() {
        return db;
    }
}