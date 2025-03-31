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
            Thread.sleep(5000); // Uygulamanın giriş ekranının tam yüklenmesini bekle

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
                DriverFactory.logAllWindowTitles();
                throw new RuntimeException("❌ 'EdtCode' elementi bulunamadı. Muhtemelen pencere aktif değil.");
            }

            Thread.sleep(1000);

            WindowsElement kullaniciAdi = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("EdtCode"));
            kullaniciAdi.clear();
            kullaniciAdi.sendKeys("LOGO");

            WindowsElement sifre = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("EdtCyp"));
            sifre.clear();
            sifre.sendKeys("LOGO");

            WindowsElement firma = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("EdtNum"));
            firma.clear();
            firma.sendKeys("1");

            WebElement girisYap = driver.findElement(MobileBy.name("Giriş Yap"));
            girisYap.click();

            System.out.println("✅ ERP giriş başarılı.");

        } catch (Exception e) {
            DriverFactory.logAllWindowTitles();
            throw new RuntimeException("❌ ERP girişi sırasında hata: " + e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}