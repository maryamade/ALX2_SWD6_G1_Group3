import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

public class TestSlideNext {
    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    @BeforeTest
    public void setup() throws IOException {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportTestSlide.txt"));
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://demoblaze.com/index.html");
    }

    @BeforeMethod
    public String getActiveSlideImageSrc() {
        try {
            WebElement activeSlide = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.carousel-item.active img.d-block.img-fluid")
            ));
            return activeSlide.getAttribute("src");
        } catch (Exception e) {
            throw new RuntimeException("Failed to locate active slide image: " + e.getMessage());
        }
    }

    @Test
    public void TestSlideNext() {
        StringBuilder result = new StringBuilder();
        result.append("Carousel Next Button Test => ");

        try {
            // Step 1: Navigate to homepage and wait for load

            result.append("Navigated to homepage. \n");

            // Step 2: Locate the initial active slide and its image source
            String initialImageSrc = getActiveSlideImageSrc();
            result.append("Initial active slide image: \n").append(initialImageSrc).append(". ");
            Thread.sleep(2000);

            // Step 3: Locate and click the "Next" button (first time)
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"carouselExampleIndicators\"]/a[2]/span[1]"))).click();
            result.append("Clicked 'Next' button (first time). \n");

            // Step 4: Wait for the slide transition to complete
            Thread.sleep(4000); // Wait for carousel animation

            // Step 5: Check the new active slide's image source (first change)
            String newImageSrc = getActiveSlideImageSrc();
            result.append("New active slide image (first change): \n").append(newImageSrc).append(". ");

            // Step 6: Verify the slide has changed (first time)
            if (!initialImageSrc.equals(newImageSrc)) {
                result.append("Success: slide changed successfully (first time). \n");
            } else {
                result.append("Bug: Carousel slide did not change (first time). \n");
            }

            // Step 7: Store the current slide image source for the second check
            String secondInitialImageSrc = newImageSrc;
            Thread.sleep(2000);

            // Step 8: Click the "Next" button again (second time)
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"carouselExampleIndicators\"]/a[2]/span[1]"))).click();
            result.append("Clicked 'Next' button (second time). \n");

            // Step 9: Wait for the slide transition to complete
            Thread.sleep(4000); // Wait for carousel animation

            // Step 10: Check the new active slide's image source (second change)
            String secondNewImageSrc = getActiveSlideImageSrc();
            result.append("New active slide image (second change): \n").append(secondNewImageSrc).append(". ");

            // Step 11: Verify the slide has changed (second time)
            if (!secondInitialImageSrc.equals(secondNewImageSrc)) {
                result.append("Success: slide changed successfully (second time). \n");
            } else {
                result.append("Bug: Carousel slide did not change (second time). \n");
            }

        } catch (Exception e) {
            result.append("Exception: ").append(e.getMessage()).append(". ");
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
            System.out.println("Report saved to reportTestSlide.txt");
        }
    }
}