// ✅ TestContext.java
package base;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestContext {
    private WindowsDriver windowsDriver;
    private WebDriver webDriver;

    public WindowsDriver getWindowsDriver() {
        return windowsDriver;
    }

    public void setWindowsDriver(WindowsDriver windowsDriver) {
        this.windowsDriver = windowsDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Object getActiveDriver() {
        if (webDriver != null) return webDriver;
        return windowsDriver;
    }
}
