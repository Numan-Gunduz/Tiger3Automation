package base;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;

/**
 * Tüm test boyunca WebDriver, WindowsDriver ve diğer global objelere erişimi sağlayan temel context yapısı.
 * Clean Code prensiplerine uygun, açık ve sade.
 */
public class TestContext {

    private WindowsDriver windowsDriver;
    private WebDriver webDriver;

    // Constructor: İstenirse driver'lar başlatılır.
    public TestContext() {
        // Varsayılan boş bırakıldı. İsteğe göre customize edilebilir.
    }

    // Getter ve Setter - WindowsDriver
    public WindowsDriver getWindowsDriver() {
        return windowsDriver;
    }

    public void setWindowsDriver(WindowsDriver windowsDriver) {
        this.windowsDriver = windowsDriver;
    }

    // Getter ve Setter - WebDriver (eğer gerekiyorsa)
    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    // Yardımcı: Hangisi aktif onu döndür (isteğe bağlı)
    public Object getActiveDriver() {
        if (webDriver != null) return webDriver;
        return windowsDriver;
    }
}
