package utils;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class DriverFactory {
    private static WindowsDriver winDriver;
    private static Process winAppDriverProcess;

    public static void startWinAppDriver() {
        if (winAppDriverProcess == null) {
            try {
                ProcessBuilder builder = new ProcessBuilder(
                        "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe"
                );
                builder.directory(new File("C:\\Program Files (x86)\\Windows Application Driver"));
                builder.inheritIO();
                winAppDriverProcess = builder.start();
                Thread.sleep(5000);
                System.out.println("🚀 WinAppDriver otomatik başlatıldı.");
            } catch (Exception e) {
                throw new RuntimeException("❌ WinAppDriver başlatılamadı.", e);
            }
        }
    }

    public static WindowsDriver startERPApplication() {
        if (winDriver == null) {
            startWinAppDriver();  // WinAppDriver her zaman çalıştırılır.
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "Windows");
            caps.setCapability("deviceName", "WindowsPC");
            caps.setCapability("automationName", "Windows");
            caps.setCapability("app", "C:\\Tiger\\Protset\\Tiger3Enterprise.exe");

            try {
                // İlk olarak doğrudan başlatmayı dene
                winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), caps);
            } catch (Exception e) {
                System.out.println("❌ App ile başlatılamadı, manuel başlatılıyor...");

                // Eğer doğrudan başlatılamazsa ERP'yi manuel başlat
                try {
                    new ProcessBuilder("C:\\Tiger\\Protset\\Tiger3Enterprise.exe").start();
                    Thread.sleep(5000);

                    // Açılmış ERP pencere handle'ını al
                    DesiredCapabilities windowCaps = new DesiredCapabilities();
                    windowCaps.setCapability("platformName", "Windows");
                    windowCaps.setCapability("deviceName", "WindowsPC");
                    windowCaps.setCapability("automationName", "Windows");
                    windowCaps.setCapability("appTopLevelWindow", getTopLevelWindow());

                    winDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), windowCaps);
                } catch (Exception ex) {
                    throw new RuntimeException("❌ ERP uygulaması manuel olarak da başlatılamadı!", ex);
                }
            }

            winDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        return winDriver;
    }
    private static String getTopLevelWindow() {
        String windowHandle = null;

        try {
            DesiredCapabilities rootCaps = new DesiredCapabilities();
            rootCaps.setCapability("platformName", "Windows");
            rootCaps.setCapability("deviceName", "WindowsPC");
            rootCaps.setCapability("automationName", "Windows");
            rootCaps.setCapability("app", "root");

            WindowsDriver rootSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), rootCaps);
            Thread.sleep(3000); // bekleme

            Set<String> allWindows = rootSession.getWindowHandles();

            for (String handle : allWindows) {
                String title = rootSession.switchTo().window(handle).getTitle();
                System.out.println("Bulunan pencere başlığı: " + title);

                if (title.contains("TIGER 3 ENTERPRISE")) {  // Bu satır güncellendi!
                    windowHandle = handle;
                    break;
                }
            }

            rootSession.quit();

            if (windowHandle == null) {
                throw new RuntimeException("❌ ERP pencere handle'ı bulunamadı!");
            }

            return windowHandle;

        } catch (Exception e) {
            throw new RuntimeException("❌ ERP pencere handle'ı alınamadı.", e);
        }
    }


    public static void cleanupDrivers() {
        if (winDriver != null) {
            winDriver.quit();
            winDriver = null;
        }
        if (winAppDriverProcess != null) {
            winAppDriverProcess.destroy();
            winAppDriverProcess = null;
        }
        System.out.println("🧹 Tüm driverlar kapatıldı.");
    }
}
