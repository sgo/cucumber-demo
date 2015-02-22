package cucumber.demo.messageboard.persistence;

import cucumber.demo.messageboard.MessageBoard;

public class PersistenceContext {
    public MessageBoard.Command addMessageCommand;
    public MessageBoard.Command clearCommand;
    public MessageBoard.Command findAllMessagesCommand;
    public MessageBoard.Command removeMessageCommand;
}
