package cucumber.demo.messageboard.validation;

import com.google.common.base.Function;
import cucumber.demo.messageboard.EndpointDefinition;
import cucumber.demo.messageboard.MessageBoard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRequestValidator implements RequestValidator {
    public Map<String, Function<Context, SubReport>> constraints = new HashMap<>();

    @Override
    public DefaultReport validate(EndpointDefinition endpoint, MessageBoard.Request request) {
        DefaultReport report = new DefaultReport();
        for (Map.Entry<String, List<String>> e : endpoint.constraints.body.entrySet()) {
            for (String ruleName : e.getValue()) {
                report.addAttributeReport(e.getKey(), constraints.get(ruleName).apply(new Context(e.getKey(), request.body)));
            }
        }
        for (Map.Entry<String, List<String>> e : endpoint.constraints.headers.entrySet()) {
            for (String ruleName : e.getValue()) {
                report.addHeaderReport(e.getKey(), constraints.get(ruleName).apply(new Context(e.getKey(), request.headers)));
            }
        }
        return report;
    }
}
