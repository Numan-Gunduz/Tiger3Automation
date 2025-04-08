
package utils;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
            throw new RuntimeException("❌ Pencere bulunamadı: " + windowTitle);
        }
        if (!User32.INSTANCE.SetForegroundWindow(window)) {
            throw new RuntimeException("❌ Pencere ön plana alınamadı: " + windowTitle);
        }
        System.out.println("✅ Pencere aktif: " + windowTitle);
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
            System.out.println("❌ Yazı yazılamadı: " + e.getMessage());
        }
    }

    public static void clickAndWait(WebElement element) {
        try {
            element.click();
            Thread.sleep(300);
        } catch (Exception e) {
            System.out.println("⚠️ Tıklama bekleme başarısız: " + e.getMessage());
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
            System.out.println("⚠️ Yazılamayan karakter: " + c);
        }
    }

    public static boolean isElementPresent(WindowsDriver driver, String accessibilityId, int timeoutInSeconds) {
        try {
            for (int i = 0; i < timeoutInSeconds * 2; i++) {
                try {
                    WebElement element = driver.findElementByAccessibilityId(accessibilityId);
                    if (element.isDisplayed()) return true;
                } catch (Exception ignored) {
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {
        }
        return false;
    }

    public static void maximizeWindowIfPresent(WindowsDriver driver, String accessibilityId) {
        try {
            WebElement button = driver.findElement(MobileBy.AccessibilityId(accessibilityId));
            button.click();
            System.out.println("🖥️ Ekran büyütme işlemi yapıldı.");
        } catch (Exception e) {
            System.out.println("⚠️ Ekran büyütülemedi: " + e.getMessage());
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
            System.out.println("⚠️ Alan doldurulamadı: " + accessibilityId + " - " + e.getMessage());
        }
    }

    public static WebElement waitForElement(WindowsDriver driver, String type, String locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
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
                    System.out.println("🪟 Pencere tespit edildi: " + title);
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

        System.out.println("⏳ Pencere bulunamadı: " + title);
        return false;
    }
    public static WebElement waitUntilClickable(WindowsDriver driver, String by, String value, int timeoutSeconds) {
        By locator = getBy(by, value);
        return new WebDriverWait(driver, timeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitUntilVisible(WindowsDriver driver, String by, String value, int timeoutSeconds) {
        By locator = getBy(by, value);
        return new WebDriverWait(driver, timeoutSeconds)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static By getBy(String type, String value) {
        switch (type.toLowerCase()) {
            case "id":
            case "accessibilityid":
                return MobileBy.AccessibilityId(value);
            case "name":
                return MobileBy.name(value);
            default:
                throw new IllegalArgumentException("❌ Desteklenmeyen locator tipi: " + type);
        }
    }



}
