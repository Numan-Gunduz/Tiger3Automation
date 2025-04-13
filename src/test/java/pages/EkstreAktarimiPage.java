package pages;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementHelper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
        System.out.println("Menü alanının yüklenmesi bekleniyor 4 sn");
        try {
            Thread.sleep (8000); // tablo yüklemesi için bekle
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

            // 1️⃣ ID üzerinden erişmeyi dene (ama bu element gerçek IBAN değil, list item değilse iş yapmaz)
            try {
                WebElement elementById = driver.findElement(By.id("rc_select_4_list_2")); // inspect'e göre id
                if (elementById.isDisplayed() && elementById.getText().contains(hesapNo)) {
                    ElementHelper.clickByRobot(elementById);
                    System.out.println("✅ ID ile seçim yapıldı.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ ID ile erişim başarısız: " + e.getMessage());
            }

            try {
                WebElement xpathByTitle = driver.findElement(By.xpath("//div[@title='" + hesapNo + "']"));
                if (xpathByTitle.isDisplayed()) {
                    ElementHelper.clickByRobot(xpathByTitle);
                    System.out.println("✅ XPath title ile seçim başarılı.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ XPath (title) ile erişilemedi: " + e.getMessage());
            }

            // 3️⃣ XPath text içerikli normal div içeriği ile (text() fonksiyonu)
            try {
                WebElement xpathByText = driver.findElement(By.xpath("//div[contains(text(),'" + hesapNo + "')]"));
                if (xpathByText.isDisplayed()) {
                    ElementHelper.clickByRobot(xpathByText);
                    System.out.println("✅ XPath text ile seçim başarılı.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ XPath text ile erişilemedi: " + e.getMessage());
            }

            // 4️⃣ className + title kombinasyonu (daha spesifik)
            try {
                WebElement byClassAndTitle = driver.findElement(By.xpath("//div[contains(@class, 'ant-select-item-option') and @title='" + hesapNo + "']"));
                if (byClassAndTitle.isDisplayed()) {
                    ElementHelper.clickByRobot(byClassAndTitle);
                    System.out.println("✅ XPath class + title ile seçim başarılı.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ XPath class + title erişilemedi: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("❌ Dropdown işleminde hata: " + e.getMessage());
        }

        // 5️⃣ Hiçbiri olmadıysa son çare: koordinat
        ElementHelper.clickByCoordinates(550, 340);
        System.out.println("✅ Koordinat ile IBAN seçimi yapıldı.");
    }


//    public void selectAccountByHelpText(String iban) {
//        try {
//            // 1. Dropdown'u aç
//            WebElement hesapDropdown = driver.findElements(By.className("ant-select-selector")).get(1);
//            wait.until(ExpectedConditions.elementToBeClickable(hesapDropdown)).click();
//            System.out.println("✅ Hesap dropdown tıklandı.");
//            Thread.sleep(1000);
//
//            // 2. IBAN öğesini HelpText ya da Name ile bul
//            WebElement ibanElement = wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//*[(contains(@HelpText, '" + iban + "')) or (@Name='" + iban + "')]")
//            ));
//
//            // 3. IBAN öğesi görünürse odaklan ve ENTER simüle et
//            if (ibanElement.isDisplayed()) {
//                // Öğeye tıkla (odaklama için)
//                ibanElement.click();
//                Thread.sleep(500);
//
//                // Robot ile ENTER gönder
//                Robot robot = new Robot();
//                robot.keyPress(KeyEvent.VK_ENTER);
//                robot.keyRelease(KeyEvent.VK_ENTER);
//
//                System.out.println("✅ IBAN seçildi: " + iban);
//            } else {
//                System.out.println("⚠️ IBAN görünür değil.");
//            }
//
//        } catch (Exception e) {
//            System.out.println("❌ IBAN seçimi hatası: " + e.getMessage());
//        }
//    }


//    public void selectAccount(String hesapNo) {
//        try {
//            WebElement hesapDropdown = driver.findElements(By.className("ant-select-selector")).get(1);
//            wait.until(ExpectedConditions.elementToBeClickable(hesapDropdown)).click();
//            Thread.sleep(1000);
//            List<WebElement> options = driver.findElements(By.className("ant-select-item-option-content"));
//            for (WebElement opt : options) {
//                System.out.println("Dropdown seçeneği: " + opt.getText());
//            }
//
//            WebElement element = null;
//
//            // 1 - title attribute ile
//            try {
//                element = driver.findElement(By.xpath("//div[@title='" + hesapNo + "']"));
//                System.out.println("✅ XPath title ile bulundu.");
//            } catch (Exception e) {
//                System.out.println("⚠️ XPath title ile bulunamadı.");
//            }
//
//            // 2 - içerik (text) ile option-content div
//            if (element == null) {
//                try {
//                    element = driver.findElement(By.xpath("//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + hesapNo + "']"));
//                    System.out.println("✅ XPath içerik ile bulundu.");
//                } catch (Exception e) {
//                    System.out.println("⚠️ XPath içerik ile bulunamadı.");
//                }
//            }
//
//            // 3 - option container div ve title attribute ile
//            if (element == null) {
//                try {
//                    element = driver.findElement(By.xpath("//div[contains(@class,'ant-select-item-option') and @title='" + hesapNo + "']"));
//                    System.out.println("✅ XPath class + title ile bulundu.");
//                } catch (Exception e) {
//                    System.out.println("⚠️ XPath class + title ile bulunamadı.");
//                }
//            }
//
//            // 4 - Erişilebiliyorsa tıkla
//            if (element != null && element.isDisplayed()) {
//                ElementHelper.clickByRobot(element);
//                System.out.println("✅ Robot ile IBAN seçimi başarılı.");
//                return;
//            }
//
//        } catch (Exception e) {
//            System.out.println("❌ Dropdown işleminde genel hata: " + e.getMessage());
//        }
//
//        // 5 - Koordinat fallback
//        ElementHelper.clickByCoordinates(550, 340);
//        System.out.println("✅ Koordinat ile IBAN seçimi yapıldı.");
//    }


    public void clickListeleVeBekle(int maxWaitSeconds) {
        clickButtonByText("Listele"); // XPath veya koordinatla tıklar

        try {
            Thread.sleep(8000); // Statik bekleme
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Tablo kolonunun geldiğini doğrula
        try {
            By kolonKontrol = By.name("Fiş Türü");
            new WebDriverWait(driver, maxWaitSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(kolonKontrol));
            System.out.println("✅ Fiş Türü kolonu bulundu, listeleme başarılı.");
        } catch (Exception e) {
            System.out.println("❌ Listele sonrası tablo yüklenmedi: " + e.getMessage());
        }
    }
    public void clickButtonByText(String visibleText) {
        try {
            System.out.println("🔍 '" + visibleText + "' butonu XPath @Name ile aranıyor...");
            WebElement btn = driver.findElement(By.xpath("//*[contains(@Name, '" + visibleText + "')]"));
            if (btn.isDisplayed() && btn.isEnabled()) {
                btn.click();
                System.out.println("✅ '" + visibleText + "' butonuna tıklandı (XPath @Name ile).");
                return;
            } else {
                System.out.println("⚠️ Buton bulundu ama aktif değil.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ XPath @Name ile tıklama başarısız: " + e.getMessage());
        }

        System.out.println("🖱️ Koordinat ile tıklama deneniyor...");
        ElementHelper.clickByCoordinates(1157, 291);
        System.out.println("✅ Koordinat ile tıklama tamamlandı.");
    }


//    public void clickButtonByText(String visibleText) {
//        try {
//            WebElement btn = new WebDriverWait(driver, 10).until(
//                    ExpectedConditions.elementToBeClickable(
//                            By.xpath("//button[.//span[normalize-space(text())='" + visibleText + "']]")
//                    )
//            );
//            btn.click();
//            System.out.println("✅ '" + visibleText + "' butonuna tıklandı (XPath ile).");
//        } catch (Exception e) {
//            System.out.println("⚠️ XPath ile tıklama başarısız, koordinat denenecek...");
//            ElementHelper.clickByCoordinates(1157, 291);
//            System.out.println("🖱️ Koordinat ile tıklama tamamlandı.");
//        }
//    }

    // 1️⃣ Scroll'u sağa kaydırmak için kullanılır
    public static void scrollRightWithRobot() {
        try {
            Robot robot = new Robot();
            robot.mouseMove(1700, 1020);  // Scroll bar üzerine odaklan
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(1850, 1020);  // Scroll sağa çek
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("✅ Scroll bar sağa kaydırıldı.");
            Thread.sleep(1000); // tablo yüklemesi için bekle
        } catch (Exception e) {
            System.out.println("❌ Scroll bar kaydırılamadı: " + e.getMessage());
        }
    }

    // 2️⃣ Durumu "Eksik Bilgi Bulunuyor" olan satırı bul ve checkbox'ı işaretle
    public void selectRowWithDurum(String durumText) {
        scrollRightWithRobot(); // Scroll yapmadan önce kolon görünmez olabilir

        try {
            List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
            for (WebElement row : rows) {
                try {
                    // Satırdaki "Durum" hücresini bul
                    WebElement durumCell = row.findElement(By.xpath(".//td[normalize-space()='Eksik Bilgi Bulunuyor']"));


                    if (durumCell != null && durumCell.getText().trim().equalsIgnoreCase(durumText)) {
                        // Aynı satırda checkbox'ı bul ve tıkla
                        WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                        if (!checkbox.isSelected()) {
                            checkbox.click();
                            System.out.println("✅ '" + durumText + "' durumundaki satır işaretlendi.");
                        } else {
                            System.out.println("ℹ️ Checkbox zaten seçiliydi.");
                        }
                        return;
                    }

                } catch (Exception inner) {
                    // Satırda "durum" hücresi yoksa hata verme, sıradakine geç
                }
            }

            System.out.println("❌ '" + durumText + "' durumuna sahip satır bulunamadı.");

        } catch (Exception e) {
            System.out.println("❌ Satır/checkbox seçiminde genel hata: " + e.getMessage());
        }
    }

    public void selectFirstCheckbox() {
        try {
            // 1️⃣ Checkbox'ı saran label'ı bul
            WebElement label = driver.findElement(By.xpath("(//label[contains(@class,'ant-checkbox-wrapper')])[1]"));

            // 2️⃣ İçindeki input'u bulup kontrol et
            WebElement input = label.findElement(By.xpath(".//input[@type='checkbox']"));

            if (!input.isSelected()) {
                label.click();  // Tıklanabilir olan öğe label’dır
                System.out.println("✅ İlk satırdaki checkbox işaretlendi.");
            } else {
                System.out.println("ℹ️ İlk checkbox zaten seçili.");
            }
        } catch (Exception e) {
            System.out.println("❌ İlk checkbox seçilemedi: " + e.getMessage());
        }
    }

    public static void clickScrollRightArrowWithRobot() {
        try {
            Robot robot = new Robot();
            robot.mouseMove(1844, 1011); // Scroll bar sağ ok koordinatı
            Thread.sleep(300);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(400); // kısa basılı tut
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("✅ Scroll sağ oku tıklandı.");
        } catch (Exception e) {
            System.out.println("❌ Scroll oku tıklanamadı: " + e.getMessage());
        }
    }
    public void testEkstreCheckboxSecimi() {
        selectFirstCheckbox(); // İlk checkbox'ı seç
        clickScrollRightArrowWithRobot(); // Scroll sağa
        selectRowWithDurum("Eksik Bilgi Bulunuyor"); // Eksik satırı seç
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
