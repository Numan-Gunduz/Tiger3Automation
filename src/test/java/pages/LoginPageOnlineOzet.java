package pages;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.time.Duration;

public class LoginPageOnlineOzet {

    private final WindowsDriver driver;
    private final WebDriverWait wait;

    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,10);
    }

    public void login(String username, String password) {
        try {
            // 1. Sayfayı büyüt (robot değil, öğe üzerinden)
            WebElement maximizeButton = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
            maximizeButton.click();
            System.out.println("🖥️ Ekran büyütüldü.");

            // 2. Kullanıcı adını clipboard ile gir
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("UserName")));

            usernameField.click();
            usernameField.clear();
            Thread.sleep(500);

            // Clipboard ile kopyalama
            StringSelection selection = new StringSelection(username);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            Actions actions = new Actions(driver);
            actions.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();

            System.out.println("🧑‍💼 Kullanıcı adı yapıştırıldı."+username);

            // 3. Şifre
            WebElement passwordField = driver.findElement(MobileBy.AccessibilityId("Password"));
            passwordField.sendKeys(password);
            System.out.println("🔐 Şifre alanı dolduruldu." + password);

            // 4. Giriş
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
}
