//package testRunners;
//import io.cucumber.testng.AbstractTestNGCucumberTests;
//import io.cucumber.testng.CucumberOptions;
//
//@CucumberOptions(
//        features = "src/test/java/features",
//        glue = {
//                "stepDefinitions",
//                "Hooks"
//        },
//        plugin = {
//                "pretty",
//                "html:target/cucumber-reports.html",              // HTML rapor
//                           // JSON rapor (isteğe bağlı)
//        },
//        tags = "@checkHooks",        dryRun = false
//)
//public class TestRunner extends AbstractTestNGCucumberTests {
//}
package testRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/features",
        glue = {
                "stepDefinitions", // Step'lerin olduğu klasör
                "utils"            // Hooks ve yardımcı sınıflar burada
        },
        plugin = {
                "pretty",
                "html:Reports/CucumberReport/html-report.html",
                "json:Reports/CucumberReport/report.json"
        },
        tags = "@checkHooks",
        dryRun = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
