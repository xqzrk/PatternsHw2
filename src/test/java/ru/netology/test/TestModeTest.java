package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;

public class TestModeTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @Test
    void shouldSuccessfullyLoginRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//span[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//span[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//h2").shouldHave(Condition.text("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void shouldNotLoginNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//span[@data-test-id='login']//input").setValue(notRegisteredUser.getLogin());
        $x("//span[@data-test-id='password']//input").setValue(notRegisteredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void shouldNotLoginIfBlockedUser() {
        var registeredUser = getRegisteredUser("blocked");
        $x("//span[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//span[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    void shouldErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var randomLogin = DataGenerator.getRandomLogin();
        $x("//span[@data-test-id='login']//input").setValue(randomLogin);
        $x("//span[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void shouldErrorIfWrongPass() {
        var registeredUser = getRegisteredUser("active");
        var randomPass = DataGenerator.getRandomPassword();
        $x("//span[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//span[@data-test-id='password']//input").setValue(randomPass);
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}
