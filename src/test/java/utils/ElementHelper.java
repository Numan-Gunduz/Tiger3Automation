package utils;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import static pages.TestRobot.typeChar;

public class ElementHelper {

    /**
     * Verilen pencere başlığına göre tam eşleşen HWND nesnesini döner.
     */
    public static HWND findWindowByExactTitle(String windowTitle) {
        final HWND[] result = new HWND[1];

        User32.INSTANCE.EnumWindows((hWnd, data) -> {
            char[] buffer = new char[1024];
            User32.INSTANCE.GetWindowTextW(hWnd, buffer, 1024);
            String title = Native.toString(buffer);

            if (title != null && title.trim().equals(windowTitle)) {
                result[0] = hWnd;
                return false; // pencere bulundu, dur
            }
            return true; // devam et
        }, null);

        return result[0];
    }

    public static void switchToWindowByTitle(String windowTitle) {
        System.out.println("🔍 Pencere aranıyor: " + windowTitle);
        HWND window = findWindowByExactTitle(windowTitle);

        if (window == null) {
            throw new RuntimeException("❌ Pencere bulunamadı: " + windowTitle);
        }

        boolean result = User32.INSTANCE.SetForegroundWindow(window);
        if (!result) {
            throw new RuntimeException("❌ Pencere ön plana alınamadı: " + windowTitle);
        }

        System.out.println("✅ Pencere aktif: " + windowTitle);
    }
    public static void typeTextSmart(WebElement element, String text)
    {
        try {
            // 1. Odağı pencereye al
            HWND hwnd = User32.INSTANCE.FindWindowW(null, "Online Hesap Özeti Uygulaması");
            if (hwnd != null) {
                User32.INSTANCE.SetForegroundWindow(hwnd);
                Thread.sleep(300);
            }

            element.click();
            Thread.sleep(300);

            // 2. Robot ile yazmayı dene
            try {
                Robot robot = new Robot();
                for (char c : text.toCharArray()) {
                    typeChar(robot, c);
                    Thread.sleep(100);
                }
                System.out.println("✅ Robot ile yazma denendi.");
                return;
            } catch (Exception e) {
                System.out.println("⚠️ Robot ile yazma başarısız: " + e.getMessage());
            }

            // 3. Clipboard ile yapıştırmayı dene
            try {
                Robot robot = new Robot();
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                System.out.println("✅ Clipboard ile yapıştırıldı.");
                return;
            } catch (Exception e) {
                System.out.println("⚠️ Clipboard ile yapıştırma başarısız: " + e.getMessage());
            }

            // 4. SendKeys fallback
            try {
                element.sendKeys(text);
                System.out.println("✅ sendKeys ile yazıldı (fallback).");
            } catch (Exception e) {
                System.out.println("⚠️ sendKeys ile yazılamadı: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("❌ Tüm yöntemler başarısız: " + e.getMessage());
        }
    }

}
