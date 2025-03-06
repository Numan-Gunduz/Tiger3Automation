package stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.OnlineHesapOzetiPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlineHesapOzetiSteps {
    private OnlineHesapOzetiPage onlineHesapOzetiPage;

    public OnlineHesapOzetiSteps() {
        this.onlineHesapOzetiPage = new OnlineHesapOzetiPage();
    }

    @When("Kullanıcı Online Hesap Özeti sayfasına gider")
    public void kullanıcı_online_hesap_özeti_sayfasına_gider() {
        onlineHesapOzetiPage.navigateToOnlineHesapOzeti();
    }

    @When("Kullanıcı Online Hesap Özeti giriş bilgilerini girer {string}, {string}")
    public void kullanıcı_online_hesap_özeti_giriş_bilgilerini_girer(String username, String password) {
        onlineHesapOzetiPage.login(username, password);
    }

    @Then("Kullanıcı Online Hesap Özeti ana sayfasını görmelidir")
    public void kullanıcı_online_hesap_özeti_ana_sayfasını_görmelidir() {
        assertTrue(onlineHesapOzetiPage.verifyLoginSuccess(), "Giriş başarısız oldu!");
    }
}
