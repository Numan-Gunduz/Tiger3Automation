package pages;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;

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
            if (driver.getPageSource().contains("Hoş Geldin Kemal Yapıcı")) {
                System.out.println("✅ Kullanıcı zaten giriş yapmış.");
                driver.findElement(MobileBy.AccessibilityId("pcMaximize")).click();
                return;
            }

            WebElement maximizeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("pcMaximize")));
            maximizeButton.click();
            System.out.println("🖥️ Ekran büyütüldü.");

            Robot robot = new Robot();

            // Kullanıcı Adı alanını bul, temizle ve odakla
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("UserName")));
            usernameField.click();
            Thread.sleep(500);
            usernameField.clear();

            // Otomatik öneri popup'ını kapatmak için ESC bas
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(500);

            usernameField.click();
            Thread.sleep(500);

            System.out.println("🧑‍💼 Kullanıcı adı alanı temizlendi, odaklandı.");
            typeTextWithRobot(robot, username);

            // Şifre alanını bul, temizle ve odakla
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("Password")));
            passwordField.click();
            Thread.sleep(500);
            passwordField.clear();

            passwordField.click();
            Thread.sleep(500);

            System.out.println("🔐 Şifre alanı temizlendi, odaklandı.");
            typeTextWithRobot(robot, password);

            // Giriş butonuna tıkla
            WebElement loginButton = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
            loginButton.click();
            System.out.println("✅ Giriş butonuna tıklandı, bekleniyor...");

            WebElement welcomeText = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.name("Hoş Geldin Kemal Yapıcı")));

            Assert.assertTrue("❌ Giriş başarısız: Ana sayfa doğrulanamadı!",
                    welcomeText.getText().contains("Kemal Yapıcı"));
            System.out.println("✅ Giriş başarılı, ana sayfa yüklendi.");

        } catch (Exception e) {
            System.out.println("⚠️ Login sırasında hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void typeTextWithRobot(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            typeChar(robot, c);
            robot.delay(150);
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
