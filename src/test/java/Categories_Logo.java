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



public class Categories_Logo {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;


    @BeforeTest
    public void setup() throws IOException {
        // Setup report file
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportCategories_Logo.txt"));
        writer.println("Test Report - Categories and Logo Navigation");

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
    public void testCategories() {
        StringBuilder result = new StringBuilder();
        try {
            // Test Phones
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
//            Thread.sleep(500);
//            waitForPageLoad();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
            Thread.sleep(2000);
//            waitForPageLoad();
            // Click logo and wait for home to reload
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();//contcont
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
//            Thread.sleep(2000);
//            waitForPageLoad();
            // Test Laptops
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
//            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i5"))).click();
            Thread.sleep(2000);
//            waitForPageLoad();
            // Click logo and wait for home to reload
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
//            Thread.sleep(2000);

            // Test Monitors
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Monitors"))).click();
//            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Apple monitor 24"))).click();
            Thread.sleep(2000);
//            waitForPageLoad();
            // Click logo and wait for home to reload
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));
//            Thread.sleep(2000);
            result.append("Test Passed: Navigated through all categories and returned home using logo.\n");
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
            System.out.println("Report saved to reportContact_Logo.txt");
        }
    }
}
