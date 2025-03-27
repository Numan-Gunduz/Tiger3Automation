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
        // 1. ERP uygulamasını aç
        WindowsDriver winDriver = DriverFactory.startERPApplication();
        context.setWinDriver(winDriver);
        System.out.println("✅ Windows uygulaması başlatıldı.");

        // 2. Login ekranı → kullanıcı adı ve şifre gir
        winDriver.findElement(By.name("Kullanıcı Adı")).sendKeys("logo");
        winDriver.findElement(By.name("Şifre")).sendKeys("logo");
        winDriver.findElement(By.name("Giriş")).click();
        System.out.println("✅ Kullanıcı giriş yaptı.");

        // 3. Menüden "Online Hesap Özeti" modülünü aç
        DriverFactory.openModule(winDriver, "Online Hesap Özeti");
        System.out.println("✅ Online Hesap Özeti modülü açıldı.");

        // 4. Açılan pencerede Eho bilgileri ile giriş yapılacaksa burada olabilir
        // WebDriver webDriver = DriverFactory.startWebApp(); → Gerekirse ekle
        // context.setWebDriver(webDriver);
    }

    @After
    public void tearDown() {
        DriverFactory.cleanupDrivers(context);
        System.out.println("🧹 Tüm driverlar kapatıldı.");
    }
}
