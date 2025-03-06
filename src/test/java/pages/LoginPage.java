package pages;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import utils.DriverFactory;

public class LoginPage {
    private WindowsDriver driver;

    public LoginPage() {
        this.driver = DriverFactory.getWinDriver(); // ✅ Doğru metod adı
    }

    public void login(String username, String password, String firma) {
        driver.findElement(By.name("Kullanıcı")).sendKeys(username);
        driver.findElement(By.name("Şifre")).sendKeys(password);
        driver.findElement(By.name("Firma")).sendKeys(firma);
        driver.findElement(By.name("Giriş Yap")).click();
    }
}
