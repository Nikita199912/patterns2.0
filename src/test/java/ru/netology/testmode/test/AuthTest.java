package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGeneration;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldLoginWithRegisteredActiveUser() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGeneration.Registration.getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        var wrongLogin = DataGeneration.Registration.getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGeneration.Registration.getRegisteredUser("active");
        var wrongPassword = DataGeneration.Registration.getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }
    @Test
    @DisplayName("Should get error message if login with active unregistered user")
    void shouldGetErrorIfActiveUnregisteredUser() {
        var activeUnregisteredUser = DataGeneration.Registration.getUser("active");

        $("[data-test-id='login'] input").setValue(activeUnregisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUnregisteredUser.getPassword());
        $("button[data-test-id='action-login']").click();

        // Проверяем, что появляется ошибка "Неверно указан логин или пароль"
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

}