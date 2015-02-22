package cucumber.demo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = "pretty"
        , glue = { "cucumber.demo" }
        , tags = { "~@Ignore" }
)
public class RunCukesTest {
}
