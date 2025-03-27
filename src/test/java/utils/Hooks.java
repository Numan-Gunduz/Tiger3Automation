package utils;

import base.TestContext;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.By;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp() {
        startWinAppDriver(); // WinAppDriver otomatik başlatılır

        // 1. ERP uygulamasını başlat
        WindowsDriver winDriver = DriverFactory.startERPApplication();
        context.setWinDriver(winDriver);
        System.out.println("✅ ERP uygulaması başlatıldı.");

        // 2. ERP login: logo / logo
        winDriver.findElement(By.name("Kullanıcı Adı")).sendKeys("logo");
        winDriver.findElement(By.name("Şifre")).sendKeys("logo");
        winDriver.findElement(By.name("Giriş")).click();
        System.out.println("✅ ERP giriş başarılı.");

        // 3. Online Hesap Özeti modülüne tıkla
        winDriver.findElement(By.name("Ara")).sendKeys("Online Hesap Özeti");
        winDriver.findElement(By.name("Aç")).click();
        System.out.println("✅ Online Hesap Özeti modülü açıldı.");

        // 4. Online Hesap Özeti giriş: kemal.yapici@elogo.com.tr / Kemal.12345
        winDriver.findElement(By.name("Kullanıcı")).sendKeys("kemal.yapici@elogo.com.tr");
        winDriver.findElement(By.name("Parola")).sendKeys("Kemal.12345");
        winDriver.findElement(By.name("Giriş")).click();
        System.out.println("✅ Online Hesap Özeti girişi başarılı.");
    }

    private void startWinAppDriver() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "cmd /c start \"\" \"C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe\"");
            Thread.sleep(3000); // Driver'ın başlatılması için bekle
            System.out.println("🚀 WinAppDriver otomatik başlatıldı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ WinAppDriver başlatılamadı.", e);
        }
    }

    @After
    public void tearDown() {
        DriverFactory.cleanupDrivers(context);
        System.out.println("🧹 Tüm driverlar kapatıldı.");
    }
}
