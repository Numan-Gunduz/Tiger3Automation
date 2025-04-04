// DriverFactory.java
package utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class DriverFactory {

    private static WindowsDriver winDriver;
    private static Process winAppDriverProcess;

    public static void logAllWindowTitles() {
        User32.INSTANCE.EnumWindows((hwnd, data) -> {
            char[] buffer = new char[1024];
            User32.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
            String title = Native.toString(buffer);
            if (title != null && !title.trim().isEmpty()) {
                System.out.println("\uD83E\uDE9F Açık Pencere: " + title + " -> HWND: " + hwnd.getPointer());
            }
            return true;
        }, null);
    }

//    private static void startWinAppDriver() {
//        try {
//            if (winAppDriverProcess == null || !winAppDriverProcess.isAlive()) {
//                String command = "Start-Process \"C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe\" -Verb runAs";
//                winAppDriverProcess = new ProcessBuilder("powershell.exe", "-Command", command).start();
//                Thread.sleep(5000);
//                System.out.println("🚀 WinAppDriver yönetici olarak başlatıldı.");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("❌ WinAppDriver başlatılamadı: " + e.getMessage(), e);
//        }
//    }
//
//
//    private static void startERPApplication() {
//        try {
//            String command = "Start-Process \"C:\\Tiger\\Protset\\Tiger3Enterprise.exe\" -Verb runAs";
//            new ProcessBuilder("powershell.exe", "-Command", command).start();
//            System.out.println("⏳ ERP uygulaması yönetici olarak açılıyor, 15 saniye bekleniyor...");
//            Thread.sleep(15000);
//            System.out.println("🚀 ERP uygulaması başlatıldı.");
//        } catch (Exception e) {
//            throw new RuntimeException("❌ ERP uygulaması başlatılamadı: " + e.getMessage(), e);
//        }
//    }

    public static WindowsDriver attachToRunningERP() {
        try {
            System.out.println("⏳ ERP uygulaması için pencere handle aranıyor...");
            Thread.sleep(5000);
            logAllWindowTitles();

            String exactTitle = "TIGER 3 ENTERPRISE 2025.LTS1 / v2.99.00.00 (LOGO YAZILIM (MERKEZ))";

            AtomicReference<WinDef.HWND> foundHwnd = new AtomicReference<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {
                char[] buffer = new char[1024];
                User32.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
                String title = Native.toString(buffer);
                if (title != null && title.trim().equals(exactTitle)) {
                    System.out.println("\uD83C\uDFAF Doğru top-level uygulama bulundu: " + title);
                    foundHwnd.set(hwnd);
                    return false;
                }
                return true;
            }, null);

            if (foundHwnd.get() == null) {
                throw new RuntimeException("❌ ERP top-level pencere handle'ı bulunamadı.");
            }

            Pointer hwndPointer = foundHwnd.get().getPointer();
            long hwndLong = Pointer.nativeValue(hwndPointer);
            String hexHandle = String.format("0x%X", hwndLong);
            System.out.println("\uD83D\uDD11 Handle bulundu: " + hwndLong + " | Hex: " + hexHandle);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appTopLevelWindow", hexHandle);
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");

            winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            System.out.println("✅ ERP uygulamasına başarıyla bağlanıldı.");

            return winDriver;

        } catch (Exception e) {
            System.out.println("❌ ERP uygulamasına bağlanılamadı: " + e.getMessage());
            throw new RuntimeException("❌ ERP uygulamasına bağlanılamadı: " + e.getMessage(), e);
        }
    }

    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
//            startWinAppDriver();
//            startERPApplication();
            return attachToRunningERP();
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
                System.out.println("\uD83E\uDEA1 WinAppDriver kapatıldı.");
            } catch (IOException e) {
                System.out.println("❌ WinAppDriver kapatılırken hata oluştu: " + e.getMessage());
            }
        }
    }
}