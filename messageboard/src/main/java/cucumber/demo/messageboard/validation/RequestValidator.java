package cucumber.demo.messageboard.validation;

import com.google.common.base.Predicate;
import cucumber.demo.messageboard.EndpointDefinition;
import cucumber.demo.messageboard.MessageBoard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Validation is not explicitly tested via acceptance tests.
 *
 * There are unit tests however to ensure:
 * - input validation happens
 * - use case execution is accepted or rejected as needed
 * - a violation report is communicated back to the user
 *
 * There are no acceptance tests for the required and secret rules used.
 * Maybe there should be but if so they can be tested in isolation and do not need to be re-tested every time a use case happens to use them.
 */
public interface RequestValidator {
    public Report validate(EndpointDefinition endpoint, MessageBoard.Request request);

    public class Context {
        public String attribute;
        public Map<String, ?> message;

        public Context(String attribute, Map<String, ?> message) {
            this.attribute = attribute;
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Context that = (Context) o;

            if (attribute != null ? !attribute.equals(that.attribute) : that.attribute != null) return false;
            if (message != null ? !message.equals(that.message) : that.message != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = attribute != null ? attribute.hashCode() : 0;
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ValidationContext{" +
                    "attribute='" + attribute + '\'' +
                    ", message=" + message +
                    '}';
        }
    }

    public static interface Report {
        boolean isValid();

        Map<String, List<SubReport>> getHeaders();
        Map<String, List<SubReport>> getBody();
    }

    public static class DefaultReport implements Report {
        public Map<String, List<SubReport>> headers = new HashMap<>();
        public Map<String, List<SubReport>> body = new HashMap<>();

        public boolean isValid() {
            return all(concat(concat(headers.values(), body.values())), new Predicate<SubReport>() {
                @Override
                public boolean apply(SubReport report) {
                    return report.valid;
                }
            });
        }

        @Override
        public Map<String, List<SubReport>> getHeaders() {
            return headers;
        }

        @Override
        public Map<String, List<SubReport>> getBody() {
            return body;
        }

        public void addHeaderReport(String headerName, SubReport report) {
            if(headers.get(headerName) == null) headers.put(headerName, newArrayList(report));
            else headers.get(headerName).add(report);
        }

        public void addAttributeReport(String attributeName, SubReport report) {
            if(body.get(attributeName) == null) body.put(attributeName, newArrayList(report));
            else body.get(attributeName).add(report);
        }
    }

    public static class SubReport {
        public String violationName;
        public boolean valid;

        SubReport() {}

        public static SubReport reject(String violationName) {
            SubReport report = new SubReport();
            report.violationName = violationName;
            return report;
        }

        public static SubReport accept(String violationName) {
            SubReport report = new SubReport();
            report.violationName = violationName;
            report.valid = true;
            return report;
        }
    }
}
