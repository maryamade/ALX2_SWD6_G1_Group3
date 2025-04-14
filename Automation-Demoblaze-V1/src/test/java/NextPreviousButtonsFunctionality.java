import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

public class NextPreviousButtonsFunctionality {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    @BeforeTest
    public void setup() throws IOException {
        // Setup report file
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/NextPreviousButtonsFunctionality.txt"));
        writer.println("Test Report - Pagination Buttons Navigation");

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://demoblaze.com/index.html");
        waitForPageLoad();

    }

    @BeforeMethod
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



    @Test
    public void testNextPreviousButtonsFunctionality() {
        StringBuilder result = new StringBuilder();
        try {





            // Test Next button
            wait.until(ExpectedConditions.elementToBeClickable(By.id("next2"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Apple monitor 24")));
            result.append("Next button clicked - 'Apple monitor 24' is visible\n");

            waitForPageLoad();

            // Test Previous button
            wait.until(ExpectedConditions.elementToBeClickable(By.id("prev2"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Nokia lumia 1520")));
            result.append("Previous button clicked - 'Nokia lumia 1520' is visible\n");

            result.append("Test Passed: Successfully tested Next and Previous button functionality.\n");
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
            System.out.println("Report saved to NextPreviousButtonsFunctionality.txt");
        }
    }
}