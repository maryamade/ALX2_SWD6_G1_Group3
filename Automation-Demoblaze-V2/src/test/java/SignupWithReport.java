import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class SignupWithReport {

     // Constructor to accept ExtentTest  object
    public SignupWithReport(ExtentTest test) {
        this.test = test;
    }

    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;
    SoftAssert softAssert;

    String[][] credentials = Constants.CREDENTIALS;

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);

        // Set Chrome options to disable the save password popup
        //  This is important to prevent the popup from interfering with the test
        //  This is a common issue when using ChromeDriver
        //  The save password popup can block elements on the page
        //  and cause tests to fail
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-save-password-bubble"); // Disable save password popup
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("credentials_enable_service", false); // Disable password manager
            put("profile.password_manager_enabled", false); // Disable password manager
        }});
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Constants.WEBSITE_URL);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
        Methods.smartWait(driver);
        softAssert = new SoftAssert(); // Initialize softAssert here
    }

   // @Test
    public void testSignupValidation() {

        for (int i = 0; i < credentials.length; i++) {
            String username = credentials[i][0];
            String password = credentials[i][1];
            String expectedType = credentials[i][2];

          //  test.log(LogStatus.INFO, "Test " + (i + 1) + " started.");

            try {
                driver.get(Constants.WEBSITE_URL);

                wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username"))).clear();
                driver.findElement(By.id("sign-username")).sendKeys(username);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password"))).clear();
                driver.findElement(By.id("sign-password")).sendKeys(password);

                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]"))).click();

                wait.until(ExpectedConditions.alertIsPresent());
                String alertText = driver.switchTo().alert().getText(); // Get the alert text
                Thread.sleep(500);
                driver.switchTo().alert().accept(); // Accept the alert // close it

                test.log(LogStatus.INFO, "User:[" + username + "], Pass:[" + password + "] ====> Alert: " + alertText);

                boolean hasSpecialChar = !username.matches("^[a-zA-Z0-9]*$");
                boolean hasSpaces = username.contains(" ");
                boolean isUsernameInvalid = username.trim().isEmpty() || username.length() < 3 || hasSpecialChar || hasSpaces;
                boolean isPasswordInvalid = password.trim().isEmpty() || password.length() < 6;

                if (alertText.contains("Please fill out")) {
                    softAssert.assertTrue(expectedType.equals("empty"), "Unexpected empty fields alert for non-empty input.");
                    if (!expectedType.equals("empty")) {
                        test.log(LogStatus.FAIL, "Bug: Unexpected empty fields alert for non-empty input.");
                    } else {
                        test.log(LogStatus.PASS, "Correct rejection for empty field(s).");
                    }
                } else if (alertText.contains("Sign up successful") || alertText.contains("This user already exist")) {
                    softAssert.assertFalse(isUsernameInvalid, "Bug: Invalid Username accepted. Username: [" + username + "]");
                    softAssert.assertFalse(isPasswordInvalid, "Bug: Invalid Password accepted. Password: [" + password + "]");

                    if (alertText.contains("Sign up successful")) {
                        boolean isInputValid = !isUsernameInvalid && !isPasswordInvalid;
                        softAssert.assertTrue(isInputValid,
                            "Bug: Invalid input accepted as successful. Username: [" + username + "], Password: [" + password + "]");
                        if (isInputValid) {
                            test.log(LogStatus.PASS, "Accepted valid input as expected.");
                        } else {
                            test.log(LogStatus.FAIL, "Bug: Invalid input accepted as successful. Username: [" + username + "], Password: [" + password + "]");
                        }
                    } else {
                        if (isUsernameInvalid || isPasswordInvalid) {
                            test.log(LogStatus.FAIL, "Bug: Invalid input marked as existing user. Username: [" + username + "], Password: [" + password + "]");
                        } else {
                            test.log(LogStatus.PASS, "Accepted existing input as expected.");
                        }
                    }
                } else {
                    test.log(LogStatus.FAIL, "Unexpected alert: " + alertText + " ");
                }

            } catch (Exception e) {
                test.log(LogStatus.ERROR, "Exception: " + e.getMessage());
            }

//            System.out.println("Test " + (i + 1) + " completed.\n------------------------------------------------");
        }

        softAssert.assertAll();
    }



    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}