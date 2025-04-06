package utils;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;

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

}
