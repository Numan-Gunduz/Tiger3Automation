package utils;

import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Hooks {
    private WindowsDriver driver;

    @Before
    public void setUp() {
        System.out.println("Tiger3Enterprise uygulaması açılıyor...");

        // ✅ Doğru metodu çağırıyoruz
        driver = DriverFactory.getWinDriver();

        // ✅ findElementByName yerine findElement(By.name()) kullanıyoruz
        driver.findElement(By.name("Kullanıcı")).sendKeys("logo");
        driver.findElement(By.name("Şifre")).sendKeys("logo");
        driver.findElement(By.name("Firma")).sendKeys("1");

        WebElement loginButton = driver.findElement(By.name("Giriş Yap"));
        loginButton.click();
        System.out.println("Giriş başarılı!");

        // ✅ Online Hesap Özeti açma
        WebElement onlineHesapOzetiButton = driver.findElement(By.name("Online Hesap Özeti"));
        onlineHesapOzetiButton.click();
        System.out.println("Online Hesap Özeti açıldı!");

        // ✅ Yeni ekranda giriş yap
        driver.findElement(By.name("Kullanıcı adı veya e-posta")).sendKeys("kemal.yapici@elogo.com.tr");
        driver.findElement(By.name("Şifre")).sendKeys("Kemal.12345");

        WebElement onlineLoginButton = driver.findElement(By.name("Giriş"));
        onlineLoginButton.click();
        System.out.println("Online Hesap Özeti giriş başarılı!");
    }

    @After
    public void tearDown() {
        System.out.println("Tiger3Enterprise uygulaması kapatılıyor...");
        DriverFactory.quitDriver();
    }
}
