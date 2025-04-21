package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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

    public void clickListele() {
        try {
            // 🎯 Shadow DOM içindeki 'Listele' butonunun host elementini bul
            WebElement host = driver.findElement(By.cssSelector("logo-elements-button[theme='secondary']"));

            // 🧩 ShadowRoot'a geçiş yap
            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].shadowRoot", host);

            // 🔍 Gömülü label içindeki görünür span'ı bul
            WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));

            // 👆 Doğrudan JavaScript ile tıkla (çünkü native click() bloklanıyor)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", span);
            System.out.println("✅ Listele butonuna başarıyla JS ile tıklandı.");

            // ⏳ Statik bekleme
            System.out.println("⏱️ Statik olarak 6 saniye bekleniyor...");
            Thread.sleep(6000);

            // 🔄 Dinamik olarak "Fiş Türü" başlığını bekle
            System.out.println("🔍 'Fiş Türü' başlığının yüklenmesi dinamik olarak bekleniyor...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[contains(.,'Fiş Türü')]")));
            System.out.println("✅ Tablonun yüklenmesi tamamlandı.");

        } catch (Exception e) {
            System.out.println("❌ Listele tıklama hatası: " + e.getMessage());
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
            WebElement updatedCell = driver.findElement(By.xpath("//*[contains(@Name, '" + expectedText + "')]"));
            return updatedCell.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean  validateDurumForEmptyCariHesap(String expectedDurumText) {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
            for (WebElement row : rows) {
                String cariHesap = row.findElement(By.xpath("./td[normalize-space()='']")).getText().trim();
                String durum = row.findElement(By.xpath("./td[6]")).getText().trim(); // Alternatif: td[contains(.,'Eksik Bilgi')]
                if (cariHesap.isEmpty()) {
                    if (!durum.equals(expectedDurumText)) {
                        System.out.println("❌ Hatalı Durum: Beklenen '" + expectedDurumText + "' ama bulundu: '" + durum + "'");
                        return false;
                    } else {
                        System.out.println("✅ Doğru: ERP Cari Hesap Kodu boş ve Durum '" + expectedDurumText + "'");
                    }
                }
            }
            return true;
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
        WebElement dotButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("...")));
        dotButton.click();
    }

    public void selectFirstCariFromPopup() {
        WebElement firstCari = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//DataItem[1]")));
        new Actions(driver).doubleClick(firstCari).perform();
    }
}
