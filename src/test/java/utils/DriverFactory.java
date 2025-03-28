package utils;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.options.WindowsOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class DriverFactory {

    private static WindowsDriver winDriver;
    private static Process winAppDriverProcess;

    private static void startWinAppDriver() {
        try {
            if (winAppDriverProcess == null || !winAppDriverProcess.isAlive()) {
                winAppDriverProcess = new ProcessBuilder("cmd.exe", "/c", "start /min WinAppDriver.exe")
                        .directory(new File("C:\\Program Files (x86)\\Windows Application Driver"))
                        .start();
                Thread.sleep(8000);
                System.out.println("🚀 WinAppDriver başarıyla başlatıldı.");
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ WinAppDriver başlatılamadı: " + e.getMessage(), e);
        }
    }
    private static String getERPWindowHandle(String windowTitleKeyword) {
        WindowsDriver desktopSession = null;
        try {
            WindowsOptions options = new WindowsOptions();
            options.setCapability("app", "Root"); // Masaüstü root erişimi
            options.setAutomationName("Windows");
            options.setPlatformName("Windows"); // büyük harf küçük fark etmez, "Windows" tercih edilir

            desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), options);
            Thread.sleep(8000);

            Set<String> handles = desktopSession.getWindowHandles();

            for (String handle : handles) {
                String windowTitle = desktopSession.switchTo().window(handle).getTitle();
                if (windowTitle != null && windowTitle.contains(windowTitleKeyword)) {
                    System.out.println("🚀 ERP pencere bulundu: " + windowTitle);
                    return handle;
                }
            }
            throw new RuntimeException("❌ ERP pencere bulunamadı!");

        } catch (Exception e) {
            throw new RuntimeException("❌ ERP pencere handle alınamadı: " + e.getMessage(), e);
        } finally {
            if (desktopSession != null) {
                desktopSession.quit();
            }
        }
    }




    private static void startERPApplication() {
        try {
            new ProcessBuilder("C:\\Tiger\\Protset\\Tiger3Enterprise.exe").start();
            Thread.sleep(15000); // ERP tam açılsın diye uzun süre bekle!

            String erpWindowHandle = getERPWindowHandle("TIGER 3 ENTERPRISE");
            if (erpWindowHandle == null) {
                throw new RuntimeException("❌ ERP pencere handle bulunamadı!");
            }

            DesiredCapabilities appCapabilities = new DesiredCapabilities();
            appCapabilities.setCapability("appTopLevelWindow", erpWindowHandle);
            appCapabilities.setCapability("deviceName", "WindowsPC");
            appCapabilities.setCapability("platformName", "Windows");
            appCapabilities.setCapability("automationName", "Windows");

            winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), appCapabilities);
            winDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("✅ ERP uygulaması başarıyla başlatıldı.");

        } catch (Exception e) {
            throw new RuntimeException("❌ ERP uygulaması başlatılırken hata oluştu: " + e.getMessage(), e);
        }
    }

    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            startWinAppDriver();
            startERPApplication();
        }
        return winDriver;
    }

    public static void quitDriver() {
        if (winDriver != null) {
            winDriver.quit();
            winDriver = null;
        }
        if (winAppDriverProcess != null && winAppDriverProcess.isAlive()) {
            try {
                new ProcessBuilder("taskkill", "/F", "/IM", "WinAppDriver.exe").start();
                winAppDriverProcess.destroy();
                winAppDriverProcess = null;
                System.out.println("🧹 WinAppDriver kapatıldı.");
            } catch (IOException e) {
                System.out.println("❌ WinAppDriver kapatılırken hata oluştu: " + e.getMessage());
            }
        }
    }
}
