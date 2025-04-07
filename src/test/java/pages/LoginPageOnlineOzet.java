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
    ElementHelper elementHelper  = new ElementHelper();

    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);
    }

    public void loginIfRequired(String username, String password) {
        try {
            // 1. Giriş yapılı mı kontrol
            if (driver.getPageSource().contains("Hoş Geldin Kemal Yapıcı")) {
                System.out.println("✅ Kullanıcı zaten giriş yapmış.");
                return;
            }

            // 2. Ekranı büyüt
            WebElement maximizeButton = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
            maximizeButton.click();
            System.out.println("🖥️ Ekran büyütüldü.");

            Robot robot = new Robot();

            // 3. Kullanıcı adı alanını bul
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("UserName")));
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

            // 4. Şifre alanı
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("Password")));
            passwordField.click();
            Thread.sleep(200);
            ElementHelper.typeTextSmart(passwordField, password);
            //typeTextWithRobot(password);
            System.out.println("✅ Şifre yazıldı.");

            // 5. Giriş butonuna tıkla
            WebElement loginButton = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
            loginButton.click();
            System.out.println("🔓 Giriş butonuna tıklandı.");

            // 6. Giriş kontrolü
            WebElement welcomeText = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.name("Hoş Geldin Kemal Yapıcı")));
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
