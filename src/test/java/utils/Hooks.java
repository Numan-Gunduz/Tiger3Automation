package utils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.MobileBy;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.openqa.selenium.WebElement;

public class Hooks {

    @Before
    public void setUp() {
        WindowsDriver driver = DriverFactory.getWinDriver();

        try {
            Thread.sleep(5000); // Uygulamanın iyice yüklenmesi için daha fazla bekle

            // Mevcut tüm pencereleri dönerek doğru handle'ı bulmaya çalış
            boolean elementFound = false;
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                try {
                    WebElement test = driver.findElement(MobileBy.AccessibilityId("EdtCode"));
                    elementFound = true;
                    break;
                } catch (Exception ignored) {}
            }

            if (!elementFound) {
                throw new RuntimeException("❌ 'EdtCode' elementi hiçbir handle içinde bulunamadı.");
            }

            Thread.sleep(1000); // Login alanlarının oturması için kısa bekleme

            WebElement kullaniciAdi = driver.findElement(MobileBy.AccessibilityId("EdtCode"));
            kullaniciAdi.clear();
            kullaniciAdi.sendKeys("LOGO");

            WebElement sifre = driver.findElement(MobileBy.AccessibilityId("EdtCyp"));
            sifre.clear();
            sifre.sendKeys("LOGO");

            WebElement firma = driver.findElement(MobileBy.AccessibilityId("EdtNum"));
            firma.clear();
            firma.sendKeys("1");

            WebElement girisYap = driver.findElement(MobileBy.name("Giriş Yap"));
            girisYap.click();

            System.out.println("✅ ERP giriş işlemi başarılı.");

        } catch (Exception e) {
            throw new RuntimeException("❌ ERP giriş yapılırken hata oluştu: " + e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
