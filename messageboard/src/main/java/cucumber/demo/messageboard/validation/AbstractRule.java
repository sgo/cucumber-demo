package cucumber.demo.messageboard.validation;

import com.google.common.base.Function;

public abstract class AbstractRule implements Function<RequestValidator.Context, RequestValidator.SubReport> {
    public Object toValue(RequestValidator.Context context) {
        return context.message.get(context.attribute);
    }
}
