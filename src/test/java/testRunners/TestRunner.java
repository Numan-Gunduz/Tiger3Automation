package testRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.DriverFactory;

@CucumberOptions(
        features = "src/test/java/features", // Feature dosyalarının yolu
        glue = "stepDefinitions", // Step Definitions'ın yolu
        plugin = {"pretty", "html:target/cucumber-reports.html"}, // HTML Raporu
        monochrome = true // Konsol çıktısını daha okunaklı yapar
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    @Parameters({"testMode"}) // TestNG parametre olarak alır
    public void setUp(@Optional("WINDOWS") String testMode) {
        System.out.println("Test Modu: " + testMode);
        if (testMode.equalsIgnoreCase("WINDOWS")) {
            DriverFactory.getWinDriver(); // Windows uygulamasını başlat
        }
    }
}
