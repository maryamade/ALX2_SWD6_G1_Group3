import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class NextPreviousButtonsFunctionality {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;

    // Constructor to initialize ExtentTest
    // This constructor is used to initialize the ExtentTest object
    // which is used for logging test results in the ExtentReports framework.
    public NextPreviousButtonsFunctionality(ExtentTest test) {
        this.test = test;
    }

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       driver.get(Constants.WEBSITE_URL);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
        Methods.smartWait(driver);
    }

   // @Test
    public void testNextPreviousButtonsFunctionality() {
        SoftAssert softAssert = new SoftAssert();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("next2"))).click();
            test.log(LogStatus.INFO, "Clicked 'Next' button.");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Apple monitor 24")));
            softAssert.assertTrue(driver.findElement(By.linkText("Apple monitor 24")).isDisplayed(),
                    "Apple monitor 24 is not visible after clicking Next button.");
            test.log(LogStatus.PASS, "Next button clicked - 'Apple monitor 24' is visible.");

            Methods.smartWait(driver);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("prev2"))).click();
            test.log(LogStatus.INFO, "Clicked 'Previous' button.");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Nokia lumia 1520")));
            softAssert.assertTrue(driver.findElement(By.linkText("Nokia lumia 1520")).isDisplayed(),
                    "Nokia lumia 1520 is not visible after clicking Previous button.");
            test.log(LogStatus.PASS, "Previous button clicked - 'Nokia lumia 1520' is visible.");

            test.log(LogStatus.PASS, "Successfully tested Next and Previous button functionality.");
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}