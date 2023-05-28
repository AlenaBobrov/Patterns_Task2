package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

class AuthTest {
    SelenideElement login = $("[data-test-id='login'] input");
    SelenideElement password = $("[data-test-id='password'] input");
    SelenideElement continueB = $(".button");
    SelenideElement personalAcc = $("[id='root'] h2");
    SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    String personalAccTitle = "Личный кабинет";
    String errorMs = "Неверно указан логин или пароль";
    String blockedUserMs = "Пользователь заблокирован";

    @BeforeAll
    static void setUpAll() {
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        login.setValue(registeredUser.getLogin());
        password.setValue(registeredUser.getPassword());
        continueB.click();
        personalAcc.shouldHave(Condition.text(personalAccTitle)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        login.setValue(notRegisteredUser.getLogin());
        password.setValue(notRegisteredUser.getPassword());
        continueB.click();
        errorMessage.shouldHave(Condition.text(errorMs)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        login.setValue(blockedUser.getLogin());
        password.setValue(blockedUser.getPassword());
        continueB.click();
        errorMessage.shouldHave(Condition.text(blockedUserMs)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        login.setValue(wrongLogin);
        password.setValue(registeredUser.getPassword());
        continueB.click();
        errorMessage.shouldHave(Condition.text(errorMs)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        login.setValue(registeredUser.getLogin());
        password.setValue(wrongPassword);
        continueB.click();
        errorMessage.shouldHave(Condition.text(errorMs)).shouldBe(Condition.visible);
    }
}