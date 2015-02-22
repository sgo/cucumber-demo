package cucumber.demo.messageboard.persistence;

import org.junit.Test;
import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class AbstractAddMessageCommandTest {
    protected MessageBoard.Command command;
    private MessageBoard.Request request = new MessageBoard.Request();
    private MessageBoard.Response response = mock(MessageBoard.Response.class);

    @Test
    public void test() {
        request.body.put("message", "M");
        command.execute(request, response);
        assertEquals(newArrayList("M"), actualMessages());
        verify(response, times(1)).ok();
    }

    protected abstract List<String> actualMessages();
}
