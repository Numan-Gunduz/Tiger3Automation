package pages;

// File: pages/KuralTanimlamaPage.java


import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

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

}
