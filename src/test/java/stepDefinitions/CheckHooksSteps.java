package stepDefinitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckHooksSteps {

    @Given("sistem hazır")
    public void sistem_hazir() {
        System.out.println("✅ Sistem hazır durumda.");
    }

    @When("test çalıştırılır")
    public void test_calistirilir() {
        System.out.println("▶️ Test başlatıldı.");
    }

    @Then("test başarılı şekilde tamamlanır")
    public void test_basarili_biter() {
        System.out.println("✅ Test başarıyla tamamlandı.");
    }
}
