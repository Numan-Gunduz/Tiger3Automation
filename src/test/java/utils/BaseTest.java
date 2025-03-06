package utils;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WindowsDriver winDriver;
    protected WebDriver webDriver;
    protected String testMode = "WINDOWS"; // Default olarak Windows modunda başlat.

    @BeforeMethod
    public void setUp() {
        if (testMode.equals("WINDOWS")) {
            winDriver = DriverFactory.getWinDriver();
            System.out.println("Windows uygulaması başlatıldı.");
        } else if (testMode.equals("WEB")) {
            webDriver = DriverFactory.getWebDriver();
            System.out.println("Web tarayıcı başlatıldı.");
        } else {
            throw new IllegalStateException("Geçersiz test modu: " + testMode);
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        System.out.println("Test ortamı temizlendi.");
    }

    public void setTestMode(String mode) {
        this.testMode = mode;
        DriverFactory.setTestMode(mode);
    }
}
