package pages;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import utils.ElementHelper;

public class LoginPageOnlineOzet {

    private final WindowsDriver driver;

    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        // Pencereyi ön plana al
        WebElement rootElement = driver.findElement(MobileBy.xpath("//Window"));
        String windowTitle = rootElement.getAttribute("Name");
        ElementHelper.switchToWindowByTitle(windowTitle);

        // Kullanıcı adı
        WebElement usernameField = driver.findElement(MobileBy.AccessibilityId("UserName"));
        usernameField.clear();
        usernameField.sendKeys(username);

        // Şifre
        WebElement passwordField = driver.findElement(MobileBy.AccessibilityId("Password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        // Giriş Butonu (örnek name değerine göre, istersen Inspect ile bakılır)
        WebElement loginBtn = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
        loginBtn.click();


        System.out.println("✅ Online Hesap Özeti ekranında giriş tamamlandı.");
    }
}
