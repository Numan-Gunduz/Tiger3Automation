package pages;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import utils.DriverFactory;

public class LoginPage {
    private WindowsDriver driver;

    // Element locators
    private final By usernameField = By.name("Kullanıcı");
    private final By passwordField = By.name("Şifre");
    private final By companyField = By.name("Firma");
    private final By loginButton = By.name("Giriş Yap");

    public LoginPage() {
        this.driver = DriverFactory.getWinDriver();
    }

    public void login(String username, String password, String company) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(companyField).sendKeys(company);
        driver.findElement(loginButton).click();
    }
}
