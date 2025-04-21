package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementHelper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.Duration;
import java.util.List;

public class EkstreAktarimiPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public EkstreAktarimiPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));;
    }

    public void clickSidebarMenu(String menuName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class, 'ant-menu-title-content') and text()='Ekstre Aktarımı']")));
        element.click();

    }

    public void selectBank(String bankaAdi) {
        try {
            // "Banka" başlığına göre ilgili dropdown'ı bul ve tıkla
            WebElement bankaDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Banka']/ancestor::div[contains(@id,'select_container')]//div[contains(@class,'ant-select-selector')]")
            ));
            bankaDropdown.click();

            // Dropdown görünür hale gelene kadar bekle
            wait.until(driver -> {
                List<WebElement> dropdowns = driver.findElements(By.className("ant-select-dropdown"));
                return dropdowns.stream().anyMatch(e -> !e.getAttribute("class").contains("hidden"));
            });

            // İlgili banka seçeneğini bul ve tıkla
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='ant-select-item-option-content' and text()='" + bankaAdi + "']")
            ));
            option.click();

            System.out.println("✅ Banka seçildi: " + bankaAdi);
        } catch (Exception e) {
            System.out.println("❌ Banka seçimi hatası: " + e.getMessage());
            throw e;
        }
    }

    public void selectAccount(String hesapNo) {
        try {
            WebElement hesapDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Hesap']/ancestor::div[contains(@id,'select_container')]//div[contains(@class,'ant-select-selector')]")
            ));
            hesapDropdown.click();

            wait.until(driver -> {
                List<WebElement> dropdowns = driver.findElements(By.className("ant-select-dropdown"));
                return dropdowns.stream().anyMatch(e -> !e.getAttribute("class").contains("hidden"));
            });

            WebElement hesapOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='ant-select-item-option-content' and text()='" + hesapNo + "']")
            ));
            hesapOption.click();

            System.out.println("✅ Hesap seçildi: " + hesapNo);
        } catch (Exception e) {
            System.out.println("❌ IBAN seçimi başarısız: " + e.getMessage());
            throw e;
        }
    }


    public void clickListeleVeBekle(int maxWaitSeconds) {
        clickButtonByText("Listele");
        sleep(8000);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Fiş Türü")));
        } catch (Exception e) {
            System.out.println("❌ Listele sonrası tablo yüklenmedi: " + e.getMessage());
        }
    }

    public void clickButtonByText(String visibleText) {
        try {
            WebElement btn = driver.findElement(By.xpath("//*[contains(@Name, '" + visibleText + "')]"));
            btn.click();
        } catch (Exception e) {
            ElementHelper.clickByCoordinates(1157, 291);
        }
    }

    public void testEkstreCheckboxSecimi() {
        selectFirstCheckbox();
        clickScrollRightArrowWithRobot();
        selectRowWithDurum("Eksik Bilgi Bulunuyor");
    }

    public void selectFirstCheckbox() {
        try {
            WebElement label = driver.findElement(By.xpath("(//label[contains(@class,'ant-checkbox-wrapper')])[1]"));
            WebElement input = label.findElement(By.xpath(".//input[@type='checkbox']"));
            if (!input.isSelected()) label.click();
        } catch (Exception e) {
            System.out.println("❌ İlk checkbox seçilemedi: " + e.getMessage());
        }
    }

    public void selectRowWithDurum(String durumText) {
//        scrollRightWithRobot();
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
            for (WebElement row : rows) {
                try {
                    WebElement durumCell = row.findElement(By.xpath(".//td[normalize-space()='" + durumText + "']"));
                    WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                    if (!checkbox.isSelected()) checkbox.click();
                    return;
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.out.println("❌ Satır/checkbox seçiminde hata: " + e.getMessage());
        }
    }

    public void changeFisTypeTo(String menu, String fisTuru) {
        Actions actions = new Actions(driver);
        WebElement selectedRow = driver.findElement(By.xpath("//*[contains(@Name, 'Seçildi')]"));
        actions.contextClick(selectedRow).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.name(menu))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.name(fisTuru))).click();
    }

    public boolean isFisTuruUpdated(String expectedText) {
        try {
            WebElement updatedCell = driver.findElement(By.xpath("//*[contains(@Name, '" + expectedText + "')]"));
            return updatedCell.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDurumColumnShows(String durumText) {
        try {
            WebElement durum = driver.findElement(By.xpath("//*[contains(@Name, '" + durumText + "')]"));
            return durum.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickErpCariKodDots() {
        List<WebElement> dots = driver.findElements(By.name("..."));
        for (WebElement dot : dots) {
            if (dot.isDisplayed()) {
                dot.click();
                break;
            }
        }
    }

    public void selectFirstCariFromPopup() {
        WebElement firstCari = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//DataItem[1]")));
        new Actions(driver).doubleClick(firstCari).perform();
    }

    public static void clickScrollRightArrowWithRobot() {
        try {
            Robot robot = new Robot();
            robot.mouseMove(1844, 1011);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(400);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (Exception e) {
            System.out.println("❌ Scroll oku tıklanamadı: " + e.getMessage());
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
