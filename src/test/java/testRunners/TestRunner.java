
package testRunners;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepDefinitions", "utils"},
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"  // ← Allure eklendi!



        },
        dryRun = false
)

public class TestRunner extends AbstractTestNGCucumberTests {
}
