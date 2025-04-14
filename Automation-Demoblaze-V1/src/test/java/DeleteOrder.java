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
import java.util.List;

public class DeleteOrder {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    @BeforeTest
    public void setup() throws IOException {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportDeleteItems.txt"));
        writer.println("Test Report - Delete Items");

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
    public void testCategories() {
        StringBuilder result = new StringBuilder();
        try {
            // ==== Test Phones ====
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            Thread.sleep(1500);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click(); // Logo
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));

            // ==== Test Laptops ====
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sony vaio i5"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            Thread.sleep(1500);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click(); // Logo
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));

            // ==== Test Monitors ====
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Monitors"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Apple monitor 24"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            Thread.sleep(1500);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click(); // Logo
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cat")));

            // ==== Go to Cart ====
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
            waitForPageLoad();

            // ==== Delete all products ====
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));
            Thread.sleep(1500);
            while (true) {
                List<?> deleteButtons = driver.findElements(By.xpath("//tbody[@id='tbodyid']//a[text()='Delete']"));
                if (deleteButtons.isEmpty()) {
                    result.append("All items deleted from the cart.\n");
                    break;
                }

                deleteButtons = driver.findElements(By.xpath("//tbody[@id='tbodyid']//a[text()='Delete']"));
                deleteButtons.get(0).toString(); // just access to avoid stale
                driver.findElement(By.xpath("//tbody[@id='tbodyid']//a[text()='Delete']")).click();
                Thread.sleep(1500);
            }

            result.append("Test Passed: Navigated, added to cart, and cleared cart.\n");

        } catch (Exception e) {
            result.append("Test Failed: ").append(e.getMessage()).append("\n");
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
            System.out.println("Report saved to reportDeleteAllItems.txt");
        }
    }
}
