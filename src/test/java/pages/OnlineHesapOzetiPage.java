package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

public class OnlineHesapOzetiPage {
    private WebDriver driver;

    // Online Hesap Özeti butonu
    private final By onlineHesapOzetiButton = By.xpath("//span[contains(text(),'Online Hesap Özeti')]");

    // Kullanıcı adı ve şifre alanları
    private final By emailField = By.id("username"); // ID değişebilir, kontrol et
    private final By passwordField = By.id("password"); // ID değişebilir, kontrol et
    private final By loginButton = By.xpath("//button[contains(text(),'Giriş')]");

    // Başarılı giriş doğrulama elementi
    private final By dashboardTitle = By.xpath("//h1[contains(text(),'Online Hesap Özeti')]");

    public OnlineHesapOzetiPage() {
        this.driver = DriverFactory.getWebDriver();
    }

    // Online Hesap Özeti'ne git
    public void navigateToOnlineHesapOzeti() {
        driver.findElement(onlineHesapOzetiButton).click();
    }

    // Kullanıcı bilgileri ile giriş yap
    public void login(String email, String password) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    // Başarılı giriş doğrulaması
    public void verifyLoginSuccess() {
        boolean isDisplayed = driver.findElement(dashboardTitle).isDisplayed();
        if (!isDisplayed) {
            throw new AssertionError("Online Hesap Özeti giriş başarısız!");
        }
    }
}
