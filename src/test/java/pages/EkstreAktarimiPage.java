package pages;

import base.TestContext;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.windows.WindowsDriver;

import io.appium.java_client.windows.WindowsDriver;

import io.appium.java_client.MobileBy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import io.appium.java_client.windows.WindowsDriver;


public class EkstreAktarimiPage {
    private WebElement selectedRowElement; // en son işlem yapılan satır

    private final TestContext context;
    private final WebDriver webDriver;    // WebView2 için
    private final WindowsDriver winDriver; // Win32 popup için
    private final WebDriverWait wait;

    public EkstreAktarimiPage(TestContext context) {
        this.context = context;
        this.webDriver = context.getWebDriver();         // WebView2 için
        this.winDriver = context.getWindowsDriver();     // Win32 popup için
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
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
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // 🎯 Shadow DOM içindeki 'Listele' butonunun host elementini bul
            WebElement host = webDriver.findElement(By.cssSelector("logo-elements-button[theme='secondary']"));
            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) webDriver)
                    .executeScript("return arguments[0].shadowRoot", host);

            WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);
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


//    public void selectRowWithDurum(String durumText) {
//        List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//        for (WebElement row : rows) {
//            try {
//                List<WebElement> cells = row.findElements(By.tagName("td"));
//
//                for (WebElement cell : cells) {
//                    String cellText = cell.getText().trim();
//                    if (cellText.equals(durumText)) { // contains DEĞİL!
//                        WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                        if (!checkbox.isSelected()) {
//                            checkbox.click();
//                            System.out.println("✅ Checkbox işaretlendi: " + durumText);
//                        }
//                        return;
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("❌ Satırda seçim yapılırken hata: " + e.getMessage());
//            }
//        }
//
//        throw new RuntimeException("❌ '" + durumText + "' eşleşen satır bulunamadı!");
//    }
public void selectRowWithDurum(String durumText) {
    List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
    for (WebElement row : rows) {
        try {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                String cellText = cell.getText().trim();
                if (cellText.equalsIgnoreCase(durumText)) {
                    WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                    if (!checkbox.isSelected()) {
                        checkbox.click();
                        System.out.println("✅ Checkbox işaretlendi: " + durumText);
                    }
                    selectedRowElement = row; // 📌 Satırı sakla
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
            new Actions(webDriver).contextClick(tableArea).perform();
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
            List<WebElement> cells = webDriver.findElements(By.xpath("//*[contains(text(),'" + expectedText + "')]"));
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
            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
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
            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
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





    public void clickErpCariKodDots() {
        try {
            // 1️⃣ ERP Cari Hesap Kodu sütun index'ini bul
            List<WebElement> headers = webDriver.findElements(By.xpath("//table//thead//th"));
            int targetIndex = -1;
            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equals("ERP Cari Hesap Kodu")) {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex == -1)
                throw new RuntimeException("❌ 'ERP Cari Hesap Kodu' başlığı bulunamadı.");

            System.out.println("🔎 ERP Cari Hesap Kodu sütun index: " + targetIndex);

            // 2️⃣ Satırları bul ve checkbox'ı seçili olanı bul
            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
            WebElement selectedRow = null;

            for (WebElement row : rows) {
                try {
                    WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                    if (checkbox.isSelected()) {
                        selectedRow = row;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            if (selectedRow == null)
                throw new RuntimeException("❌ Seçili (checked) satır bulunamadı.");

            // 3️⃣ Doğru hücreyi al
            List<WebElement> cells = selectedRow.findElements(By.tagName("td"));
            if (targetIndex >= cells.size())
                throw new RuntimeException("❌ ERP Cari Hesap Kodu sütununa denk gelen hücre yok.");

            WebElement targetCell = cells.get(targetIndex);

            // 4️⃣ Hücredeki üç nokta butonunu bul
            WebElement host = targetCell.findElement(By.cssSelector("logo-elements-icon[icon='leds:three_dots_hor']"));

            // 5️⃣ Scroll + JS click
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", host);
            Thread.sleep(300); // scroll sonrası küçük gecikme
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", host);

            System.out.println("✅ ERP Cari Hesap Kodu alanındaki üç nokta butonuna başarıyla tıklandı.");

        } catch (Exception e) {
            System.out.println("❌ Üç nokta tıklanırken hata: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void clickSelectButtonOnCariPopup() {
        try {
            WebElement selectButton = winDriver.findElement(MobileBy.AccessibilityId("SelBtn"));
            selectButton.click();
            System.out.println("✅ 'Seç' butonuna başarıyla tıklandı.");
        } catch (Exception e) {
            System.out.println("❌ 'Seç' butonuna tıklarken hata: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public boolean isDurumKaydedilebilirGorunuyor() {
        try {
            // Başlık indexlerini bul
            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
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
                throw new RuntimeException("❌ 'ERP Cari Hesap Kodu' veya 'Durum' sütunu bulunamadı.");
            }

            // Satırları gez, seçili olan checkbox'ı bul
            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
            for (WebElement row : rows) {
                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                if (checkbox.isSelected()) {
                    WebElement cariCell = row.findElement(By.xpath("./td[" + cariHesapIndex + "]"));
                    WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));

                    String cariValue = cariCell.getText().trim();
                    String durumValue = durumCell.getText().trim();

                    System.out.println("🔍 Cari: '" + cariValue + "', Durum: '" + durumValue + "'");

                    return !cariValue.isEmpty() && durumValue.equalsIgnoreCase("Kaydedilebilir");
                }
            }

            System.out.println("❌ Seçili satır bulunamadı.");
            return false;

        } catch (Exception e) {
            System.out.println("❌ Durum kontrolünde hata: " + e.getMessage());
            return false;
        }
    }



    public void clickFisOlusturButton() {
        try {
            WebElement host = webDriver.findElement(By.cssSelector("logo-elements-button[theme='primary']"));
            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) webDriver)
                    .executeScript("return arguments[0].shadowRoot", host);

            WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);

            System.out.println("✅ 'Fiş Oluştur' butonuna başarıyla tıklandı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ Fiş Oluştur butonuna tıklanırken hata: " + e.getMessage());
        }
    }

    public void clickEvetOnConfirmationPopup() {
        try {
            WebElement evetBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'ant-btn-primary')]//span[text()='Evet']")));
            evetBtn.click();
            System.out.println("✅ Onay popup'ındaki 'Evet' butonuna tıklandı.");
        } catch (Exception e) {
            throw new RuntimeException("❌ 'Evet' butonuna tıklanırken hata: " + e.getMessage());
        }
    }

    public boolean isSuccessToastMessageVisible() {
        try {
            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Hesap ekstresi kayıtlarına ait fiş oluşturma süreci tamamlandı')]")));
            System.out.println("✅ Başarı mesajı göründü.");
            return toast.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Toast mesajı görünmedi: " + e.getMessage());
            return false;
        }
    }

    public boolean isDurumEslendiGorunuyor() {
        try {
            if (selectedRowElement == null)
                throw new RuntimeException("❌ Önceden seçilen satır kaydedilmemiş.");

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
                throw new RuntimeException("❌ 'Durum' sütunu bulunamadı.");

            WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + durumIndex + "]"));
            String text = durumCell.getText().trim();
            System.out.println("🔍 Seçilen satırdaki Durum: '" + text + "'");
            return text.equalsIgnoreCase("Eşlendi");

        } catch (Exception e) {
            System.out.println("❌ Durum eşleşme kontrol hatası: " + e.getMessage());
            return false;
        }
    }


    public boolean isErpFisNoDoluMu() {
        try {
            if (selectedRowElement == null)
                throw new RuntimeException("❌ Önceden seçilen satır kaydedilmemiş.");

            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
            int fisNoIndex = -1;

            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i).getText().trim();
                if (header.equalsIgnoreCase("ERP Fiş No")) {
                    fisNoIndex = i + 1;
                    break;
                }
            }

            if (fisNoIndex == -1)
                throw new RuntimeException("❌ 'ERP Fiş No' sütunu bulunamadı.");

            WebElement fisNoCell = selectedRowElement.findElement(By.xpath("./td[" + fisNoIndex + "]"));
            String text = fisNoCell.getText().trim();
            System.out.println("🔍 Seçilen satırdaki ERP Fiş No: '" + text + "'");

            return !text.isEmpty();

        } catch (Exception e) {
            System.out.println("❌ ERP Fiş No kontrol hatası: " + e.getMessage());
            return false;
        }
    }




}



