package base;

import org.openqa.selenium.WebDriver;
import io.appium.java_client.windows.WindowsDriver;

public class TestContext {
    private WindowsDriver winDriver;
    private WebDriver webDriver;

    public WindowsDriver getWinDriver() {
        return winDriver;
    }

    public void setWinDriver(WindowsDriver winDriver) {
        this.winDriver = winDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
}
