package cucumber.demo.messageboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointDefinition {
    public ConstraintsDefinition constraints = new ConstraintsDefinition();

    public class ConstraintsDefinition {
        public Map<String, List<String>> headers = new HashMap<>();
        public Map<String, List<String>> body = new HashMap<>();
    }
}
