package org.example;

import com.github.javafaker.Faker;

public class Constants {
    static Faker faker = new Faker();

    public static final String DRIVER_PATH = "./drivers/chromedriver.exe";
    public static final String WEBSITE_URL = "https://demoblaze.com/";


    public static final String[][] CONTACT_DATA = {
            {"", "", "", "empty"},
            {"user@example.com", "", "", "empty"},
            {"", "Test User", "", "empty"},
            {"", "", "This is a message", "empty"},
            {"user@example.com", "Test", "Message", "valid"},
            {"invalidEmail", "Test", "Message", "invalid"},
            {"test@test.com", " ", "hi", "invalid"},
            {" ", "Test", "Hi", "invalid"},
    };

    public static final String[][] LOGIN_DATA = {
            {"123456", "abcdef", "invalid"},
            {"wrongUser", "abcdef", "notExist"},
            {"123456", "wrongPass", "invalid"},
            {"", "", "empty"},
            {"123456", "", "empty"},
            {"", "abcdef", "empty"},
            {"MaryamSara", "1234567", "valid"},
            {"wrongUser2", "abcdef", "notExist"},
    };

    public static final String[][] ORDER_DATA = {
            {"Maryam", "Egypt", "Cairo", "1234567890123456", "03", "2025", "valid"},
            {"", "Egypt", "Cairo", "1234567890123456", "03", "2025", "invalid"},
            {"Sara", "", "Cairo", "1234567890123456", "03", "2025", "invalid"},
            {"Maryam", "Egypt", "", "1234567890123456", "03", "2025", "invalid"},
            {"Sara", "Egypt", "Cairo", "", "03", "2025", "invalid"},
            {"Maryam", "Egypt", "Cairo", "1234567890123456", "", "", "invalid"}
    };


    public static final String[][] CREDENTIALS = {
            {"maryam", "123", "invalid"},     // short password
            {"Sara", "", "empty"},           // empty password
            {"", "123", "empty"},            // empty username
            {"", "", "empty"},               // empty username and password
            {"a", "1", "invalid"},           // short username and password
            {"validUser", "validPass", "valid"}, // valid credentials
            {faker.name().firstName() + " " + "qge", "pass", "invalid"}, // username has a space
            {" ", "abcdef", "invalid"},      // space username
            {"maryam", " ", "invalid"},      // space password
            {" ", " ", "invalid"},           // space username and password
            {"1", "abcdef", "invalid"},      // username is a number
            {faker.name().firstName() + "@", "123", "invalid"}, // username has a symbol
            {faker.name().lastName() + faker.number().numberBetween(2, 3), "short", "invalid"}, // short password < 6
            {faker.name().firstName() + faker.number().numberBetween(2, 3), "1234567", "valid"} // valid username
    };

}
