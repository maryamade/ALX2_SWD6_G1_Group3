////import org.openqa.selenium.By;
////import org.openqa.selenium.WebDriver;
////import org.openqa.selenium.chrome.ChromeDriver;
////import org.openqa.selenium.support.ui.ExpectedConditions;
////import org.openqa.selenium.support.ui.WebDriverWait;
////import org.testng.annotations.*;
////
////import java.io.FileWriter;
////import java.io.IOException;
////import java.io.PrintWriter;
////import java.time.Duration;
////
////public class SignupWithReport {
////
////    WebDriver driver;
////    PrintWriter writer;
////    WebDriverWait wait;
////
////    String[][] credentials = {
////            {"maryam", "123", "invalid"},         //1
////            {"Sara", "", "empty"},                //2
////            {"", "123", "empty"},                 //3
////            {"", "", "empty"},                    //4
////            {"a", "1", "invalid"},                //5
////            {"user name", "pass", "invalid"},     //6
////            {" ", "abcdef", "invalid"},           //7
////            {"maryam", " ", "invalid"},           //8
////            {" ", " ", "invalid"},                //9
////            {"123456", "abcdef", "valid"},        //10
////            {"user@", "123", "invalid"},          //11
////            {"SaraMaher", "short", "invalid"},    //12
////            {"MaryamAdel", "1234567", "valid"},   //13
////            {"SaraMaher", "1234567", "valid"},    //14
////    };
////
//////    @BeforeTest
//////    public void setup() throws IOException {
//////        System.setProperty("webdriver.chrome.driver", "D:\\testing\\code\\seleniumtest\\drivers\\chromedriver.exe");
//////        writer = new PrintWriter(new FileWriter("reportSignup.txt"));
//////    }
////    @BeforeTest
////    public void setupReport() throws Exception {
////        System.setProperty("webdriver.chrome.driver", "D:\\testing\\code\\seleniumtest\\drivers\\chromedriver.exe");
////        writer = new PrintWriter(new FileWriter("reportlogin.txt"));
////    }
////
////    @BeforeMethod
////    public void setupDriver() {
////        driver = new ChromeDriver();
////        //driver.manage().window().maximize();
////    }
////
////    @Test
////    public void testSignupValidation() {
////        for (int i = 0; i < credentials.length; i++) {
////            //driver = new ChromeDriver();
////            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
////
////            String username = credentials[i][0];
////            String password = credentials[i][1];
////            String expectedType = credentials[i][2];
////
////            StringBuilder result = new StringBuilder();
////
////            try {
////                driver.get("https://demoblaze.com/index.html");
////
////                wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2"))).click();
////                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username"))).clear();
////                driver.findElement(By.id("sign-username")).sendKeys(username);
////
////                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password"))).clear();
////                driver.findElement(By.id("sign-password")).sendKeys(password);
////
////                wait.until(ExpectedConditions.elementToBeClickable(
////                        By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]"))).click();
////
////                wait.until(ExpectedConditions.alertIsPresent());
////                String alertText = driver.switchTo().alert().getText();
////                result.append("Test ").append(i + 1).append(" => Alert: ").append(alertText).append("\n");
////                driver.switchTo().alert().accept();
////
////                boolean hasSpecialChar = !username.matches("^[a-zA-Z0-9]*$");
////                boolean isUsernameInvalid = username.trim().isEmpty() || username.length() < 3 || hasSpecialChar;
////                boolean isPasswordInvalid = password.trim().isEmpty() || password.length() < 6;
////
////                if (alertText.contains("Please fill out")) {
////                    if (!expectedType.equals("empty")) {
////                        result.append("Bug: Unexpected empty fields alert for non-empty input.\n");
////                    } else {
////                        result.append("Correct rejection for empty field(s).\n");
////                    }
////                } else if (alertText.contains("Sign up successful") || alertText.contains("This user already exist")) {
////                    if (isUsernameInvalid && isPasswordInvalid) {
////                        result.append("Bug: Invalid Username and Password accepted.\n");
////                    } else if (isUsernameInvalid) {
////                        result.append("Bug: Invalid Username accepted.\n");
////                    } else if (isPasswordInvalid) {
////                        result.append("Bug: Invalid Password accepted.\n");
////                    } else if (alertText.contains("Sign up successful")) {
////                        result.append("Accepted valid input as expected.\n");
////                    } else {
////                        result.append("Accepted existing input as expected.\n");
////                    }
////                } else {
////                    result.append("Unexpected alert: ").append(alertText).append("\n");
////                }
////
////            } catch (Exception e) {
////                result.append("Test ").append(i + 1).append(" => Exception: ").append(e.getMessage()).append("\n");
////            } finally {
////                driver.quit();
////            }
////
////            System.out.println(result.toString() + "------------------------------------------------");
////            writer.println(result.toString() + "------------------------------------------------");
////        }
////    }
////
//////    @AfterTest
//////    public void tearDown() {
//////        if (writer != null) {
//////            writer.close();
//////            System.out.println("Report saved to reportSignup.txt");
//////        }
//////    }
////    @AfterMethod
////    public void tearDownDriver() {
////        driver.quit();
////    }
////
////    @AfterTest
////    public void closeReport() {
////        writer.close();
////        System.out.println("Report saved to reportlogin.txt");
////    }
////}
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.annotations.*;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.time.Duration;
//
//public class SignupWithReport {
//
//    WebDriver driver;
//    PrintWriter writer;
//    WebDriverWait wait;
//
//    String[][] credentials = {
//            {"maryam", "123", "invalid"},         //1
//            {"Sara", "", "empty"},                //2
//            {"", "123", "empty"},                 //3
//            {"", "", "empty"},                    //4 -*empty*
//            {"a", "1", "invalid"},                //5
//            {"user name", "pass", "invalid"},     //6
//            {" ", "abcdef", "invalid"},           //7
//            {"maryam", " ", "invalid"},           //8
//            {" ", " ", "invalid"},                //9 -*space*
//            {"123456", "abcdef", "valid"},        //10
//            {"user@", "123", "invalid"},          //11
//            {"SaraMaher", "short", "invalid"},    //12
//            {"MaryamAdel2", "1234567", "valid"},   //13 Faker
//            {"SaraMaher2", "1234567", "valid"},    //14 Faker
//    };
//
//    @BeforeTest
//    public void setupReport() throws Exception {
//        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
//        writer = new PrintWriter(new FileWriter("./Reports/reportSignup.txt"));
//    }
//
//    @BeforeMethod
//    public void setupDriver() {
//        driver = new ChromeDriver();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }
//
//    @Test
//    public void testSignupValidation() {
//        for (int i = 0; i < credentials.length; i++) {
//            String username = credentials[i][0];
//            String password = credentials[i][1];
//            String expectedType = credentials[i][2];
//
//            StringBuilder result = new StringBuilder();
//
//            try {
//                driver.get("https://demoblaze.com/index.html");
//
//                wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2"))).click();
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username"))).clear();
//                driver.findElement(By.id("sign-username")).sendKeys(username);
//
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password"))).clear();
//                driver.findElement(By.id("sign-password")).sendKeys(password);
//
//                wait.until(ExpectedConditions.elementToBeClickable(
//                        By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]"))).click();
//
//                wait.until(ExpectedConditions.alertIsPresent());
//                String alertText = driver.switchTo().alert().getText();
//                result.append("Test ").append(i + 1).append(" => Alert: ").append(alertText).append("\n");
//                driver.switchTo().alert().accept();
//
//                boolean hasSpecialChar = !username.matches("^[a-zA-Z0-9]*$");
//                boolean isUsernameInvalid = username.trim().isEmpty() || username.length() < 3 || hasSpecialChar;
//                boolean isPasswordInvalid = password.trim().isEmpty() || password.length() < 6;
//
//                if (alertText.contains("Please fill out")) {
//                    if (!expectedType.equals("empty")) {
//                        result.append("Bug: Unexpected empty fields alert for non-empty input.\n");
//                    } else {
//                        result.append("Correct rejection for empty field(s).\n");
//                    }
//                } else if (alertText.contains("Sign up successful") || alertText.contains("This user already exist")) {
//                    if (isUsernameInvalid && isPasswordInvalid) {
//                        result.append("Bug: Invalid Username and Password accepted.\n");
//                    } else if (isUsernameInvalid) {
//                        result.append("Bug: Invalid Username accepted.\n");
//                    } else if (isPasswordInvalid) {
//                        result.append("Bug: Invalid Password accepted.\n");
//                    } else if (alertText.contains("Sign up successful")) {
//                        result.append("Accepted valid input as expected.\n");
//                    } else {
//                        result.append("Accepted existing input as expected.\n");
//                    }
//                } else {
//                    result.append("Unexpected alert: ").append(alertText).append("\n");
//                }
//
//            } catch (Exception e) {
//                result.append("Test ").append(i + 1).append(" => Exception: ").append(e.getMessage()).append("\n");
//            }
//
//            System.out.println(result.toString() + "------------------------------------------------");
//            writer.println(result.toString() + "------------------------------------------------");
//        }
//    }
//
//    @AfterMethod
//    public void tearDownDriver() {
//        // Ensure the driver is quit after each test method
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//    @AfterTest
//    public void closeReport() {
//        // Close the writer after all tests are done
//        if (writer != null) {
//            writer.close();
//            System.out.println("Report saved to reportSignup.txt");
//        }
//    }
//}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import com.github.javafaker.Faker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

public class SignupWithReport {

    WebDriver driver;
    PrintWriter writer;
    WebDriverWait wait;
    Faker faker = new Faker();

    String[][] credentials = {
            {"maryam", "123", "invalid"},         //1
            {"Sara", "", "empty"},                //2 -*empty pass*
            {"", "123", "empty"},                 //3 -*empty user*
            {"", "", "empty"},                    //4 -*empty user and pass*
            {"a", "1", "invalid"},                //5 -*short user and pass*
            {faker.name().firstName() + " " + "qge", "pass", "invalid"},     //6 -*space user*
            {" ", "abcdef", "invalid"},           //7 -*space user*
            {"maryam", " ", "invalid"},           //8 -*space password*
            {" ", " ", "invalid"},                //9 -*space user and password*
            {"1", "abcdef", "valid"},        //10 -*user is number*
            {faker.name().firstName() + "@", "123", "invalid"},     //11 -*symbol user*
            {faker.name().lastName() + faker.number().numberBetween(2, 3), "short", "invalid"},    //12
            {faker.name().firstName()+ faker.number().numberBetween(2, 3), "1234567", "valid"},   //13 Faker UserName
            {faker.name().firstName() + faker.number().numberBetween(2, 3), "1234567", "valid"},   //14  Faker UserName
    };

    @BeforeTest
    public void setupReport() throws Exception {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportSignup.txt"));
    }

    @BeforeMethod
    public void setupDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testSignupValidation() {
        for (int i = 0; i < credentials.length; i++) {
            String username = credentials[i][0];
            String password = credentials[i][1];
            String expectedType = credentials[i][2];

            StringBuilder result = new StringBuilder();

            try {
                driver.get("https://demoblaze.com/index.html");

                wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username"))).clear();
                driver.findElement(By.id("sign-username")).sendKeys(username);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password"))).clear();
                driver.findElement(By.id("sign-password")).sendKeys(password);

                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]"))).click();

                wait.until(ExpectedConditions.alertIsPresent());
                String alertText = driver.switchTo().alert().getText();
                result.append("Test ").append(i + 1).append(" => Alert: ").append(alertText).append("\n");
                Thread.sleep(500);
                driver.switchTo().alert().accept();

                boolean hasSpecialChar = !username.matches("^[a-zA-Z0-9]*$");
                boolean isUsernameInvalid = username.trim().isEmpty() || username.length() < 3 || hasSpecialChar;
                boolean isPasswordInvalid = password.trim().isEmpty() || password.length() < 6;

                if (alertText.contains("Please fill out")) {
                    if (!expectedType.equals("empty")) {
                        result.append("Bug: Unexpected empty fields alert for non-empty input.\n");
                    } else {
                        result.append("Correct rejection for empty field(s).\n");
                    }
                } else if (alertText.contains("Sign up successful") || alertText.contains("This user already exist")) {
                    if (isUsernameInvalid && isPasswordInvalid) {
                        result.append("Bug: Invalid Username: [")
                                .append(username)
                                .append("] and Password: [")
                                .append(password)
                                .append("] accepted.\n");
                    } else if (isUsernameInvalid) {
                        result.append("Bug: Invalid Username: [")
                                .append(username)
                                .append("] accepted.\n");
                    } else if (isPasswordInvalid) {
                        result.append("Bug: Invalid Password: [")
                                .append(password)
                                .append("] accepted.\n");
                    } else if (alertText.contains("Sign up successful")) {
                        result.append("Accepted valid input as expected.\n");
                    } else {
                        result.append("Accepted existing input as expected.\n");
                    }
                } else {
                    result.append("Unexpected alert: ").append(alertText).append("\n");
                }

            } catch (Exception e) {
                result.append("Test ").append(i + 1).append(" => Exception: ").append(e.getMessage()).append("\n");
            }

            System.out.println(result.toString() + "------------------------------------------------");
            writer.println(result.toString() + "------------------------------------------------");
        }
    }

    @AfterMethod
    public void tearDownDriver() {
        // Ensure the driver is quit after each test method
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void closeReport() {
        // Close the writer after all tests are done
        if (writer != null) {
            writer.close();
            System.out.println("Report saved to reportSignup.txt");
        }
    }
}