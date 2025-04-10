package org.depigroup3.utility;
import com.github.javafaker.Faker;


public class Const {

    private static final Faker faker = new Faker();

    // URL TO TEST
    public static final String BASE_URL = "https://www.demoblaze.com/";



    // Specific User and Password
    public static final String USERNAMES = "gomaa342432";
    public static final String PASSWORDS = "123";

    // Random User and Password for all test
    public static final String USERNAME = faker.name().username();
    public static final String PASSWORD = faker.name().bloodGroup();


    // Wait timeouts
    public static final int IMPLICIT_WAIT = 10; //waiting for all elements
    public static final int EXPLICIT_WAIT = 10; // waitng for specific condition

    // Test data
    // Products
    public static final String PHONE1_NAME = "Samsung galaxy s6";
    public static final String PHONE2_NAME = "Nokia lumia 1520";
    public static final String PHONE3_NAME = "Nexus 6";
    public static final String PHONE4_NAME = "Samsung galaxy s7";
    public static final String PHONE5_NAME = "Iphone 6 32gb";
    public static final String PHONE6_NAME = "Sony xperia z5";
    public static final String PHONE7_NAME = "HTC One M9";

    public static final String LAPTOP1_NAME = "Sony vaio i5";
    public static final String LAPTOP2_NAME = "Sony vaio i7";
    public static final String LAPTOP3_NAME = "MacBook air";
    public static final String LAPTOP4_NAME = "Dell i7 8gb";
    public static final String LAPTOP5_NAME = "2017 Dell 15.6 Inch";
    public static final String LAPTOP6_NAME = "MacBook Pro";

    public static final String MONITOR1_NAME = "Apple monitor 24";
    public static final String MONITOR2_NAME = "ASUS Full HD";


    //OrderData
    public static final String CUSTOMER_NAME = "Depi";
    public static final String CARD_NUMBER = "0000000000000000";
    public static final String COUNTRY = "Egypt";
    public static final String CITY = "Alexandria";
    public static final String MONTH = "3";
    public static final String YEAR = "1996";

    // File paths
    //Report
    public static final String SCREENSHOT_PATH = "./Reports/Screenshots/";
    public static final String REPORT_PATH = "./Reports/";
    public static final String REPORT_FILE = "DemoblazeTestReport.html";
    //Driver Path
    public static final String CHROME_DRIVER_PATH = "./drivers/chromedriver.exe";
}
