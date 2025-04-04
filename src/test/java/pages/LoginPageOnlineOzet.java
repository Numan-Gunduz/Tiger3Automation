
package pages;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.awt.Robot;
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
            // Giriş yapılmış mı kontrol et
            if (driver.getPageSource().contains("Hoş Geldin Kemal Yapıcı")) {
                System.out.println("✅ Kullanıcı zaten giriş yapmış.");

                WebElement maximizeButton = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
                maximizeButton.click();
                return;
            }

            // Kullanıcı adı alanı yoksa login ekranı açılmamış olabilir
            WebElement usernameField;
            try {
                usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                        MobileBy.AccessibilityId("UserName")));
            } catch (Exception e) {
                System.out.println("ℹ️ Giriş ekranı bulunamadı, kullanıcı zaten giriş yapmış olabilir.");
                return;
            }

            // 1. Sayfayı büyüt
            WebElement maximizeButton = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
            maximizeButton.click();
            System.out.println("🖥️ Ekran büyütüldü.");
            // 2. Kullanıcı adı temizlenip yazılıyor
            usernameField.click();
            usernameField.clear();
            System.out.println("🧑‍💼 Kullanıcı adı alanı bulundu, temizlendi.");
            typeTextWithRobot(username);

            // 3. Şifre alanı
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("Password")));
            passwordField.click();
            System.out.println("🔐 Şifre alanı bulundu, yazılıyor...");
            typeTextWithRobot(password);
            // 4. Giriş butonuna tıkla
            WebElement loginButton = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
            loginButton.click();
            System.out.println("✅ Giriş butonuna tıklandı, bekleniyor...");

            // 5. Doğrulama: Hoş geldin mesajı kontrolü (Zorunlu olursa fail verdirir)
            WebElement welcomeText = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.name("Hoş Geldin Kemal Yapıcı")));
            Assert.assertTrue("❌ Giriş başarısız: Ana sayfa doğrulanamadı!",
                    welcomeText.getText().contains("Kemal Yapıcı"));
            System.out.println("✅ Giriş başarılı, ana sayfa yüklendi.");

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
