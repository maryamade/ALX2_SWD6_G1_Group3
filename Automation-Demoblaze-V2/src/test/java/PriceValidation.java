import java.time.Duration;
import java.util.List;

import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import org.example.Constants;


public class PriceValidation {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;
    SoftAssert softAssert;

    // Constructor to accept ExtentTest
    public PriceValidation(ExtentTest test) {
        this.test = test;
    }

    @BeforeTest
    public void setup() {
        softAssert = new SoftAssert();
        System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Constants.WEBSITE_URL);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
        Methods.smartWait(driver);
    }

 //   @Test
    public void testCartTotal() {
        try {
            // Step 1: Click Phones category
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
            test.log(LogStatus.INFO, "Clicked 'Phones' category.");

            // Step 2: Click Nokia lumia 1520
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Nokia lumia 1520"))).click();
            test.log(LogStatus.INFO, "Clicked 'Nokia lumia 1520'.");

            // Step 3: Add to cart and handle alert
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-success.btn-lg"))).click();
            test.log(LogStatus.INFO, "Clicked 'Add to cart' for Nokia lumia 1520.");
            wait.until(ExpectedConditions.alertIsPresent());
            String alertText = driver.switchTo().alert().getText();
            Thread.sleep(500);
            driver.switchTo().alert().accept();
            softAssert.assertTrue(alertText.contains("Product added"), "Product added alert not displayed for Nokia lumia 1520.");
            test.log(LogStatus.PASS, "Product added alert accepted for Nokia lumia 1520.");

            // Step 4: Navigate to Home
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='nav-link' and contains(text(), 'Home')]"))).click();
            test.log(LogStatus.INFO, "Clicked 'Home' link.");

            // Step 5: Click Laptops category
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
            test.log(LogStatus.INFO, "Clicked 'Laptops' category.");

            Methods.smartWait(driver); // to prevent element not found error after click on laptop category

            // Step 6: Click Sony vaio i7
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i7"))).click();
            test.log(LogStatus.INFO, "Clicked 'Sony vaio i7'.");

            // Step 7: Add to cart and handle alert
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-success.btn-lg"))).click();
            test.log(LogStatus.INFO, "Clicked 'Add to cart' for Sony vaio i7.");
            wait.until(ExpectedConditions.alertIsPresent());
            alertText = driver.switchTo().alert().getText();
            Thread.sleep(500);
            driver.switchTo().alert().accept();
            softAssert.assertTrue(alertText.contains("Product added"), "Product added alert not displayed for Sony vaio i7.");
            test.log(LogStatus.PASS, "Product added alert accepted for Sony vaio i7.");

            // Step 8: Go to Cart
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
            test.log(LogStatus.INFO, "Navigated to cart.");

            Methods.smartWait(driver); // for showing the prices in the cart

            // Step 9 & 10: Validate total price
            List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//tr[@class='success']/td[3]")));
            int sum = 0;
            for (WebElement priceElement : priceElements) {

                // Get the price text and convert to integer
                // Remove any non-numeric characters
                // Assuming the price is in the format "$123.45"
                // Remove the dollar sign and parse the number
                // Assuming the price is in the format "123.45"
                String priceText = priceElement.getText().trim();
                softAssert.assertFalse(priceText.isEmpty(), "Price element is empty for an item in the cart.");
                sum += Integer.parseInt(priceText);
                test.log(LogStatus.INFO, "Added price: " + priceText + " to sum.");
            }

            WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
            String totalText = totalElement.getText().trim();
            softAssert.assertFalse(totalText.isEmpty(), "Total price element is empty.");
            int total = Integer.parseInt(totalText);

            softAssert.assertEquals(total, sum, "Total price " + total + " does not match sum of items " + sum + ".");
            if (sum == total) {
                test.log(LogStatus.PASS, "Total price " + total + " matches sum of items " + sum + ".");
            } else {
                test.log(LogStatus.FAIL, "Total price " + total + " does not match sum " + sum + ".");
            }

        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
            softAssert.fail("Test failed due to exception: " + e.getMessage());
        }

        // Assert all soft assertions
        softAssert.assertAll();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}