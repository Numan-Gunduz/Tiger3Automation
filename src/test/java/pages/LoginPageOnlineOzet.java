
package pages;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import utils.ElementHelper;

import java.time.Duration;

public class LoginPageOnlineOzet {
    private final WindowsDriver driver;
    private final WebDriverWait wait;

    public LoginPageOnlineOzet(WindowsDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,  Duration.ofSeconds(15));
    }

    public void loginIfRequired(String username, String password) {
        boolean isLoginPageLoaded = ElementHelper.isElementPresent(driver, "UserName", 5);

        ElementHelper.maximizeWindowIfPresent(driver, "pcMaximize");

        if (!isLoginPageLoaded || driver.getPageSource().contains("Hoş Geldin Kemal Yapıcı")) {
            System.out.println("✅ Kullanıcı zaten giriş yapmış (refresh token ile).");
            return;
        }

        WebElement usernameField = driver.findElement(MobileBy.AccessibilityId("UserName"));
        ElementHelper.clickAndWait(usernameField);

        String currentUsername = usernameField.getText().trim();
        System.out.println("📋 Kullanıcı adı alanındaki mevcut değer: " + currentUsername);

        if (!currentUsername.equalsIgnoreCase(username)) {
            System.out.println("✏️ Kullanıcı adı farklı. Alan temizleniyor...");
            usernameField.clear();
            ElementHelper.clickAndWait(usernameField);
            ElementHelper.typeTextSmart(usernameField, username);
        } else {
            System.out.println("✅ Kullanıcı adı zaten doğru, yazılmadı.");
        }

        WebElement passwordField = driver.findElement(MobileBy.AccessibilityId("Password"));
        ElementHelper.clickAndWait(passwordField);
        ElementHelper.typeTextSmart(passwordField, password);
        System.out.println("✅ Şifre yazıldı.");

        WebElement loginButton = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
        loginButton.click();
        System.out.println("🔓 Giriş butonuna tıklandı.");

//        WebElement welcomeText = wait.until(
//                ExpectedConditions.presenceOfElementLocated(MobileBy.name("Hoş Geldin Kemal Yapıcı")));
//
//        Assert.assertTrue("❌ Giriş başarısız!", welcomeText.getText().contains("Kemal Yapıcı"));
//        System.out.println("✅ Giriş başarılı.");

    }
}
