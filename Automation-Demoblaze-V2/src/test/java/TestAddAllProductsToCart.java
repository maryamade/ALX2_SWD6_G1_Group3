import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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


public class TestAddAllProductsToCart {
    

       // Constructor to accept ExtentTest  
  public TestAddAllProductsToCart(ExtentTest test) {
       this.test = test;
    }               


      


    ExtentTest test;
    WebDriver driver;
    WebDriverWait wait;
    SoftAssert softAssert;

    // Valid login data
    String[] validLoginData = {"MaryamSara", "1234567", "valid"};

    // Define product IDs to test (1 to 15)
    int[] productIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    // Single valid order data
    String[] orderData = {"Maryam", "Egypt", "Cairo", "1234567890123456", "03", "2025", "valid"};

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

  //  @Test
    public void testAddProductsToCart() {
        test.log(LogStatus.INFO, "Login Test => Navigated to homepage.");
        try {
            driver.get(Constants.WEBSITE_URL);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("login2"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).clear();
            driver.findElement(By.id("loginusername")).sendKeys(validLoginData[0]);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword"))).clear();
            driver.findElement(By.id("loginpassword")).sendKeys(validLoginData[1]);

            // Click login button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='logInModal']/div/div/div[3]/button[2]"))).click();

            // Verify login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
            String welcomeText = driver.findElement(By.id("nameofuser")).getText();
            softAssert.assertTrue(welcomeText.contains("Welcome"), "Login failed: No welcome message displayed.");
            if (welcomeText.contains("Welcome")) {
                test.log(LogStatus.INFO, "Valid login as expected.");
            } else {
                test.log(LogStatus.FAIL, "Bug: Valid credentials but no welcome message.");
                return; // Exit test if login fails
            }
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Bug: Valid login failed. Exception: " + e.getMessage());
            softAssert.fail("Login failed: Exception: " + e.getMessage());
            return; // Exit test if login fails
        }

        // Test adding each product to cart
        for (int i = 0; i < productIds.length; i++) {
            int productId = productIds[i];
            String productUrl = ""+ Constants.WEBSITE_URL+"prod.html?idp_=" + productId;


            test.log(LogStatus.INFO, "Test " + (i + 1) + " (Product ID: " + productId + ") : ");
            try {
                // Step 1: Go to homepage
                driver.get(Constants.WEBSITE_URL);

                // Step 2: Navigate to product page
                driver.get(productUrl);

                // Step 3: Extract product name
                WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.name")));
                String productName = productNameElement.getText();
                softAssert.assertFalse(productName.isEmpty(), "Test " + (i + 1) + ": Product name is empty for ID " + productId);
                test.log(LogStatus.INFO, "Product name: " + productName);

                // Step 4: Click "Add to cart"
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tbodyid\"]/div[2]/div/a"))).click();
                test.log(LogStatus.INFO, "Clicked 'Add to cart'.");

                // Step 5: Handle "Product added" alert
                try {
                    wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    Thread.sleep(500);
                    alert.accept();
                    softAssert.assertTrue(alertText.equals("Product added") || alertText.equals("Product added."),
                            "Test " + (i + 1) + ": Unexpected alert text for product ID " + productId + ": " + alertText);
                    test.log(LogStatus.PASS, "Success: 'Product added' alert displayed and accepted.");
                } catch (TimeoutException e) {
                    test.log(LogStatus.FAIL, "Bug: 'Product added' alert not displayed.");
                    softAssert.fail("Test " + (i + 1) + ": 'Product added' alert not displayed for product ID " + productId);
                }

            } catch (Exception e) {
                test.log(LogStatus.FAIL, "Exception: " + e.getMessage());
                softAssert.fail("Test " + (i + 1) + ": Exception for product ID " + productId + ": " + e.getMessage());
            }
        }

        // Part 2: Checkout with one order data
        test.log(LogStatus.INFO, "Checkout Test ");
        try {
            // Go to cart
            driver.get(Constants.WEBSITE_URL);
            Methods.smartWait(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
            Methods.smartWait(driver);

            // Click "Place Order"
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']"))).click();

            // Fill in form with orderData
            fillInput(By.id("name"), orderData[0]);
            fillInput(By.id("country"), orderData[1]);
            fillInput(By.id("city"), orderData[2]);
            fillInput(By.id("card"), orderData[3]);
            fillInput(By.id("month"), orderData[4]);
            fillInput(By.id("year"), orderData[5]);

            // Click purchase
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Purchase']"))).click();

            Methods.smartWait(driver); // waiting for thankyou for purshase
            // Check for confirmation
            try {
                WebElement confirmation = wait.withTimeout(Duration.ofSeconds(15))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));
                String confirmationText = confirmation.getText();
                softAssert.assertFalse(confirmationText.isEmpty(), "Checkout confirmation text is empty.");
                if (orderData[6].equals("valid")) {
                    test.log(LogStatus.INFO, "" + confirmationText);
                    test.log(LogStatus.PASS, "Success: Order placed successfully.\n");
                } else {
                    test.log(LogStatus.INFO, "" + confirmationText);
                    test.log(LogStatus.FAIL, "Bug: Invalid data was accepted.\n");
                    softAssert.fail("Checkout failed: Invalid data was accepted.");
                }
                driver.findElement(By.xpath("//button[text()='OK']")).click();
            } catch (TimeoutException e) {
                test.log(LogStatus.FAIL, "Bug: Valid data was not processed (no confirmation).");
                softAssert.fail("Checkout failed: Valid data was not processed (no confirmation).");
            }

        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Exception during checkout: " + e.getMessage());
            softAssert.fail("Checkout failed: Exception: " + e.getMessage());
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