package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

public class InMemoryRemoveMessageCommand implements MessageBoard.Command {
    public List<String> db;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        db.remove(request.body.get("message"));
        response.ok();
    }
}
