package cucumber.demo.messageboard;

import cucumber.demo.messageboard.MessageBoard;
import cucumber.demo.messageboard.PostMessageCommand;
import org.junit.Before;
import org.junit.Test;
import cucumber.demo.messageboard.inmemory.InMemoryFindAllMessagesCommand;
import cucumber.demo.messageboard.inmemory.InMemoryAddMessageCommand;
import cucumber.demo.messageboard.inmemory.InMemoryRemoveMessageCommand;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PostMessageCommandTest {
    private PostMessageCommand command = new PostMessageCommand();
    private InMemoryFindAllMessagesCommand findAllMessagesCommand = new InMemoryFindAllMessagesCommand();
    private InMemoryRemoveMessageCommand removeMessageCommand = new InMemoryRemoveMessageCommand();
    private InMemoryAddMessageCommand addMessageCommand = new InMemoryAddMessageCommand();
    private MessageBoard.Request request = new MessageBoard.Request();
    private MessageBoard.Response response = mock(MessageBoard.Response.class);
    private List<String> db = new ArrayList<>();

    @Before
    public void setup() {
        findAllMessagesCommand.db = db;
        addMessageCommand.db = db;
        removeMessageCommand.db = db;

        command.addMessageCommand = addMessageCommand;
        command.findAllMessagesCommand = findAllMessagesCommand;
        command.removeMessageCommand = removeMessageCommand;
    }

    @Test
    public void post_one_message() {
        request.body.put("message", "M");
        command.execute(request, response);
        assertEquals(newArrayList("M"), db);
        verify(response, times(1)).ok();
    }

    @Test
    public void post_more_messages_then_can_fit() {
        command.maxSize = 1;

        request.body.put("message", "A");
        command.execute(request, response);
        request.body.put("message", "B");
        command.execute(request, response);

        assertEquals(newArrayList("B"), db);
    }
}