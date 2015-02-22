package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.persistence.AbstractClearCommandTest;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClearCommandTest extends AbstractClearCommandTest {
    private List<String> db = new ArrayList();

    @Before
    public void setup() {
        InMemoryClearCommand command = new InMemoryClearCommand();
        command.db = db;
        this.command = command;
    }

    @Override
    protected List<String> actualMessages() {
        return db;
    }

    @Override
    protected void add(String msg) {
        db.add(msg);
    }
}