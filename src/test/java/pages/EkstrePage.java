package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EkstrePage {

    private final WebDriver driver;

    public EkstrePage(WebDriver driver) {
        this.driver = driver;
    }

    // Web elementi tanımlamaları
    private final By ekstreAktarMenu = By.xpath("//span[text()='Ekstre Aktar']");
    private final By sayfaBasligi = By.xpath("//h1[text()='Ekstre Aktar']");

    private final By baslangicTarihiInput = By.id("startDate");
    private final By bitisTarihiInput = By.id("endDate");
    private final By filtreleButton = By.id("filter");

    private final By sonucTablosu = By.id("resultTable");

    public void tiklaEkstreAktarMenusu() {
        driver.findElement(ekstreAktarMenu).click();
    }

    public boolean ekstreSayfasindaMi() {
        return driver.findElement(sayfaBasligi).isDisplayed();
    }

    public void tarihGir(String baslangic, String bitis) {
        driver.findElement(baslangicTarihiInput).sendKeys(baslangic);
        driver.findElement(bitisTarihiInput).sendKeys(bitis);
    }

    public void filtrele() {
        driver.findElement(filtreleButton).click();
    }

    public boolean sonucGorunurMu() {
        return driver.findElement(sonucTablosu).isDisplayed();
    }
}
