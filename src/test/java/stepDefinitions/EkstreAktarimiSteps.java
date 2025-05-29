
package stepDefinitions;

import base.TestContext;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.EkstreAktarimiPage;
import utils.ElementHelper;

public class EkstreAktarimiSteps {
    private final EkstreAktarimiPage page;
    private String kayitliErpFisNo;
    private String selectedFisTuru;

    public EkstreAktarimiSteps(TestContext context) {
        this.page = new EkstreAktarimiPage(context);
    }

    @Given("Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir")
    public void kullaniciGirisYapti() {
        System.out.println(" Giriş yapılmış ve ana sayfa hazır.");
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
        String currentBank = page.getCurrentlySelectedBank();
        page.selectAccount(iban, currentBank);
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
        Assert.assertTrue(page.isFisTuruUpdated(expected), "Fiş türü güncellenmedi: " + expected);
    }

    @And("Kullanıcı {string} boş olan satırda, Durum alanı {string} olmalıdır")
    public void alanBoskenDurumKontrol(String alanTipi, String beklenenDurum) {
        boolean result = page.validateDurumForEmptyField(alanTipi, beklenenDurum);
        Assert.assertTrue(result, "Alan boşken beklenen durum sağlanmadı.");
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
            System.out.println("Alan tipi boş, seç butonu adımı atlanıyor.");
            return;
        }
        page.clickSelectButtonForField(alanTipi);
    }


    @Then("{string} dolduğunda, Durum sütunu {string} olarak güncellenir")
    public void alanDoldugundaDurumKontrol(String alanTipi, String beklenenDurum) {
        boolean result;

        if (alanTipi == null || alanTipi.trim().isEmpty()) {
            result = page.checkDurumOnly(beklenenDurum); // yeni metot: sadece Durum kontrolü
        } else {
            result = page.checkDurumUpdatedAfterFieldFill(alanTipi, beklenenDurum);
        }

        Assert.assertTrue(result, "Alan doldurulduğunda beklenen durum sağlanmadı.");
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
        Assert.assertTrue(page.isSuccessToastMessageVisible(), "Toast mesajı görünmedi.");
    }

    @Then("Durum alanı Eşlendi olarak güncellenmelidir")
    public void durumEslendiMi() {
        Assert.assertTrue(page.isDurumEslendiGorunuyor(), "Durum 'Eşlendi' değil.");
    }

    @Then("ERP Fiş No alanı dolu olmalıdır")
    public void erpFisNoDoluMu() {
        Assert.assertTrue(page.isErpFisNoDoluMu(), "ERP Fiş No alanı boş.");
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

    System.out.println(" ERP'den alınan Fiş No: " + kayitliErpFisNo);
    System.out.println(" Açılan popup'taki Fiş No: " + popupFisNo);
    Assert.assertEquals(popupFisNo, kayitliErpFisNo, " Fiş No uyuşmuyor!");
}


    @And("Açılan popup pencereleri kapatır")
    public void popupPencereleriKapatir() {
        page.closeOpenPopupsOneByOne();
    }

    @And("Tutarı negatif ve Durumu {string} olan kaydın checkbox'ını işaretler")
    public void negatifVeDurumluSatirSec(String durum) {
        page.selectRowWithNegativeTutarAndDurum(durum);
    }
    @And("Tutarı pozitif ve Durumu {string} olan kaydın checkbox'ını işaretler")
    public void pozitifVeDurumluSatirSec(String durum) {
        page.selectRowWithPositiveTutarAndDurum(durum);
    }


    @When("Kullanıcı ERP Kasa Kodu alanına göre Kasa İşlemleri'ne gider")
    public void kasaIslemlerineGider() {
        page.navigateToKasaIslemleriFromGlobalSearch();
    }
    @When("Kasa İşlemleri ekranında ERP Fiş No satırına çift tıklar")
    public void erpFisNoSatirinaCiftTiklar() {
        page.openKasaFormByFicheNo();
    }
    @Then("Açılan form Bankadan Çekilen formu olmalıdır")
    public void bankadanCekilenFormuAcildiMi() {
        Assert.assertTrue(page.isBankadanCekilenFormAcildi(), " Açılan form 'Bankadan Çekilen' değil.");
    }
    @Then("Açılan form Bankaya Yatırılan formu olmalıdır")
    public void bankayaYatirilanFormuAcildiMi() {
        Assert.assertTrue(page.isBankayaYatirilanFormAcildi(), "Açılan form 'Bankaya Yatırılan' değil.");
    }

    @Then("Açılan kayıt Kasa İşlem No ile eşleşmeli ve form ekranı açılmalıdır")
    public void kasaFormuAcilmali() {
        Assert.assertTrue(page.verifyKasaFormOpenedWithCorrectFicheNo(),
                " Bankadan Çekilen formu açılmadı veya işlem no uyuşmuyor.");
    }
    @And("kasa işlemleri için Açılan popup pencereleri kapatır")
    public void kasaIslemleripopupPencereleriKapatir() {
        page.closeOpenPopupsOneByOneforKasa();
    }


    @And("Online Hesap Özeti uygulamasına tıklar")
    public void onlineHesapÖzetiUygulamasınaTıklar()
    {
        page.clickOnlineHesapOzetiApp();
    }

    @Then("Açılan form ekranındaki fiş Banka işlem fişi olmalıdır")
    public void bankaIslemFısıAcildiMi() {
        Assert.assertTrue(page.isBankaislemFisiFormAcildi(), "Açılan form 'Banka İşlem Fişi' değil.");
    }

    @Then("İlgili kaydın fiş türü {string} olarak değişmeden kaldığı doğrulanır")
    public void fisTuruDegismediMi(String beklenenDeger) {
        Assert.assertTrue(
                page.isFisTuruDegismedi(beklenenDeger),
                " Fiş türü yanlışlıkla değişmiş: '" + beklenenDeger + "' oldu!"
        );
    }


}
