//// ✅ DriverFactory.java
//package utils;
//
//import com.sun.jna.Native;
//import com.sun.jna.Pointer;
//import com.sun.jna.platform.win32.WinDef;
//import io.appium.java_client.windows.WindowsDriver;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.net.URL;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class DriverFactory {
//    private static WindowsDriver winDriver;
//    private static WebDriver seleniumDriver;
//    private static Process erpProcess;
//
//    public static void startERPApplication() {
//        try {
//            System.out.println("🚀 ERP uygulaması başlatılıyor...");
//            String command = "Start-Process \"C:\\Tiger\\Protset\\Tiger3Enterprise.exe\" -Verb runAs";
//            erpProcess = new ProcessBuilder("powershell.exe", "-Command", command).start();
//            Thread.sleep(16000);
//            System.out.println("✅ ERP uygulaması başlatıldı.");
//        } catch (Exception e) {
//            throw new RuntimeException("❌ ERP başlatılamadı: " + e.getMessage(), e);
//        }
//    }
//
//    public static WebDriver getSeleniumDriver() {
//        if (seleniumDriver == null) {
//            try {
//                System.out.println("🌐 [Selenium] WebView2 debug bağlantısı deneniyor...");
//
//                EdgeOptions options = new EdgeOptions();
//                options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
//
//                seleniumDriver = new EdgeDriver(options);
//
//                System.out.println("✅ [Selenium] WebView2 bağlantısı başarılı!");
//            } catch (Exception e) {
//                System.out.println("❌ [Selenium] WebView2 bağlantısı kurulamadı!");
//                throw new RuntimeException("WebView2 Selenium bağlantı hatası!", e);
//            }
//        }
//        return seleniumDriver;
//    }
//
//    public static WindowsDriver getWinDriver() {
//        if (winDriver == null) {
//            try {
//                String targetTitle = "TIGER 3 ENTERPRISE 2025.LTS1 / v2.99.00.00 (LOGO YAZILIM (MERKEZ))";
//                AtomicReference<WinDef.HWND> hwndRef = new AtomicReference<>();
//
//                User32.INSTANCE.EnumWindows((hwnd, data) -> {
//                    char[] buffer = new char[1024];
//                    User32.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
//                    String title = Native.toString(buffer);
//                    if (title != null && title.trim().equals(targetTitle)) {
//                        hwndRef.set(hwnd);
//                        return false;
//                    }
//                    return true;
//                }, null);
//
//                if (hwndRef.get() == null) {
//                    throw new RuntimeException("❌ ERP top-level pencere bulunamadı.");
//                }
//
//                Pointer hwndPointer = hwndRef.get().getPointer();
//                long hwndLong = Pointer.nativeValue(hwndPointer);
//                String hexHandle = String.format("0x%X", hwndLong);
//
//                DesiredCapabilities caps = new DesiredCapabilities();
//                caps.setCapability("appTopLevelWindow", hexHandle);
//                caps.setCapability("platformName", "Windows");
//                caps.setCapability("deviceName", "WindowsPC");
//
//                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
//                System.out.println("✅ ERP bağlantısı başarılı.");
//
//            } catch (Exception e) {
//                throw new RuntimeException("❌ ERP bağlanılamadı: " + e.getMessage(), e);
//            }
//        }
//        return winDriver;
//    }
//
//    public static void quitDriver() {
//        if (winDriver != null) {
//            winDriver.quit();
//            winDriver = null;
//        }
//        if (seleniumDriver != null) {
//            seleniumDriver.quit();
//            seleniumDriver = null;
//        }
//        if (erpProcess != null && erpProcess.isAlive()) {
//            erpProcess.destroy();
//            System.out.println("🛑 ERP uygulaması sonlandırıldı.");
//        }
//    }
//}
// ✅ DriverFactory.java
package utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class DriverFactory {
    private static WindowsDriver winDriver;
    private static WebDriver seleniumDriver;
    private static Process erpProcess;
    private static Process appiumProcess;

    public static void startERPApplication() {
        try {
            System.out.println("🚀 ERP uygulaması başlatılıyor...");
            String command = "Start-Process \"C:\\Tiger\\Protset\\Tiger3Enterprise.exe\" -Verb runAs";
            erpProcess = new ProcessBuilder("powershell.exe", "-Command", command).start();
            Thread.sleep(16000);
            System.out.println("✅ ERP uygulaması başlatıldı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP başlatılamadı: " + e.getMessage(), e);
        }
    }

    public static void startAppiumServer() {
        try {
            System.out.println("🚀 Appium Server başlatılıyor...");
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start cmd.exe /k appium");
            builder.redirectErrorStream(true);
            appiumProcess = builder.start();
            Thread.sleep(4000);
            System.out.println("✅ Appium Server başlatıldı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ Appium Server başlatılamadı: " + e.getMessage(), e);
        }
    }

    public static WebDriver getSeleniumDriver() {
        if (seleniumDriver == null) {
            try {
                System.out.println("🌐 [Selenium] WebView2 debug bağlantısı deneniyor...");

                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

                seleniumDriver = new EdgeDriver(options);

                System.out.println("✅ [Selenium] WebView2 bağlantısı başarılı!");
            } catch (Exception e) {
                System.out.println("❌ [Selenium] WebView2 bağlantısı kurulamadı!");
                throw new RuntimeException("WebView2 Selenium bağlantı hatası!", e);
            }
        }
        return seleniumDriver;
    }

    public static WindowsDriver getWinDriver() {
        if (winDriver == null) {
            try {
                String targetTitle = "TIGER 3 ENTERPRISE 2025.LTS1 / v2.99.00.00 (LOGO YAZILIM (MERKEZ))";
                AtomicReference<WinDef.HWND> hwndRef = new AtomicReference<>();

                User32.INSTANCE.EnumWindows((hwnd, data) -> {
                    char[] buffer = new char[1024];
                    User32.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
                    String title = Native.toString(buffer);
                    if (title != null && title.trim().equals(targetTitle)) {
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

                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("appTopLevelWindow", hexHandle);
                caps.setCapability("platformName", "Windows");
                caps.setCapability("deviceName", "WindowsPC");

                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
                System.out.println("✅ ERP bağlantısı başarılı.");

            } catch (Exception e) {
                throw new RuntimeException("❌ ERP bağlanılamadı: " + e.getMessage(), e);
            }
        }
        return winDriver;
    }

    public static void quitDriver() {
        if (winDriver != null) {
            winDriver.quit();
            winDriver = null;
        }
        if (seleniumDriver != null) {
            seleniumDriver.quit();
            seleniumDriver = null;
        }
        if (erpProcess != null && erpProcess.isAlive()) {
            erpProcess.destroy();
            System.out.println("🛑 ERP uygulaması sonlandırıldı.");
        }
        if (appiumProcess != null && appiumProcess.isAlive()) {
            appiumProcess.destroy();
            System.out.println("🛑 Appium Server sonlandırıldı.");
        }
    }
}