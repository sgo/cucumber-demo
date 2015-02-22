package cucucmber.demo.messageboardx.cli;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import cucumber.demo.messageboard.MessageBoard;
import cucumber.demo.messageboard.inmemory.InMemoryPersistenceContext;
import cucumber.demo.messageboard.persistence.PersistenceContext;
import cucumber.demo.messageboard.validation.RequestValidator;
import cucucmber.demo.messageboardx.sql.SQLPersistenceContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.transform;

/**
 * A cli-shell connector for the message board.
 * This was not test driven as I simply fiddled till it looked like something I liked.
 *
 * Not a whole lot happens here other than submitting use case execution requests and formatting the responses.
 * What would be useful to test here?
 */
public class MessageBoardCli {
    private static Map<String, PersistenceContext> persistenceStrategies = new HashMap<String, PersistenceContext>() {{
        put("inmem", new InMemoryPersistenceContext());
        put("sql", new SQLPersistenceContext());
    }};
    private MessageBoard board;
    private String accessKey;

    @Command
    public String view() {
        ResponseHandler response = new ResponseHandler();
        MessageBoard.Request request = new MessageBoard.Request();
        request.headers.put("usecase", "message.findAll");

        board.submit(request, response);

        return response.toString();
    }

    @Command
    public String accessKey(String key) {
        accessKey = key;
        return "access key set!";
    }

    @Command
    public String postMessage(String message) {
        ResponseHandler response = new ResponseHandler();
        MessageBoard.Request request = new MessageBoard.Request();
        request.headers.put("usecase", "message.post");
        request.headers.put("access_key", accessKey);
        request.body.put("message", message);

        board.submit(request, response);

        return response.toString();
    }

    @Command
    public String connect(String persistenceStrategyName) {
        if (!persistenceStrategies.containsKey(persistenceStrategyName)) return "unknown persistence strategy! [" + persistenceStrategyName + "]";
        MessageBoard.persistenceContext = persistenceStrategies.get(persistenceStrategyName);
        board = new MessageBoard();
        return "using " + persistenceStrategyName + " persistence strategy!";
    }

    public static void main(String... args) throws IOException {
        MessageBoardCli cli = new MessageBoardCli();
        cli.connect("inmem");
        ShellFactory.createConsoleShell("messageboard", "The access key is 1234.", cli).commandLoop();
    }

    private static class ResponseHandler implements MessageBoard.Response {
        String response = "";

        @Override
        public void ok() {
            response = "ok";
        }

        @Override
        public void ok(Iterable messages) {
            final AtomicInteger sequence = new AtomicInteger(0);
            response = Joiner.on("\n").join(transform(messages, new Function<Object, String>() {
                @Override
                public String apply(Object msg) {
                    return (sequence.addAndGet(1)) + ": " + msg;
                }
            }));
        }

        @Override
        public void rejected(RequestValidator.Report report) {
            StringWriter sw = new StringWriter();
            PrintWriter out = new PrintWriter(sw);
            out.println("request rejected!");
            out.println("headers:");
            writeViolationReport(out, report.getHeaders().entrySet());
            out.println("body:");
            writeViolationReport(out, report.getBody().entrySet());
            response = sw.toString();
        }

        private void writeViolationReport(PrintWriter out, Set<Map.Entry<String, List<RequestValidator.SubReport>>> entries) {
            for (Map.Entry<String, List<RequestValidator.SubReport>> entry : entries) {
                Iterable<RequestValidator.SubReport> violations = filter(entry.getValue(), new IsRejectionReport());
                if (!isEmpty(violations)) {
                    String violationString = Joiner.on(", ").join(transform(violations, new SubReportToString()));
                    out.println("> " + entry.getKey() + ": " + violationString);
                }
            }
        }

        public String toString() {
            return response;
        }

        private static class SubReportToString implements Function<RequestValidator.SubReport, String> {
            @Override
            public String apply(RequestValidator.SubReport report) {
                return report.violationName;
            }
        }

        private static class IsRejectionReport implements Predicate<RequestValidator.SubReport> {
            @Override
            public boolean apply(RequestValidator.SubReport report) {
                return !report.valid;
            }
        }
    }
}
