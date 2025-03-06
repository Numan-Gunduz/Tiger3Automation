package pages;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import utils.DriverFactory;

public class LoginPage {
    private WindowsDriver driver;

    public LoginPage() {
        this.driver = DriverFactory.getWinDriver();
    }

    // Tiger3 Login ekranındaki elementlerin tanımlanması
    private By usernameField = By.name("Kullanıcı");
    private By passwordField = By.name("Şifre");
    private By firmaField = By.className("Edit"); // Firma alanı için sınıf ismi kullanıyoruz
    private By girisButton = By.name("Giriş Yap");

    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void enterFirma(String firma) {
        driver.findElement(firmaField).sendKeys(firma);
    }

    public void clickLogin() {
        driver.findElement(girisButton).click();
    }

    public void login(String username, String password, String firma) {
        enterUsername(username);
        enterPassword(password);
        enterFirma(firma);
        clickLogin();
    }
}
