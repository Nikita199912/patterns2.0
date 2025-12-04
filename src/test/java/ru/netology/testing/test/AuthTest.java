package ru.netology.testing.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testing.data.DataGenerator;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.visible;

public class AuthTest {

    private static final String LOGIN_FIELD = "[data-test-id='login'] input";
    private static final String PASSWORD_FIELD = "[data-test-id='password'] input";
    private static final String LOGIN_BUTTON = "button.button";
    private static final String ERROR_NOTIFICATION = "[data-test-id='error-notification'] .notification__content";
    private static final String DASHBOARD_TITLE_SELECTOR = "h2";
    private static final String DASHBOARD_TITLE_TEXT = "Личный кабинет";
    public static final SelenideElement LOGIN_FIELD_ERROR = $("[data-test-id='login'] .input__sub");
    public static final SelenideElement PASSWORD_FIELD_ERROR = $("[data-test-id='password'] .input__sub");

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    private void fillLoginForm(String login, String password) {
        $(LOGIN_FIELD).val(login);
        $(PASSWORD_FIELD).val(password);
        $(LOGIN_BUTTON).click();
    }

    @Test
    @DisplayName("Успешный вход активного зарегистрированного пользователя")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser("active");
        fillLoginForm(user.getLogin(), user.getPassword());
        $(DASHBOARD_TITLE_SELECTOR)
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText(DASHBOARD_TITLE_TEXT));
    }

    @Test
    @DisplayName("Ошибка: Попытка входа с неверным логином")
    void shouldGetErrorIfWrongLogin() {
        DataGenerator.RegistrationDto registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        String wrongLogin = DataGenerator.getRandomLogin();

        fillLoginForm(wrongLogin, registeredUser.getPassword());

        $(ERROR_NOTIFICATION)
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Ошибка: Попытка входа с неверным паролем")
    void shouldGetErrorIfWrongPassword() {
        DataGenerator.RegistrationDto registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        String wrongPassword = DataGenerator.getRandomPassword();

        fillLoginForm(registeredUser.getLogin(), wrongPassword);

        $(ERROR_NOTIFICATION)
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Ошибка: Попытка входа заблокированного пользователя")
    void shouldGetErrorIfBlockedUser() {
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser("blocked");
        fillLoginForm(user.getLogin(), user.getPassword());
        $(ERROR_NOTIFICATION)
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Ошибка: Попытка входа незарегистрированного пользователя")
    void shouldGetErrorIfNotRegisteredUser() {
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getUser("active");
        fillLoginForm(user.getLogin(), user.getPassword());
        $(ERROR_NOTIFICATION)
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Ошибка: Валидация при попытке входа с пустым полем логина")
    void shouldShowValidationErrorIfEmptyLogin() {
        DataGenerator.RegistrationDto registeredUser = DataGenerator.Registration.getRegisteredUser("active"); // Убедитесь, что синтаксис 'status:' удален
        fillLoginForm("", registeredUser.getPassword());

        LOGIN_FIELD_ERROR
                .shouldBe(visible, Duration.ofSeconds(18))
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }


    // --- НЕГАТИВНЫЙ ТЕСТ: Валидация (пустое поле пароля) ---
    @Test
    @DisplayName("Ошибка: Валидация при попытке входа с пустым полем пароля")
    void shouldShowValidationErrorIfEmptyPassword() {
        DataGenerator.RegistrationDto registeredUser = DataGenerator.Registration.getRegisteredUser("active"); // Убедитесь, что синтаксис 'status:' удален
        fillLoginForm(registeredUser.getLogin(), ""); // Убедитесь, что синтаксис 'password:' удален

        PASSWORD_FIELD_ERROR // Теперь проверяем ошибку под полем "Пароль"
                .shouldBe(visible, Duration.ofSeconds(18))
                .shouldHave(Condition.exactText("Поле обязательно для заполнения")); // Точный текст из скриншота
    }
}
