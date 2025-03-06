package testRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.BaseTest;

@CucumberOptions(
        features = "src/test/java/features",
        glue = "stepDefinitions",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
    private static BaseTest baseTest = new BaseTest();

    @BeforeClass
    @Parameters({"testMode"})
    public static void setUp(@Optional("WINDOWS") String testMode) {
        baseTest.setTestMode(testMode);
        System.out.println("Test Modu: " + testMode);
    }
}
