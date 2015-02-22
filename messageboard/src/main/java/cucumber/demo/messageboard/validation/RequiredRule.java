package cucumber.demo.messageboard.validation;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

public class RequiredRule extends AbstractRule {
    private List<Predicate<RequestValidator.Context>> predicates = Lists.<Predicate<RequestValidator.Context>>newArrayList(
            new IsNotBlank(),
            new IsNotNull()
    );

    @Override
    public RequestValidator.SubReport apply(final RequestValidator.Context context) {
        if(isTruthy(context)) {
            return RequestValidator.SubReport.accept("required");
        } else
            return RequestValidator.SubReport.reject("required");
    }

    private boolean isTruthy(final RequestValidator.Context context) {
        return Iterables.any(predicates, new Predicate<Predicate<RequestValidator.Context>>() {
            @Override
            public boolean apply(Predicate<RequestValidator.Context> predicate) {
                return predicate.apply(context);
            }
        });
    }

    private class IsNotBlank implements Predicate<RequestValidator.Context> {
        @Override
        public boolean apply(RequestValidator.Context context) {
            Object value = toValue(context);
            if(value == null) return false;
            if(!(value instanceof String)) return false;
            if(((String)value).trim().equals("")) return false;
            return true;
        }
    }

    private class IsNotNull implements Predicate<RequestValidator.Context> {
        @Override
        public boolean apply(RequestValidator.Context context) {
            Object value = toValue(context);
            if(value == null) return false;
            if(value instanceof String) return false;
            return true;
        }
    }
}
