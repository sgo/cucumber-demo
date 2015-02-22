package cucumber.demo.messageboard.validation;

public class SecretRule extends AbstractRule {
    @Override
    public RequestValidator.SubReport apply(RequestValidator.Context context) {
        String actual = (String)toValue(context);
        return actual != null && actual.equals("1234") ? RequestValidator.SubReport.accept("secret") : RequestValidator.SubReport.reject("secret");
    }
}
