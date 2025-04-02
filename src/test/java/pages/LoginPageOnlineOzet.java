package pages;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementHelper;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class LoginPageOnlineOzet {

    private final WindowsDriver driver;

    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        try {
            // 1. Pencereyi öne getir (gerekirse)
            WebElement rootElement = driver.findElement(MobileBy.xpath("//Window"));
            String windowTitle = rootElement.getAttribute("Name");
            ElementHelper.switchToWindowByTitle(windowTitle);

            // 2. Maximize butonuna tıkla (pencereyi büyüt)
            WebElement maximizeBtn = driver.findElement(MobileBy.AccessibilityId("pcMaximize"));
            maximizeBtn.click();
            System.out.println("🖥️ Ekran büyütüldü.");

            // 3. Kullanıcı adı alanını doldur
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("UserName")));
            usernameField.click();
            usernameField.sendKeys(Keys.CONTROL + "a");
            usernameField.sendKeys(Keys.DELETE);

            setClipboardData(username); // panoya mail adresini at
            pasteFromClipboard();       // yapıştır (autocomplete tetiklemez)
            System.out.println("🧑‍💼 Kullanıcı adı yapıştırıldı.");

            // 4. Şifre
// 4. Şifre
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("Password")));
            passwordField.click();
            passwordField.sendKeys(Keys.CONTROL + "a");
            passwordField.sendKeys(Keys.DELETE);
            passwordField.sendKeys(password);
            System.out.println("🔐 Şifre alanı dolduruldu."+password);


            // 5. Giriş butonuna tıkla
            WebElement loginBtn = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
            loginBtn.click();

            System.out.println("✅ Giriş işlemi tamamlandı.");
        } catch (Exception e) {
            System.out.println("❌ Login sırasında hata oluştu: " + e.getMessage());
        }
    }

    // --- Yardımcı metodlar ---
    private void setClipboardData(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    private void pasteFromClipboard() {
        try {
            Robot robot = new Robot();
            robot.delay(300);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
