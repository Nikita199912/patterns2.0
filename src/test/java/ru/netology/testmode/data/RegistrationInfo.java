package ru.netology.testmode.data;

import lombok.*;
@Value
public class RegistrationInfo {
    private String login;
    private String password;
    private String status;
}