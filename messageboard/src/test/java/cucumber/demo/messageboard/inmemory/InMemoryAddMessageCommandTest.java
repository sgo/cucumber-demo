package cucumber.demo.messageboard.inmemory;

import org.junit.Before;
import cucumber.demo.messageboard.persistence.AbstractAddMessageCommandTest;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InMemoryAddMessageCommandTest extends AbstractAddMessageCommandTest {
    private List<String> db = new ArrayList<>();

    @Before
    public void setup() {
        InMemoryAddMessageCommand command = new InMemoryAddMessageCommand();
        command.db = db;
        this.command = command;
    }

    @Override
    protected List<String> actualMessages() {
        return db;
    }
}