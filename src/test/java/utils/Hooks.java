// ✅ Hooks.java
package utils;

import base.TestContext;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPageOnlineOzet;

import java.time.Duration;

public class Hooks {
    private static ExtentReports extent;
    private static ExtentTest test;
    private static final String DEFAULT_USERNAME = "LOGO";
    private static final String DEFAULT_PASSWORD = "LOGO";
    private static final String DEFAULT_COMPANY = "1";
    private static final String APP_NAME = "Online Hesap Özeti Uygulaması";

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("🚀 Test başlatılıyor: " + scenario.getName());

        System.out.println("Appium server başlatılıyor");
        DriverFactory.startAppiumServer();
        // ERP uygulamasını başlat
        DriverFactory.startERPApplication();

        // Masaüstü ERP uygulamasına bağlan
        WindowsDriver driver = DriverFactory.getWinDriver();
        context.setWindowsDriver(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        System.out.println("📋 Giriş ekranı kontrol ediliyor...");

        ElementHelper.clearAndFillFieldIfExists(driver, "EdtCode", DEFAULT_USERNAME);
        ElementHelper.clearAndFillField(driver, "EdtCyp", DEFAULT_PASSWORD);
        ElementHelper.clearAndFillField(driver, "EdtNum", DEFAULT_COMPANY);

        try {
            WebElement girisYap = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.name("Giriş Yap")));
            girisYap.click();
            System.out.println("✅ ERP giriş başarılı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ ERP girişi sırasında hata oluştu: " + e.getMessage(), e);
        }

        // Online Hesap Özeti Uygulamasına geçiş
        ElementHelper.waitForElement(driver, "name", APP_NAME, 13).click();
        ElementHelper.waitForWindowByTitle(APP_NAME, 5);
        ElementHelper.switchToWindowByTitle(APP_NAME);

        // Gerekirse ikinci giriş ekranı için kontrol
        LoginPageOnlineOzet loginPage = new LoginPageOnlineOzet(driver);
        loginPage.loginIfRequired("kemal.yapici@elogo.com.tr", "Kemal.123456");

        ElementHelper.waitUntilClickable(driver, "name", "Ana Sayfa", 15);
        System.out.println("✅ Sayfa etkileşim için hazır, testler başlıyor...");

// WebView2 ekranı için Selenium WebDriver başlat
        try {
            System.out.println("🧭 [Hooks] 'Online Hesap Özeti' ekranı geldi. Selenium geçişi başlatılıyor...");

            WebDriver seleniumDriver = DriverFactory.getSeleniumDriver();
            context.setWebDriver(seleniumDriver);

            // ⚠️ DOM yüklendikten sonra Ana Sayfa butonu veya ekranı için statik bekleme koy
            System.out.println("⏳ Ana sayfa yüklenmesi bekleniyor (maks. 15 saniye)...");
            Thread.sleep(3000); // Statik bekleme
            System.out.println("✅ Ana sayfanın yüklendiği varsayıldı. Teste geçiliyor.");

            System.out.println("✅ [Hooks] Selenium WebDriver aktifleştirildi. DOM üzerinden testlere geçildi.");
            System.out.println("🔍 Başlık: " + seleniumDriver.getTitle());

        } catch (Exception e) {
            System.out.println("❌ [Hooks] Selenium geçişinde hata oluştu.");
            System.out.println("🔍 Hata mesajı: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("WebView2 geçişi yapılamadı!", e);
        }

    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("❌ Test başarısız oldu: " + scenario.getName());
        } else {
            System.out.println("✅ Test başarıyla tamamlandı.");
        }
        DriverFactory.quitDriver();
    }
}