package stepDefinitions;

import base.TestContext;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.EkstreAktarimiPage;

import static org.junit.Assert.assertTrue;

public class EkstreAktarimiSteps {

    TestContext context;
    EkstreAktarimiPage page;

    public EkstreAktarimiSteps(TestContext context) {
        this.context = context;
        this.page = new EkstreAktarimiPage(context);

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
    @And("Kullanıcı başlangıç tarihi olarak bugünden {int} gün önceki tarihi girer")
    public void kullanici_dinamik_baslangic_tarihi_girer(int daysAgo) {
        page.enterStartDateDaysAgo(daysAgo);
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
    public void fisTuruDogrulama(String expectedText) {
        boolean result = page.isFisTuruUpdated(expectedText);
        Assert.assertTrue(result, "❌ Fiş türü beklenen gibi güncellenmedi! Beklenen: " + expectedText);
        System.out.println("✅ Fiş türü doğru şekilde güncellendi: " + expectedText);
    }


    @And("ERP Cari Hesap Kodu boş olan satırda, Durum alanı {string} olmalıdır")
    public void validateDurumByExpectedValue(String expectedDurumText) {
        boolean result = page.validateDurumForEmptyCariHesap(expectedDurumText);
        Assert.assertTrue(result, "❌ Doğrulama başarısız! ERP Cari Hesap boşken durum yanlış.");
    }




    @When("ERP Cari Hesap Kodu alanındaki üç noktaya tıklar")
    public void ucNoktayaTiklar() {
        page.clickErpCariKodDots();
    }

    @And("Açılan pencerede seç butonuna tıklar")
    public void cariSec() {
        page.clickSelectButtonOnCariPopup();
    }

    @Then("ERP Cari Hesap Kodu alanı dolduğunda, Durum sütunu {string} olarak güncellenir")
    public void durumKaydedilebilir(String expectedDurum) {
        boolean result = page.isDurumKaydedilebilirGorunuyor();
        Assert.assertTrue(result, "❌ Durum sütunu 'Kaydedilebilir' değil!");
        System.out.println("✅ Durum sütunu doğru şekilde 'Kaydedilebilir' oldu.");
    }

}

