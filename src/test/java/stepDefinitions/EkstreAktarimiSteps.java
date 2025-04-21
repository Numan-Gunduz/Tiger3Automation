package stepDefinitions;

import base.TestContext;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.EkstreAktarimiPage;

public class EkstreAktarimiSteps {

    TestContext context;
    EkstreAktarimiPage page;

    public EkstreAktarimiSteps(TestContext context) {
        this.context = context;
        this.page = new EkstreAktarimiPage(context.getWebDriver());
    }

    @Given("Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir")
    public void kullanici_giris_yapti() {
        System.out.println("✅ Giriş yapılmış ve ana sayfa hazır.");
    }

    @When("Sol menüdeki {string} seçeneğine tıklar")
    public void sol_menu_sec(String menu) {
        page.clickSidebarMenu(menu);
    }

    @And("Banka dropdown'undan {string} seçer")
    public void banka_dropdown_sec(String banka) {
        page.selectBank(banka);
    }

    @And("Hesap dropdown'undan {string} hesabını seçer")
    public void hesap_dropdown_sec(String iban) {
        page.selectAccount(iban);
    }

    @And("{string} butonuna tıklar ve sonuçların yüklenmesi beklenir")
    public void butona_tikla(String btnText) {
        page.clickListele(); // özel method yaptık zaten
    }

    @And("Yüklenen ekstre kayıtlarından {string} durumundaki bir kaydın solundaki seçim kutusunu işaretler")
    public void ekstre_kaydi_sec(String durum) {
        page.selectRowWithDurum(durum);
    }

    @And("Seçilen kayda sağ tık yapar ve {string} > {string} seçeneğini seçer")
    public void sagTiklaVeFisTuruDegistir(String menu, String fisTuru) {
        page.changeFisTypeTo(menu, fisTuru);
    }

    @Then("Fiş türünün {string} olarak güncellendiği doğrulanır")
    public void fisTuruDogrulama(String text) {
        assert page.isFisTuruUpdated(text);
    }

    @And("ERP Cari Hesap Kodu alanı boş ise, Durum sütununda {string} yazdığı görülür")
    public void durumKontrol(String durum) {
        assert page.isDurumColumnShows(durum);
    }

    @When("ERP Cari Hesap Kodu alanındaki üç noktaya tıklar")
    public void ucNoktayaTiklar() {
        page.clickErpCariKodDots();
    }

    @And("Açılan pencerede ilk satırdaki cari hesap değerine çift tıklar")
    public void cariSec() {
        page.selectFirstCariFromPopup();
    }

    @Then("ERP Cari Hesap Kodu alanı dolduğunda, Durum sütunu {string} olarak güncellenir")
    public void durumKaydedilebilir(String durum) {
        assert page.isDurumColumnShows(durum);
    }
}

