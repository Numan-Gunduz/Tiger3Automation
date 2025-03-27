package utils;

import base.TestContext;
import io.appium.java_client.windows.WindowsDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

    // ERP (Windows) uygulamasını başlatır
    public static WindowsDriver startERPApplication() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("app", ConfigReader.get("erpPath"));
        try {
            return new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
        } catch (Exception e) {
            throw new RuntimeException("ERP Uygulaması başlatılamadı.", e);
        }
    }

    // ERP uygulamasında belirtilen modülü açar
    public static void openModule(WindowsDriver winDriver, String moduleName) {
        winDriver.findElement(By.name("Ara")).sendKeys(moduleName);
        winDriver.findElement(By.name("Aç")).click();
    }

    // Web uygulamasını başlatır
    public static WebDriver startWebApp() {
        String browser = ConfigReader.get("browser","chrome").toLowerCase();
        WebDriver driver;

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Desteklenmeyen tarayıcı tipi: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        driver.get(ConfigReader.get("webUrl"));
        return driver;
    }

    // TestContext nesnesi içindeki driver'ları temizler
    public static void cleanupDrivers(TestContext context) {
        try {
            if (context.getWinDriver() != null) {
                context.getWinDriver().quit();
            }
            if (context.getWebDriver() != null) {
                context.getWebDriver().quit();
            }
        } catch (Exception e) {
            System.out.println("Driver kapatma sırasında hata: " + e.getMessage());
        }
    }
}
