package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGeneration {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGeneration() {
    }

    private static void sendRequest(RegistrationInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static class Registration {
        private Registration() {
        }

        // МЕТОДЫ ДОЛЖНЫ БЫТЬ ТУТ, ЧТОБЫ ТЕСТ ИХ УВИДЕЛ
        public static String getRandomLogin() {
            return faker.name().username();
        }

        public static String getRandomPassword() {
            return faker.internet().password();
        }

        public static RegistrationInfo getUser(String status) {
            return new RegistrationInfo(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationInfo getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationInfo {
        String login;
        String password;
        String status;
    }
}
