package utils;


import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;

import io.appium.java_client.windows.options.WindowsOptions;
import io.appium.java_client.AppiumBy;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;
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
                Thread.sleep(5000);
                System.out.println("🚀 WinAppDriver başarıyla başlatıldı.");
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ WinAppDriver başlatılamadı: " + e.getMessage(), e);
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
            return handle; // Artık HEX değil, DECIMAL olarak dönüyoruz
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP pencere handle alınamadı (PowerShell): " + e.getMessage(), e);
        }
    }




    private static void attachToRunningERP() {
        try {
            String erpHandle = getERPWindowHandle("TIGER 3 ENTERPRISE");

            if (erpHandle != null && !erpHandle.isEmpty()) {
                while (erpHandle.length() < 8) {
                    erpHandle = "0" + erpHandle;
                }
                erpHandle = erpHandle.toUpperCase();
            }

            System.out.println("✅ Final handle (hex): " + erpHandle);

            WindowsOptions options = new WindowsOptions();
            options.setCapability("appTopLevelWindow", erpHandle);
            options.setPlatformName("Windows");
            options.setAutomationName("Windows");
            options.setCapability("deviceName", "WindowsPC");

            winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), options);
            winDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("✅ ERP uygulamasına başarıyla bağlanıldı.");


            System.out.println("✅ ERP uygulamasına başarıyla bağlanıldı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP uygulamasına bağlanılamadı: " + e.getMessage(), e);
        }
    }




    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            startWinAppDriver();
            // startERPApplication();
            attachToRunningERP();
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

//    private static void startERPApplication() {
//        try {
//            new ProcessBuilder("C:\\Tiger\\Protset\\Tiger3Enterprise.exe").start();
//            Thread.sleep(15000); // ERP'nin açılması için bekle
//
//            String erpHandle = getERPWindowHandle("TIGER 3 ENTERPRISE");
//
//            WindowsOptions options = new WindowsOptions();
//            options.setCapability("appTopLevelWindow", erpHandle);
//            options.setPlatformName("Windows");          // Burası büyük W ile yazılmalı.
//            options.setAutomationName("Windows");
//
//            winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), options);
//            winDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//            System.out.println("✅ ERP uygulaması başarıyla başlatıldı.");
//        } catch (Exception e) {
//            throw new RuntimeException("❌ ERP uygulaması başlatılırken hata oluştu: " + e.getMessage(), e);
//        }
//    }