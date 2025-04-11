import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

public class TestAddAllProductsToCart {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    // Valid login data
    String[] validLoginData = {"MaryamSara", "1234567", "valid"};

    // Define product IDs to test (1 to 15)
    int[] productIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    @BeforeTest
    public void setupReport() throws IOException {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/ReportTestAddAllProductsToCart.txt"));
    }

    @BeforeMethod
    public void setupDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testAddProductsToCart() {
        StringBuilder result = new StringBuilder();

        // Part 1: Log in with valid credentials
        result.append("Login Test => ");
        try {
            driver.get("https://demoblaze.com/index.html");

            // Open login modal
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
            if (welcomeText.contains("Welcome")) {
                result.append("Valid login as expected. ");
            } else {
                result.append("Bug: Valid credentials but no welcome message. ");
                System.out.println(result.toString() + "------------------------------------------------");
                writer.println(result.toString() + "------------------------------------------------");
                return; // Exit test if login fails
            }
        } catch (Exception e) {
            result.append("Bug: Valid login failed. Exception: ").append(e.getMessage()).append(". ");
            System.out.println(result.toString() + "------------------------------------------------");
            writer.println(result.toString() + "------------------------------------------------");
            return; // Exit test if login fails
        }

        System.out.println(result.toString() + "------------------------------------------------");
        writer.println(result.toString() + "------------------------------------------------");

        // Clear result for product tests
        result.setLength(0);

        // Test adding each product to cart
        for (int i = 0; i < productIds.length; i++) {
            int productId = productIds[i];
            String productUrl = "https://demoblaze.com/prod.html?idp_=" + productId;

            result.append("Test ").append(i + 1).append(" (Product ID: ").append(productId).append(") => ");

            try {
                // Step 1: Go to homepage
                driver.get("https://demoblaze.com/index.html");
                result.append("Navigated to homepage. \n");

                // Step 2: Navigate to product page
                driver.get(productUrl);
                result.append("Navigated to product page: ").append(productUrl).append(". \n");

                // Step 3: Click "Add to cart"
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tbodyid\"]/div[2]/div/a"))).click();
                result.append("Clicked 'Add to cart'. \n");

                // Step 4: Handle "Product added" alert
                try {
                    wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    alert.accept();

                    if (alertText.equals("Product added") || alertText.equals("Product added.")) {
                        result.append("Success: 'Product added' alert displayed and accepted. \n");
                    } else {
                        result.append("Bug: Unexpected alert text: ").append(alertText).append(". \n");
                    }
                } catch (TimeoutException e) {
                    result.append("Bug: 'Product added' alert not displayed. \n");
                }

            } catch (Exception e) {
                result.append("Exception: ").append(e.getMessage()).append(". \n");
            }

            // Print and write the result for this product
            System.out.println(result.toString() + "------------------------------------------------\n");
            writer.println(result.toString() + "------------------------------------------------\n");

            // Clear result for the next product
            result.setLength(0);
        }
    }

    @AfterMethod
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void closeReport() {
        if (writer != null) {
            writer.close();
            System.out.println("Report saved to ReportTestAddAllProductsToCart.txt");
        }
    }
}