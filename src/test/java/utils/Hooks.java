
package utils;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPageOnlineOzet;

public class Hooks {

    @Before
    public void setUp() {
        WindowsDriver driver = DriverFactory.getWinDriver();

        try {
            System.out.println("📋 Giriş ekranı kontrol ediliyor...");
            WebDriverWait wait = new WebDriverWait(driver, 15);

            // Kullanıcı alanı varsa doldur, yoksa zaten doludur diye devam et
            try {
                WebElement kullaniciAdi = wait.until(ExpectedConditions.presenceOfElementLocated(
                        MobileBy.AccessibilityId("EdtCode")));

                if (kullaniciAdi.isEnabled() && kullaniciAdi.isDisplayed()) {
                    System.out.println("🧑‍💼 Kullanıcı adı alanı bulundu. Temizleniyor...");
                    kullaniciAdi.click();
                    kullaniciAdi.sendKeys(Keys.CONTROL + "a");
                    kullaniciAdi.sendKeys(Keys.DELETE);
                    kullaniciAdi.sendKeys("LOGO");
                } else {
                    System.out.println("ℹ️ 'EdtCode' alanı pasif durumda, zaten otomatik dolu olabilir.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Kullanıcı seçimi bulunamadı veya otomatik seçildi: " + e.getMessage());
            }

            // Şifre alanı
            WebElement sifre = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("EdtCyp")));
            sifre.click();
            sifre.sendKeys(Keys.CONTROL + "a");
            sifre.sendKeys(Keys.DELETE);
            sifre.sendKeys("LOGO");

// Firma numarası
            WebElement firma = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("EdtNum")));
            firma.click();
            firma.sendKeys(Keys.CONTROL + "a");
            firma.sendKeys(Keys.DELETE);
            firma.sendKeys("1");

            // Giriş Yap butonu
            WebElement girisYap = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.name("Giriş Yap")));
            girisYap.click();

            System.out.println("✅ ERP giriş başarılı.");

        } catch (Exception e) {
            System.out.println("❌ ERP girişi sırasında hata oluştu: " + e.getMessage());
            System.out.println("🔍 Mevcut açık pencereler listeleniyor...");
            DriverFactory.logAllWindowTitles();
            throw new RuntimeException(e);
        }

        // 🕒 Giriş sonrası 8 saniye bekleme
        try {
            System.out.println("⏳ Giriş sonrası bekleniyor...");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement öho = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.name("Online Hesap Özeti Uygulaması")));
        öho.click();
        System.out.println("✅ 'Online Hesap Özeti Uygulaması' tıklandı.");

        try {
            System.out.println("⏳ Giriş sonrası bekleniyor...");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        ElementHelper.switchToWindowByTitle("Online Hesap Özeti Uygulaması");

        LoginPageOnlineOzet loginPage = new LoginPageOnlineOzet(driver);
        loginPage.loginIfRequired("kemal.yapici@elogo.com.tr", "Kemal.12345");

    }



    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
