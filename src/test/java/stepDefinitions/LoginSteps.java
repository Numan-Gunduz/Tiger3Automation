package stepDefinitions;

import io.cucumber.java.en.Given;
import utils.Hooks;

public class LoginSteps {

    @Given("kullanıcı Windows uygulamasını açar ve giriş yapar")
    public void kullanıcıWindowsUygulamasınıAçarVeGirişYapar() {
        System.out.println("Tiger3Enterprise giriş adımları zaten Hooks içinde çalışıyor.");
    }

    @Given("kullanıcı Online Hesap Özeti uygulamasına giriş yapar")
    public void kullanıcıOnlineHesapOzetiUygulamasınaGirişYapar() {
        System.out.println("Online Hesap Özeti giriş adımları zaten Hooks içinde çalışıyor.");
    }
}
