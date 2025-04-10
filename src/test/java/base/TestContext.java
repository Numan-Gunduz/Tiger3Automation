package base;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tüm test boyunca WebDriver, WindowsDriver ve diğer global objelere erişimi sağlayan temel context yapısı.
 * Clean Code prensiplerine uygun, açık ve sade.
 */
public class TestContext {

    private WindowsDriver<WebElement> windowsDriver;
    private WebDriver webDriver;

    // Constructor: İstenirse driver'lar başlatılır.
    public TestContext() {
        // Varsayılan boş bırakıldı. İsteğe göre customize edilebilir.
    }

    // ✅ Getter ve Setter - WindowsDriver (GENERIC)
    public WindowsDriver<WebElement> getWindowsDriver() {
        return windowsDriver;
    }

    public void setWindowsDriver(WindowsDriver<WebElement> windowsDriver) {
        this.windowsDriver = windowsDriver;
    }

    // ✅ Getter ve Setter - WebDriver (Selenium için)
    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    // ✅ Yardımcı: Aktif olan driver’ı döndür
    public Object getActiveDriver() {
        if (webDriver != null) return webDriver;
        return windowsDriver;
    }
}
