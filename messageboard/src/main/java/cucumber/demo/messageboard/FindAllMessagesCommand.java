package cucumber.demo.messageboard;

import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

public class FindAllMessagesCommand implements MessageBoard.Command<String> {
    public MessageBoard.Command<String> query;

    @Override
    public void execute(MessageBoard.Request request, final MessageBoard.Response<String> response) {
        query.execute(request, new MessageBoard.DefaultResponse<String>() {
            @Override
            public void ok(Iterable<String> msgs) {
                response.ok(isEmpty(msgs) ? newArrayList("This board is empty so try and add some messages.") : msgs);
            }
        });
    }
}
