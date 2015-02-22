package cucumber.demo.messageboard;

import cucumber.demo.messageboard.inmemory.InMemoryPersistenceContext;
import cucumber.demo.messageboard.persistence.PersistenceContext;
import cucumber.demo.messageboard.validation.DefaultRequestValidator;
import cucumber.demo.messageboard.validation.RequestValidator;
import cucumber.demo.messageboard.validation.RequiredRule;
import cucumber.demo.messageboard.validation.SecretRule;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Functional requirements are described in: $MAVEN_ROOT/standalone/src/test/resources/pg/cukes/example/messageboard.feature
 *
 * The objective of this example is to demonstrate specification first, test first, integration last, network last, ui last development.
 * The Cucumber specification was written first and all tests were made to pass without network, database or ui.
 * A fast, standalone, purely behavioral and business level test suite.
 *
 * This is not meant to be an example of the design or code style you must use to implement Cucumber specs but rather an example of how you could satisfy
 * them with a minimum of effort. For instance there were intentionally no frameworks used to avoid their complexity. Obviously a solution could take
 * advantage of frameworks such as Spring, javax.validation, rx-java and the like as well.
 */
public class MessageBoard {
    // ================================ main =====================================
    // Wire up all the components.
    // Normally this would be extracted in a main module
    // ===========================================================================

    // replace this with the persistence strategy you wish to use and create a new instance of MessageBoard
    public static PersistenceContext persistenceContext = new InMemoryPersistenceContext();

    // Business level components
    // Typically, acceptance tests will execute these directly bypassing preconditions defined on endpoints below
    public FindAllMessagesCommand findAllMessagesOrInvitationCommand = new FindAllMessagesCommand() {{
        query = persistenceContext.findAllMessagesCommand;
    }};
    public RequestValidator requestValidator = new DefaultRequestValidator() {{
        constraints.put("required", new RequiredRule());
        constraints.put("secret", new SecretRule());
    }};
    private PostMessageCommand postMessageCommand = new PostMessageCommand() {{
        addMessageCommand = persistenceContext.addMessageCommand;
        findAllMessagesCommand = persistenceContext.findAllMessagesCommand;
        removeMessageCommand = persistenceContext.removeMessageCommand;
    }};

    Map<String, Command> commands = new HashMap<String, Command>() {{
        put("clear", persistenceContext.clearCommand);
        put("message.findAll", findAllMessagesOrInvitationCommand);
        put("message.post", postMessageCommand);
    }};

    // UI connector endpoint configuration
    // This configuration is inspected by acceptance tests rather than tested through executing the code.
    // Reasoning for this is that PO and possibly even QA care less about the actual implementation as they do about whether the system knows these
    // preconditions. Another kind of PO, presumably, dev would of course care and test through their own unit and/or integrations tests.
    public Map<String, EndpointDefinition> endpoints = new HashMap<String, EndpointDefinition>() {{
        put("message.findAll", new EndpointDefinition());
        put("message.post", new EndpointDefinition() {{
            constraints.headers.put("access_key", newArrayList("required", "secret"));
            constraints.body.put("message", newArrayList("required"));
        }});
    }};
    // ================================ main =====================================

    /**
     * convenience method for testing
     */
    public void clear() {
        commands.get("clear").execute(null, null);
    }

    /**
     * convenience method for testing
     */
    public void postMessage(String msg) {
        Request request = new Request();
        request.body.put("message", msg);
        commands.get("message.post").execute(request, new DefaultResponse() {
            @Override
            public void ok() {
            }
        });
    }

    /**
     * Entry point for UI's to source events through.
     * Here all preconditions will be checked before allowing execution of a use case.
     */
    public void submit(Request request, Response response) {
        EndpointDefinition endpoint = endpoints.get(request.headers.get("usecase"));
        if (endpoint == null) {
            unknownUsecase(response);
        } else {
            RequestValidator.Report report = requestValidator.validate(endpoint, request);
            if (report.isValid()) executeEndpoint(request, response);
            else response.rejected(report);
        }
    }

    private void unknownUsecase(Response response) {
        RequestValidator.DefaultReport report = new RequestValidator.DefaultReport();
        report.headers.put("usecase", newArrayList(RequestValidator.SubReport.reject("unknown")));
        response.rejected(report);
    }

    /**
     * Executes a usecase directly.
     * UI's should not use this method but rather 'submit' instead.
     * This method is used to ease testing from acceptance tests without having to satisfy all preconditions.
     */
    public void executeEndpoint(Request request, Response response) {
        Command usecase = commands.get(request.headers.get("usecase"));
        if (usecase == null) {
            unknownUsecase(response);
        } else {
            usecase.execute(request, response);
        }
    }

    public void setMaxSize(int size) {
        postMessageCommand.maxSize = size;
    }

    public static interface Command<T> {
        void execute(Request request, Response<T> response);
    }

    /**
     * Data structure used to request execution of a use case.
     * The relevant use case is selected via the usecase-header value.
     * The body attributes depend on the use case being executed and can be constrained via an associated EndpointDefinition.
     *
     * For this example I did not bother to statically type every possible message but obviously you could.
     */
    public static class Request {
        public Map<String, String> headers = new HashMap<>();
        public Map<String, Object> body = new HashMap<>();
    }

    /**
     * A callback interface to be implemented by UI or transport layer clients.
     */
    public static interface Response<T> {
        void ok();

        void ok(Iterable<T> iterable);

        void rejected(RequestValidator.Report report);
    }

    /**
     * Convenience implementation for when you don't want to implement every method.
     */
    public static class DefaultResponse<T> implements Response<T> {
        @Override
        public void ok() {
            throw new UnsupportedOperationException("implement me!");
        }

        @Override
        public void ok(Iterable<T> iterable) {
            throw new UnsupportedOperationException("implement me!");
        }

        @Override
        public void rejected(RequestValidator.Report report) {
            throw new UnsupportedOperationException("implement me!");
        }
    }

}
