package base;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class TestContext {

    private WindowsDriver<WebElement> windowsDriver;
    private WebDriver webDriver;


    public TestContext() {

    }

    //  Getter ve Setter - WindowsDriver (GENERIC)
    public WindowsDriver<WebElement> getWindowsDriver() {
        return windowsDriver;
    }

    public void setWindowsDriver(WindowsDriver<WebElement> windowsDriver) {
        this.windowsDriver = windowsDriver;
    }

    //  Getter ve Setter - WebDriver (Selenium için)
    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //  Yardımcı: Aktif olan driver’ı döndür
    public Object getActiveDriver() {
        if (webDriver != null) return webDriver;
        return windowsDriver;
    }
}

