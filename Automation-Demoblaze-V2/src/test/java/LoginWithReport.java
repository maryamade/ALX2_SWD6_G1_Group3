import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class LoginWithReport {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;
    SoftAssert softAssert;

    String[][] loginData = Constants.LOGIN_DATA;

    // Constructor to accept ExtentTest object
    public LoginWithReport(ExtentTest test) {
        this.test = test;
    }

    // TestNG @BeforeTest method to set up the WebDriver and navigate to the website
    // This method is called before each test method in the class
    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Constants.WEBSITE_URL);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
        Methods.smartWait(driver);
        softAssert = new SoftAssert(); // Initialize softAssert here
    }


    @BeforeMethod
    public boolean isUserLogin() {
        try {
            WebElement userNameElement = driver.findElement(By.id("nameofuser"));

            //
            String usernameText = userNameElement.getText().trim();
            return !usernameText.isEmpty() && usernameText.startsWith("Welcome");
        } catch (Exception e) {
            return false;
        }
    }


//@Test
    public void testLogin() {
        for (int i = 0; i < loginData.length; i++) {
            String username = loginData[i][0];
            String password = loginData[i][1];
            String expectedType = loginData[i][2];

            test.log(LogStatus.INFO, "Test " + (i + 1) + " started.");

            try {
           
                driver.get(Constants.WEBSITE_URL);
                test.log(LogStatus.INFO, "Navigated to homepage.");

                wait.until(ExpectedConditions.elementToBeClickable(By.id("login2"))).click();
                test.log(LogStatus.INFO, "Clicked 'Login' link.");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).clear();
                driver.findElement(By.id("loginusername")).sendKeys(username);
                test.log(LogStatus.INFO, "Entered username: [" + username + "]");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword"))).clear();
                driver.findElement(By.id("loginpassword")).sendKeys(password);
                 test.log(LogStatus.INFO, "Entered password: [" + password + "]");

                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='logInModal']/div/div/div[3]/button[2]"))).click();
                test.log(LogStatus.INFO, "Clicked 'Login' button.");

                if (expectedType.equals("valid")) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
                    String welcomeText = driver.findElement(By.id("nameofuser")).getText();
                    softAssert.assertTrue(welcomeText.contains("Welcome"), "Test " + (i + 1) + ": Valid credentials but no welcome message.");
                    if (welcomeText.contains("Welcome")) {
                        test.log(LogStatus.PASS, "Valid login as expected.");
                    } else {
                        test.log(LogStatus.FAIL, "Bug: Valid credentials but no welcome message.");
                    }

                    driver.findElement(By.id("logout2")).click();
                    test.log(LogStatus.INFO, "Clicked 'Logout' button.");
                    softAssert.assertFalse(isUserLogin(), "Test " + (i + 1) + ": Logout failed; user still logged in.");
                    if (!isUserLogin()) {
                        test.log(LogStatus.PASS, "Logout successful.");
                    } else {
                        test.log(LogStatus.FAIL, "Bug: Logout failed; user still logged in.");
                    }
                } else {
                    String alertText = "";
                    try {
                        wait.until(ExpectedConditions.alertIsPresent());
                        alertText = driver.switchTo().alert().getText();
                        Thread.sleep(500);
                        driver.switchTo().alert().accept();
                        test.log(LogStatus.INFO, "Alert text: " + alertText);
                    } catch (Exception e) {
                        test.log(LogStatus.FAIL, "Bug: No alert shown for invalid or empty login.");
                        softAssert.fail("Test " + (i + 1) + ": No alert shown for invalid or empty login.");
                        continue;
                    }

                    if (expectedType.equals("notExist")) {
                        softAssert.assertTrue(alertText.contains("User does not exist"), "Test " + (i + 1) + ": Expected 'User does not exist' alert, got: " + alertText);
                        test.log(LogStatus.PASS, "Valid rejection for non-existing user.");
                    } else if (expectedType.equals("invalid")) {
                        softAssert.assertTrue(alertText.contains("Wrong password"), "Test " + (i + 1) + ": Expected 'Wrong password' alert, got: " + alertText);
                        test.log(LogStatus.PASS, "Valid rejection for invalid password.");
                    } else if (expectedType.equals("empty")) {
                        softAssert.assertTrue(alertText.contains("Please fill out Username and Password"), "Test " + (i + 1) + ": Expected 'Please fill out Username and Password' alert, got: " + alertText);
                        test.log(LogStatus.PASS, "Valid rejection for empty fields.");
                    }
                }

            } catch (Exception e) {
                test.log(LogStatus.FAIL, "Bug: Exception during test: " + e.getMessage());
                softAssert.fail("Test " + (i + 1) + ": Exception during test: " + e.getMessage());
            }

           // test.log(LogStatus.INFO, "Test " + (i + 1) + " completed.");
        }

        softAssert.assertAll();
    }
    // TestNG @AfterTest method to close the browser after all tests are done
   @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}