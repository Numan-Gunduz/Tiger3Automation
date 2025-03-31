package utils;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;

import static java.time.Duration.ofSeconds;

public class DriverFactory {

    private static WindowsDriver winDriver;

    private static Process winAppDriverProcess;

    private static void startWinAppDriver() {
        try {
            if (winAppDriverProcess == null || !winAppDriverProcess.isAlive()) {
                winAppDriverProcess = new ProcessBuilder("cmd.exe", "/c", "start /min WinAppDriver.exe")
                        .directory(new File("C:\\Program Files (x86)\\Windows Application Driver"))
                        .start();
                Thread.sleep(5000);
                System.out.println("🚀 WinAppDriver başarıyla başlatıldı.");
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ WinAppDriver başlatılamadı: " + e.getMessage(), e);
        }
    }

    private static void startERPApplication() {
        try {
            new ProcessBuilder("cmd.exe", "/c", "start /min C:\\Tiger\\Protset\\Tiger3Enterprise.exe").start();
            System.out.println("⏳ ERP uygulaması açılıyor, 30 saniye bekleniyor...");
            Thread.sleep(30000);
            System.out.println("🚀 ERP uygulaması başlatıldı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP uygulaması başlatılamadı: " + e.getMessage(), e);
        }
    }


    private static String getERPWindowHandle(String windowTitleKeyword) {
        try {
            ProcessBuilder builder = new ProcessBuilder("powershell.exe",
                    "$windows = Get-Process | Where-Object { $_.MainWindowTitle -like '*" + windowTitleKeyword + "*' };",
                    "$windows.MainWindowHandle");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            Scanner sc = new Scanner(process.getInputStream());
            String handle = null;
            while (sc.hasNextLine()) {
                handle = sc.nextLine().trim();
                if (!handle.isEmpty()) break;
            }

            if (handle == null || handle.isEmpty()) {
                throw new RuntimeException("❌ ERP pencere handle alınamadı: Boş geldi.");
            }

            System.out.println("🔑 Handle bulundu: " + handle);
            return handle;
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP pencere handle alınamadı (PowerShell): " + e.getMessage(), e);
        }
    }

//    private static void attachToRunningERP() {
//        try {
//            String handle = getERPWindowHandle("TIGER 3 ENTERPRISE 2025.LTS1");
//
//            String hexHandle = Long.toHexString(Long.parseLong(handle)).toUpperCase();
//            while (hexHandle.length() < 8) {
//                hexHandle = "0" + hexHandle;
//            }
//            hexHandle = "0x" + hexHandle;
//            System.out.println("✅ Final handle (hex): " + hexHandle);
//
//            DesiredCapabilities capabilities = new DesiredCapabilities();
//            capabilities.setCapability("appTopLevelWindow", hexHandle);
//            capabilities.setCapability("automationName", "Windows");
//            capabilities.setCapability("deviceName", "WindowsPC");
//            capabilities.setCapability("platformName", "Windows");
//
//            winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
//            winDriver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);
//
////            winDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//
//            System.out.println("✅ ERP uygulamasına başarıyla bağlanıldı.");
//        } catch (Exception e) {
//            throw new RuntimeException("❌ ERP uygulamasına bağlanılamadı: " + e.getMessage(), e);
//        }
//    }
private static void attachToRunningERP() {
    try {
        String handle = getERPWindowHandle("TIGER 3 ENTERPRISE 2025.LTS1");
        String hexHandle = Long.toHexString(Long.parseLong(handle)).toUpperCase();
        while (hexHandle.length() < 8) {
            hexHandle = "0" + hexHandle;
        }
        hexHandle = "0x" + hexHandle;
        System.out.println("✅ Final handle (hex): " + hexHandle);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appTopLevelWindow", hexHandle);
        capabilities.setCapability("automationName", "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
        capabilities.setCapability("platformName", "Windows");

        winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);

        // Retry mekanizması: windowHandles hazır olana kadar bekle
        int retries = 0;
        while (winDriver.getWindowHandles().isEmpty() && retries < 10) {
            System.out.println("⌛ Pencere handle'ları alınamadı, tekrar deneniyor...");
            Thread.sleep(2000);
            retries++;
        }

        if (winDriver.getWindowHandles().isEmpty()) {
            throw new RuntimeException("❌ Hiçbir pencere handle'ı alınamadı. Uygulama açılmamış olabilir.");
        }

        winDriver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);
        System.out.println("✅ ERP uygulamasına başarıyla bağlanıldı.");

    } catch (Exception e) {
        throw new RuntimeException("❌ ERP uygulamasına bağlanılamadı: " + e.getMessage(), e);
    }
}


//sğrüm  kontrolü
    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            startWinAppDriver();
            startERPApplication();
            attachToRunningERP();
        }
        return (WindowsDriver) winDriver;
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
