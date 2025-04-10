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
        this.page = new EkstreAktarimiPage(context.getWindowsDriver());
    }

    @Given("Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir")
    public void kullanici_giris_yapti() {
        System.out.println("✅ Giriş yapılmış ve ana sayfa hazır.");
    }

    @When("Sol menüdeki {string} seçeneğine tıklar")
    public void sol_menu_secenek_tiklar(String menu) {
        page.clickSidebarMenu(menu);
    }

    @When("Banka dropdown'undan {string} seçer")
    public void banka_dropdown_sec(String bankaAdi) {
        page.selectBank(bankaAdi);
    }

    @And("Hesap dropdown'undan {string} hesabını seçer")
    public void hesap_dropdown_sec(String ibanNo) {
        page.selectAccount(ibanNo);
    }

//    @When("{string} butonuna tıklar ve sonuçların yüklenmesi beklenir")
//    public void butona_tikla_ve_bekle(String buttonName) {
//        page.clickButtonByText(buttonName);
//
//    }

    @When("{string} butonuna tıklar ve sonuçların yüklenmesi beklenir")
    public void butona_tikla_ve_bekle(String buttonName) {
        page.clickListeleVeBekle(15); // max 15 saniyeye kadar beklesin
    }


    @When("Yüklenen ekstre kayıtlarından {string} durumundaki bir kaydın solundaki seçim kutusunu işaretler")
    public void eksik_kaydi_sec(String durum) {
        page.selectEksikBilgiKaydi(durum);
    }


    @When("Seçilen kayda sağ tık yapar ve {string} > {string} seçeneğini seçer")
    public void sag_tik_ve_fis_turu_degistir(String menu, String fisTuru) {
        page.changeFisTypeTo(menu, fisTuru);
    }

    @Then("Fiş türünün {string} olarak güncellendiği doğrulanır")
    public void fis_turu_dogrula(String beklenen) {
        Assert.assertTrue(page.isFisTuruUpdated(beklenen));
    }

    @Then("ERP Cari Hesap Kodu alanı boş ise, Durum sütununda {string} yazdığı görülür")
    public void eksik_bilgi_durumu_dogrula(String beklenenDurum) {
        Assert.assertTrue(page.isDurumColumnShows(beklenenDurum));
    }

    @When("ERP Cari Hesap Kodu alanındaki üç noktaya tıklar")
    public void erp_cari_kod_uc_noktaya_tiklar() {
        page.clickErpCariKodDots();
    }

    @When("Açılan pencerede ilk satırdaki cari hesap değerine çift tıklar")
    public void ilk_cari_degerine_cift_tiklar() {
        page.selectFirstCariFromPopup();
    }

    @Then("ERP Cari Hesap Kodu alanı dolduğunda, Durum sütunu {string} olarak güncellenir")
    public void durum_guncellenir_kaydedilebilir(String expected) {
        Assert.assertTrue(page.isDurumColumnShows(expected));
    }
}
