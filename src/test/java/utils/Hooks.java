// Hooks.java
package utils;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebElement;

public class Hooks {

    @Before
    public void setUp() {
        WindowsDriver driver = DriverFactory.getWinDriver();

        try {
            Thread.sleep(5000);

            System.out.println("🔍 'EdtCode' elementi aranıyor...");
            WebElement kullaniciAdi;
            try {
                kullaniciAdi = driver.findElement(MobileBy.AccessibilityId("EdtCode"));
            } catch (Exception e) {
                System.out.println("❌ 'EdtCode' elementi bulunamadı.");
                DriverFactory.logAllWindowTitles();
                throw e;
            }

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

            System.out.println("✅ ERP giriş başarılı.");

        } catch (Exception e) {
            System.out.println("❌ ERP girişi sırasında beklenmeyen hata: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
