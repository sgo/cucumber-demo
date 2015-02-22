package cucumber.demo.messageboard.validation;

import cucumber.demo.messageboard.validation.RequestValidator;
import cucumber.demo.messageboard.validation.RequiredRule;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class RequiredRuleTest {
    private RequiredRule rule = new RequiredRule();
    private HashMap<String, Object> message = new HashMap<>();
    private RequestValidator.Context context = new RequestValidator.Context("A", message);
    private RequestValidator.SubReport report;

    @Test
    public void reject_when_missing() {
        report = rule.apply(context);
        assertFalse(report.valid);
    }

    @Test
    public void reject_when_null() {
        setValue(null);
        report = rule.apply(context);
        assertFalse(report.valid);
    }

    @Test
    public void reject_when_empty_string() {
        setValue("");
        report = rule.apply(context);
        assertFalse(report.valid);
    }

    @Test
    public void reject_when_blank_string() {
        setValue(" ");
        report = rule.apply(context);
        assertFalse(report.valid);
    }

    @Test
    public void accept_when_not_null() {
        setValue(new Object());
        report = rule.apply(context);
        assertTrue(report.valid);
    }

    @Test
    public void accept_when_not_blank_string() {
        setValue("x");
        report = rule.apply(context);
        assertTrue(report.valid);
    }

    private void setValue(Object value) {
        message.put("A", value);
    }
}