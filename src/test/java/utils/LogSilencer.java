package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSilencer {

    public static void silenceSeleniumWarnings() {
        Logger.getLogger("org.openqa.selenium.remote.http.WebSocket").setLevel(Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.chromium.ChromiumDriver").setLevel(Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder").setLevel(Level.SEVERE);
    }
}
