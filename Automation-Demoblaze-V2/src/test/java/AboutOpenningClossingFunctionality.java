import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class AboutOpenningClossingFunctionality {


    //Constructor for ExtentTest
    public AboutOpenningClossingFunctionality(ExtentTest test) {
        this.test = test;
    }

    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;

    // Locators
    private final By aboutLink = By.xpath("//*[@id=\"navbarExample\"]/ul/li[3]/a");
    private final By closeButton = By.xpath("//*[@id=\"videoModal\"]//button[contains(@class, 'close')]");
    private final By modalContent = By.id("videoModal");


    @BeforeTest
       public void setup() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Constants.WEBSITE_URL);
        Methods.smartWait(driver);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
    }

 //   @Test
    public void TestAboutOpenningClossingFunctionality() {
        SoftAssert softAssert = new SoftAssert();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(aboutLink)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));
            test.log(LogStatus.INFO, "Clicked 'About' link, modal is visible.");

            Methods.smartWait(driver);

            wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalContent));
            test.log(LogStatus.INFO, "Clicked 'Close' button.");

            Methods.smartWait(driver);

            boolean isModalClosed = driver.findElements(modalContent).isEmpty() ||
                    !driver.findElement(modalContent).isDisplayed();
            softAssert.assertTrue(isModalClosed, "About modal did not close properly.");
            test.log(LogStatus.PASS, "Test Passed: About modal opened and closed successfully.");
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test Failed: " + e.getMessage());
            softAssert.fail("Test failed due to exception: " + e.getMessage());
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