package cucumber.demo;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.mockito.ArgumentCaptor;
import cucumber.demo.messageboard.EndpointDefinition;
import cucumber.demo.messageboard.MessageBoard;

import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Glue code for the acceptance tests and the application code.
 *
 * Everything is run in process without servers or complex databases.
 * In a way this is the first UI built for the application as the code here doesn't do much more than submitting use case execution requests and evaluating
 * the responses.
 */
public class MessageBoardSteps {
    private MessageBoard board = new MessageBoard();
    private MessageBoard.Response response = mock(MessageBoard.Response.class);
    private ArgumentCaptor<Iterable> iterableCaptor = ArgumentCaptor.forClass(Iterable.class);
    private EndpointDefinition endpoint;

    @Given("^an empty board$")
    public void an_empty_board() throws Throwable {
        board.clear();
    }

    @Then("^the board holds the messages:$")
    public void the_board_holds_the_messages(final DataTable table) throws Throwable {
        MessageBoard.Request request = new MessageBoard.Request();
        request.headers.put("usecase", "message.findAll");

        board.executeEndpoint(request, response);

        verify(response, times(1)).ok(iterableCaptor.capture());
        Iterable<String> messages = iterableCaptor.getValue();
        assertEquals(table.asList(String.class).size(), size(messages));
        assertEquals(table.asList(String.class), newArrayList(messages));
    }

    @When("^posting a message \"([^\"]*)\" to the board$")
    public void posting_a_message_to_the_board(String message) throws Throwable {
        MessageBoard.Request request = new MessageBoard.Request();
        request.headers.put("usecase", "message.post");
        request.body.put("message", message);

        board.executeEndpoint(request, response);
        verify(response).ok();
    }

    @Given("^a board with messages:$")
    public void a_board_with_messages(DataTable table) throws Throwable {
        board.clear();
        for (Object msg : table.asList(String.class))
            board.postMessage((String) msg);
    }

    @And("^the board can hold \"([^\"]*)\" messages$")
    public void the_board_can_hold_messages(String boardSizeAsString) throws Throwable {
        board.setMaxSize(parseInt(boardSizeAsString));
    }

    @Given("^usecase \"([^\"]*)\"$")
    public void usecase(String usecaseName) throws Throwable {
        endpoint = board.endpoints.get(Joiner.on('.').join(reverse(newArrayList(Splitter.on(' ').split(usecaseName)))));
        assertNotNull(endpoint);
    }

    @Then("^attribute \"([^\"]*)\" has constraint \"([^\"]*)\"$")
    public void attribute_has_constraint(String attributeName, String ruleName) throws Throwable {
        assertTrue(endpoint.constraints.body.get(attributeName).contains(ruleName));
    }

    @Then("^header \"([^\"]*)\" has constraint \"([^\"]*)\"$")
    public void header_has_constraint(String headerName, String ruleName) throws Throwable {
        assertTrue(endpoint.constraints.headers.get(headerName).contains(ruleName));
    }
}
