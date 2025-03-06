package utils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    private static WindowsDriver<WindowsElement> driver;

    public static WindowsDriver<WindowsElement> getDriver() {
        if (driver == null) {
            try {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("app", "C:\\Tiger\\Protset\\Tiger3Enterprise.exe");
                capabilities.setCapability("platformName", "Windows");
                capabilities.setCapability("deviceName", "WindowsPC");

                driver = new WindowsDriver<>(new URL("http://127.0.0.1:4723"), capabilities);
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
