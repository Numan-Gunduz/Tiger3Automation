package utils;

import base.TestContext;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverFactory {

    private static WindowsDriver winDriver;

    // ERP (Windows) uygulamasını başlatır
    public static WindowsDriver startERPApplication() {
        if (winDriver == null) {
            try {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("app", ConfigReader.get("erpPath"));
                caps.setCapability("platformName", "Windows");
                caps.setCapability("deviceName", "WindowsPC");  // Bu satır önemli!

                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
                System.out.println("✅ ERP uygulaması başarıyla başlatıldı.");
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
