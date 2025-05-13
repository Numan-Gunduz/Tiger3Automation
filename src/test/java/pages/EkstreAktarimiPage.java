
package pages;
import io.appium.java_client.AppiumBy;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import org.junit.Assert;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.awt.event.InputEvent;
import java.lang.reflect.Field;
import base.TestContext;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EkstreAktarimiPage {
    private final WebDriver webDriver;
    private final WindowsDriver winDriver;
    private final WebDriverWait wait;
    private WebElement selectedRowElement;


    public EkstreAktarimiPage(TestContext context) {
        this.webDriver = context.getWebDriver();
        this.winDriver = context.getWindowsDriver();
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(40));
    }

    public void clickSidebarMenu(String menu) {
        WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'ant-menu-title-content') and text()='" + menu + "']")));
        menuElement.click();
    }

    public void selectBank(String bank) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));

        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='select_container']//div[contains(@class, 'ant-select-selector')]")));
        dropdown.click();

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(text(), 'Veri Yok')]")));
        } catch (TimeoutException ignored) {}

        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + bank + "']")));

        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", option);

        boolean success = false;
        for (int i = 0; i < 5; i++) {
            try {
                WebElement selected = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("span.ant-select-selection-item")));
                if (selected.getText().trim().equals(bank)) {
                    success = true;
                    break;
                }
            } catch (Exception ignored) {}
            ElementHelper.sleep(500);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        if (!success) {
            throw new RuntimeException("Dropdown seçimi başarısız: " + bank);
        }

        System.out.println("'" + bank + "' bankası başarıyla seçildi.");
    }


    public String getCurrentlySelectedBank() {
        WebElement selected = webDriver.findElement(By.xpath("(//span[@class='ant-select-selection-item'])[1]"));
        return selected.getText().trim();
    }


    public void selectAccount(String iban, String expectedBank) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[contains(@class,'ant-select-selector')])[2]")));
            dropdown.click();

            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='ant-select-item-option-content' and normalize-space(text())='" + iban + "']")));

            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", option);
            System.out.println("✅ '" + iban + "' hesabı başarıyla seçildi.");

            WebElement selectedBank = webDriver.findElement(By.xpath("(//span[@class='ant-select-selection-item'])[1]"));
            String currentBank = selectedBank.getText().trim();

            if (!currentBank.equals(expectedBank)) {
                System.out.println("Hesap seçimi sonrası banka resetlendi. '" + expectedBank + "' tekrar seçiliyor.");
                selectBank(expectedBank);
            }

        } catch (Exception e) {
            throw new RuntimeException("Hesap seçimi başarısız: " + e.getMessage());
        }
    }



    public void enterStartDateDaysAgo(int daysAgo) {
        String dateStr = LocalDate.now().minusDays(daysAgo).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("logo-elements-date-picker input[slot='input']")));

        dateInput.click();
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Ctrl + A
        dateInput.sendKeys(Keys.DELETE);                   // Sil
        dateInput.sendKeys(dateStr);
        dateInput.sendKeys(Keys.ENTER);
    }


    public void clickListele() {
        WebElement host = webDriver.findElement(By.cssSelector("logo-elements-button[theme='secondary']"));
        SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) webDriver)
                .executeScript("return arguments[0].shadowRoot", host);
        WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Verilerinizi bankalardan listeliyoruz')]")));
    }

    public void selectRowWithDurumOrDurum(String durum1, String durum2) {
        for (int i = 0; i < 30; i++) {
            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                for (WebElement cell : cells) {
                    String cellText = cell.getText().trim();
                    if (cellText.equalsIgnoreCase(durum1) || cellText.equalsIgnoreCase(durum2)) {
                        WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                        if (!checkbox.isSelected()) checkbox.click();
                        selectedRowElement = row;
                        return;
                    }
                }
            }
            ((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0, 800);");
        }
        throw new RuntimeException("İstenen durumda satır bulunamadı.");
    }

    public void changeFisTypeTo(String menu, String fisTuru) {
        new Actions(webDriver).contextClick(selectedRowElement).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + menu + "']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + fisTuru + "']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Tamam']"))).click();
    }

    public boolean isFisTuruUpdated(String expectedText) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            // Beklenen Fiş Türü değeri DOM'a gelsin (örneğin "Virman Fişi")
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'" + expectedText + "')]")
            ));

            // Tüm eşleşen hücreleri al
            List<WebElement> cells = webDriver.findElements(By.xpath("//*[contains(text(),'" + expectedText + "')]"));

            for (WebElement cell : cells) {
                try {
                    if (cell.isDisplayed()) {
                        System.out.println("Fiş türü bulundu: " + expectedText);
                        return true;
                    }
                } catch (StaleElementReferenceException stale) {
                    System.out.println("Stale element oluştu, yeniden deneniyor...");
                    // tekrar locate edelim
                    WebElement refreshedCell = webDriver.findElement(By.xpath("//*[contains(text(),'" + expectedText + "')]"));
                    if (refreshedCell.isDisplayed()) {
                        return true;
                    }
                }
            }

            System.out.println("Fiş türü bulunamadı: " + expectedText);
            return false;

        } catch (Exception e) {
            System.out.println("Fiş türü kontrolü sırasında hata: " + e.getMessage());
            return false;
        }
    }


    public boolean validateDurumForEmptyField(String fieldHeader, String expectedDurumText) {
        List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
        int fieldIndex = -1, durumIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if (text.equalsIgnoreCase(fieldHeader)) fieldIndex = i + 1;
            if (text.equalsIgnoreCase("Durum")) durumIndex = i + 1;
        }
        if (fieldIndex == -1 || durumIndex == -1) throw new RuntimeException("Başlıklar bulunamadı");

        WebElement fieldCell = selectedRowElement.findElement(By.xpath("./td[" + fieldIndex + "]"));
        WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + durumIndex + "]"));
        return fieldCell.getText().trim().isEmpty() && durumCell.getText().trim().equals(expectedDurumText);
    }

    public void clickErpCariKodDots() {
        clickThreeDotsInField("ERP Cari Hesap Kodu");
    }

    public void clickErpBankaKodDots() {
        clickThreeDotsInField("ERP Banka Hesap Kodu");
    }

    public void clickErpHizmetKodDots() {
        clickThreeDotsInField("ERP Hizmet Kodu");
    }

    public void clickErpKasaKodDots() {
        clickThreeDotsInField("ERP Kasa Kodu");
    }

    private void clickThreeDotsInField(String header) {
        List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
        int targetIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(header)) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex == -1) throw new RuntimeException("Sütun bulunamadı");
        WebElement cell = selectedRowElement.findElements(By.tagName("td")).get(targetIndex);
        WebElement host = cell.findElement(By.cssSelector("logo-elements-icon[icon='leds:three_dots_hor']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", host);
    }

    public void clickThreeDotsForField(String alan) {
        if (alan.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
            clickErpCariKodDots();
        } else if (alan.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
            clickErpBankaKodDots();
        } else if (alan.equalsIgnoreCase("ERP Hizmet Kodu")) {
            clickErpHizmetKodDots();
        } else if (alan.equalsIgnoreCase("ERP Kasa Kodu")) {
            clickErpKasaKodDots();
        } else {
            throw new RuntimeException("Üç nokta tıklama desteklenmiyor: " + alan);
        }
    }

    public void clickSelectButtonForField(String alan) {
        try {
            if (alan.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
                clickSelectButtonOnCariPopupBankaKodu();
            } else if (alan.equalsIgnoreCase("ERP Hizmet Kodu")) {
                clickCheckboxAndThenSelectButtonForHizmet();
            } else if (alan.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
                clickSelectButtonOnCariPopup();
            } else if (alan.equalsIgnoreCase("ERP Kasa Kodu")) {
                clickErpKasaKoduCheckboxAlanı();
            } else {
                throw new RuntimeException("Select butonu tıklama desteklenmiyor: " + alan);
            }
        } catch (AWTException | InterruptedException e) {
            throw new RuntimeException("Select butonu tıklanırken hata oluştu: " + e.getMessage(), e);
        }
    }


    public boolean checkDurumUpdatedAfterFieldFill(String alan, String expectedDurum) {
        if (alan.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
            return isDurumKaydedilebilirGorunuyor();
        } else if (alan.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
            return isDurumKaydedilebilirBankaKod();
        } else if (alan.equalsIgnoreCase("ERP Hizmet Kodu")) {
            return isDurumKaydedilebilirHizmetKodu();
        } else if (alan.equalsIgnoreCase("ERP Kasa Kodu")) {
            return isDurumKaydedilebilirKasaKodu();
        } else {
            throw new RuntimeException("Alan tipi desteklenmiyor: " + alan);
        }
    }

    public void clickSelectButtonOnCariPopup() throws AWTException, InterruptedException {
        Thread.sleep(3000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void clickSelectButtonOnCariPopupBankaKodu() throws AWTException, InterruptedException {
        clickSelectButtonOnCariPopup();
        Thread.sleep(2000);
        winDriver.findElement(MobileBy.AccessibilityId("SelBtn")).click();
    }

    public void clickCheckboxAndThenSelectButtonForHizmet() throws InterruptedException {
        Thread.sleep(2000); // pencere tam açılsın
        WebElement checkbox = winDriver.findElement(MobileBy.name("Seçim row0"));
        checkbox.click();
        Thread.sleep(1000);
        WebElement selectButton = winDriver.findElement(MobileBy.name("Seç"));
        selectButton.click();
    }

    public void clickErpKasaKoduCheckboxAlanı() throws InterruptedException {
        Thread.sleep(2000);
        winDriver.findElement(MobileBy.AccessibilityId("chooseButton")).click();
    }

    public boolean isDurumKaydedilebilirGorunuyor() {
        return isFieldFilledAndDurum("ERP Cari Hesap Kodu", "Kaydedilebilir");
    }

    public boolean isDurumKaydedilebilirBankaKod() {
        return isFieldFilledAndDurum("ERP Banka Hesap Kodu", "Kaydedilebilir");
    }

    public boolean isDurumKaydedilebilirHizmetKodu() {
        return isFieldFilledAndDurum("ERP Hizmet Kodu", "Kaydedilebilir");
    }

    public boolean isDurumKaydedilebilirKasaKodu() {
        return isFieldFilledAndDurum("ERP Kasa Kodu", "Kaydedilebilir");
    }

    private boolean isFieldFilledAndDurum(String header, String expectedDurum) {
        try {
            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
            int fieldIndex = -1, durumIndex = -1;

            for (int i = 0; i < headers.size(); i++) {
                String text = headers.get(i).getText().trim();
                if (text.equalsIgnoreCase(header)) fieldIndex = i + 1;
                if (text.equalsIgnoreCase("Durum")) durumIndex = i + 1;
            }

            if (fieldIndex == -1 || durumIndex == -1) {
                throw new RuntimeException("'" + header + "' veya 'Durum' başlığı bulunamadı.");
            }

            WebElement fieldCell = selectedRowElement.findElement(By.xpath("./td[" + fieldIndex + "]"));
            WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + durumIndex + "]"));

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            boolean fieldFilled = wait.until(driver -> !fieldCell.getText().trim().isEmpty());

            boolean durumDogru = wait.until(driver -> {
                String durumText = durumCell.getAttribute("innerText").trim();
                System.out.println("Beklenen: '" + expectedDurum + "', Gerçek: '" + durumText + "'");
                return durumText.equalsIgnoreCase(expectedDurum);
            });

            return fieldFilled && durumDogru;

        } catch (Exception e) {
            System.out.println("isFieldFilledAndDurum() içinde hata: " + e.getMessage());
            return false;
        }
    }


    public void clickFisOlusturButton() {
        WebElement host = webDriver.findElement(By.cssSelector("logo-elements-button[theme='primary']"));
        SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) webDriver)
                .executeScript("return arguments[0].shadowRoot", host);
        WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);
    }

    public void clickEvetOnConfirmationPopup() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'ant-btn-primary') and .//span[text()='Evet']]")));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", btn);
    }

    public boolean isSuccessToastMessageVisible() {
        String toastMessage = "Hesap ekstresi kayıtlarına ait fiş oluşturma süreci tamamlandı";
        By toastLocator = By.xpath("//*[contains(text(),'" + toastMessage + "')]");

        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(35));
            wait.pollingEvery(Duration.ofMillis(300));
            wait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

            WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(toastLocator));

            // Stil + boyut kontrolüyle görünürlük garantisi
            return (Boolean) ((JavascriptExecutor) webDriver).executeScript(
                    "const elem = arguments[0];" +
                            "const rect = elem.getBoundingClientRect();" +
                            "return (" +
                            "rect.width > 0 && rect.height > 0 && " +
                            "window.getComputedStyle(elem).visibility !== 'hidden' && " +
                            "window.getComputedStyle(elem).display !== 'none');",
                    toast
            );

        } catch (TimeoutException e) {
            System.out.println("Toast mesajı beklenen sürede DOM'a gelmedi.");
            return false;
        } catch (Exception e) {
            System.out.println("Toast kontrolü sırasında hata: " + e.getMessage());
            return false;
        }
    }


    public boolean isDurumEslendiGorunuyor() {
        try {
            if (selectedRowElement == null)
                throw new RuntimeException("Önceden seçilen satır kaydedilmemiş.");

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
            wait.pollingEvery(Duration.ofMillis(250));
            wait.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);

            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
            int durumIndex = -1;

            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i).getText().trim();
                if (header.equalsIgnoreCase("Durum")) {
                    durumIndex = i + 1;
                    break;
                }
            }

            if (durumIndex == -1)
                throw new RuntimeException("'Durum' sütunu bulunamadı.");

            final int finalDurumIndex = durumIndex;

            return wait.until(driver -> {
                WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + finalDurumIndex + "]"));
                String durumText = durumCell.getText().trim();
                System.out.println("Bekleniyor: '" + durumText + "'");
                return durumText.equalsIgnoreCase("Eşlendi");
            });

        } catch (TimeoutException te) {
            System.out.println("'Eşlendi' durumu zaman aşımına uğradı.");
            return false;
        } catch (Exception e) {
            System.out.println(" Durum kontrol hatası: " + e.getMessage());
            return false;
        }
    }


    public boolean isErpFisNoDoluMu() {
        return !getErpFisNoFromSelectedRow().isEmpty();
    }

    public String getErpFisNoFromSelectedRow() {
        List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
        int index = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase("ERP Fiş No")) {
                index = i + 1;
                break;
            }
        }
        return selectedRowElement.findElement(By.xpath("./td[" + index + "]")).getText().trim();
    }

    public void openFisPopupFromContextMenu(String secenek) {
        new Actions(webDriver).moveToElement(selectedRowElement).contextClick().perform();
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'menu-title-content') and text()='" + secenek + "']")));
        menu.click();
    }

    public String getFisNoFromPopup(String fisTuru) {
        try {
            WebDriverWait wait = new WebDriverWait(winDriver, Duration.ofSeconds(10));

            By locator;
            if (fisTuru.equalsIgnoreCase("Hizmet Faturası Fişi")) {
                locator = MobileBy.AccessibilityId("ficheNoEdit");
            } else {
                locator = MobileBy.AccessibilityId("FicheNoEdit");
            }

            WebElement fisNo = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return fisNo.getText().trim();
        } catch (Exception e) {
            System.out.println("Fiş No alanı bulunamadı: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getCurrentFisTuru() {
        try {
            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
            int fisTuruIndex = -1;

            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equalsIgnoreCase("Fiş Türü")) {
                    fisTuruIndex = i + 1;
                    break;
                }
            }

            if (fisTuruIndex == -1) {
                throw new RuntimeException(" 'Fiş Türü' sütunu bulunamadı.");
            }

            WebElement cell = selectedRowElement.findElement(By.xpath("./td[" + fisTuruIndex + "]"));
            String text = cell.getText().trim();
            System.out.println("Seçili satırdaki fiş türü: " + text);
            return text;

        } catch (Exception e) {
            System.out.println(" getCurrentFisTuru() hata: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String getFisNoFromPopup_HizmetFaturasi() {
        try {
            WebDriverWait wait = new WebDriverWait(winDriver, Duration.ofSeconds(10));
            WebElement fisNo = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("ficheNoEdit")));
            return fisNo.getText().trim();
        } catch (Exception e) {
            System.out.println("Hizmet Faturası fiş no okunamadı: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getFisNoFromPopup_Classic() {
        try {
            WebDriverWait wait = new WebDriverWait(winDriver, Duration.ofSeconds(10));
            WebElement fisNo = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("FicheNoEdit")));
            return fisNo.getText().trim();
        } catch (Exception e) {
            System.out.println(" Klasik fiş no okunamadı: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void closeOpenPopupsOneByOne() {
        try {
            System.out.println(" Açık popup pencereleri kapatılıyor...");

            // İlk pencereyi kapat
            WebElement closeBtn1 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn1.click();
            System.out.println("İlk popup kapatıldı.");

            Thread.sleep(1500); // bekle, ikinci pencerenin ön plana geçmesini sağla

            // İkinci pencereyi kapat
            WebElement closeBtn2 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn2.click();
            System.out.println(" İkinci popup kapatıldı.");

        } catch (Exception e) {
            System.out.println(" Popup kapatılırken hata oluştu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void closeOpenPopupsOneByOneforKasa() {
        try {
            System.out.println(" Açık popup pencereleri kapatılıyor...");

            // İlk pencereyi kapat
            WebElement closeBtn1 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn1.click();
            System.out.println("İlk popup kapatıldı.");

            Thread.sleep(1500); // bekle, ikinci pencerenin ön plana geçmesini sağla

            // İkinci pencereyi kapat
            WebElement closeBtn2 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn2.click();
            System.out.println(" İkinci popup kapatıldı.");


            // üçüncüsü pencereyi kapat
            WebElement closeBtn3 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn3.click();
            System.out.println(" üçüncü popup kapatıldı.");

        } catch (Exception e) {
            System.out.println(" Popup kapatılırken hata oluştu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private static final String APP_NAME = "Online Hesap Özeti Uygulaması";
    public void clickOnlineHesapOzetiApp()
    {
        System.out.println("Online Hesap Özeti'ne tıklanıyor");
        ElementHelper.sleep(2000);
        WebElement  OnlineHesapOzetiButton = winDriver.findElement((MobileBy.name("Online Hesap Özeti Uygulaması")));
        OnlineHesapOzetiButton.click();
        System.out.println("Online Hesap Özeti Uygulamasına Tıklandı");
    }
    public boolean checkDurumOnly(String expectedDurum) {
        try {
            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
            int durumIndex = -1;

            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equalsIgnoreCase("Durum")) {
                    durumIndex = i + 1;
                    break;
                }
            }

            if (durumIndex == -1) {
                throw new RuntimeException(" 'Durum' sütunu bulunamadı.");
            }

            WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + durumIndex + "]"));

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            return wait.until(driver -> {
                String durumText = durumCell.getAttribute("innerText").trim();
                System.out.println("(Boş alan tipi) Beklenen Durum: " + expectedDurum + " | Gerçek: " + durumText);
                return durumText.equalsIgnoreCase(expectedDurum);
            });

        } catch (Exception e) {
            System.out.println("checkDurumOnly() hata: " + e.getMessage());
            return false;
        }
    }

    public void selectRowWithNegativeTutarAndDurum(String beklenenDurum) {
        List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
        List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));

        int tutarIndex = -1;
        int durumIndex = -1;

        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if (text.equalsIgnoreCase("Tutar")) tutarIndex = i + 1;
            if (text.equalsIgnoreCase("Durum")) durumIndex = i + 1;
        }

        if (tutarIndex == -1 || durumIndex == -1)
            throw new RuntimeException(" 'Tutar' veya 'Durum' sütunu bulunamadı");

        for (WebElement row : rows) {
            WebElement tutarCell = row.findElement(By.xpath("./td[" + tutarIndex + "]"));
            WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));

            String tutarText = tutarCell.getText().trim().replace(",", "."); // olası formatlar
            String durumText = durumCell.getText().trim();

            if (tutarText.startsWith("-") && durumText.equalsIgnoreCase(beklenenDurum)) {
                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                if (!checkbox.isSelected()) {
                    // checkbox devre dışı ise doğrudan tıklamak hata verir, iç span’a tıkla
                    WebElement span = row.findElement(By.cssSelector(".ant-checkbox-inner"));
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);
                }
                selectedRowElement = row;
                return;
            }
        }

        throw new RuntimeException(" Negatif tutarlı ve '" + beklenenDurum + "' durumuna sahip satır bulunamadı.");
    }
    public void selectRowWithPositiveTutarAndDurum(String beklenenDurum) {
        List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
        List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));

        int tutarIndex = -1;
        int durumIndex = -1;

        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if (text.equalsIgnoreCase("Tutar")) tutarIndex = i + 1;
            if (text.equalsIgnoreCase("Durum")) durumIndex = i + 1;
        }

        if (tutarIndex == -1 || durumIndex == -1)
            throw new RuntimeException(" 'Tutar' veya 'Durum' sütunu bulunamadı");

        for (WebElement row : rows) {
            WebElement tutarCell = row.findElement(By.xpath("./td[" + tutarIndex + "]"));
            WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));

            String tutarText = tutarCell.getText().trim().replace(",", "."); // Noktalı formatları normalize et
            String durumText = durumCell.getText().trim();

            if (!tutarText.startsWith("-") && durumText.equalsIgnoreCase(beklenenDurum)) {
                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                if (!checkbox.isSelected()) {
                    WebElement span = row.findElement(By.cssSelector(".ant-checkbox-inner"));
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);
                }
                selectedRowElement = row;
                return;
            }
        }

        throw new RuntimeException(" Pozitif tutarlı ve '" + beklenenDurum + "' durumuna sahip satır bulunamadı.");
    }


    public void navigateToKasaIslemleriFromGlobalSearch() {
        try {
            System.out.println("Uygulama ekranı simge durumuna alınıyor...");
            WebElement minimizeBtn = winDriver.findElement(MobileBy.AccessibilityId("pcMinimize"));
            minimizeBtn.click();
            Thread.sleep(1000);

            System.out.println(" Genel arama alanı bulunuyor ve 'Kasa İşlemleri' yazılıyor...");
            WebElement globalSearch = winDriver.findElement(By.className("TcxCustomInnerTextEdit"));
            globalSearch.click();
            globalSearch.clear();
            globalSearch.sendKeys("Kasa İşlemleri");
            Thread.sleep(1500);

            System.out.println("Uygulama listesinde 'Kasa İşlemleri' öğesi bulunuyor...");
            WebElement kasaIslemleri = winDriver.findElement(MobileBy.name("Kasa İşlemleri"));
            kasaIslemleri.click();
            Thread.sleep(200);
            kasaIslemleri.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            System.out.println(" 'Kasa İşlemleri' uygulaması başarıyla açıldı.");
            System.out.println(" LDataGrid tablo koordinatına sağ tıklanıyor...");

            WebElement grid = winDriver.findElement(By.name("CashCardDataGrid"));
            Point location = grid.getLocation();
            int x = location.getX() + 50;
            int y = location.getY() + 50;

            Robot robot = new Robot();
            robot.mouseMove(x, y);
            Thread.sleep(500);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            System.out.println("Sağ tıklama yapıldı.");
            Thread.sleep(1000);

            //  Menüde "İşlemler" öğesinin konumuna tıklama
            robot.mouseMove(x + 20, y + 5); // "İşlemler" öğesi üstte yer alır
            Thread.sleep(300);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("'İşlemler' menü öğesi tıklandı.");

        } catch (Exception e) {
            throw new RuntimeException(" Genel işlem başarısız: " + e.getMessage());
        }
    }


    public void openKasaFormByFicheNo() {
        try {
            String expectedFicheNo = getErpFisNoFromSelectedRow();
            System.out.println("\uD83D\uDD0D Beklenen Fiş No (tam): " + expectedFicheNo);

            WebElement grid = winDriver.findElement(By.name("CashTransGrid"));
            List<WebElement> rows = grid.findElements(By.xpath(".//*"));

            for (WebElement row : rows) {
                try {
                    String text = row.getText().trim();
                    String normalizedText = text.replaceAll("^0+", "");
                    String normalizedExpected = expectedFicheNo.replaceAll("^0+", "");

                    System.out.println("\uD83D\uDD0D Satır metni: " + text);

                    if (normalizedText.equals(normalizedExpected)) {
                        System.out.println("✅ Satır bulundu, tıklanıyor...");
                        row.click();
                        Thread.sleep(1000);

                        WebElement viewBtn = winDriver.findElement(MobileBy.AccessibilityId("ViewBtn"));
                        System.out.println("\uD83D\uDD0D 'İncele' butonu tıklanıyor...");
                        viewBtn.click();
                        Thread.sleep(2000);
                        return;
                    }

                } catch (Exception e) {
                    System.out.println("Satır erişim hatası: " + e.getMessage());
                }
            }
            throw new RuntimeException("Fiş No içeren satır bulunamadı: " + expectedFicheNo);

        } catch (Exception e) {
            throw new RuntimeException(" Form açma başarısız: " + e.getMessage());
        }

    }


    public boolean isBankadanCekilenFormAcildi() {
        try {
            WebElement titleBar = winDriver.findElement(By.xpath("//*[contains(@Name, 'Bankadan Çekilen')]"));
            return titleBar != null && titleBar.isDisplayed();
        } catch (Exception e) {
            System.out.println(" Bankadan Çekilen formu bulunamadı: " + e.getMessage());
            return false;
        }
    }

    public boolean isBankayaYatirilanFormAcildi() {
        try {
            WebElement titleBar = winDriver.findElement(By.xpath("//*[contains(@Name, 'Bankaya Yatýrýlan')]"));
            return titleBar != null && titleBar.isDisplayed();
        } catch (Exception e) {
            System.out.println(" Bankaya Yatırılan formu bulunamadı: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyKasaFormOpenedWithCorrectFicheNo() {
        try {
            String expectedFicheNo = getErpFisNoFromSelectedRow();
            WebElement kasaIslemNoField = winDriver.findElement(MobileBy.AccessibilityId("ficheNoEdit"));
            String openedFicheNo = kasaIslemNoField.getText().trim();

            System.out.println("Beklenen: " + expectedFicheNo + " | Açılan: " + openedFicheNo);
            return expectedFicheNo.equals(openedFicheNo);

        } catch (Exception e) {
            System.out.println("Kasa işlem ekranı doğrulanamadı: " + e.getMessage());
            return false;
        }
    }


}
