package utils;
import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    interface WNDENUMPROC extends Callback {
        boolean callback(HWND hWnd, Pointer data);
    }

    boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
    int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
    HWND FindWindowW(String lpClassName, String lpWindowName);

    // ✅ BU SATIRI EKLE
    HWND FindWindowExW(HWND hwndParent, HWND hwndChildAfter, String lpszClass, String lpszWindow);

    int GetWindowThreadProcessId(HWND hWnd, int[] lpdwProcessId);
    boolean IsWindow(HWND hWnd);
    boolean SetForegroundWindow(HWND hWnd);
    HWND GetForegroundWindow();
}
