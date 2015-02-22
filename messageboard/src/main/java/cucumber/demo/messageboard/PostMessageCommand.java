package cucumber.demo.messageboard;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.size;

public class PostMessageCommand implements MessageBoard.Command {
    public MessageBoard.Command<String> findAllMessagesCommand;
    public MessageBoard.Command removeMessageCommand;
    public MessageBoard.Command addMessageCommand;
    public int maxSize = 5;

    @Override
    public void execute(final MessageBoard.Request request, final MessageBoard.Response response) {
        findAllMessagesCommand.execute(null, new MessageBoard.DefaultResponse<String>() {
            @Override
            public void ok(Iterable<String> messages) {
                if(size(messages) >= maxSize) {
                    MessageBoard.Request r = new MessageBoard.Request();
                    r.body.put("message", getFirst(messages, null));
                    removeMessageCommand.execute(r, new MessageBoard.DefaultResponse() {
                        @Override
                        public void ok() {
                            addMessageCommand.execute(request, response);
                        }
                    });
                } else
                    addMessageCommand.execute(request, response);
            }
        });
    }
}
