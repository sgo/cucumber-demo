package cucumber.demo.messageboard;

import com.google.common.base.Predicate;
import cucumber.demo.messageboard.EndpointDefinition;
import cucumber.demo.messageboard.MessageBoard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import cucumber.demo.messageboard.validation.RequestValidator;

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MessageBoardTest {
    private MessageBoard board = new MessageBoard();
    private MessageBoard.Request request = new MessageBoard.Request();
    private MessageBoard.Response response = mock(MessageBoard.Response.class);
    private RequestValidator requestValidator = mock(RequestValidator.class);
    private RequestValidator.Report validationReport = mock(RequestValidator.Report.class);
    private ArgumentCaptor<RequestValidator.Report> violationReportCaptor = ArgumentCaptor.forClass(RequestValidator.Report.class);
    private MessageBoard.Command command = mock(MessageBoard.Command.class);

    @Before
    public void setup() {
        request.headers.put("usecase", "U");
        board.requestValidator = requestValidator;
        when(requestValidator.validate(any(EndpointDefinition.class), any(MessageBoard.Request.class))).thenReturn(validationReport);
    }

    @Test
    public void requests_for_unknown_usecases_are_rejected() {
        request.headers.put("usecase", "unknown");
        board.submit(request, response);
        assertRequestRejectedAsUnknownUsecase();
    }

    private void assertRequestRejectedAsUnknownUsecase() {
        verify(response, times(1)).rejected(violationReportCaptor.capture());
        assertTrue(any(violationReportCaptor.getValue().getHeaders().get("usecase"), new Predicate<RequestValidator.SubReport>() {
            @Override
            public boolean apply(RequestValidator.SubReport report) {
                return report.violationName.equals("unknown");
            }
        }));
    }

    @Test
    public void requests_for_usecases_not_exposed_as_endpoints_are_rejected() {
        board.commands.put("U", command);
        board.submit(request, response);
        assertRequestRejectedAsUnknownUsecase();
    }

    @Test
    public void validate_request() {
        board.commands.put("U", command);
        board.endpoints.put("U", new EndpointDefinition());
        board.submit(request, response);
        verify(requestValidator, times(1)).validate(eq(board.endpoints.get("U")), eq(request));
    }

    @Test
    public void invalid_requests_prevent_execution() {
        board.commands.put("U", command);
        board.endpoints.put("U", new EndpointDefinition());
        board.submit(request, response);
        verifyNoMoreInteractions(command);
        verify(response).rejected(eq(validationReport));
    }

    @Test
    public void valid_requests_allow_usecase_execution() {
        when(validationReport.isValid()).thenReturn(true);
        board.commands.put("U", command);
        board.endpoints.put("U", new EndpointDefinition());
        board.submit(request, response);
        verify(command, times(1)).execute(eq(request), eq(response));
    }
}