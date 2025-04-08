package pages;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import utils.ElementHelper;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginPageOnlineOzet {

    private final WindowsDriver driver;
    private final WebDriverWait wait;


    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);
    }

    public void loginIfRequired(String username, String password) {
        try {
            boolean isLoginPageLoaded = false;

            try {
                WebElement usernameField = new WebDriverWait(driver, 5)
                        .until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("UserName")));
                isLoginPageLoaded = usernameField.isDisplayed();
            } catch (Exception e) {
                System.out.println("ℹ️ Giriş sayfası yüklenmemiş olabilir, token ile otomatik giriş yapılmış olabilir.");
            }

            // 🖥️ ID ekranı yüklense de yüklenmese de ekranı büyüt
            try {
                WebElement maximizeButton = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
                maximizeButton.click();
                System.out.println("🖥️ Uygulama ekranı büyütüldü (login ekranı görünmese bile).");
            } catch (Exception e) {
                System.out.println("⚠️ Ekran büyütme başarısız: " + e.getMessage());
            }

            if (!isLoginPageLoaded || driver.getPageSource().contains("Hoş Geldin Kemal Yapıcı")) {
                System.out.println("✅ Kullanıcı zaten giriş yapmış (refresh token ile).");
                return;
            }

            Robot robot = new Robot();

            WebElement usernameField = driver.findElement(MobileBy.AccessibilityId("UserName"));
            usernameField.click();
            Thread.sleep(500);

            String currentUsername = usernameField.getText().trim();
            System.out.println("📋 Kullanıcı adı alanındaki mevcut değer: " + currentUsername);

            if (!currentUsername.equalsIgnoreCase(username)) {
                System.out.println("✏️ Kullanıcı adı farklı. Alan temizleniyor...");
                usernameField.clear();
                Thread.sleep(200);
                usernameField.click();
                typeTextWithRobot(username);
            } else {
                System.out.println("✅ Kullanıcı adı zaten doğru, yazılmadı.");
            }

            WebElement passwordField = driver.findElement(MobileBy.AccessibilityId("Password"));
            passwordField.click();
            Thread.sleep(200);
            typeTextWithRobot(password);
            System.out.println("✅ Şifre yazıldı.");

            WebElement loginButton = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
            loginButton.click();
            System.out.println("🔓 Giriş butonuna tıklandı.");

            WebElement welcomeText = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(MobileBy.name("Hoş Geldin Kemal Yapıcı")));
            Assert.assertTrue("❌ Giriş başarısız!", welcomeText.getText().contains("Kemal Yapıcı"));
            System.out.println("✅ Giriş başarılı.");

        } catch (Exception e) {
            System.out.println("⚠️ Login sırasında hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private void typeTextWithRobot(String text) {
        try {
            Robot robot = new Robot();
            for (char c : text.toCharArray()) {
                typeChar(robot, c);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void typeChar(Robot robot, char c) {
        try {
            switch (c) {
                case '@':
                    robot.keyPress(KeyEvent.VK_ALT_GRAPH);
                    robot.keyPress(KeyEvent.VK_Q);
                    robot.keyRelease(KeyEvent.VK_Q);
                    robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
                    break;
                case '.':
                    robot.keyPress(KeyEvent.VK_PERIOD);
                    robot.keyRelease(KeyEvent.VK_PERIOD);
                    break;
                default:
                    boolean upperCase = Character.isUpperCase(c);
                    int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

                    if (upperCase) robot.keyPress(KeyEvent.VK_SHIFT);

                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);

                    if (upperCase) robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Yazılamayan karakter: " + c);
        }
    }
}
