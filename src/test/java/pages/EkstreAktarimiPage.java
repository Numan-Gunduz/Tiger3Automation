package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EkstreAktarimiPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public EkstreAktarimiPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void clickSidebarMenu(String menuText) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'ant-menu-title-content') and text()='" + menuText + "']")));
        element.click();
    }

    public void selectBank(String bankaAdi) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='select_container']//div[@class='ant-select-selector']")));
            dropdown.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'ant-select-dropdown') and not(contains(@class,'hidden'))]")));

            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='ant-select-item-option-content' and text()='" + bankaAdi + "']")));
            option.click();
        } catch (Exception e) {
            System.out.println("❌ Banka seçimi hatası: " + e.getMessage());
            throw e;
        }
    }

    public void selectAccount(String hesapNo) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[contains(@class,'ant-select-selector')])[2]")));
            dropdown.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'ant-select-dropdown') and not(contains(@class,'hidden'))]")));

            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='ant-select-item-option-content' and text()='" + hesapNo + "']")));
            option.click();
        } catch (Exception e) {
            System.out.println("❌ IBAN seçimi hatası: " + e.getMessage());
            throw e;
        }
    }
    public void enterStartDateDaysAgo(int daysAgo) {
        try {
            LocalDate targetDate = LocalDate.now().minusDays(daysAgo);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = targetDate.format(formatter);

            WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("logo-elements-date-picker input[slot='input']")));

            dateInput.click();
            dateInput.clear();
            dateInput.sendKeys(formattedDate);
            dateInput.sendKeys(Keys.ENTER);

            System.out.println("✅ Başlangıç tarihi olarak " + formattedDate + " girildi (" + daysAgo + " gün önce).");
        } catch (Exception e) {
            System.out.println("❌ Dinamik tarih girilemedi: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public void clickListele() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // 🎯 Shadow DOM içindeki 'Listele' butonunun host elementini bul
            WebElement host = driver.findElement(By.cssSelector("logo-elements-button[theme='secondary']"));
            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].shadowRoot", host);

            WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", span);
            System.out.println("✅ Listele butonuna başarıyla JS ile tıklandı.");

            // "Lütfen bekleyiniz..." mesajının görünüp sonra kaybolmasını bekle
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Verilerinizi bankalardan listeliyoruz')]")));
            System.out.println("⏳ 'Lütfen bekleyiniz' mesajı göründü.");

            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Verilerinizi bankalardan listeliyoruz')]")));
            System.out.println("⏱️ 'Lütfen bekleyiniz' mesajı kapandı.");

            // 🔍 Tablo veri hücrelerinden birinin (örneğin "Fiş Türü") göründüğünden emin ol
            // Not: Bu, bir tablo hücresi. Başlık değil. Arka planda grid/table yapısına bağlı olarak değişebilir.
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Havale/EFT Fişi')]")));
            System.out.println("✅ Kayıtlar başarıyla yüklendi.");

        } catch (Exception e) {
            System.out.println("❌ Listeleme sürecinde hata: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }





    public void selectRowWithDurum(String durumText) {
        List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
        for (WebElement row : rows) {
            try {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                for (WebElement cell : cells) {
                    String cellText = cell.getText().trim();
                    if (cellText.equals(durumText)) { // contains DEĞİL!
                        WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                        if (!checkbox.isSelected()) {
                            checkbox.click();
                            System.out.println("✅ Checkbox işaretlendi: " + durumText);
                        }
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ Satırda seçim yapılırken hata: " + e.getMessage());
            }
        }

        throw new RuntimeException("❌ '" + durumText + "' eşleşen satır bulunamadı!");
    }




    public void changeFisTypeTo(String contextMenuText, String fisTuru) {
        try {
            // Tablo üzerindeki bir alanı bul (örneğin "Fiş Türü" başlığı olabilir)
            WebElement tableArea = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//th[contains(.,'Fiş Türü')]")));

            // Sağ tık aksiyonu tetikleniyor
            new Actions(driver).contextClick(tableArea).perform();
            System.out.println("✅ Sağ tık başarıyla yapıldı.");

            // "Fiş Türü Değiştir" menüsünü tıkla
            WebElement contextMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + contextMenuText + "']")));
            contextMenu.click();
            System.out.println("✅ '" + contextMenuText + "' menüsü seçildi.");

            // Alt menüden istenen fiş türünü seç
            WebElement fisOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + fisTuru + "']")));
            fisOption.click();
            System.out.println("✅ '" + fisTuru + "' seçeneği seçildi.");

            // Onay popup'ında "Tamam" butonuna tıkla
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Tamam']")));
            confirmButton.click();
            System.out.println("✅ Fiş türü değişikliği onaylandı.");

        } catch (Exception e) {
            System.out.println("❌ Fiş türü değiştirme hatası: " + e.getMessage());
            throw e;
        }
    }

    public boolean isFisTuruUpdated(String expectedText) {
        try {
            List<WebElement> cells = driver.findElements(By.xpath("//*[contains(text(),'" + expectedText + "')]"));
            for (WebElement cell : cells) {
                if (cell.isDisplayed()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("❌ Fiş türü kontrolü sırasında hata: " + e.getMessage());
            return false;
        }
    }

    //buradaki tabloların sütün bilgisi yerie direk olarak dinamik bulacağımız şekilde eklemeliyiz
    public boolean validateDurumForEmptyCariHesap(String expectedDurumText) {
        try {
            // Başlıkların index'lerini bul
            List<WebElement> headers = driver.findElements(By.xpath("//thead//th"));
            int cariHesapIndex = -1;
            int durumIndex = -1;

            for (int i = 0; i < headers.size(); i++) {
                String headerText = headers.get(i).getText().trim();
                if (headerText.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
                    cariHesapIndex = i + 1;
                }
                if (headerText.equalsIgnoreCase("Durum")) {
                    durumIndex = i + 1;
                }
            }

            if (cariHesapIndex == -1 || durumIndex == -1) {
                throw new RuntimeException("❌ 'ERP Cari Hesap Kodu' veya 'Durum' başlığı bulunamadı.");
            }

            // Satırları gez ve sadece seçilmiş checkbox olan satırı bul
            List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
            for (WebElement row : rows) {
                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                if (checkbox.isSelected()) {
                    WebElement cariHesapCell = row.findElement(By.xpath("./td[" + cariHesapIndex + "]"));
                    WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));

                    String cariHesapText = cariHesapCell.getText().trim();
                    String durumText = durumCell.getText().trim();

                    if (cariHesapText.isEmpty()) {
                        if (!durumText.equals(expectedDurumText)) {
                            System.out.println("❌ Durum hatalı! Beklenen: '" + expectedDurumText + "', Bulunan: '" + durumText + "'");
                            return false;
                        } else {
                            System.out.println("✅ Doğru: Cari hesap boş ve Durum alanı doğru: '" + durumText + "'");
                            return true;
                        }
                    } else {
                        System.out.println("ℹ️ Cari hesap boş değil, kontrol edilmedi.");
                    }
                }
            }

            System.out.println("❌ Seçilen ve cari hesabı boş olan satır bulunamadı.");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Hata oluştu: " + e.getMessage());
            return false;
        }
    }



    public boolean isDurumColumnShows(String expectedDurum) {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));

            for (WebElement row : rows) {
                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                boolean isChecked = checkbox.isSelected();

                if (isChecked) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));

                    for (WebElement cell : cells) {
                        String text = cell.getText().trim();
                        if (text.equals(expectedDurum)) {
                            System.out.println("✅ Seçilen satırda '" + expectedDurum + "' bulundu.");
                            return true;
                        }
                    }

                    System.out.println("❌ Seçilen satırda '" + expectedDurum + "' bulunamadı.");
                    return false;
                }
            }

            System.out.println("❌ Hiçbir satır seçili değil.");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Durum kontrol hatası: " + e.getMessage());
            return false;
        }
    }



    public void clickErpCariKodDots() {
        try {
            WebElement host = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("logo-elements-icon[icon='leds:three_dots_hor']")));

            // Shadow root'a eriş
            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].shadowRoot", host);

            // Bu sefer svg yerine doğrudan shadow host'u JS ile tıklayacağız
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", host);
            Thread.sleep(300); // ufak gecikme

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", host);
            System.out.println("✅ Üç nokta butonuna (host element) başarıyla tıklandı (JS).");

        } catch (Exception e) {
            System.out.println("❌ Üç nokta tıklanırken JS hatası: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public void selectFirstCariFromPopup() {
        WebElement firstCari = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//DataItem[1]")));
        new Actions(driver).doubleClick(firstCari).perform();
    }
}
