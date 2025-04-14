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

    // Single valid order data
    String[] orderData = {"Maryam", "Egypt", "Cairo", "1234567890123456", "03", "2025", "valid"};

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

    // Reused method from PlaceOrderWithReport
    public void waitForPageLoad() {
        // Wait until the document.readyState is complete
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(webDriver ->
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
        );


        // Wait until all AJAX requests are complete
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(webDriver ->
                ((JavascriptExecutor) webDriver).executeScript("return jQuery.active == 0").toString().equals("true")
        );

        // Wait until all visible elements are present and stable
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(webDriver ->
                webDriver.findElements(By.xpath("//*[not(contains(@style,'display:none'))]")).size() > 0
        );
    }

    // Reused method from PlaceOrderWithReport
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

                // Step 2: Navigate to product page
                driver.get(productUrl);

                // Step 3: Extract product name
                WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.name")));
                String productName = productNameElement.getText();
                result.append(productName).append("\n");

                // Step 4: Click "Add to cart"
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tbodyid\"]/div[2]/div/a"))).click();
                result.append("Clicked 'Add to cart'. \n");

                // Step 5: Handle "Product added" alert
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

        // Part 2: Checkout with one order data
        result.append("Checkout Test => ");
        try {
            // Go to cart
            driver.get("https://demoblaze.com/index.html");
            waitForPageLoad();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
            waitForPageLoad();

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
            waitForPageLoad();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Purchase']"))).click();

            // Check for confirmation
            try {
                WebElement confirmation = wait.withTimeout(Duration.ofSeconds(15))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));
                String confirmationText = confirmation.getText();
                if (orderData[6].equals("valid")) {
                    result.append("Success: Order placed successfully.\n").append(confirmationText).append("\n");
                } else {
                    result.append("Bug: Invalid data was accepted.\n").append(confirmationText).append("\n");
                }
                driver.findElement(By.xpath("//button[text()='OK']")).click();
            } catch (TimeoutException e) {
                result.append("Bug: Valid data was not processed (no confirmation).\n");
            }

        } catch (Exception e) {
            result.append("Exception during checkout: ").append(e.getMessage()).append("\n");
        }

        System.out.println(result.toString() + "------------------------------------------------");
        writer.println(result.toString() + "------------------------------------------------");
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