
package utils;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import static io.appium.java_client.remote.IOSMobileCapabilityType.APP_NAME;

public class DriverFactory {

    private static WindowsDriver winDriver;
    private static Process winAppDriverProcess;

//    public static void logAllWindowTitles() {
//        User32.INSTANCE.EnumWindows((hwnd, data) -> {
//            char[] buffer = new char[1024];
//            User32.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
//            String title = Native.toString(buffer);
//            if (title != null && !title.trim().isEmpty()) {
//                System.out.println("🧠 Açık Pencere: " + title + " -> HWND: " + hwnd.getPointer());
//            }
//            return true;
//        }, null);
//    }

    private static void startWinAppDriver() {
        try {
            if (winAppDriverProcess == null || !winAppDriverProcess.isAlive()) {
                String path = "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
                winAppDriverProcess = new ProcessBuilder("cmd.exe", "/c", "start \"\" \"" + path + "\"").start();
                System.out.println("🚀 WinAppDriver başlatıldı.");
                waitUntilWinAppDriverReady();
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ WinAppDriver başlatılamadı: " + e.getMessage(), e);
        }
    }

    private static void waitUntilWinAppDriverReady() {
        int retries = 10;
        while (retries-- > 0) {
            try (Socket socket = new Socket("127.0.0.1", 4723)) {
                System.out.println("✅ WinAppDriver hazır.");
                return;
            } catch (IOException e) {
                System.out.println("⏳ WinAppDriver bekleniyor...");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                }
            }
        }
        throw new RuntimeException("❌ WinAppDriver zamanında başlatılamadı!");
    }

    private static void startERPApplication() {
        try {
            String command = "Start-Process \"C:\\Tiger\\Protset\\Tiger3Enterprise.exe\" -Verb runAs";
            new ProcessBuilder("powershell.exe", "-Command", command).start();
            System.out.println("⏳ ERP uygulaması başlatılıyor...");
            Thread.sleep(17000);
            System.out.println("🚀 ERP uygulaması çalışıyor.");
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP başlatılamadı: " + e.getMessage(), e);
        }
    }

    public static WindowsDriver attachToRunningERP() {
        try {
            System.out.println("🔎 ERP için pencere handle aranıyor...");
            Thread.sleep(3000);
//            logAllWindowTitles();

            String targetTitle = "TIGER 3 ENTERPRISE 2025.LTS1 / v2.99.00.00 (LOGO YAZILIM (MERKEZ))";
            AtomicReference<WinDef.HWND> hwndRef = new AtomicReference<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {
                char[] buffer = new char[1024];
                User32.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
                String title = Native.toString(buffer);
                if (title != null && title.trim().equals(targetTitle)) {
                    System.out.println("🎯 Doğru pencere bulundu: " + title);
                    hwndRef.set(hwnd);
                    return false;
                }
                return true;
            }, null);

            if (hwndRef.get() == null) {
                throw new RuntimeException("❌ ERP top-level pencere bulunamadı.");
            }

            Pointer hwndPointer = hwndRef.get().getPointer();
            long hwndLong = Pointer.nativeValue(hwndPointer);
            String hexHandle = String.format("0x%X", hwndLong);
            System.out.println("🔑 Handle: " + hexHandle);

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("appTopLevelWindow", hexHandle);
            caps.setCapability("platformName", "Windows");
            caps.setCapability("deviceName", "WindowsPC");

            winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
            System.out.println("✅ ERP bağlantısı başarılı.");
            return winDriver;

        } catch (Exception e) {
            throw new RuntimeException("❌ ERP bağlanılamadı: " + e.getMessage(), e);
        }
    }

    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            startWinAppDriver();
            startERPApplication();
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
                System.out.println("🛑 WinAppDriver kapatıldı.");
            } catch (IOException e) {
                System.out.println("❌ WinAppDriver kapatılamadı: " + e.getMessage());
            }
        }
    }
}
