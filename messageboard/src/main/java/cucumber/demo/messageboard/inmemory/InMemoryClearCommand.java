package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

public class InMemoryClearCommand implements MessageBoard.Command {
    public List db;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        db.clear();
    }
}
