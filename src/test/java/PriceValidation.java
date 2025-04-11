import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

public class PriceValidation {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    @BeforeTest
    public void setup() throws IOException {
        // Setup report file
        writer = new PrintWriter(new FileWriter("./Reports/ReportTestTotalPrice.txt"));
        writer.println("Test Report - Test Total Price");

        // Setup browser
        // WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        driver = new ChromeDriver();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://demoblaze.com/index.html");
        waitForPageLoad();
    }

    @BeforeMethod
    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    @Test
    public void testCartTotal() {
        StringBuilder result = new StringBuilder();
        try {
            // Step 1: Click Phones category
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();

            // Step 2: Click Nokia lumia 1520
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Nokia lumia 1520"))).click();

            Thread.sleep(500);

            // Step 3: Add to cart and handle alert
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-success.btn-lg"))).click();
            wait.until(ExpectedConditions.alertIsPresent()).accept();

            Thread.sleep(2000);

            // Step 4: Navigate to Home
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='nav-link' and contains(text(), 'Home')]"))).click();

            // Step 5: Click Laptops category
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();

            // Step 6: Click Sony vaio i7
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i7"))).click();

            // Step 7: Add to cart and handle alert
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-success.btn-lg"))).click();
            wait.until(ExpectedConditions.alertIsPresent()).accept();

            Thread.sleep(2000);

            // Step 8: Go to Cart
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();

            Thread.sleep(2000);

            // Step 9 & 10: Validate total price
            List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//tr[@class='success']/td[3]")));
            int sum = 0;
            for (WebElement priceElement : priceElements) {
                sum += Integer.parseInt(priceElement.getText());
            }

            WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
            int total = Integer.parseInt(totalElement.getText());

            if (sum == total) {
                result.append("Test Passed: Total price ").append(total).append(" matches sum of items ").append(sum).append(".\n");
            } else {
                result.append("Test Failed: Total price ").append(total).append(" does not match sum ").append(sum).append(".\n");
            }

        } catch (Exception e) {
            result.append("Test Failed: ").append(e.getMessage()).append("\n");
        }

        // Log result
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
            System.out.println("Report saved to reportPrice.txt");
        }
    }
}

