
package utils;
import base.TestContext;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPageOnlineOzet;

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

//        extent = ExtentReportManager.createInstance(); // Yeni report dosyası
//        test = extent.createTest(scenario.getName());  // Senaryonun ismiyle test başlat
//        ExtentReportManager.setTest(test);

      //  test.info("🚀 Test başlatılıyor: " + scenario.getName());



        WindowsDriver driver = DriverFactory.getWinDriver();
        context.setWindowsDriver(driver); // 🔥 burada driver’ı context'e set ediyoruz


        WebDriverWait wait = new WebDriverWait(driver, 15);

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
            System.out.println("❌ ERP girişi sırasında hata oluştu: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Online Hesap Özeti uygulamasına tıklama
        ElementHelper.waitForElement(driver, "name", APP_NAME, 8).click();
        System.out.println("✅ '" + APP_NAME + "' tıklandı.");

        // Sadece pencere geldi mi kontrolü, içerik değil
        ElementHelper.waitForWindowByTitle(APP_NAME, 5);
        ElementHelper.switchToWindowByTitle(APP_NAME);

        // Login gerekiyorsa yapılır
        LoginPageOnlineOzet loginPage = new LoginPageOnlineOzet(driver);
        loginPage.loginIfRequired("kemal.yapici@elogo.com.tr", "Kemal.12345");

        // ✅ Giriş sonrası sayfa tam yüklensin diye menüden "Ana Sayfa" bekleniyor
        ElementHelper.waitUntilClickable(driver, "name", "Ana Sayfa", 15);
        System.out.println("✅ Sayfa etkileşim için hazır, testler başlıyor...");

    }

//    @After
//    public void tearDown() {
//        DriverFactory.quitDriver();
//    }
@After
public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
        test.fail("❌ Test başarısız oldu: " + scenario.getName());
    } else {
        test.pass("✅ Test başarıyla tamamlandı.");
    }

    extent.flush(); // Raporu finalize et
    DriverFactory.quitDriver();
}
}