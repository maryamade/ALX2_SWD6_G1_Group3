import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Categories_Logo {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;
    SoftAssert softAssert = new SoftAssert();

    // Constructor to accept ExtentTest
    public Categories_Logo(ExtentTest test) {
        this.test = test;
    }

    @BeforeTest
       public void setup() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        softAssert = new SoftAssert();
        driver.get(Constants.WEBSITE_URL);
        Methods.smartWait(driver);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
    }

   // @Test
    public void testCategoriesLogo() {
        try {
            // Test Phones
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
            test.log(LogStatus.INFO, "Clicked 'Phones' category.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
            test.log(LogStatus.INFO, "Clicked 'Samsung galaxy s6'.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
            softAssert.assertTrue(driver.findElement(By.id("cat")).isDisplayed(), 
                "Failed to return to homepage after clicking logo (Phones).");
            test.log(LogStatus.PASS, "Returned to homepage after clicking logo (Phones).");

            // Test Laptops
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
            test.log(LogStatus.INFO, "Clicked 'Laptops' category.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i5"))).click();
            test.log(LogStatus.INFO, "Clicked 'Sony vaio i5'.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
            softAssert.assertTrue(driver.findElement(By.id("cat")).isDisplayed(), 
                "Failed to return to homepage after clicking logo (Laptops).");
            test.log(LogStatus.PASS, "Returned to homepage after clicking logo (Laptops).");

            // Test Monitors
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Monitors"))).click();
            test.log(LogStatus.INFO, "Clicked 'Monitors' category.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Apple monitor 24"))).click();
            test.log(LogStatus.INFO, "Clicked 'Apple monitor 24'.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
            softAssert.assertTrue(driver.findElement(By.id("cat")).isDisplayed(), 
                "Failed to return to homepage after clicking logo (Monitors).");
            test.log(LogStatus.PASS, "Returned to homepage after clicking logo (Monitors).");

            test.log(LogStatus.PASS, "Navigated through all categories and returned home using logo.");
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
            throw new RuntimeException("Test failed due to exception: " + e.getMessage());
        } finally {
            softAssert.assertAll(); // Ensure all soft assertions are verified
            System.out.println("------------------------------------------------");
        }
    }
    
     @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}