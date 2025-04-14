import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
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

public class PlaceOrderWithReport {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;
    SoftAssert softAssert;

    
    // Constructor to accept ExtentTest object
    // This allows the test class to log messages to the ExtentReports instance
    // The ExtentTest object is used to log test results and messages
    // It is passed from the test class that creates an instance of this class
    // This constructor is used to initialize the ExtentTest object
    public PlaceOrderWithReport(ExtentTest test) {
        this.test = test;
    }

    String[][] orderData = Constants.ORDER_DATA;

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

    public void fillInput(By by, String value) {
        int retries = 3;
        while (retries > 0) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                element.clear();
                element.sendKeys(value);
                test.log(LogStatus.INFO, "Filled input " + by.toString() + " with value: " + value);
                return;
            } catch (StaleElementReferenceException e) {
                retries--;
                if (retries == 0) {
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

   // @Test
    public void testPlaceOrder() {
        for (int i = 0; i < orderData.length; i++) {
            String name = orderData[i][0];
            String country = orderData[i][1];
            String city = orderData[i][2];
            String card = orderData[i][3];
            String month = orderData[i][4];
            String year = orderData[i][5];
            String expected = orderData[i][6];

            test.log(LogStatus.INFO, "Starting Test " + (i + 1));

            try {
                driver.get("https://demoblaze.com/index.html");
                Methods.smartWait(driver);
                test.log(LogStatus.INFO, "Navigated to homepage.");

                // Add one product to the cart
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
                test.log(LogStatus.INFO, "Clicked 'Phones' category.");
                Methods.smartWait(driver);
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
                test.log(LogStatus.INFO, "Clicked 'Samsung galaxy s6'.");
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
                test.log(LogStatus.INFO, "Clicked 'Add to cart' for Samsung galaxy s6.");

                wait.until(ExpectedConditions.alertIsPresent());
                String alertText = driver.switchTo().alert().getText();
                Thread.sleep(500);
                driver.switchTo().alert().accept();
                softAssert.assertTrue(alertText.contains("Product added"), "Test " + (i + 1) + ": Product added alert not displayed for Samsung galaxy s6.");
                test.log(LogStatus.PASS, "Product added alert accepted for Samsung galaxy s6.");

                // Go to cart
                wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
                Methods.smartWait(driver);
                test.log(LogStatus.INFO, "Navigated to cart.");

                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']"))).click();
                test.log(LogStatus.INFO, "Clicked 'Place Order' button.");

                // Fill in form
                fillInput(By.id("name"), name);
                fillInput(By.id("country"), country);
                fillInput(By.id("city"), city);
                fillInput(By.id("card"), card);
                fillInput(By.id("month"), month);
                fillInput(By.id("year"), year);

                // Click purchase
                Methods.smartWait(driver);
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Purchase']"))).click();
                test.log(LogStatus.INFO, "Clicked 'Purchase' button.");

                // Check for alert (invalid data case)
                try {
                    wait.withTimeout(Duration.ofSeconds(2)).until(ExpectedConditions.alertIsPresent());
                    alertText = driver.switchTo().alert().getText();
                    driver.switchTo().alert().accept();
                    test.log(LogStatus.INFO, "Alert text: " + alertText);
                    if (expected.equals("invalid")) {
                        test.log(LogStatus.PASS, "Correct rejection: Form submission failed due to invalid/missing fields. Alert: " + alertText);
                    } else {
                        test.log(LogStatus.FAIL, "Bug: Valid data was rejected with alert: " + alertText);
                        softAssert.fail("Test " + (i + 1) + ": Valid data was rejected with alert: " + alertText);
                    }
                } catch (TimeoutException alertNotPresent) {
                    // No alert, check for confirmation
                    try {
                        WebElement confirmation = wait.withTimeout(Duration.ofSeconds(15))
                                .until(ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));
                        String confirmationText = confirmation.getText();
                        test.log(LogStatus.INFO, "Confirmation text: " + confirmationText);
                        if (expected.equals("valid")) {
                            test.log(LogStatus.PASS, "Success: Order placed successfully. " + confirmationText);
                        } else {
                            StringBuilder failureDetails = new StringBuilder("Bug: Invalid data was accepted. Issues: ");
                            if (name.trim().isEmpty()) failureDetails.append("Empty name; ");
                            if (country.trim().isEmpty()) failureDetails.append("Empty country; ");
                            if (city.trim().isEmpty()) failureDetails.append("Empty city; ");
                            if (card.trim().isEmpty()) failureDetails.append("Empty card; ");
                            if (month.trim().isEmpty()) failureDetails.append("Empty month; ");
                            if (year.trim().isEmpty()) failureDetails.append("Empty year; ");
                            test.log(LogStatus.FAIL, failureDetails.toString());
                            softAssert.fail("Test " + (i + 1) + ": " + failureDetails.toString());
                        }
                        driver.findElement(By.xpath("//button[text()='OK']")).click();
                        test.log(LogStatus.INFO, "Clicked 'OK' on confirmation.");
                    } catch (TimeoutException e) {
                        if (expected.equals("invalid")) {
                            test.log(LogStatus.PASS, "Correct rejection: Form submission failed due to invalid/missing fields (no confirmation).");
                        } else {
                            test.log(LogStatus.FAIL, "Bug: Valid data was not processed.");
                            softAssert.fail("Test " + (i + 1) + ": Valid data was not processed.");
                        }
                    }
                }

            } catch (Exception e) {
                test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
                softAssert.fail("Test " + (i + 1) + ": Test failed due to exception: " + e.getMessage());
            }

            test.log(LogStatus.INFO, "------------------------------------------------");
        }

        // Assert all soft assertions at the end
        softAssert.assertAll();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}