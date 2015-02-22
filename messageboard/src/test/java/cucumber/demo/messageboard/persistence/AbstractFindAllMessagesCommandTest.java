package cucumber.demo.messageboard.persistence;

import org.junit.Test;
import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class AbstractFindAllMessagesCommandTest {
    protected MessageBoard.Command command;
    private MessageBoard.Response<String> response = mock(MessageBoard.Response.class);

    @Test
    public void test() {
        add("M");
        command.execute(null, response);
        verify(response, times(1)).ok(actualMessages());
    }

    protected abstract void add(String msg);

    protected abstract List<String> actualMessages();
}
