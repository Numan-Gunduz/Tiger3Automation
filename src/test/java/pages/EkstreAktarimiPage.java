//package pages;
//
//import java.awt.Robot;
//import java.awt.event.KeyEvent;
//import base.TestContext;
//import io.appium.java_client.MobileBy;
//import io.appium.java_client.windows.WindowsDriver;
//import org.openqa.selenium.*;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import io.appium.java_client.windows.WindowsDriver;
//
//import io.appium.java_client.windows.WindowsDriver;
//
//import io.appium.java_client.MobileBy;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Set;
//
//import io.appium.java_client.windows.WindowsDriver;
//
//
//public class EkstreAktarimiPage {
//    private WebElement selectedRowElement; // en son işlem yapılan satır
//
//    private final TestContext context;
//    private final WebDriver webDriver;    // WebView2 için
//    private final WindowsDriver winDriver; // Win32 popup için
//    private final WebDriverWait wait;
//    private String kayitliErpFisNo;
//
//    public EkstreAktarimiPage(TestContext context) {
//        this.context = context;
//        this.webDriver = context.getWebDriver();         // WebView2 için
//        this.winDriver = context.getWindowsDriver();     // Win32 popup için
//        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
//    }
//
//    public void clickSidebarMenu(String menuText) {
//        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//span[contains(@class,'ant-menu-title-content') and text()='" + menuText + "']")));
//        element.click();
//    }
//
//    public void selectBank(String bankaAdi) {
//        try {
//            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//div[@id='select_container']//div[@class='ant-select-selector']")));
//            dropdown.click();
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//div[contains(@class,'ant-select-dropdown') and not(contains(@class,'hidden'))]")));
//
//            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//div[@class='ant-select-item-option-content' and text()='" + bankaAdi + "']")));
//            option.click();
//        } catch (Exception e) {
//            System.out.println("❌ Banka seçimi hatası: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    public void selectAccount(String hesapNo) {
//        try {
//            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("(//div[contains(@class,'ant-select-selector')])[2]")));
//            dropdown.click();
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//div[contains(@class,'ant-select-dropdown') and not(contains(@class,'hidden'))]")));
//
//            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//div[@class='ant-select-item-option-content' and text()='" + hesapNo + "']")));
//            option.click();
//        } catch (Exception e) {
//            System.out.println("❌ IBAN seçimi hatası: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    public void enterStartDateDaysAgo(int daysAgo) {
//        try {
//            LocalDate targetDate = LocalDate.now().minusDays(daysAgo);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            String formattedDate = targetDate.format(formatter);
//
//            WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("logo-elements-date-picker input[slot='input']")));
//
//            dateInput.click();
//            dateInput.clear();
//            dateInput.sendKeys(formattedDate);
//            dateInput.sendKeys(Keys.ENTER);
//
//            System.out.println("✅ Başlangıç tarihi olarak " + formattedDate + " girildi (" + daysAgo + " gün önce).");
//        } catch (Exception e) {
//            System.out.println("❌ Dinamik tarih girilemedi: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void clickListele() {
//        try {
//            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//            // 🎯 Shadow DOM içindeki 'Listele' butonunun host elementini bul
//            WebElement host = webDriver.findElement(By.cssSelector("logo-elements-button[theme='secondary']"));
//            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) webDriver)
//                    .executeScript("return arguments[0].shadowRoot", host);
//
//            WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
//            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);
//            System.out.println("✅ Listele butonuna başarıyla JS ile tıklandı.");
//            WebDriverWait longWait = new WebDriverWait(webDriver, Duration.ofSeconds(45));
//            // "Lütfen bekleyiniz..." mesajının görünüp sonra kaybolmasını bekle
//            longWait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//*[contains(text(),'Verilerinizi bankalardan listeliyoruz')]")));
//            System.out.println("⏳ 'Lütfen bekleyiniz' mesajı göründü.");
//
//            longWait.until(ExpectedConditions.invisibilityOfElementLocated(
//                    By.xpath("//*[contains(text(),'Verilerinizi bankalardan listeliyoruz')]")));
//            System.out.println("⏱️ 'Lütfen bekleyiniz' mesajı kapandı.");
//
//            // 🔍 Tablo veri hücrelerinden birinin (örneğin "Fiş Türü") göründüğünden emin ol
//            // Not: Bu, bir tablo hücresi. Başlık değil. Arka planda grid/table yapısına bağlı olarak değişebilir.
//            longWait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//*[contains(text(),'Havale/EFT Fişi')]")));
//            System.out.println("✅ Kayıtlar başarıyla yüklendi.");
//
//        } catch (Exception e) {
//            System.out.println("❌ Listeleme sürecinde hata: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
////    public void selectRowWithDurum(String durumText) {
////        JavascriptExecutor js = (JavascriptExecutor) webDriver;
////        int maxScrollAttempts = 30; // Maksimum kaydırma denemesi
////        boolean durumBulundu = false;
////
////        for (int i = 0; i < maxScrollAttempts; i++) {
////            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
////
////            for (WebElement row : rows) {
////                try {
////                    // Her row'u merkeze al
////                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", row);
////                    Thread.sleep(100);
////
////                    List<WebElement> cells = row.findElements(By.tagName("td"));
////                    for (WebElement cell : cells) {
////                        String cellText = cell.getText().trim();
////                        if (cellText.equalsIgnoreCase(durumText)) {
////                            WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
////                            if (!checkbox.isSelected()) {
////                                checkbox.click();
////                            }
////                            selectedRowElement = row;
////                            System.out.println("✅ '" + durumText + "' durumlu satır bulundu ve işaretlendi.");
////                            return; // Satır bulunduğu anda metodu bitiriyoruz
////                        }
////                    }
////                } catch (StaleElementReferenceException e) {
////                    System.out.println("⚠️ Stale element yakalandı, satır yeniden alınacak...");
////                    continue;
////                } catch (Exception e) {
////                    System.out.println("⚠️ Hata oluştu: " + e.getMessage());
////                }
////            }
////
////            // Eğer bu scroll turunda da bulunamadıysa aşağı kaydır
////            js.executeScript("window.scrollBy(0, 800);");
////            try {
////                Thread.sleep(200); // Biraz bekle (sayfa yüklenebilsin)
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }
////        }
////
////        throw new RuntimeException("❌ '" + durumText + "' durumunda kayıt bulunamadı!");
////    }
//
//    public void selectRowWithDurum(String durumText) {
//        JavascriptExecutor js = (JavascriptExecutor) webDriver;
//        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
//        int maxScrollAttempts = 30;
//
//        for (int attempt = 0; attempt < maxScrollAttempts; attempt++) {
//            try {
//                List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//
//                for (WebElement row : rows) {
//                    try {
//                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", row);
//                        Thread.sleep(100); // Küçük scroll bekleme
//
//                        List<WebElement> cells = row.findElements(By.tagName("td"));
//                        for (WebElement cell : cells) {
//                            String cellText = cell.getText().trim();
//                            if (cellText.equalsIgnoreCase(durumText)) {
//                                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                                if (!checkbox.isSelected()) {
//                                    checkbox.click();
//                                }
//                                selectedRowElement = row;
//                                System.out.println("✅ '" + durumText + "' durumlu satır bulundu ve işaretlendi.");
//                                return;
//                            }
//                        }
//                    } catch (StaleElementReferenceException e) {
//                        System.out.println("⚠️ Stale element yakalandı, sadece bu satır atlanıyor.");
//                        continue;
//                    }
//                }
//
//                // Eğer burada bulunmadıysa scroll yapmadan önce bekleyelim
//                js.executeScript("window.scrollBy(0, 800);");
//                Thread.sleep(300); // Scroll yaptıktan sonra küçük bekleme
//
//                // SCROLL SONRASI DOM'un stabil hale gelmesini bekle
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr")));
//
//            } catch (Exception e) {
//                System.out.println("⚠️ Genel hata: " + e.getMessage());
//            }
//        }
//
//        throw new RuntimeException("❌ '" + durumText + "' durumunda kayıt bulunamadı!");
//    }
//
//
//    public void changeFisTypeTo(String contextMenuText, String fisTuru) {
//        try {
//            // Tablo üzerindeki bir alanı bul (örneğin "Fiş Türü" başlığı olabilir)
//            WebElement tableArea = wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//th[contains(.,'Fiş Türü')]")));
//
//            // Sağ tık aksiyonu tetikleniyor
//            new Actions(webDriver).contextClick(tableArea).perform();
//            System.out.println("✅ Sağ tık başarıyla yapıldı.");
//
//            // "Fiş Türü Değiştir" menüsünü tıkla
//            WebElement contextMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + contextMenuText + "']")));
//            contextMenu.click();
//            System.out.println("✅ '" + contextMenuText + "' menüsü seçildi.");
//
//            // Alt menüden istenen fiş türünü seç
//            WebElement fisOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + fisTuru + "']")));
//            fisOption.click();
//            System.out.println("✅ '" + fisTuru + "' seçeneği seçildi.");
//
//            // Onay popup'ında "Tamam" butonuna tıkla
//            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Tamam']")));
//            confirmButton.click();
//            System.out.println("✅ Fiş türü değişikliği onaylandı.");
//
//        } catch (Exception e) {
//            System.out.println("❌ Fiş türü değiştirme hatası: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    public boolean isFisTuruUpdated(String expectedText) {
//        try {
//            // Önce sayfanın Fiş Türü kolonundaki yeni değerlerin gelmesini bekleyelim
//            WebDriverWait shortWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
//            shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + expectedText + "')]")));
//
//            // Şimdi güncel şekilde locate edip kontrol edelim
//            List<WebElement> cells = webDriver.findElements(By.xpath("//*[contains(text(),'" + expectedText + "')]"));
//            for (WebElement cell : cells) {
//                try {
//                    if (cell.isDisplayed()) {
//                        return true;
//                    }
//                } catch (StaleElementReferenceException stale) {
//                    System.out.println("⚠️ Stale element yakalandı, tekrar bulmaya çalışılıyor...");
//                    // Eğer stale olursa bile ignore edip devam etsin
//                    continue;
//                }
//            }
//            return false;
//        } catch (Exception e) {
//            System.out.println("❌ Fiş türü kontrolü sırasında hata: " + e.getMessage());
//            return false;
//        }
//    }
//
//    //buradaki tabloların sütün bilgisi yerie direk olarak dinamik bulacağımız şekilde eklemeliyiz
//    public boolean validateDurumForEmptyCariHesap(String expectedDurumText) {
//        try {
//            // Başlıkların index'lerini bul
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int cariHesapIndex = -1;
//            int durumIndex = -1;
//
//            for (int i = 0; i < headers.size(); i++) {
//                String headerText = headers.get(i).getText().trim();
//                if (headerText.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
//                    cariHesapIndex = i + 1;
//                }
//                if (headerText.equalsIgnoreCase("Durum")) {
//                    durumIndex = i + 1;
//                }
//            }
//
//            if (cariHesapIndex == -1 || durumIndex == -1) {
//                throw new RuntimeException("❌ 'ERP Cari Hesap Kodu' veya 'Durum' başlığı bulunamadı.");
//            }
//
//            // Satırları gez ve sadece seçilmiş checkbox olan satırı bul
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//            for (WebElement row : rows) {
//                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                if (checkbox.isSelected()) {
//                    WebElement cariHesapCell = row.findElement(By.xpath("./td[" + cariHesapIndex + "]"));
//                    WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));
//
//                    String cariHesapText = cariHesapCell.getText().trim();
//                    String durumText = durumCell.getText().trim();
//
//                    if (cariHesapText.isEmpty()) {
//                        if (!durumText.equals(expectedDurumText)) {
//                            System.out.println("❌ Durum hatalı! Beklenen: '" + expectedDurumText + "', Bulunan: '" + durumText + "'");
//                            return false;
//                        } else {
//                            System.out.println("✅ Doğru: Cari hesap boş ve Durum alanı doğru: '" + durumText + "'");
//                            return true;
//                        }
//                    } else {
//                        System.out.println("ℹ️ Cari hesap boş değil, kontrol edilmedi.");
//                    }
//                }
//            }
//
//            System.out.println("❌ Seçilen ve cari hesabı boş olan satır bulunamadı.");
//            return false;
//        } catch (Exception e) {
//            System.out.println("❌ Hata oluştu: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public void clickErpCariKodDots() {
//        try {
//            // 1️⃣ ERP Cari Hesap Kodu sütun index'ini bul
//            List<WebElement> headers = webDriver.findElements(By.xpath("//table//thead//th"));
//            int targetIndex = -1;
//            for (int i = 0; i < headers.size(); i++) {
//                if (headers.get(i).getText().trim().equals("ERP Cari Hesap Kodu")) {
//                    targetIndex = i;
//                    break;
//                }
//            }
//
//            if (targetIndex == -1)
//                throw new RuntimeException("❌ 'ERP Cari Hesap Kodu' başlığı bulunamadı.");
//
//            System.out.println("🔎 ERP Cari Hesap Kodu sütun index: " + targetIndex);
//
//            // 2️⃣ Satırları bul ve checkbox'ı seçili olanı bul
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//            WebElement selectedRow = null;
//
//            for (WebElement row : rows) {
//                try {
//                    WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                    if (checkbox.isSelected()) {
//                        selectedRow = row;
//                        break;
//                    }
//                } catch (Exception ignored) {
//                }
//            }
//
//            if (selectedRow == null)
//                throw new RuntimeException("❌ Seçili (checked) satır bulunamadı.");
//
//            // 3️⃣ Doğru hücreyi al
//            List<WebElement> cells = selectedRow.findElements(By.tagName("td"));
//            if (targetIndex >= cells.size())
//                throw new RuntimeException("❌ ERP Cari Hesap Kodu sütununa denk gelen hücre yok.");
//
//            WebElement targetCell = cells.get(targetIndex);
//
//            // 4️⃣ Hücredeki üç nokta butonunu bul
//            WebElement host = targetCell.findElement(By.cssSelector("logo-elements-icon[icon='leds:three_dots_hor']"));
//
//            // 5️⃣ Scroll + JS click
//            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", host);
//            Thread.sleep(300); // scroll sonrası küçük gecikme
//            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", host);
//
//            System.out.println("✅ ERP Cari Hesap Kodu alanındaki üç nokta butonuna başarıyla tıklandı.");
//
//        } catch (Exception e) {
//            System.out.println("❌ Üç nokta tıklanırken hata: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void clickSelectButtonOnCariPopup() {
//        try {
//            System.out.println("✅ Bankalar popup açıldı. Şimdi ENTER gönderiliyor...");
//            Thread.sleep(4000); // Popup'ın tam yüklenmesini bekliyoruz
//
//            // 🎯 Gerçek klavye ile ENTER tuşuna bas
//            Robot robot = new Robot();
//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);
//            System.out.println("✅ Robot ile ENTER tuşu gönderildi.");
//            Thread.sleep(300); // Yeni pencere açılması için biraz bekleme
//
//        } catch (Exception e) {
//            System.out.println("❌ Banka seçimi sırasında hata oluştu: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void clickSelectButtonOnCariPopupBankaKodu() {
//        try {
//            System.out.println("✅ Bankalar popup açıldı. Şimdi ENTER gönderiliyor...");
//            Thread.sleep(2000); // Popup'ın tam yüklenmesini bekliyoruz
//
//            // 🎯 Gerçek klavye ile ENTER tuşuna bas
//            Robot robot = new Robot();
//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);
//            System.out.println("✅ Robot ile ENTER tuşu gönderildi.");
//
//            Thread.sleep(3000); // Yeni pencere açılması için biraz bekleme
//
//            // 🎯 Şimdi açılan pencerede Seç butonuna tıklıyoruz
//            WebElement selectButton = winDriver.findElement(MobileBy.AccessibilityId("SelBtn"));
//            selectButton.click();
//            System.out.println("✅ 'Seç' butonuna başarıyla tıklandı.");
//
//        } catch (Exception e) {
//            System.out.println("❌ Banka seçimi sırasında hata oluştu: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean isDurumKaydedilebilirGorunuyor() {
//        try {
//            // Başlık indexlerini bul
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int cariHesapIndex = -1;
//            int durumIndex = -1;
//
//            for (int i = 0; i < headers.size(); i++) {
//                String headerText = headers.get(i).getText().trim();
//                if (headerText.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
//                    cariHesapIndex = i + 1;
//                }
//                if (headerText.equalsIgnoreCase("Durum")) {
//                    durumIndex = i + 1;
//                }
//            }
//
//            if (cariHesapIndex == -1 || durumIndex == -1) {
//                throw new RuntimeException("❌ 'ERP Cari Hesap Kodu' veya 'Durum' sütunu bulunamadı.");
//            }
//
//            // Satırları gez, seçili olan checkbox'ı bul
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//            for (WebElement row : rows) {
//                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                if (checkbox.isSelected()) {
//                    WebElement cariCell = row.findElement(By.xpath("./td[" + cariHesapIndex + "]"));
//                    WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));
//
//                    String cariValue = cariCell.getText().trim();
//                    String durumValue = durumCell.getText().trim();
//
//                    System.out.println("🔍 Cari: '" + cariValue + "', Durum: '" + durumValue + "'");
//
//                    return !cariValue.isEmpty() && durumValue.equalsIgnoreCase("Kaydedilebilir");
//                }
//            }
//
//            System.out.println("❌ Seçili satır bulunamadı.");
//            return false;
//
//        } catch (Exception e) {
//            System.out.println("❌ Durum kontrolünde hata: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public void clickFisOlusturButton() {
//        try {
//            WebElement host = webDriver.findElement(By.cssSelector("logo-elements-button[theme='primary']"));
//            SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) webDriver)
//                    .executeScript("return arguments[0].shadowRoot", host);
//
//            WebElement span = shadowRoot.findElement(By.cssSelector("span[part='label']"));
//            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", span);
//
//            System.out.println("✅ 'Fiş Oluştur' butonuna başarıyla tıklandı.");
//        } catch (Exception e) {
//            throw new RuntimeException("❌ Fiş Oluştur butonuna tıklanırken hata: " + e.getMessage());
//        }
//    }
//
//    public void clickEvetOnConfirmationPopup() {
//        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
//        WebElement evetButton = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//button[contains(@class, 'ant-btn-primary') and .//span[text()='Evet']]")
//        ));
//
//        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", evetButton);
//        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", evetButton);
//
//        System.out.println("✅ 'Evet' butonuna tıklandı.");
//    }
//
//    public boolean isSuccessToastMessageVisible() {
//        String toastMessage = "Hesap ekstresi kayıtlarına ait fiş oluşturma süreci tamamlandı";
//        By toastLocator = By.xpath("//*[contains(text(),'" + toastMessage + "')]");
//
//        try {
//            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
//            wait.pollingEvery(Duration.ofMillis(250));
//            wait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
//
//            // Öncelikle toast mesajının DOM'a yüklenmesini bekleyelim
//            WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(toastLocator));
//
//            // Toast'un görünürlüğünü kontrol etmek için alternatif yaklaşım
//            boolean isVisible = (Boolean)((JavascriptExecutor) webDriver).executeScript(
//                    "const elem = arguments[0];" +
//                            "const rect = elem.getBoundingClientRect();" +
//                            "return (" +
//                            "rect.width > 0 && rect.height > 0 && " +
//                            "window.getComputedStyle(elem).visibility !== 'hidden' && " +
//                            "window.getComputedStyle(elem).display !== 'none');",
//                    toast
//            );
//
//            if (isVisible) {
//                System.out.println("✅ Başarı mesajı gerçekten görünür durumda.");
//            } else {
//                System.out.println("⚠️ Başarı mesajı DOM'da mevcut fakat stil veya boyut nedeniyle görünmüyor.");
//            }
//
//            return isVisible;
//
//        } catch (TimeoutException e) {
//            System.out.println("❌ Toast mesajı zaman aşımına uğradı: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean isDurumEslendiGorunuyor() {
//        try {
//            if (selectedRowElement == null)
//                throw new RuntimeException("❌ Önceden seçilen satır kaydedilmemiş.");
//
//            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
//            wait.pollingEvery(Duration.ofMillis(250));
//            wait.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
//
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int durumIndex = -1;
//
//            for (int i = 0; i < headers.size(); i++) {
//                String header = headers.get(i).getText().trim();
//                if (header.equalsIgnoreCase("Durum")) {
//                    durumIndex = i + 1;
//                    break;
//                }
//            }
//
//            if (durumIndex == -1)
//                throw new RuntimeException("❌ 'Durum' sütunu bulunamadı.");
//            final int finalDurumIndex = durumIndex; // 🔥 Burada kopyaladık
//            // Durum hücresinin Eşlendi olmasını bekleyelim
//            boolean isMatched = wait.until(driver -> {
//                WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + finalDurumIndex + "]"));
//                String durumText = durumCell.getText().trim();
//                System.out.println("⏳ Güncel Durum: " + durumText);
//                return durumText.equalsIgnoreCase("Eşlendi");
//            });
//
//            if (isMatched) {
//                System.out.println("✅ Durum başarıyla 'Eşlendi' oldu.");
//            }
//
//            return isMatched;
//
//        } catch (TimeoutException te) {
//            System.out.println("❌ Durum zaman aşımı: Durum 'Eşlendi' olmadı.");
//            return false;
//        } catch (Exception e) {
//            System.out.println("❌ Durum eşleşme kontrol hatası: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean isErpFisNoDoluMu() {
//        try {
//            if (selectedRowElement == null)
//                throw new RuntimeException("❌ Önceden seçilen satır kaydedilmemiş.");
//
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int fisNoIndex = -1;
//
//            for (int i = 0; i < headers.size(); i++) {
//                String header = headers.get(i).getText().trim();
//                if (header.equalsIgnoreCase("ERP Fiş No")) {
//                    fisNoIndex = i + 1;
//                    break;
//                }
//            }
//
//            if (fisNoIndex == -1)
//                throw new RuntimeException("❌ 'ERP Fiş No' sütunu bulunamadı.");
//
//            WebElement fisNoCell = selectedRowElement.findElement(By.xpath("./td[" + fisNoIndex + "]"));
//            String text = fisNoCell.getText().trim();
//            System.out.println("🔍 Seçilen satırdaki ERP Fiş No: '" + text + "'");
//
//            return !text.isEmpty();
//
//        } catch (Exception e) {
//            System.out.println("❌ ERP Fiş No kontrol hatası: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public String getErpFisNoFromSelectedRow() {
//        if (selectedRowElement == null) {
//            throw new RuntimeException("❌ Önceden seçili satır yok.");
//        }
//
//        List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//        int fisNoIndex = -1;
//
//        for (int i = 0; i < headers.size(); i++) {
//            String header = headers.get(i).getText().trim();
//            if (header.equalsIgnoreCase("ERP Fiş No")) {
//                fisNoIndex = i + 1;
//                break;
//            }
//        }
//
//        if (fisNoIndex == -1)
//            throw new RuntimeException("❌ ERP Fiş No sütunu bulunamadı.");
//
//        WebElement fisNoCell = selectedRowElement.findElement(By.xpath("./td[" + fisNoIndex + "]"));
//        return fisNoCell.getText().trim();
//    }
//
//    public void openFisPopupFromContextMenu(String secenek) {
//        try {
//            if (selectedRowElement == null) {
//                throw new RuntimeException("❌ Sağ tıklanacak satır bulunamadı. 'selectedRowElement' boş!");
//            }
//
//            Actions actions = new Actions(webDriver);
//            actions.moveToElement(selectedRowElement).contextClick().perform(); // 📢 sadece satırın kendisine sağ tık yapıyoruz
//            System.out.println("✅ Seçili satıra başarıyla sağ tık yapıldı.");
//
//            Thread.sleep(500); // Menü açılması için küçük bekleme
//
//            By secenekLocator = By.xpath("//span[contains(@class,'menu-title-content') and text()='" + secenek + "']");
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(secenekLocator));
//            System.out.println("✅ '" + secenek + "' seçeneği görünür durumda.");
//
//            wait.until(ExpectedConditions.elementToBeClickable(secenekLocator));
//            WebElement secenekElement = webDriver.findElement(secenekLocator);
//            secenekElement.click();
//            System.out.println("✅ '" + secenek + "' seçeneğine başarıyla tıklandı.");
//
//            Thread.sleep(3000); // popup'ın açılması için sabit bekleme
//
//        } catch (Exception e) {
//            System.out.println("❌ '" + secenek + "' popup açılamadı: " + e.getMessage());
//            throw new RuntimeException("❌ '" + secenek + "' popup açılamadı: " + e.getMessage(), e);
//        }
//    }
//
//    public String getFisNoFromPopup() {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            WebElement fisNoField = winDriver.findElement(MobileBy.AccessibilityId("FicheNoEdit"));
//            String value = fisNoField.getText().trim();
//            System.out.println("📋 Popup içinden alınan fiş no: " + value);
//            return value;
//        } catch (Exception e) {
//            throw new RuntimeException("❌ Win32 popup'tan Fiş No alınamadı: " + e.getMessage());
//        }
//    }
//
//
//
//
///*virman ile ilgili metotlar buraya gelecek */
//
//    // 📌 Yüklenen ekstre kayıtlarında "Eksik Bilgi Bulunuyor" veya "Kaydedilebilir" durumundaki satırı seç
//    public void selectRowWithDurumOrDurum(String durum1, String durum2) {
//        JavascriptExecutor js = (JavascriptExecutor) webDriver;
//        int maxScrollAttempts = 30;
//
//        for (int i = 0; i < maxScrollAttempts; i++) {
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//
//            for (WebElement row : rows) {
//                try {
//                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", row);
//                    Thread.sleep(100);
//
//                    List<WebElement> cells = row.findElements(By.tagName("td"));
//                    for (WebElement cell : cells) {
//                        String cellText = cell.getText().trim();
//                        if (cellText.equalsIgnoreCase(durum1) || cellText.equalsIgnoreCase(durum2)) {
//                            WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                            if (!checkbox.isSelected()) {
//                                checkbox.click();
//                            }
//                            selectedRowElement = row;
//                            System.out.println("✅ '" + cellText + "' durumlu satır bulundu ve işaretlendi.");
//                            return;
//                        }
//                    }
//                } catch (StaleElementReferenceException e) {
//                    System.out.println("⚠️ Stale element yakalandı, row yeniden alınacak...");
//                    continue;
//                } catch (Exception e) {
//                    System.out.println("⚠️ Diğer hata: " + e.getMessage());
//                }
//            }
//
//            js.executeScript("window.scrollBy(0, 800);");
//            try {
//                Thread.sleep(150);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        throw new RuntimeException("❌ '" + durum1 + "' veya '" + durum2 + "' durumlu kayıt bulunamadı!");
//    }
//
//    // 📌 ERP Banka Hesap Kodu boşken, Durum değerini kontrol et
//    public boolean validateDurumForEmptyBankaHesap(String expectedDurumText) {
//        try {
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int bankaHesapIndex = -1;
//            int durumIndex = -1;
//
//            for (int i = 0; i < headers.size(); i++) {
//                String headerText = headers.get(i).getText().trim();
//                if (headerText.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
//                    bankaHesapIndex = i + 1;
//                }
//                if (headerText.equalsIgnoreCase("Durum")) {
//                    durumIndex = i + 1;
//                }
//            }
//
//            if (bankaHesapIndex == -1 || durumIndex == -1) {
//                throw new RuntimeException("❌ 'ERP Banka Hesap Kodu' veya 'Durum' başlığı bulunamadı.");
//            }
//
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//            for (WebElement row : rows) {
//                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                if (checkbox.isSelected()) {
//                    WebElement bankaHesapCell = row.findElement(By.xpath("./td[" + bankaHesapIndex + "]"));
//                    WebElement durumCell = row.findElement(By.xpath("./td[" + durumIndex + "]"));
//
//                    String bankaHesapText = bankaHesapCell.getText().trim();
//                    String durumText = durumCell.getText().trim();
//
//                    if (bankaHesapText.isEmpty()) {
//                        if (!durumText.equals(expectedDurumText)) {
//                            System.out.println("❌ Durum hatalı! Beklenen: '" + expectedDurumText + "', Bulunan: '" + durumText + "'");
//                            return false;
//                        } else {
//                            System.out.println("✅ Doğru: Banka hesap boş ve Durum doğru: '" + durumText + "'");
//                            return true;
//                        }
//                    } else {
//                        System.out.println("ℹ️ Banka hesap boş değil, kontrol edilmedi.");
//                    }
//                }
//            }
//
//            System.out.println("❌ Seçili ve banka hesabı boş olan satır bulunamadı.");
//            return false;
//        } catch (Exception e) {
//            System.out.println("❌ Hata oluştu: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // 📌 ERP Banka Hesap Kodu alanındaki üç noktaya tıkla
//    public void clickErpBankaKodDots() {
//        try {
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int targetIndex = -1;
//            for (int i = 0; i < headers.size(); i++) {
//                if (headers.get(i).getText().trim().equalsIgnoreCase("ERP Banka Hesap Kodu")) {
//                    targetIndex = i;
//                    break;
//                }
//            }
//
//            if (targetIndex == -1)
//                throw new RuntimeException("❌ 'ERP Banka Hesap Kodu' başlığı bulunamadı.");
//
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//            WebElement selectedRow = null;
//            for (WebElement row : rows) {
//                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                if (checkbox.isSelected()) {
//                    selectedRow = row;
//                    break;
//                }
//            }
//
//            if (selectedRow == null)
//                throw new RuntimeException("❌ Seçili (checked) satır bulunamadı.");
//
//            List<WebElement> cells = selectedRow.findElements(By.tagName("td"));
//            if (targetIndex >= cells.size())
//                throw new RuntimeException("❌ ERP Banka Hesap Kodu hücresi bulunamadı.");
//
//            WebElement targetCell = cells.get(targetIndex);
//            WebElement host = targetCell.findElement(By.cssSelector("logo-elements-icon[icon='leds:three_dots_hor']"));
//
//            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", host);
//            Thread.sleep(300);
//            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", host);
//
//            System.out.println("✅ ERP Banka Hesap Kodu alanındaki üç noktaya başarıyla tıklandı.");
//
//        } catch (Exception e) {
//            System.out.println("❌ ERP Banka Hesap Kodu üç nokta tıklama hatası: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean isDurumKaydedilebilirBankaKod() {
//        try {
//
//            Thread.sleep(4000);
//
//            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
//            // Önce tablo satırlarının yüklendiğinden emin ol
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr")));
//
//            List<WebElement> headers = webDriver.findElements(By.xpath("//thead//th"));
//            int bankaHesapIndex = -1;
//            int durumIndex = -1;
//
//            for (int i = 0; i < headers.size(); i++) {
//                String headerText = headers.get(i).getText().trim();
//                if (headerText.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
//                    bankaHesapIndex = i + 1;
//                }
//                if (headerText.equalsIgnoreCase("Durum")) {
//                    durumIndex = i + 1;
//                }
//            }
//
//            if (bankaHesapIndex == -1 || durumIndex == -1)
//                throw new RuntimeException("❌ 'ERP Banka Hesap Kodu' veya 'Durum' başlığı bulunamadı.");
//
//            List<WebElement> rows = webDriver.findElements(By.xpath("//tbody/tr"));
//            for (WebElement row : rows) {
//                try {
//                    WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
//                    if (checkbox.isSelected()) {
//                        List<WebElement> cells = row.findElements(By.tagName("td"));
//
//                        if (bankaHesapIndex - 1 >= cells.size() || durumIndex - 1 >= cells.size()) {
//                            System.out.println("❌ Hücre sayısı başlık sayısıyla uyuşmuyor.");
//                            return false;
//                        }
//
//                        String bankaValue = cells.get(bankaHesapIndex - 1).getText().trim();
//                        String durumValue = cells.get(durumIndex - 1).getText().trim();
//
//                        System.out.println("🔍 Banka Hesap: '" + bankaValue + "', Durum: '" + durumValue + "'");
//
//                        return !bankaValue.isEmpty() && durumValue.equalsIgnoreCase("Kaydedilebilir");
//                    }
//                } catch (StaleElementReferenceException staleEx) {
//                    System.out.println("⚠️ Stale element oluştu, satır atlandı.");
//                    continue;
//                }
//            }
//
//            System.out.println("❌ Seçili satır bulunamadı veya koşullar sağlanmadı.");
//            return false;
//
//        } catch (Exception e) {
//            System.out.println("❌ Banka Hesap kontrolünde hata: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//}
//
//
//
package pages;

import base.TestContext;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='select_container']//div[@class='ant-select-selector']")));
        dropdown.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='ant-select-item-option-content' and text()='" + bank + "']")));
        option.click();
    }

    public void selectAccount(String iban) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class,'ant-select-selector')])[2]")));
        dropdown.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='ant-select-item-option-content' and text()='" + iban + "']")));
        option.click();
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
        throw new RuntimeException("❌ İstenen durumda satır bulunamadı.");
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
                        System.out.println("✅ Fiş türü bulundu: " + expectedText);
                        return true;
                    }
                } catch (StaleElementReferenceException stale) {
                    System.out.println("⚠️ Stale element oluştu, yeniden deneniyor...");
                    // tekrar locate edelim
                    WebElement refreshedCell = webDriver.findElement(By.xpath("//*[contains(text(),'" + expectedText + "')]"));
                    if (refreshedCell.isDisplayed()) {
                        return true;
                    }
                }
            }

            System.out.println("❌ Fiş türü bulunamadı: " + expectedText);
            return false;

        } catch (Exception e) {
            System.out.println("❌ Fiş türü kontrolü sırasında hata: " + e.getMessage());
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
        if (fieldIndex == -1 || durumIndex == -1) throw new RuntimeException("❌ Başlıklar bulunamadı");

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

    public void clickErpHizmetKodDots() {clickThreeDotsInField("ERP Hizmet Kodu");
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
        if (targetIndex == -1) throw new RuntimeException("❌ Sütun bulunamadı");
        WebElement cell = selectedRowElement.findElements(By.tagName("td")).get(targetIndex);
        WebElement host = cell.findElement(By.cssSelector("logo-elements-icon[icon='leds:three_dots_hor']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", host);
    }
    public void clickThreeDotsForField(String alan) {
        if (alan.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
            clickErpCariKodDots();
        } else if (alan.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
            clickErpBankaKodDots();
        }
        else if (alan.equalsIgnoreCase("ERP Hizmet Kodu")) {
            clickErpHizmetKodDots();
        }

        else {
            throw new RuntimeException("❌ Üç nokta tıklama desteklenmiyor: " + alan);
        }
    }
//    public void clickSelectButtonForField(String alan) {
//        try {
//            if (alan.toLowerCase().contains("banka")) {
//                clickSelectButtonOnCariPopupBankaKodu();
//            } else {
//                clickSelectButtonOnCariPopup();
//            }
//        } catch (AWTException | InterruptedException e) {
//            throw new RuntimeException("❌ Select butonu tıklanırken hata oluştu: " + e.getMessage(), e);
//        }
//    }
public void clickSelectButtonForField(String alan) {
    try {
        if (alan.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
            clickSelectButtonOnCariPopupBankaKodu();
        } else if (alan.equalsIgnoreCase("ERP Hizmet Kodu")) {
            clickCheckboxAndThenSelectButtonForHizmet();
        } else if (alan.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
            clickSelectButtonOnCariPopup();
        } else {
            throw new RuntimeException("❌ Select butonu tıklama desteklenmiyor: " + alan);
        }
    } catch (AWTException | InterruptedException e) {
        throw new RuntimeException("❌ Select butonu tıklanırken hata oluştu: " + e.getMessage(), e);
    }
}



    public boolean checkDurumUpdatedAfterFieldFill(String alan, String expectedDurum) {
        if (alan.equalsIgnoreCase("ERP Cari Hesap Kodu")) {
            return isDurumKaydedilebilirGorunuyor();
        } else if (alan.equalsIgnoreCase("ERP Banka Hesap Kodu")) {
            return isDurumKaydedilebilirBankaKod();
        }
        else if (alan.equalsIgnoreCase("ERP Hizmet Kodu")) {
            return isDurumKaydedilebilirHizmetKodu();
        }

        else {
            throw new RuntimeException("❌ Alan tipi desteklenmiyor: " + alan);
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

    public boolean isDurumKaydedilebilirGorunuyor() {
        return isFieldFilledAndDurum("ERP Cari Hesap Kodu", "Kaydedilebilir");
    }

    public boolean isDurumKaydedilebilirBankaKod() {
        return isFieldFilledAndDurum("ERP Banka Hesap Kodu", "Kaydedilebilir");
    }
    public boolean isDurumKaydedilebilirHizmetKodu() {
        return isFieldFilledAndDurum("ERP Hizmet Kodu", "Kaydedilebilir");
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
                throw new RuntimeException("❌ '" + header + "' veya 'Durum' başlığı bulunamadı.");
            }

            WebElement fieldCell = selectedRowElement.findElement(By.xpath("./td[" + fieldIndex + "]"));
            WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + durumIndex + "]"));

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            boolean fieldFilled = wait.until(driver -> !fieldCell.getText().trim().isEmpty());

            boolean durumDogru = wait.until(driver -> {
                String durumText = durumCell.getAttribute("innerText").trim();
                System.out.println("🔍 Beklenen: '" + expectedDurum + "', Gerçek: '" + durumText + "'");
                return durumText.equalsIgnoreCase(expectedDurum);
            });

            return fieldFilled && durumDogru;

        } catch (Exception e) {
            System.out.println("❌ isFieldFilledAndDurum() içinde hata: " + e.getMessage());
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
            System.out.println("❌ Toast mesajı beklenen sürede DOM'a gelmedi.");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Toast kontrolü sırasında hata: " + e.getMessage());
            return false;
        }
    }


    public boolean isDurumEslendiGorunuyor() {
        try {
            if (selectedRowElement == null)
                throw new RuntimeException("❌ Önceden seçilen satır kaydedilmemiş.");

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
                throw new RuntimeException("❌ 'Durum' sütunu bulunamadı.");

            final int finalDurumIndex = durumIndex;

            return wait.until(driver -> {
                WebElement durumCell = selectedRowElement.findElement(By.xpath("./td[" + finalDurumIndex + "]"));
                String durumText = durumCell.getText().trim();
                System.out.println("⏳ Bekleniyor: '" + durumText + "'");
                return durumText.equalsIgnoreCase("Eşlendi");
            });

        } catch (TimeoutException te) {
            System.out.println("❌ 'Eşlendi' durumu zaman aşımına uğradı.");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Durum kontrol hatası: " + e.getMessage());
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
            System.out.println("❌ Fiş No alanı bulunamadı: " + e.getMessage());
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
                throw new RuntimeException("❌ 'Fiş Türü' sütunu bulunamadı.");
            }

            WebElement cell = selectedRowElement.findElement(By.xpath("./td[" + fisTuruIndex + "]"));
            String text = cell.getText().trim();
            System.out.println("🔍 Seçili satırdaki fiş türü: " + text);
            return text;

        } catch (Exception e) {
            System.out.println("❌ getCurrentFisTuru() hata: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



//    public String getFisNoFromPopup(String fisTuru) {
//        try {
//            WebDriverWait wait = new WebDriverWait(winDriver, Duration.ofSeconds(10));
//
//            By locator;
//            if (fisTuru.equalsIgnoreCase("Hizmet Faturası Fişi")) {
//                locator = MobileBy.AccessibilityId("ficheNoEdit"); // Hizmet
//            } else {
//                locator = MobileBy.AccessibilityId("FicheNoEdit"); // Diğer fişler
//            }
//
//            WebElement fisNoField = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//            return fisNoField.getText().trim();
//
//        } catch (Exception e) {
//            System.out.println("❌ Fiş no alanı alınamadı: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }

    public String getFisNoFromPopup_HizmetFaturasi() {
        try {
            WebDriverWait wait = new WebDriverWait(winDriver, Duration.ofSeconds(10));
            WebElement fisNo = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId("ficheNoEdit")));
            return fisNo.getText().trim();
        } catch (Exception e) {
            System.out.println("❌ Hizmet Faturası fiş no okunamadı: " + e.getMessage());
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
            System.out.println("❌ Klasik fiş no okunamadı: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public void closeOpenPopupsOneByOne() {
        try {
            System.out.println("🧹 Açık popup pencereleri kapatılıyor...");

            // İlk pencereyi kapat
            WebElement closeBtn1 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn1.click();
            System.out.println("✅ İlk popup kapatıldı.");

            Thread.sleep(1500); // bekle, ikinci pencerenin ön plana geçmesini sağla

            // İkinci pencereyi kapat
            WebElement closeBtn2 = winDriver.findElement(MobileBy.AccessibilityId("CloseBtn"));
            closeBtn2.click();
            System.out.println("✅ İkinci popup kapatıldı.");

        } catch (Exception e) {
            System.out.println("❌ Popup kapatılırken hata oluştu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



}
