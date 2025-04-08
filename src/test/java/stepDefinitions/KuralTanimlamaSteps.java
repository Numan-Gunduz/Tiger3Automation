package stepDefinitions;

import base.TestContext;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import pages.KuralTanimlamaPage;

public class KuralTanimlamaSteps {

    TestContext context;
    KuralTanimlamaPage page;

    public KuralTanimlamaSteps(TestContext context) {
        this.context = context;
        this.page = new KuralTanimlamaPage(context.getWindowsDriver());
    }


    @Given("Kullanıcı ana sayfaya başarıyla giriş yapmış")
    public void kullanici_ana_sayfada() {
        System.out.println("✅ Kullanıcı anasayfada.");
    }

    @When("Kullanıcı sol menüden {string} seçeneğine tıklar")
    public void kullanici_sol_menude_tiklar(String menuAdi) {
        page.clickSidebarMenu(menuAdi);
    }

    @When("Kullanıcı {string} butonuna tıklar")
    public void kullanici_butona_tiklar(String butonText) {
        page.clickButtonByText(butonText);
    }

    @Then("{string} sayfasını görmeli")
    public void sayfa_basligi_dogrulama(String expectedText) {
        Assert.assertTrue(page.isPageTitleDisplayed(expectedText), "Sayfa başlığı doğrulanamadı");
    }
}
