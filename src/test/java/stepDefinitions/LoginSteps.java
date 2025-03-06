package stepDefinitions;

import io.cucumber.java.en.Then;
import pages.OnlineHesapOzetiPage;
import utils.DriverFactory;

public class LoginSteps {
    private OnlineHesapOzetiPage onlineHesapOzetiPage = new OnlineHesapOzetiPage();

    @Then("Kullanıcı Online Hesap Özeti ana sayfasını görmelidir")
    public void kullanici_online_hesap_ozeti_ana_sayfasini_gormelidir() {
        onlineHesapOzetiPage.verifyLoginSuccess();
    }
}
