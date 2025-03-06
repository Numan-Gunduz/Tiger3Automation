package utils;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    private static WindowsDriver winDriver;
    private static WebDriver webDriver;
    private static Process tigerProcess;

    // Tiger3'ü başlatan metot
    public static void startTiger3() {
        try {
            if (tigerProcess == null) {
                tigerProcess = Runtime.getRuntime().exec("C:\\Tiger\\Protset\\Tiger3Enterprise.exe");
                Thread.sleep(5000); // Uygulamanın açılması için bekleme süresi
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Windows Driver Başlatma
    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            try {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("app", "Root"); // Tüm açık Windows uygulamalarını görme yetkisi
                caps.setCapability("platformName", "Windows");
                caps.setCapability("deviceName", "WindowsPC");

                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
                winDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return winDriver;
    }

    // WebDriver Başlatma
    public static WebDriver getWebDriver() {
        if (webDriver == null) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            webDriver = new ChromeDriver();
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    // Driver'ları ve Tiger3 uygulamasını kapatma
    public static void closeDrivers() {
        if (winDriver != null) {
            winDriver.quit();
            winDriver = null;
        }
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
        if (tigerProcess != null) {
            tigerProcess.destroy();
            tigerProcess = null;
        }
    }
}
