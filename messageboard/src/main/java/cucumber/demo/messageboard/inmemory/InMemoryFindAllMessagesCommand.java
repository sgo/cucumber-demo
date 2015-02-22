package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

public class InMemoryFindAllMessagesCommand implements MessageBoard.Command<String> {
    public List<String> db;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response<String> response) {
        response.ok(db);
    }
}
