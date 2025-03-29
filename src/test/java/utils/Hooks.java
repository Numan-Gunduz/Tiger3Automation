package utils;

import base.TestContext;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp() {
        WindowsDriver winDriver = DriverFactory.getWinDriver(); // ✅ artık cast hatası yok
        context.setWinDriver(winDriver);

        System.out.println("✅ ERP uygulaması başlatıldı.");

        try {
            Thread.sleep(10000); // uygulamanın yüklenmesini bekle

            winDriver.findElement(By.name("Kullanıcı Adı")).sendKeys("logo");
            winDriver.findElement(By.name("Şifre")).sendKeys("logo");
            winDriver.findElement(By.name("Giriş")).click();

            System.out.println("✅ ERP giriş başarılı.");

        } catch (Exception e) {
            throw new RuntimeException("❌ ERP giriş yapılırken hata oluştu: " + e.getMessage(), e);
        }
    }


    @After
    public void tearDown() {
        System.out.println("🧪 Test sonrası işlemler başlatılıyor...");
        DriverFactory.quitDriver();
    }
}
