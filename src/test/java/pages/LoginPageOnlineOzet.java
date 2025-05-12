
package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementHelper;

import java.time.Duration;

public class LoginPageOnlineOzet {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPageOnlineOzet(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void loginIfRequired(String username, String password) {
        ElementHelper.waitForPageLoad(driver, 15);

        // Kullanıcı zaten login olduysa kontrol et
        boolean alreadyLoggedIn = driver.getPageSource().contains("Hoş Geldin Kemal Yapıcı");
        if (alreadyLoggedIn) {
            System.out.println("Kullanıcı zaten giriş yapmış (refresh token ile).");
            return;
        }

        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UserName")));
            String currentUsername = usernameField.getAttribute("value").trim();
            System.out.println("Kullanıcı adı alanındaki mevcut değer: " + currentUsername);

            if (!currentUsername.equalsIgnoreCase(username)) {
                usernameField.clear();
                ElementHelper.clickAndWait(usernameField);
                ElementHelper.typeTextSmart(usernameField, username);
                System.out.println(" Kullanıcı adı yeniden yazıldı: " + username);
            } else {
                System.out.println(" Kullanıcı adı zaten doğru, yazılmadı.");
            }

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));
            ElementHelper.clickAndWait(passwordField);
            ElementHelper.typeTextSmart(passwordField, password);
            System.out.println("Şifre yazıldı.");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
            loginButton.click();
            System.out.println("Giriş butonuna tıklandı.");

            // Dinamik bekleme: 'Hoş Geldin' yazısının yüklenmesini bekle
            By welcomeText = By.cssSelector("div.title-text");
            ElementHelper.waitForTextInElement(driver, welcomeText, "Hoş Geldin", 20);
            ElementHelper.waitForTextInElement(driver, welcomeText, "Kemal Yapıcı", 20);

// Element gerçekten sayfada görünür mü? Ek güvenlik için:
            WebElement welcomeElement = driver.findElement(welcomeText);
            Assert.assertTrue(" Giriş başarısız! 'Hoş Geldin' metni görünmüyor.", welcomeElement.isDisplayed());
            System.out.println(" Giriş başarılı.");

        } catch (Exception e) {
            throw new RuntimeException(" Login ekranında hata oluştu: " + e.getMessage(), e);
        }
    }
}
