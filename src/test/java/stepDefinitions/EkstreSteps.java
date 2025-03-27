package stepDefinitions;

import base.BaseSteps;
import base.TestContext;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.EkstrePage;
import utils.DriverFactory;

public class EkstreSteps extends BaseSteps {

    private WebDriver driver;
    private EkstrePage ekstrePage;

    public EkstreSteps(TestContext context) {
        super(context);
        if (context.getWebDriver() == null) {
            context.setWebDriver(DriverFactory.startWebApp()); // Web tarafına geçiş yapılır
        }
        this.driver = context.getWebDriver();
        ekstrePage = new EkstrePage(driver);
    }

    @Given("kullanıcı web arayüzü üzerinden Ekstre Aktar menüsüne ulaşır")
    public void kullanici_ekstre_menusu_ac() {
        System.out.println("🌐 Kullanıcı web uygulamasına geçti.");
    }

    @When("kullanıcı menüden Ekstre Aktar alanına tıklar")
    public void kullanici_menuden_ekstreye_tiklar() {
        ekstrePage.tiklaEkstreAktarMenusu();
    }

    @Then("kullanıcı Ekstre Aktar sayfasında olduğunu doğrular")
    public void ekstre_sayfasi_dogrulama() {
        Assert.assertTrue("Ekstre sayfasında değil!", ekstrePage.ekstreSayfasindaMi());
    }

    @Given("kullanıcı Ekstre Aktar sayfasında başlangıç ve bitiş tarihlerini girer")
    public void tarihleri_gir() {
        ekstrePage.tarihGir("01.03.2024", "27.03.2024");
    }

    @And("kullanıcı filtrele butonuna tıklar")
    public void filtrele_butonu() {
        ekstrePage.filtrele();
    }

    @Then("ekrana filtrelenmiş ekstre sonuçları gelmelidir")
    public void ekstre_sonuclari_dogrulama() {
        Assert.assertTrue("Filtrelenmiş sonuç görünmedi!", ekstrePage.sonucGorunurMu());
    }


}
