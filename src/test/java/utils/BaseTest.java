package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pages.LoginPage;
import pages.OnlineHesapOzetiPage;

public class BaseTest {
    protected LoginPage loginPage;
    protected OnlineHesapOzetiPage onlineHesapOzetiPage;

    @BeforeEach
    public void setup() {
        // Tiger3 uygulamasını başlat
        DriverFactory.startTiger3();

        // Tiger3 Login işlemini gerçekleştir
        loginPage = new LoginPage();
        loginPage.login("logo", "logo", "1");

        // Online Hesap Özeti Uygulamasına giriş yap
        onlineHesapOzetiPage = new OnlineHesapOzetiPage();
        onlineHesapOzetiPage.navigateToOnlineHesapOzeti();
        onlineHesapOzetiPage.login("kemal.yapici@elogo.com.tr", "Kemal.12345");
        onlineHesapOzetiPage.verifyLoginSuccess();
    }

    @AfterEach
    public void tearDown() {
        // Testten sonra driver'ları kapat
        DriverFactory.closeDrivers();
    }
}
