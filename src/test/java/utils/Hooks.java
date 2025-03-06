package utils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.DriverFactory;

public class Hooks {

    @Before
    public void setUp() {
        System.out.println("Test başlamadan önce çalıştırılıyor...");
        DriverFactory.startTiger3();
    }

    @After
    public void tearDown() {
        System.out.println("Test bittikten sonra çalıştırılıyor...");
        DriverFactory.closeDrivers();
    }
}
