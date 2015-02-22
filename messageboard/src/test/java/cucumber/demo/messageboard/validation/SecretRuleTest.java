package cucumber.demo.messageboard.validation;

import cucumber.demo.messageboard.validation.RequestValidator;
import cucumber.demo.messageboard.validation.SecretRule;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SecretRuleTest {
    private SecretRule rule = new SecretRule();
    private HashMap<String, Object> message = new HashMap<>();
    private RequestValidator.Context context = new RequestValidator.Context("A", message);
    private RequestValidator.SubReport report;

    @Test
    public void reject_null() {
        setValue(null);
        report = rule.apply(context);
        assertFalse(report.valid);
    }

    @Test
    public void reject_wrong_secret() {
        setValue("wrong");
        report = rule.apply(context);
        assertFalse(report.valid);
    }

    @Test
    public void accept_correct_secret() {
        setValue("1234");
        report = rule.apply(context);
        assertTrue(report.valid);
    }

    private void setValue(Object value) {
        message.put("A", value);
    }
}