package cucumber.demo.messageboard.validation;

import com.google.common.base.Function;
import cucumber.demo.messageboard.validation.DefaultRequestValidator;
import cucumber.demo.messageboard.validation.RequestValidator;
import org.junit.Before;
import org.junit.Test;
import cucumber.demo.messageboard.EndpointDefinition;
import cucumber.demo.messageboard.MessageBoard;
import cucumber.demo.messageboard.validation.RequestValidator.Context;
import cucumber.demo.messageboard.validation.RequestValidator.DefaultReport;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultRequestValidatorTest {
    private final RequestValidator.SubReport subReport = new RequestValidator.SubReport();
    private DefaultRequestValidator validator = new DefaultRequestValidator();
    private Function<Context, RequestValidator.SubReport> constraint = mock(Function.class);
    private EndpointDefinition definition = new EndpointDefinition();
    private MessageBoard.Request request = new MessageBoard.Request();

    @Before
    public void setup() {
        validator.constraints.put("R", constraint);
        request.headers.put("H", "x");
        request.body.put("A", "y");
        definition.constraints.headers.put("H", newArrayList("R"));
        definition.constraints.body.put("A", newArrayList("R"));
        when(constraint.apply(any(Context.class))).thenReturn(subReport);
    }

    @Test
    public void validate_headers() {
        validator.validate(definition, request);
        verify(constraint, times(1)).apply(eq(new Context("H", request.headers)));
    }

    @Test
    public void validate_body() {
        validator.validate(definition, request);
        verify(constraint, times(1)).apply(eq(new Context("A", request.body)));
    }

    @Test
    public void returns_validation_report() {
        DefaultReport report = validator.validate(definition, request);
        assertNotNull(report.headers.get("H"));
        assertNotNull(report.body.get("A"));
    }

    @Test
    public void invalid_report() {
        assertFalse(validator.validate(definition, request).isValid());
    }

    @Test
    public void valid_report() {
        subReport.valid = true;
        assertTrue(validator.validate(definition, request).isValid());
    }

    @Test
    public void multiple_violations_per_value() {
        definition.constraints.headers.put("H", newArrayList("R", "R"));
        definition.constraints.body.put("A", newArrayList("R", "R"));
        DefaultReport report = validator.validate(definition, request);
        assertEquals(2, report.headers.get("H").size());
        assertEquals(2, report.body.get("A").size());
    }
}
