package pages;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementHelper;

import java.util.List;

public class EkstreAktarimiPage {

    private final WindowsDriver<WebElement> driver;

    private final WebDriverWait wait;
    public EkstreAktarimiPage(WindowsDriver<WebElement> driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
    }

    public void clickSidebarMenu(String menuName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name(menuName)));
        element.click();
    }
    public void selectBank(String bankaAdi) {
        WebElement bankaDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.className("ant-select-selector")));
        bankaDropdown.click();

        try {
            Thread.sleep(1000);
            WebElement element = driver.findElement(By.xpath("//*[contains(@title, '" + bankaAdi + "')]"));
            ElementHelper.clickByRobot(element);
            System.out.println("✅ '" + bankaAdi + "' başarıyla seçildi (Robot ile XPath).");
        } catch (Exception e) {
            System.out.println("⚠️ XPath ile de bulunamadı. Koordinat ile deneniyor...");
            ElementHelper.clickByCoordinates(267, 400); // Inspect çıktısından alınan gerçek nokta
        }
    }




    public void selectAccount(String hesapNo) {
        try {
            WebElement hesapDropdown = driver.findElements(By.className("ant-select-selector")).get(1);
            wait.until(ExpectedConditions.elementToBeClickable(hesapDropdown)).click();
            Thread.sleep(1000);

            // XPath ile öğeyi bul ve Robot ile tıkla
            WebElement element = driver.findElement(By.xpath("//*[text()='" + hesapNo + "']"));
            ElementHelper.clickByRobot(element);
            System.out.println("✅ IBAN başarıyla seçildi: " + hesapNo);
        } catch (Exception e) {
            System.out.println("⚠️ XPath ile seçim başarısız. Koordinat ile deneniyor...");
            ElementHelper.clickByCoordinates(550, 340); // IBAN'ın gerçek koordinatı
        }
    }




    public void clickListeleVeBekle(int maxWaitSeconds) {
        WebElement listeleButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("Listele")));
        listeleButton.click();

        // Kolonlardan herhangi biri yüklendi mi diye kontrol
        By kolonKontrol = By.name("Fiş Türü"); // örnek bir kolon başlığı
        new WebDriverWait(driver, maxWaitSeconds)
                .until(ExpectedConditions.presenceOfElementLocated(kolonKontrol));
    }

    public void selectRowWithDurum(String durumText) {
        List<WebElement> durumHucres = driver.findElements(By.xpath("//*[contains(@Name, '" + durumText + "')]"));
        for (WebElement durumHucre : durumHucres) {
            WebElement checkbox = getCheckboxFromRow(durumHucre);
            if (checkbox != null && !checkbox.isSelected()) {
                checkbox.click();
                break;
            }
        }
    }

    private WebElement getCheckboxFromRow(WebElement durumHucre) {
        try {
            WebElement row = durumHucre.findElement(By.xpath("ancestor::*[@ClassName='DataGridRow']"));
            return row.findElement(By.className("CheckBox"));
        } catch (Exception e) {
            System.out.println("⚠️ Satırdan checkbox alınamadı.");
            return null;
        }
    }

    public void changeFisTypeTo(String menu, String fisTuru) {
        Actions actions = new Actions(driver);
        WebElement selectedRow = driver.findElement(By.xpath("//*[contains(@Name, 'Seçildi')]")); // örnek: seçilen satır

        actions.contextClick(selectedRow).perform();

        WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.name(menu)));
        menuElement.click();

        WebElement fisOption = wait.until(ExpectedConditions.elementToBeClickable(By.name(fisTuru)));
        fisOption.click();
    }

    public boolean isFisTuruUpdated(String expectedText) {
        try {
            WebElement updatedCell = driver.findElement(By.xpath("//*[contains(@Name, '" + expectedText + "')]"));
            return updatedCell != null && updatedCell.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDurumColumnShows(String durumText) {
        try {
            WebElement durum = driver.findElement(By.xpath("//*[contains(@Name, '" + durumText + "')]"));
            return durum != null && durum.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickErpCariKodDots() {
        List<WebElement> ucNoktaButonlari = driver.findElements(By.name("..."));
        for (WebElement ucNokta : ucNoktaButonlari) {
            // Muhtemelen boş olan alanların yanında olur
            if (ucNokta.isDisplayed()) {
                ucNokta.click();
                break;
            }
        }
    }

    public void selectFirstCariFromPopup() {
        WebElement firstCari = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//DataItem[1]")));
        Actions actions = new Actions(driver);
        actions.doubleClick(firstCari).perform();
    }
}
