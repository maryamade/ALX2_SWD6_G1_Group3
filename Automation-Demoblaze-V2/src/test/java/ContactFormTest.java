import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ContactFormTest {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;
    SoftAssert softAssert;

    String[][] contactData = Constants.CONTACT_DATA;

    // Constructor to accept ExtentTest
    public ContactFormTest(ExtentTest test) {
        this.test = test;
    }

    public void setup() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        softAssert = new SoftAssert();
        driver.get(Constants.WEBSITE_URL);
        Methods.smartWait(driver);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
    }

 //   @Test
    public void TestContactForm() {
        for (int i = 0; i < contactData.length; i++) {
            String email = contactData[i][0];
            String name = contactData[i][1];
            String message = contactData[i][2];
            String expected = contactData[i][3];

            test.log(LogStatus.INFO, "Test " + (i + 1) + " started.");

            try {
                Methods.smartWait(driver);

                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Contact']"))).click();
                test.log(LogStatus.INFO, "Clicked 'Contact' link.");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-email"))).clear();
                driver.findElement(By.id("recipient-email")).sendKeys(email);
                test.log(LogStatus.INFO, "Entered email: [" + email + "]");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-name"))).clear();
                driver.findElement(By.id("recipient-name")).sendKeys(name);
                test.log(LogStatus.INFO, "Entered name: [" + name + "]");
                

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-text"))).clear();
                driver.findElement(By.id("message-text")).sendKeys(message);
                test.log(LogStatus.INFO, "Entered message: [" + message + "]");

                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Send message']"))).click();
                test.log(LogStatus.INFO, "Clicked 'Send message' button.");

                wait.until(ExpectedConditions.alertIsPresent());
                String alertText = driver.switchTo().alert().getText();
                Thread.sleep(500);
                driver.switchTo().alert().accept();
                test.log(LogStatus.INFO, "Alert text: " + alertText);

                if (alertText.contains("Thanks for the message")) {
                    if (expected.equals("valid")) {
                        softAssert.assertFalse(email.trim().isEmpty(), "Test " + (i + 1) + ": Email field is empty but accepted.");
                        softAssert.assertFalse(name.trim().isEmpty(), "Test " + (i + 1) + ": Name field is empty but accepted.");
                        softAssert.assertFalse(message.trim().isEmpty(), "Test " + (i + 1) + ": Message field is empty but accepted.");
                        softAssert.assertTrue(email.contains("@") && !email.startsWith(" "), "Test " + (i + 1) + ": Invalid email format accepted.");
                        test.log(LogStatus.PASS, "Valid input accepted as expected.");
                    } else {
                        StringBuilder issues = new StringBuilder("Bug: Invalid/empty input accepted. Issue: ");
                        boolean hasIssue = false;

                        if (email.trim().isEmpty()) {
                            issues.append("Empty email");
                            hasIssue = true;
                        }
                        if (name.trim().isEmpty()) {
                            if (hasIssue) issues.append(", ");
                            issues.append("Empty name");
                            hasIssue = true;
                        }
                        if (message.trim().isEmpty()) {
                            if (hasIssue) issues.append(", ");
                            issues.append("Empty message");
                            hasIssue = true;
                        }
                        if (!email.contains("@") || email.startsWith(" ")) {
                            if (hasIssue) issues.append(", ");
                            issues.append("Invalid email format");
                        }

                        test.log(LogStatus.FAIL, issues.toString() + ".");
                        softAssert.fail("Test " + (i + 1) + ": " + issues.toString() + ".");
                    }
                } else {
                    if (expected.equals("invalid") || expected.equals("empty")) {
                        test.log(LogStatus.PASS, "Correctly rejected invalid/empty input.");
                    } else {
                        test.log(LogStatus.FAIL, "Bug: Valid input rejected.");
                        softAssert.fail("Test " + (i + 1) + ": Valid input rejected without confirmation message.");
                    }
                }

            } catch (Exception e) {
                test.log(LogStatus.ERROR, "Exception: " + e.getMessage());
                softAssert.fail("Test " + (i + 1) + ": Test failed due to exception: " + e.getMessage());
            }
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