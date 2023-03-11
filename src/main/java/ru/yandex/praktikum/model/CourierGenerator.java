package ru.yandex.praktikum.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public static Courier getRandom() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login, password, firstName);
    }

}
