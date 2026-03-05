package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGeneration;


import static com.codeborne.selenide.Selenide.*;



public class AuthTest {


    @BeforeEach void setUp() {
        open("http://localhost:9999/");
        Configuration.holdBrowserOpen = true;
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    void shouldTestActive() {
        var validUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] .input__control").val(validUser.getLogin());
        $("[data-test-id=password] .input__control").val(validUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.exactText("  Личный кабинет"));

    }

    @Test
    void shouldTestBlocked() {
        var validUser = DataGeneration.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] .input__control").val(validUser.getLogin());
        $("[data-test-id=password] .input__control").val(validUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Пользователь заблокирован"));
    }

    @Test
    void shouldTestRandomLoginUnregistered() {
        var invalidUser = DataGeneration.Registration.getUser("blocked");
        $("[data-test-id=login] .input__control").val(DataGeneration.Registration.getRandomLogin());
        $("[data-test-id=password] .input__control").val(invalidUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    void shouldTestRandomPassUnregistered() {
        var invalidUser = DataGeneration.Registration.getUser("blocked");
        $("[data-test-id=login] .input__control").val(invalidUser.getLogin());
        $("[data-test-id=password] .input__control").val(DataGeneration.Registration.getRandomPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    void shouldTestRandomPassAndLoginRegistered() {
        var validUser = DataGeneration.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] .input__control").val(DataGeneration.Registration.getRandomLogin());
        $("[data-test-id=password] .input__control").val(DataGeneration.Registration.getRandomPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    void shouldTestRandomLoginRegistered() {
        var validUser = DataGeneration.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] .input__control").val(DataGeneration.Registration.getRandomLogin());
        $("[data-test-id=password] .input__control").val(validUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    void shouldTestRandomPassRegistered() {
        var validUser = DataGeneration.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] .input__control").val(validUser.getLogin());
        $("[data-test-id=password] .input__control").val(DataGeneration.Registration.getRandomPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    void shouldTestLoginNotification() {
        var validUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] .input__control").val();
        $("[data-test-id=password] .input__control").val(validUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=login].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestPassNotification() {
        var validUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] .input__control").val(validUser.getLogin());
        $("[data-test-id=password] .input__control").val();
        $("[data-test-id=action-login]").click();
        $("[data-test-id=password].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }
    @Test
    void shouldTestBothNotifications() {
        var validUser = DataGeneration.Registration.getRegisteredUser("active");
        $("[data-test-id=login] .input__control").val();
        $("[data-test-id=password] .input__control").val();
        $("[data-test-id=action-login]").click();
        $("[data-test-id=login].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        $("[data-test-id=password].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));

    }
}