package pages;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.awt.*;
import java.awt.event.InputEvent;

public class KuralTanimlamaPage {

    private final WindowsDriver driver;

    public KuralTanimlamaPage(WindowsDriver driver) {
        this.driver = driver;
    }

    public void clickSidebarMenu(String menuName) {
        WebElement element = driver.findElement(By.name(menuName));
        element.click();
    }

    public void clickButtonByText(String buttonText) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(@Name, '" + buttonText + "')]")
        ));
        button.click();
    }

    public boolean isPageTitleDisplayed(String expectedText) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@Name, '" + expectedText + "')]")
            ));
            return true;
        } catch (Exception e) {
            System.out.println("❌ Sayfa başlığı bulunamadı: " + expectedText);
            return false;
        }
    }

    public void clickDropdownByClassName(String className) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.className(className)));
        dropdown.click();
    }

    public void selectOptionByVisibleText(String visibleText) {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        try {
            List<WebElement> options = driver.findElements(By.className("ant-select-item-option"));
            System.out.println("🔢 Seçenek sayısı: " + options.size());

            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(visibleText)) {
                    option.click();
                    System.out.println("✅ '" + visibleText + "' seçeneği tıklandı.");
                    return;
                }
            }

            // Eğer liste boşsa ya da seçenek bulunamazsa fallback olarak Robot tıklama
            if (visibleText.equalsIgnoreCase("Finansbank")) {
                System.out.println("⚠️ DOM üzerinden Finansbank bulunamadı, Robot ile tıklanıyor...");
                clickByCoordinates(475, 421); // burayı inspect çıktına göre ayarlayabilirsin
            } else {
                throw new RuntimeException("❌ '" + visibleText + "' seçeneği dropdown içinde bulunamadı.");
            }

        } catch (Exception e) {
            System.out.println("❌ Seçenek bulunamadı veya tıklanamadı: " + visibleText);
            throw new RuntimeException(e);
        }
    }


    public void selectBank(String bankaAdi) {
        clickDropdownByClassName("ant-select-selector");

        try {
            Thread.sleep(1000); // dropdown'ın render edilmesi için bekleme
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        selectOptionByVisibleText(bankaAdi);
    }

    public void clickByCoordinates(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            Thread.sleep(300); // çok kısa bir duraksama
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("🖱️ Koordinata tıklama yapıldı: x=" + x + ", y=" + y);
        } catch (AWTException | InterruptedException e) {
            throw new RuntimeException("❌ Koordinata tıklama başarısız", e);
        }
    }


}
