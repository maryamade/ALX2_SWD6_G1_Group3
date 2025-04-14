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

public class AboutOpenningClossingFunctionality {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    // Locators
    private final By aboutLink = By.xpath("//*[@id=\"navbarExample\"]/ul/li[3]/a");
    private final By closeButton = By.xpath("//*[@id=\"videoModal\"]//button[contains(@class, 'close')]"); // Adjust based on actual modal close button
    private final By modalContent = By.id("videoModal"); // Adjust based on actual modal ID

    @BeforeTest
    public void setup() throws IOException {
        // Setup report file
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/AboutOpenningClossingFunctionality.txt"));
        writer.println("Test Report - About Modal Functionality");

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
    public void TestAboutOpenningClossingFunctionality() {
        StringBuilder result = new StringBuilder();
        try {
            // Click About link
            wait.until(ExpectedConditions.elementToBeClickable(aboutLink)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));

            waitForPageLoad();

            // Click Close button
            wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalContent));

            waitForPageLoad();

            // Verify modal is closed
            boolean isModalClosed = driver.findElements(modalContent).isEmpty() ||
                    !driver.findElement(modalContent).isDisplayed();
            if (isModalClosed) {
                result.append("Test Passed: About modal opened and closed successfully.\n");
            } else {
                result.append("Test Failed: About modal did not close properly.\n");
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
            System.out.println("Report saved to AboutOpenningClossingFunctionality.txt");
        }
    }
}