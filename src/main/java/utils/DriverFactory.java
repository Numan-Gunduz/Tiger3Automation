package utils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.remote.AppiumCommandExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    private static WindowsDriver winDriver;
    private static WebDriver webDriver;

    // **✅ WinAppDriver'ı Otomatik Başlatan Metot**
    private static void startWinAppDriver() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "WinAppDriver.exe");
            processBuilder.directory(new File("C:\\Program Files (x86)\\Windows Application Driver"));
            processBuilder.start();
            Thread.sleep(5000); // WinAppDriver'ın açılmasını bekle
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // **✅ Windows Driver Başlatma Metodu**
    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            try {
                startWinAppDriver(); // **WinAppDriver'ı başlat**
                Thread.sleep(3000); // **Bağlantının sağlanması için bekleme**

                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("platformName", "Windows");
                caps.setCapability("deviceName", "WindowsPC");
                caps.setCapability("app", "C:\\Tiger\\Protset\\Tiger3Enterprise.exe"); // **Uygulama yolu**

                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723/"), caps);
                winDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return winDriver;
    }

    // **✅ Web Driver Başlatma Metodu**
    public static WebDriver getWebDriver() {
        if (webDriver == null) {
            System.setProperty("webdriver.chrome.driver", "C:\\path\\to\\chromedriver.exe");
            webDriver = new ChromeDriver();
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
        return webDriver;
    }

    // **✅ Driver'ı Kapatma Metodu**
    public static void quitDriver() {
        if (winDriver != null) {
            winDriver.quit();
            winDriver = null;
        }
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }
}
