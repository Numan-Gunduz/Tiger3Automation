
package utils;

import base.TestContext;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.awt.event.InputEvent;
import org.openqa.selenium.WebElement;
import pages.EkstreAktarimiPage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementHelper {

    public static HWND findWindowByExactTitle(String windowTitle) {
        final HWND[] result = new HWND[1];

        User32.INSTANCE.EnumWindows((hWnd, data) -> {
            char[] buffer = new char[1024];
            User32.INSTANCE.GetWindowTextW(hWnd, buffer, 1024);
            String title = Native.toString(buffer);

            if (title != null && title.trim().equals(windowTitle)) {
                result[0] = hWnd;
                return false;
            }
            return true;
        }, null);

        return result[0];
    }

    public static void switchToWindowByTitle(String windowTitle) {
        HWND window = findWindowByExactTitle(windowTitle);
        if (window == null) {
            throw new RuntimeException(" Pencere bulunamadı: " + windowTitle);
        }
        if (!User32.INSTANCE.SetForegroundWindow(window)) {
            throw new RuntimeException(" Pencere ön plana alınamadı: " + windowTitle);
        }
        System.out.println("Pencere aktif: " + windowTitle);
    }

    public static void typeTextSmart(WebElement element, String text) {
        try {
            clickAndWait(element);
            Robot robot = new Robot();
            for (char c : text.toCharArray()) {
                typeChar(robot, c);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println(" Yazı yazılamadı: " + e.getMessage());
        }
    }

    public static void clickAndWait(WebElement element) {
        try {
            element.click();
            Thread.sleep(300);
        } catch (Exception e) {
            System.out.println(" Tıklama bekleme başarısız: " + e.getMessage());
        }
    }

    private static void typeChar(Robot robot, char c) {
        try {
            boolean upperCase = Character.isUpperCase(c);
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

            if (c == '@') {
                robot.keyPress(KeyEvent.VK_ALT_GRAPH);
                robot.keyPress(KeyEvent.VK_Q);
                robot.keyRelease(KeyEvent.VK_Q);
                robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
                return;
            }
            if (c == '.') {
                robot.keyPress(KeyEvent.VK_PERIOD);
                robot.keyRelease(KeyEvent.VK_PERIOD);
                return;
            }

            if (upperCase) robot.keyPress(KeyEvent.VK_SHIFT);

            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);

            if (upperCase) robot.keyRelease(KeyEvent.VK_SHIFT);

        } catch (IllegalArgumentException e) {
            System.out.println(" Yazılamayan karakter: " + c);
        }
    }



    public static void clearAndFillFieldIfExists(WindowsDriver driver, String accessibilityId, String value) {
        try {
            WebElement element = waitForElement(driver, "accessibilityId", accessibilityId, 10);
            if (element.isEnabled() && element.isDisplayed()) {
                clearAndFillField(driver, accessibilityId, value);
            }
        } catch (Exception ignored) {}
    }

    public static void clearAndFillField(WindowsDriver driver, String accessibilityId, String value) {
        try {
            WebElement element = waitForElement(driver, "accessibilityId", accessibilityId, 10);
            element.click();
            element.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
            element.sendKeys(value);
        } catch (Exception e) {
            System.out.println("️ Alan doldurulamadı: " + accessibilityId + " - " + e.getMessage());
        }
    }

    public static WebElement waitForElement(WindowsDriver driver, String type, String locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(timeout));
        switch (type) {
            case "accessibilityId":
                return wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId(locator)));
            case "name":
                return wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.name(locator)));
            default:
                throw new IllegalArgumentException("Desteklenmeyen locator türü: " + type);
        }
    }



    public static boolean waitForWindowByTitle(String title, int timeoutInSeconds) {
        long endTime = System.currentTimeMillis() + timeoutInSeconds * 1000;

        while (System.currentTimeMillis() < endTime) {
            List<String> handles = new ArrayList<>(DriverFactory.getWinDriver().getWindowHandles());
            for (String handle : handles) {
                DriverFactory.getWinDriver().switchTo().window(handle);
                if (DriverFactory.getWinDriver().getTitle().equalsIgnoreCase(title)) {
                    System.out.println("Pencere tespit edildi: " + title);
                    return true;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println(" Pencere bulunamadı: " + title);
        return false;
    }





    public static void waitForPageLoad(WebDriver driver, int timeoutInSeconds) {
        try {
            // Sadece Selenium WebDriver (örneğin EdgeDriver) ise çalıştır
            if (driver.getClass().getName().contains("EdgeDriver") || driver instanceof JavascriptExecutor) {
                new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(
                        webDriver -> ((JavascriptExecutor) webDriver)
                                .executeScript("return document.readyState")
                                .equals("complete")
                );
            } else {
                System.out.println("Driver tipi desteklenmiyor → " + driver.getClass().getSimpleName() + " (waitForPageLoad atlandı)");
            }
        } catch (Exception e) {
            System.out.println(" waitForPageLoad exception: " + e.getMessage());
        }
    }



    public static void waitForTextInElement(WebDriver driver, By locator, String expectedText, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
    }

    public static void navigateToHomePage(WebDriver seleniumDriver) {
        try {
            System.out.println("Ana Sayfa'ya dönülüyor...");

            WebDriverWait wait = new WebDriverWait(seleniumDriver, Duration.ofSeconds(15));

            // Ana sayfa menü öğesi gelene kadar bekle
            WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class,'ant-menu-item') and .//span[text()='Ana Sayfa']]")
            ));

            ((JavascriptExecutor) seleniumDriver).executeScript("arguments[0].scrollIntoView(true);", menuItem);
            Thread.sleep(500); // kaydırma sonrası kısa bekleme
            menuItem.click();

            System.out.println(" Ana Sayfa'ya geçiş başarılı.");
            Thread.sleep(2000); // Sayfa geçiş sonrası stabilite

        } catch (Exception e) {
            System.out.println(" Ana Sayfa'ya geçerken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }





    public static void maximizeWindowWithRobot(String partialTitle) {
        System.out.println(" Maximize işlemi başlatılıyor: " + partialTitle);

        HWND[] foundWindow = new HWND[1];

        // Pencere başlığını tararken logları kaldırdık
        User32.INSTANCE.EnumWindows((hWnd, data) -> {
            char[] windowText = new char[512];
            User32.INSTANCE.GetWindowTextW(hWnd, windowText, 512);
            String wText = Native.toString(windowText).trim();

            if (wText.contains(partialTitle)) {
                foundWindow[0] = hWnd;
                return false;
            }
            return true;
        }, null);

        if (foundWindow[0] == null) {
            throw new RuntimeException(" Pencere bulunamadı: " + partialTitle);
        }

        // Maximize (3 = SW_MAXIMIZE)
        User32Extra.INSTANCE.ShowWindow(foundWindow[0], 3);

        boolean foregroundResult = User32.INSTANCE.SetForegroundWindow(foundWindow[0]);
        if (!foregroundResult) {
            throw new RuntimeException(" SetForegroundWindow başarısız! Pencere ön plana alınamadı: " + partialTitle);
        }

        try {
            Robot robot = new Robot();
            robot.mouseMove(100, 100); // Odak için küçük bir hareket
            robot.delay(800);

            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_ALT);

            Thread.sleep(500);

            robot.keyPress(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_X);

            System.out.println(" Robot ile pencere maximize komutu gönderildi.");
        } catch (Exception e) {
            throw new RuntimeException(" Maximize işlemi başarısız!", e);
        }
    }




        public static WebElement waitClickable(WebDriver driver, By locator, int timeoutSeconds) {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.elementToBeClickable(locator));
        }

        public static WebElement waitPresence(WebDriver driver, By locator, int timeoutSeconds) {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }

        public static WebElement waitVisible(WebDriver driver, By locator, int timeoutSeconds) {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }

        public static boolean waitInvisible(WebDriver driver, By locator, int timeoutSeconds) {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.invisibilityOfElementLocated(locator));
        }

        public static void clickWithJS(WebDriver driver, WebElement element) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }

        public static void sleep(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public static boolean isElementDisplayed(WebElement element) {
            try {
                return element.isDisplayed();
            } catch (StaleElementReferenceException e) {
                return false;
            }
        }

        public static WebElement findElementSafe(WebDriver driver, By locator) {
            try {
                return driver.findElement(locator);
            } catch (Exception e) {
                return null;
            }
        }
    public static void scrollBy(WebDriver driver, int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public static void safeContextClick(WebDriver driver, WebElement element) {
        try {
            new Actions(driver).contextClick(element).perform();
        } catch (Exception e) {
            throw new RuntimeException("Context click başarısız: " + e.getMessage());
        }
    }

    public static void waitAndClickTextSpanByExactText(WebDriver driver, String text, int timeoutSeconds) {
        By locator = By.xpath("//span[normalize-space(text())='" + text + "']");
        WebElement element = waitClickable(driver, locator, timeoutSeconds);
        element.click();
    }

}





