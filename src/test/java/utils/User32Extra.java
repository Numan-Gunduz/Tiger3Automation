package utils;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;

public interface User32Extra extends StdCallLibrary {
    User32Extra INSTANCE = Native.load("user32", User32Extra.class);

    int SW_RESTORE = 9;

    boolean ShowWindow(HWND hWnd, int nCmdShow);

    static void showWindow(HWND hwnd) {
        INSTANCE.ShowWindow(hwnd, SW_RESTORE);
    }
}
