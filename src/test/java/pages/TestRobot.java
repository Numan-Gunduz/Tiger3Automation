package pages;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TestRobot {

        public static void main(String[] args) throws Exception {
            Robot robot = new Robot();

            System.out.println("📝 Not Defteri odakta mı? 3 saniye içinde yazmaya başlıyor...");
            Thread.sleep(3000); // Not defterini aktifleştirmek için zaman

            String password = "Kemal.12345";
            for (char c : password.toCharArray()) {
                typeChar(robot, c);
                Thread.sleep(150);
            }

            System.out.println("\n✅ Yazım tamamlandı.");
        }

        public static void typeChar(Robot robot, char c) {
            try {
                boolean upperCase = Character.isUpperCase(c);
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

                if (keyCode == KeyEvent.CHAR_UNDEFINED) {
                    System.out.println("⚠️ Yazılamayan karakter: " + c);
                    return;
                }

                if (upperCase || "!@#$%^&*()_+".indexOf(c) >= 0) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }

                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);

                if (upperCase || "!@#$%^&*()_+".indexOf(c) >= 0) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }

                System.out.print(c);
            } catch (Exception e) {
                System.out.println("⚠️ Hata: " + c + " -> " + e.getMessage());
            }
        }
}
