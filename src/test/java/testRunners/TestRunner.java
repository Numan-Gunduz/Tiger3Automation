package testRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/features",              // Feature dosyalarının yolu
        glue = {
                "stepDefinitions",
                "hooks"                                            // Hooks klasörünü de unutma!
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",              // HTML rapor
                "json:target/cucumber.json"                       // JSON rapor (isteğe bağlı)
        },
        monochrome = true,
        dryRun = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
