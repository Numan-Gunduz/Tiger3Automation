//package stepDefinitions;
//
//import base.TestContext;
//import io.cucumber.java.en.*;
//import org.openqa.selenium.WebElement;
//import org.testng.Assert;
//import pages.EkstreAktarimiPage;
//import utils.DriverFactory;
//
//import static org.junit.Assert.assertTrue;
//
//public class EkstreAktarimiSteps {
//
//    TestContext context;
//    EkstreAktarimiPage page;
//    private String kayitliErpFisNo;
//
//    public EkstreAktarimiSteps(TestContext context) {
//        this.context = context;
//        this.page = new EkstreAktarimiPage(context);
//
//    }
//
//    @Given("Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir")
//    public void kullanici_giris_yapti() {
//        System.out.println("✅ Giriş yapılmış ve ana sayfa hazır.");
//    }
//
//    @When("Sol menüdeki {string} seçeneğine tıklar")
//    public void sol_menu_sec(String menu) {
//        page.clickSidebarMenu(menu);
//    }
//
//    @And("Banka dropdown'undan {string} seçer")
//    public void banka_dropdown_sec(String banka) {
//        page.selectBank(banka);
//    }
//
//    @And("Hesap dropdown'undan {string} hesabını seçer")
//    public void hesap_dropdown_sec(String iban) {
//        page.selectAccount(iban);
//    }
//    @And("Kullanıcı başlangıç tarihi olarak bugünden {int} gün önceki tarihi girer")
//    public void kullanici_dinamik_baslangic_tarihi_girer(int daysAgo) {
//        page.enterStartDateDaysAgo(daysAgo);
//    }
//
//
//
//    @And("{string} butonuna tıklar ve sonuçların yüklenmesi beklenir")
//    public void butona_tikla(String btnText) {
//        page.clickListele(); // özel method yaptık zaten
//    }
//
//    @And("Yüklenen ekstre kayıtlarından {string} durumundaki bir kaydın solundaki seçim kutusunu işaretler")
//    public void ekstre_kaydi_sec(String durum) {
//        page.selectRowWithDurum(durum);
//    }
//
//    @And("Seçilen kayda sağ tık yapar ve {string} > {string} seçeneğini seçer")
//    public void sagTiklaVeFisTuruDegistir(String menu, String fisTuru) {
//        page.changeFisTypeTo(menu, fisTuru);
//    }
//
//    @Then("Fiş türünün {string} olarak güncellendiği doğrulanır")
//    public void fisTuruDogrulama(String expectedText) {
//        boolean result = page.isFisTuruUpdated(expectedText);
//        Assert.assertTrue(result, "❌ Fiş türü beklenen gibi güncellenmedi! Beklenen: " + expectedText);
//        System.out.println("✅ Fiş türü doğru şekilde güncellendi: " + expectedText);
//    }
//
//
//    @And("ERP Cari Hesap Kodu boş olan satırda, Durum alanı {string} olmalıdır")
//    public void validateDurumByExpectedValue(String expectedDurumText) {
//        boolean result = page.validateDurumForEmptyCariHesap(expectedDurumText);
//        Assert.assertTrue(result, "❌ Doğrulama başarısız! ERP Cari Hesap boşken durum yanlış.");
//    }
//
//
//
//
//    @When("ERP Cari Hesap Kodu alanındaki üç noktaya tıklar")
//    public void ucNoktayaTiklar() {
//        page.clickErpCariKodDots();
//    }
//
//    @And("Açılan pencerede seç butonuna tıklar")
//    public void cariSec() {
//        page.clickSelectButtonOnCariPopup();
//    }
//
//    @Then("ERP Cari Hesap Kodu alanı dolduğunda, Durum sütunu {string} olarak güncellenir")
//    public void durumKaydedilebilir(String expectedDurum) {
//        boolean result = page.isDurumKaydedilebilirGorunuyor();
//        Assert.assertTrue(result, "❌ Durum sütunu 'Kaydedilebilir' değil!");
//        System.out.println("✅ Durum sütunu doğru şekilde 'Kaydedilebilir' oldu.");
//    }
//    @And("Fiş oluştur butonuna tıklar")
//    public void fisOlusturTiklar() {
//        page.clickFisOlusturButton();
//    }
//
//    @And("Açılan onay popup'ında Evet'e tıklar")
//    public void evetTiklar() {
//        page.clickEvetOnConfirmationPopup();
//    }
//
//    @Then("Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir")
//    public void toastGorunur() {
//        boolean result = page.isSuccessToastMessageVisible();
//        Assert.assertTrue(result, "❌ Toast başarı mesajı görünmedi!");
//    }
//
//    @Then("Durum alanı Eşlendi olarak güncellenmelidir")
//    public void durumEslendiOlmali() {
//        boolean result = page.isDurumEslendiGorunuyor();
//        Assert.assertTrue(result, "❌ Durum 'Eşlendi' değil!");
//    }
//
//    @Then("ERP Fiş No alanı dolu olmalıdır")
//    public void erpFisNoDoluOlmali() {
//        Assert.assertTrue(page.isErpFisNoDoluMu(), "❌ ERP Fiş No alanı boş!");
//
//    }
//    @When("Kullanıcı sağ tıklayıp {string} seçeneğini tıklar")
//    public void kullanici_sag_tiklayip_fis_incele_secenegini_tiklar(String secenek) {
//        kayitliErpFisNo = page.getErpFisNoFromSelectedRow(); // ERP fiş no'yu kaydet
//        page.openFisPopupFromContextMenu(secenek);
//    }
//
////    @Then("Açılan ekrandaki Fiş No alanı ile ERP Fiş No değeri aynı olmalıdır")
////    public void acilan_ekranda_fis_no_alanı_dogrulanmali() {
////        String popupFisNo = page.getFisNoFromPopup();
////        System.out.println("🔍 ERP'den alınan Fiş No: " + kayitliErpFisNo);
////        System.out.println("🔍 Açılan popup'taki Fiş No: " + popupFisNo);
////        Assert.assertEquals(popupFisNo, kayitliErpFisNo, "❌ Fiş no uyuşmuyor!");
////        System.out.println("✅ Açılan ekrandaki fiş no doğrulandı.");
////    }
//
//    @Then("Açılan ekrandaki Fiş No alanı ile ERP Fiş No değeri aynı olmalıdır")
//    public void acilan_ekranda_fis_no_alanı_dogrulanmali() {
//        String popupFisNo = page.getFisNoFromPopup();
//        System.out.println("🔍 ERP'den alınan Fiş No: " + kayitliErpFisNo);
//        System.out.println("🔍 Açılan popup'taki Fiş No: " + popupFisNo);
//        Assert.assertEquals(popupFisNo, kayitliErpFisNo, "❌ Fiş no uyuşmuyor!");
//        System.out.println("✅ Açılan ekrandaki fiş no doğrulandı.");
//    }
//
//
//    /*jsdıooooooooooooooooooooooooooooo*/
//
//    @And("Banka kodu için Açılan pencerede seç butonuna tıklar")
//    public void cariSecBankaKodu() {
//        page.clickSelectButtonOnCariPopupBankaKodu();
//    }
//
//    @And("Yüklenen ekstre kayıtlarından {string} veya {string} durumundaki bir kaydın solundaki seçim kutusunu işaretler")
//    public void ekstreKaydiSec_VeyaDurum(String durum1, String durum2) {
//        page.selectRowWithDurumOrDurum(durum1, durum2);
//    }
//
//    @And("ERP Banka Hesap Kodu boş olan satırda, Durum alanı {string} olmalıdır")
//    public void validateDurumByExpectedValue_BankaKod(String expectedDurumText) {
//        boolean result = page.validateDurumForEmptyBankaHesap(expectedDurumText);
//        Assert.assertTrue(result, "❌ Doğrulama başarısız! ERP Banka Hesap Kodu boşken durum yanlış.");
//    }
//
//    @When("ERP Banka Hesap Kodu alanındaki üç noktaya tıklar")
//    public void ucNoktayaTiklar_BankaKod() {
//        page.clickErpBankaKodDots();
//    }
//
//    @Then("ERP Banka Hesap Kodu alanı dolduğunda, Durum sütunu {string} olarak güncellenir")
//    public void durumKaydedilebilir_BankaKod(String expectedDurum) {
//        boolean result = page.isDurumKaydedilebilirBankaKod();
//        Assert.assertTrue(result, "❌ Durum sütunu '" + expectedDurum + "' değil!");
//        System.out.println("✅ Durum sütunu doğru şekilde '" + expectedDurum + "' oldu.");
//    }
//
//
//
//}
//














// ✅ EkstreAktarimiSteps.java
package stepDefinitions;

import base.TestContext;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.EkstreAktarimiPage;

public class EkstreAktarimiSteps {
    private final EkstreAktarimiPage page;
    private String kayitliErpFisNo;
    private String selectedFisTuru;

    public EkstreAktarimiSteps(TestContext context) {
        this.page = new EkstreAktarimiPage(context);
    }

    @Given("Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir")
    public void kullaniciGirisYapti() {
        System.out.println("✅ Giriş yapılmış ve ana sayfa hazır.");
    }

    @When("Sol menüdeki {string} seçeneğine tıklar")
    public void solMenuSec(String menu) {
        page.clickSidebarMenu(menu);
    }

    @And("Banka dropdown'undan {string} seçer")
    public void bankaDropdownSec(String banka) {
        page.selectBank(banka);
    }

    @And("Hesap dropdown'undan {string} hesabını seçer")
    public void hesapDropdownSec(String iban) {
        page.selectAccount(iban);
    }

    @And("Kullanıcı başlangıç tarihi olarak bugünden {int} gün önceki tarihi girer")
    public void kullaniciBaslangicTarihiGirer(int daysAgo) {
        page.enterStartDateDaysAgo(daysAgo);
    }

    @And("{string} butonuna tıklar ve sonuçların yüklenmesi beklenir")
    public void listeleButonunaTikla(String buton) {
        page.clickListele();
    }

    @And("Yüklenen ekstre kayıtlarından {string} veya {string} durumundaki bir kaydın solundaki seçim kutusunu işaretler")
    public void durumluSatirSec(String durum1, String durum2) {
        page.selectRowWithDurumOrDurum(durum1, durum2);
    }

    @And("Seçilen kayda sağ tık yapar ve {string} > {string} seçeneğini seçer")
    public void sagTiklaVeFisTuruDegistir(String menu, String fisTuru) {
        page.changeFisTypeTo(menu, fisTuru);
    }

    @Then("Fiş türünün {string} olarak güncellendiği doğrulanır")
    public void fisTuruGuncellendiMi(String expected) {
        Assert.assertTrue(page.isFisTuruUpdated(expected), "❌ Fiş türü güncellenmedi: " + expected);
    }

    @And("Kullanıcı {string} boş olan satırda, Durum alanı {string} olmalıdır")
    public void alanBoskenDurumKontrol(String alanTipi, String beklenenDurum) {
        boolean result = page.validateDurumForEmptyField(alanTipi, beklenenDurum);
        Assert.assertTrue(result, "❌ Alan boşken beklenen durum sağlanmadı.");
    }

    @When("Kullanıcı {string} alanındaki üç noktaya tıklar")
    public void ucNoktaTikla(String alanTipi) {
        if (alanTipi == null || alanTipi.trim().isEmpty()) {
            System.out.println("ℹ️ Alan tipi boş, üç nokta adımı atlanıyor.");
            return;
        }
        page.clickThreeDotsForField(alanTipi);
    }

    @And("Kullanıcı {string} için açılan pencerede seç butonuna tıklar")
    public void secButonunaTikla(String alanTipi) {
        if (alanTipi == null || alanTipi.trim().isEmpty()) {
            System.out.println("ℹ️ Alan tipi boş, seç butonu adımı atlanıyor.");
            return;
        }
        page.clickSelectButtonForField(alanTipi);
    }
//
//    @Then("{string} dolduğunda, Durum sütunu {string} olarak güncellenir")
//    public void alanDoldugundaDurumKontrol(String alanTipi, String beklenenDurum) {
//        boolean result = page.checkDurumUpdatedAfterFieldFill(alanTipi, beklenenDurum);
//        Assert.assertTrue(result, "❌ Alan doldurulduğunda beklenen durum sağlanmadı.");
//    }

    @Then("{string} dolduğunda, Durum sütunu {string} olarak güncellenir")
    public void alanDoldugundaDurumKontrol(String alanTipi, String beklenenDurum) {
        boolean result;

        if (alanTipi == null || alanTipi.trim().isEmpty()) {
            result = page.checkDurumOnly(beklenenDurum); // yeni metot: sadece Durum kontrolü
        } else {
            result = page.checkDurumUpdatedAfterFieldFill(alanTipi, beklenenDurum);
        }

        Assert.assertTrue(result, "❌ Alan doldurulduğunda beklenen durum sağlanmadı.");
    }


    @And("Fiş oluştur butonuna tıklar")
    public void fisOlusturTiklar() {
        page.clickFisOlusturButton();
    }

    @And("Açılan onay popup'ında Evet'e tıklar")
    public void evetPopupTikla() {
        page.clickEvetOnConfirmationPopup();
    }

    @Then("Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir")
    public void basariToastGorunur() {
        Assert.assertTrue(page.isSuccessToastMessageVisible(), "❌ Toast mesajı görünmedi.");
    }

    @Then("Durum alanı Eşlendi olarak güncellenmelidir")
    public void durumEslendiMi() {
        Assert.assertTrue(page.isDurumEslendiGorunuyor(), "❌ Durum 'Eşlendi' değil.");
    }

    @Then("ERP Fiş No alanı dolu olmalıdır")
    public void erpFisNoDoluMu() {
        Assert.assertTrue(page.isErpFisNoDoluMu(), "❌ ERP Fiş No alanı boş.");
    }

@When("Kullanıcı sağ tıklayıp {string} seçeneğini tıklar")
public void sagTiklaPopupSecenek(String popupSecenek) {
    selectedFisTuru = page.getCurrentFisTuru(); // → sayfada aktif tür alınmalı (veya senaryodan alınmalı)
    kayitliErpFisNo = page.getErpFisNoFromSelectedRow();
    page.openFisPopupFromContextMenu(popupSecenek);
}


@Then("Açılan ekrandaki Fiş No alanı ile ERP Fiş No değeri aynı olmalıdır")
public void fisNoDogruMu() {
    String popupFisNo;

    if (selectedFisTuru.toLowerCase().contains("hizmet")) {
        popupFisNo = page.getFisNoFromPopup_HizmetFaturasi();
    } else {
        popupFisNo = page.getFisNoFromPopup_Classic();
    }

    System.out.println("🔍 ERP'den alınan Fiş No: " + kayitliErpFisNo);
    System.out.println("🔍 Açılan popup'taki Fiş No: " + popupFisNo);
    Assert.assertEquals(popupFisNo, kayitliErpFisNo, "❌ Fiş No uyuşmuyor!");
}




    @And("Açılan popup pencereleri kapatır")
    public void popupPencereleriKapatir() {
        page.closeOpenPopupsOneByOne();
    }



}
