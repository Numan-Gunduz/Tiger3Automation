package utils;

import base.TestContext;
import io.cucumber.java.AfterStep;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

    private static final String DEFAULT_USERNAME = "LOGO";
    private static final String DEFAULT_PASSWORD = "LOGO";
    private static final String DEFAULT_COMPANY = "1";
    private static final String APP_NAME = "Online Hesap Özeti Uygulaması";

    private final TestContext context;
    private static boolean uygulamaZatenBaslatildi = false; //  ssadece ilk testte başlat

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        LogSilencer.silenceSeleniumWarnings();
        System.out.println(" Test başlatılıyor: " + scenario.getName());

        if (!uygulamaZatenBaslatildi) {
            System.out.println("Uygulama ilk kez başlatılıyor...");

            DriverFactory.startAppiumServer();
            DriverFactory.startERPApplication();

            WindowsDriver driver = DriverFactory.getWinDriver();
            context.setWindowsDriver(driver);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            ElementHelper.clearAndFillFieldIfExists(driver, "EdtCode", DEFAULT_USERNAME);
            ElementHelper.clearAndFillField(driver, "EdtCyp", DEFAULT_PASSWORD);
            ElementHelper.clearAndFillField(driver, "EdtNum", DEFAULT_COMPANY);

            try {
                WebElement girisYap = wait.until(ExpectedConditions.elementToBeClickable(
                        MobileBy.name("Giriş Yap")));
                girisYap.click();
                System.out.println(" ERP giriş başarılı.");
            } catch (Exception e) {
                throw new RuntimeException(" ERP girişi sırasında hata oluştu: " + e.getMessage(), e);
            }

            // Online Hesap Özetti Uygulamasını aç
            ElementHelper.waitForElement(driver, "name", APP_NAME, 13).click();
            ElementHelper.waitForWindowByTitle(APP_NAME, 5);
            ElementHelper.switchToWindowByTitle(APP_NAME);


            if (!DriverFactory.isDebugPortListening(20)) {
                throw new RuntimeException(" WebView2 debug port zaman aşımına uğradı.");
            }
            WebDriver seleniumDriver = DriverFactory.getSeleniumDriver();
            if (seleniumDriver == null) {
                throw new RuntimeException(" Selenium WebDriver oluşturulamadı. Test iptal ediliyor.");
            }
            context.setWebDriver(seleniumDriver);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ElementHelper.maximizeWindowWithRobot("Online Hesap Özeti Uygulaması");

            LoginPageOnlineOzet loginPage = new LoginPageOnlineOzet(seleniumDriver);
            loginPage.loginIfRequired("kemal.yapici@elogo.com.tr", "Kemal.123456");

            System.out.println("7 Saniye Dinamik Bekleme Ekleniyor...");
            ElementHelper.sleep(7000);
//            try {
//                Thread.sleep(6000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            System.out.println("Slenium WebDriver aktifleştirildi.");
            uygulamaZatenBaslatildi = true; // bir daha çalıştırmaa
        } else {
            System.out.println("Uygulama zaten açık. Yeni senaryoya geçiliyor.");
            //  buraya anasayfaya dön komutları eklenebilir
            context.setWindowsDriver(DriverFactory.getWinDriver());
            context.setWebDriver(DriverFactory.getSeleniumDriver());
            // Anasayfa'ya dön komutu
            ElementHelper.navigateToHomePage(context.getWebDriver());
        }
    }


    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {

            System.out.println(" Test başarısız oldu: " + scenario.getName());
        } else {
            System.out.println(" Test başarıyla tamamlandı.");
        }


        //teslerden sonra oluşan raporu görüntülemek için kullanılan komuy:
        //http://192.168.1.37:54099/index.html
        //mvn allure:report
    }

//    @AfterStep
//    public void afterEachStep(Scenario scenario) {
//        if (scenario.isFailed()) {
//            TakesScreenshot ts = (TakesScreenshot) context.getWebDriver();
//            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
//            attachScreenshot(screenshot);
//        }
//    }
//
//    @Attachment(value = "Failed Step Screenshot", type = "image/png")
//    public byte[] attachScreenshot(byte[] screenshot) {
//        return screenshot;
//    }

}