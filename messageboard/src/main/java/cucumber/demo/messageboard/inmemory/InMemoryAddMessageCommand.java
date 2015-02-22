package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.MessageBoard;

import java.util.List;

public class InMemoryAddMessageCommand implements MessageBoard.Command {
    public List<String> db;

    @Override
    public void execute(MessageBoard.Request request, MessageBoard.Response response) {
        db.add((String)request.body.get("message"));
        response.ok();
    }
}
