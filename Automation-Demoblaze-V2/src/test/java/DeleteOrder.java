import java.time.Duration;
import java.util.List;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class DeleteOrder {
    WebDriver driver;
    WebDriverWait wait;
    ExtentReports report;
    ExtentTest test;

        // Constructor to accept ExtentTest
    public DeleteOrder(ExtentTest test) {
        this.test = test;
    }

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Constants.WEBSITE_URL);
        Methods.smartWait(driver);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
    }


   // @Test
    public void testDeleteOrder() {
        SoftAssert softAssert = new SoftAssert();
        try {
            // ==== Test Phones ====
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
            test.log(LogStatus.INFO, "Clicked 'Phones' category.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
            test.log(LogStatus.INFO, "Clicked 'Samsung galaxy s6'.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            test.log(LogStatus.INFO, "Clicked 'Add to cart' for Samsung galaxy s6.");
            wait.until(ExpectedConditions.alertIsPresent());
            String alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            softAssert.assertTrue(alertText.contains("Product added"), "Product added alert not displayed for Samsung galaxy s6.");
            test.log(LogStatus.PASS, "Product added alert accepted for Samsung galaxy s6.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click(); // Logo
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
            softAssert.assertTrue(driver.findElement(By.id("cat")).isDisplayed(), "Failed to return to homepage after adding phone.");
            test.log(LogStatus.PASS, "Returned to homepage after adding phone.");

            // ==== Test Laptops ====
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
            Methods.smartWait(driver);
            test.log(LogStatus.INFO, "Clicked 'Laptops' category.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i5"))).click();
            test.log(LogStatus.INFO, "Clicked 'Sony vaio i5'.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            test.log(LogStatus.INFO, "Clicked 'Add to cart' for Sony vaio i5.");
            wait.until(ExpectedConditions.alertIsPresent());
            alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            softAssert.assertTrue(alertText.contains("Product added"), "Product added alert not displayed for Sony vaio i5.");
            test.log(LogStatus.PASS, "Product added alert accepted for Sony vaio i5.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click(); // Logo
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
            softAssert.assertTrue(driver.findElement(By.id("cat")).isDisplayed(), "Failed to return to homepage after adding laptop.");
            test.log(LogStatus.PASS, "Returned to homepage after adding laptop.");

            // ==== Test Monitors ====
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Monitors"))).click();
            Methods.smartWait(driver);
            test.log(LogStatus.INFO, "Clicked 'Monitors' category.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Apple monitor 24"))).click();
            test.log(LogStatus.INFO, "Clicked 'Apple monitor 24'.");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            test.log(LogStatus.INFO, "Clicked 'Add to cart' for Apple monitor 24.");
            wait.until(ExpectedConditions.alertIsPresent());
            alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            softAssert.assertTrue(alertText.contains("Product added"), "Product added alert not displayed for Apple monitor 24.");
            test.log(LogStatus.PASS, "Product added alert accepted for Apple monitor 24.");
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click(); // Logo
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
            softAssert.assertTrue(driver.findElement(By.id("cat")).isDisplayed(), "Failed to return to homepage after adding monitor.");
            test.log(LogStatus.PASS, "Returned to homepage after adding monitor.");

            // ==== Go to Cart ====
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
            Methods.smartWait(driver);
            test.log(LogStatus.INFO, "Navigated to cart.");

            // ==== Delete all products ====
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));
            Methods.smartWait(driver);
            List<WebElement> deleteButtons = driver.findElements(By.xpath("//tbody[@id='tbodyid']//a[text()='Delete']"));
            softAssert.assertFalse(deleteButtons.isEmpty(), "Cart is empty; expected items to be present.");
            test.log(LogStatus.INFO, "Cart contains items to delete.");

            int deleteCount = 0;
            while (true) {
                // Fetch the delete buttons fresh each time
                deleteButtons = driver.findElements(By.xpath("//tbody[@id='tbodyid']//a[text()='Delete']"));
                if (deleteButtons.isEmpty()) {
                    break; // Exit loop when no more delete buttons are found
                }

                // Click the first delete button
                WebElement deleteButton = deleteButtons.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
                deleteCount++;
                // Wait for the item to be removed or it will delete 1 product only
                Methods.smartWait(driver);
                test.log(LogStatus.INFO, "Clicked 'Delete' for item " + deleteCount + ".");
                wait.until(ExpectedConditions.stalenessOf(deleteButton));
            }

            softAssert.assertTrue(driver.findElements(By.xpath("//tbody[@id='tbodyid']//a[text()='Delete']")).isEmpty(), "Failed to delete all items from cart.");
            test.log(LogStatus.PASS, "All items deleted from the cart.");
            test.log(LogStatus.PASS, "Test Passed: Navigated, added to cart, and cleared cart.");

        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            softAssert.assertAll(); // Aggregate all soft assertion results
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