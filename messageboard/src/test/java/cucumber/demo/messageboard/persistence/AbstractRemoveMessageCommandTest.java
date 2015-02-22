package cucumber.demo.messageboard.persistence;

import org.junit.Test;
import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class AbstractRemoveMessageCommandTest {
    protected MessageBoard.Command command;
    private MessageBoard.Request request = new MessageBoard.Request();
    private MessageBoard.Response response = mock(MessageBoard.Response.class);

    public void setup() {
        addMessage("A");
        addMessage("B");
    }

    protected abstract void addMessage(String msg);

    @Test
    public void test() {
        request.body.put("message", "A");
        command.execute(request, response);
        assertEquals(newArrayList("B"), actualMessages());
        verify(response, times(1)).ok();
    }

    protected abstract List<String> actualMessages();
}
