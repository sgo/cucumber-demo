package cucucmber.demo.messageboardx.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucucmber.demo.messageboardx.sql.SQLPersistenceContext;
import cucumber.demo.messageboard.MessageBoard;
import cucumber.demo.messageboard.inmemory.InMemoryPersistenceContext;
import cucumber.demo.messageboard.persistence.PersistenceContext;
import cucumber.demo.messageboard.validation.RequestValidator;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import static org.apache.camel.model.rest.RestBindingMode.json;

public class Main {
    private static Map<String, PersistenceContext> persistenceStrategies = new HashMap<String, PersistenceContext>() {{
        put("inmem", new InMemoryPersistenceContext());
        put("sql", new SQLPersistenceContext());
    }};
    private MessageBoard board;

    private Main() throws Exception {
        DefaultCamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                restConfiguration().component("jetty").host("0.0.0.0").bindingMode(json).dataFormatProperty("prettyPrint", "true").port(9091);
                rest("/say").get("/hello").to("direct:hello");
                rest("/app")
                        .get("/board").to("direct:view.board")
                        .post("/board").type(Map.class).to("direct:post.message")
                        .post("/connect").type(Map.class).to("direct:connect.persistence.strategy");
                from("direct:hello").transform().constant("Hello World!");
                from("direct:view.board").bean(new ViewBoardFunction(), "apply");
                from("direct:post.message").bean(new PostMessageFunction(), "apply");
                from("direct:connect.persistence.strategy").bean(new ConnectToPersistenceStrategy());
            }
        });
        context.start();
    }

    public Map<String, Object> connect(final String persistenceStrategyName) {
        if (!persistenceStrategies.containsKey(persistenceStrategyName)) new HashMap() {{
            put("status", "not found");
            put("requestedStrategy", persistenceStrategyName);
        }};
        MessageBoard.persistenceContext = persistenceStrategies.get(persistenceStrategyName);
        board = new MessageBoard();
        return new HashMap() {{
            put("status", "ok");
            put("requestedStrategy", persistenceStrategyName);
        }};
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.connect("inmem");
        new CountDownLatch(1).await();
    }

    private static class ResponseHandler implements MessageBoard.Response {
        int status = 500;
        Object message = new HashMap<>();

        @Override
        public void ok() {
            status = 200;
            message = new HashMap<String, String>() {{
                put("status", "ok");
            }};
        }

        @Override
        public void ok(Iterable messages) {
            status = 200;
            message = messages;
        }

        @Override
        public void rejected(RequestValidator.Report report) {
            status = 412;
            message = report;
        }
    }

    public class ViewBoardFunction implements Function<Exchange, Object> {
        @Override
        public Object apply(Exchange o) {
            ResponseHandler response = new ResponseHandler();
            MessageBoard.Request request = new MessageBoard.Request();
            request.headers.put("usecase", "message.findAll");

            board.submit(request, response);

            return response.message;
        }
    }

    public class PostMessageFunction implements Function<Exchange, Object> {
        @Override
        public Object apply(Exchange e) {
            ResponseHandler response = new ResponseHandler();
            MessageBoard.Request request = new MessageBoard.Request();
            request.headers.put("usecase", "message.post");
            Message in = e.getIn(Message.class);
            Map m = (Map) in.getBody();
            request.headers.put("access_key", (String) in.getHeader("access-key"));
            request.body.put("message", m.get("message"));

            board.submit(request, response);

            try {
                if (response.status >= 400) {
                    in.setHeader(Exchange.HTTP_RESPONSE_CODE, response.status);
                    in.setHeader(Exchange.CONTENT_TYPE, "application/json");
                    return new ObjectMapper().writeValueAsString(response.message);
                } else
                    return response.message;
            } catch (JsonProcessingException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public class ConnectToPersistenceStrategy implements Function<Map, Object> {
        @Override
        public Object apply(Map map) {
            return connect((String) map.get("strategyName"));
        }
    }
}
