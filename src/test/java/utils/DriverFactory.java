package utils;

import base.TestContext;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverFactory {

    private static WindowsDriver winDriver;

    public static WindowsDriver startERPApplication() {
        if (winDriver == null) {
            try {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("appium:app", ConfigReader.get("erpPath")); // appium:app olması önemli!
                caps.setCapability("appium:deviceName", "WindowsPC");
                caps.setCapability("appium:platformName", "Windows");
                caps.setCapability("appium:automationName", "Windows");

                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
            } catch (Exception e) {
                throw new RuntimeException("❌ ERP uygulaması başlatılamadı.", e);
            }
        }
        return winDriver;
    }

    public static void switchToWindows(TestContext context) {
        if (context.getWinDriver() == null) {
            context.setWinDriver(startERPApplication());
            System.out.println("🪟 Windows ortamına geçildi.");
        }
    }

    public static void cleanupDrivers(TestContext context) {
        try {
            if (context.getWinDriver() != null) {
                context.getWinDriver().quit();
                context.setWinDriver(null);
            }
        } catch (Exception e) {
            System.out.println("🛑 Driver kapatma sırasında hata: " + e.getMessage());
        }
    }
}
