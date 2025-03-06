package utils;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WindowsDriver winDriver;

    @BeforeMethod
    public void setUp() {
        winDriver = DriverFactory.getWinDriver(); // ✅ Doğru metod adı kullanıldı
        System.out.println("Windows uygulaması başlatıldı.");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        System.out.println("Windows uygulaması kapatıldı.");
    }
}
