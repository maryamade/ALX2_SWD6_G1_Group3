import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

public class PlaceOrderWithReport {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    String[][] orderData = {
            // name,   country,  city,   card,               month, year, expectedType
            {"Maryam", "Egypt", "Cairo", "1234567890123456", "03", "2025", "valid"},
            {"", "Egypt", "Cairo", "1234567890123456", "03", "2025", "invalid"},
            {"Sara", "", "Cairo", "1234567890123456", "03", "2025", "invalid"},
            {"Maryam", "Egypt", "", "1234567890123456", "03", "2025", "invalid"},
            {"Sara", "Egypt", "Cairo", "", "03", "2025", "invalid"},
            {"Maryam", "Egypt", "Cairo", "1234567890123456", "", "", "invalid"}
    };

    @BeforeTest
    public void setupReport() throws IOException {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportPlaceOrder.txt"));
    }

    @BeforeMethod
    public void setupDriver() {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
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
    public void testPlaceOrder() {
        for (int i = 0; i < orderData.length; i++) {
            String name = orderData[i][0];
            String country = orderData[i][1];
            String city = orderData[i][2];
            String card = orderData[i][3];
            String month = orderData[i][4];
            String year = orderData[i][5];
            String expected = orderData[i][6];

            StringBuilder result = new StringBuilder();
            result.append("Test ").append(i + 1).append(" => ");

            try {
                driver.get("https://demoblaze.com/index.html");
                waitForPageLoad();
                // Add one product to the cart
//                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
//                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i5"))).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
//                Thread.sleep(500);
                waitForPageLoad();
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();

                wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(500);
                driver.switchTo().alert().accept();

                // Go to cart
                wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
                waitForPageLoad();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']"))).click();

                // Fill in form
                fillInput(By.id("name"), name);
                fillInput(By.id("country"), country);
                fillInput(By.id("city"), city);
                fillInput(By.id("card"), card);
                fillInput(By.id("month"), month);
                fillInput(By.id("year"), year);

                // Click purchase
//                Thread.sleep(1000); // Wait for client-side validation
                waitForPageLoad();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Purchase']"))).click();

                // Check for alert (invalid data case)
                try {
                    wait.withTimeout(Duration.ofSeconds(2)).until(ExpectedConditions.alertIsPresent());
                    String alertText = driver.switchTo().alert().getText();
                    driver.switchTo().alert().accept();
                    if (expected.equals("invalid")) {
                        result.append("Correct rejection: Form submission failed due to invalid/missing fields. Alert: ").append(alertText).append("\n");
                    } else {
                        result.append("Bug: Valid data was rejected with alert: ").append(alertText).append("\n");
                    }
                } catch (TimeoutException alertNotPresent) {
                    // No alert, check for confirmation
                    try {
                        WebElement confirmation = wait.withTimeout(Duration.ofSeconds(15))
                                .until(ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));
                        String confirmationText = confirmation.getText();
                        if (expected.equals("valid")) {
                            result.append("Success: Order placed successfully.\n").append(confirmationText).append("\n");
                        } else {
                            result.append("Bug: Invalid data was accepted.\n").append(confirmationText).append("\n");
                        }
                        driver.findElement(By.xpath("//button[text()='OK']")).click();
                    } catch (TimeoutException e) {
                        if (expected.equals("invalid")) {
                            result.append("Correct rejection: Form submission failed due to invalid/missing fields (no confirmation).\n");
                        } else {
                            result.append("Bug: Valid data was not processed.\n");
                        }
                    }
                }

            } catch (Exception e) {
                result.append("Exception: ").append(e.getMessage()).append("\n");
            }

            System.out.println(result.toString() + "------------------------------------------------");
            writer.println(result.toString() + "------------------------------------------------");
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
            System.out.println("Report saved to reportPlaceOrder.txt");
        }
    }
}