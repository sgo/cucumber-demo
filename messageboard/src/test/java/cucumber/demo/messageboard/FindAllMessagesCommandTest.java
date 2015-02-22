package cucumber.demo.messageboard;

import cucumber.demo.messageboard.FindAllMessagesCommand;
import cucumber.demo.messageboard.MessageBoard;
import org.junit.Before;
import org.junit.Test;
import cucumber.demo.messageboard.inmemory.InMemoryFindAllMessagesCommand;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FindAllMessagesCommandTest {
    private FindAllMessagesCommand command = new FindAllMessagesCommand();
    private InMemoryFindAllMessagesCommand query = new InMemoryFindAllMessagesCommand();
    private MessageBoard.Response<String> response = mock(MessageBoard.Response.class);
    private List<String> db = new ArrayList<>();

    @Before public void setup() {
        query.db = db;
        command.query = query;
    }

    @Test public void return_invitation_for_empty_db() {
        command.execute(null, response);
        verify(response, times(1)).ok(newArrayList("This board is empty so try and add some messages."));
    }

    @Test
    public void happy_path() {
        db.add("M");
        command.execute(null, response);
        verify(response, times(1)).ok(db);
    }
}