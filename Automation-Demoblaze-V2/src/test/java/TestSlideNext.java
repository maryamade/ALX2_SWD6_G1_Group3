import java.time.Duration;

import org.example.Constants;
import org.example.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.Test;


public class TestSlideNext {
    WebDriver driver;
    WebDriverWait wait;
    ExtentTest test;

    // Constructor to accept ExtentTest
    public TestSlideNext(ExtentTest test) {
        this.test = test;
    }

 
    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Constants.WEBSITE_URL);
        test.log(LogStatus.INFO, "Browser setup and navigated to Demoblaze");
        Methods.smartWait(driver);
    }

    @BeforeMethod
    public String getActiveSlideImageSrc() {
        try {
            WebElement activeSlide = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.carousel-item.active img.d-block.img-fluid")
            ));
            return activeSlide.getAttribute("src");
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Failed to locate active slide image: " + e.getMessage());
            Assert.fail("Failed to locate active slide image: " + e.getMessage());
            return null;
        }
    }

   // @Test
    public void SlideNextTest() {
        try {
            test.log(LogStatus.INFO, "Carousel Next Button Test => Navigated to homepage.");
            String initialImageSrc = getActiveSlideImageSrc();
            test.log(LogStatus.INFO, "Initial active slide image: " + initialImageSrc);
           // Thread.sleep(2000);
            Methods.smartWait(driver);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"carouselExampleIndicators\"]/a[2]/span[1]"))).click();
            test.log(LogStatus.INFO, "Clicked 'Next' button (first time).");

         //   Thread.sleep(4000);
            Methods.smartWait(driver);

            String newImageSrc = getActiveSlideImageSrc();
            test.log(LogStatus.INFO, "New active slide image (first change): " + newImageSrc);

            Assert.assertNotEquals(initialImageSrc, newImageSrc, "Carousel slide did not change (first time).");
            test.log(LogStatus.PASS, "Slide changed successfully (first time).");

            String secondInitialImageSrc = newImageSrc;
          //  Thread.sleep(2000);
            Methods.smartWait(driver);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"carouselExampleIndicators\"]/a[2]/span[1]"))).click();
            test.log(LogStatus.INFO, "Clicked 'Next' button (second time).");

            Methods.smartWait(driver);
          //  Thread.sleep(4000);

            String secondNewImageSrc = getActiveSlideImageSrc();
            test.log(LogStatus.INFO, "New active slide image (second change): " + secondNewImageSrc);

            Assert.assertNotEquals(secondInitialImageSrc, secondNewImageSrc, "Carousel slide did not change (second time).");
            test.log(LogStatus.PASS, "Slide changed successfully (second time).");
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

        @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}