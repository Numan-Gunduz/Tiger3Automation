

package pages;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginPageOnlineOzet {

    private final WindowsDriver driver;
    private final WebDriverWait wait;

    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }




    public void loginWithRobot(String username, String password) {
        try {
            // 1. Sayfayı büyüt
            WebElement maximizeButton = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
            maximizeButton.click();
            System.out.println("🖥️ Ekran büyütüldü.");

            // 2. Kullanıcı adı alanını bul ve odaklan
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("UserName")));
            usernameField.click();
            usernameField.clear();
            Thread.sleep(300);
            usernameField.click();
            System.out.println("🧑‍💼 Kullanıcı adı alanı bulundu, yazılıyor...");
            typeTextWithRobot(username);

            // 3. Şifre alanını bul ve tıkla
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("Password")));
            passwordField.click();
            System.out.println("🔐 Şifre alanı bulundu, yazılıyor...");
            typeTextWithRobot(password);

            // 4. Giriş butonuna tıkla
            WebElement loginButton = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
            loginButton.click();
            System.out.println("✅ Giriş işlemi tamamlandı.");

            // 5. Hatalı giriş kontrolü
            Thread.sleep(2000);
            boolean loginWarning = driver.getPageSource().contains("Kullanıcı adı ya da şifre hatalı");
            if (loginWarning) {
                System.out.println("❌ Hatalı giriş uyarısı görüldü! Kullanıcı adı veya şifre yanlış olabilir.");
            } else {
                System.out.println("✅ Giriş başarılı, uyarı mesajı yok.");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Login sırasında hata oluştu: " + e.getMessage());
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
                    robot.keyPress(KeyEvent.VK_ALT_GRAPH); // ALT GR (ALT + CTRL)
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



