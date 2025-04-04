package testRunners;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/features",              // Feature dosyalarının yolu
        glue = {
                "stepDefinitions",
                "Hooks"                                            // Hooks klasörünü de unutma!
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",              // HTML rapor
                           // JSON rapor (isteğe bağlı)
        },
        tags = "@checkHooks",        dryRun = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
