import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Duration;

public class ContactFormTest {

    WebDriver driver;
    WebDriverWait wait;
    PrintWriter writer;

    String[][] contactData = {
            {"", "", "", "empty"},                           //1
            {"user@example.com", "", "", "empty"},           //2
            {"", "Test User", "", "empty"},                  //3
            {"", "", "This is a message", "empty"},          //4
            {"user@example.com", "Test", "Message", "valid"},//5
            {"invalidEmail", "Test", "Message", "invalid"},  //6
            {"test@test.com", " ", "hi", "invalid"},         //7
            {" ", "Test", "Hi", "invalid"},                  //8
    };

    @BeforeTest
    public void setupReport() throws Exception {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportContact.txt"));
    }

    @BeforeMethod
    public void setupDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testContactForm() {
        for (int i = 0; i < contactData.length; i++) {
            String email = contactData[i][0];
            String name = contactData[i][1];
            String message = contactData[i][2];
            String expected = contactData[i][3];

            StringBuilder result = new StringBuilder();

            try {
                driver.get("https://demoblaze.com/index.html");

                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Contact']"))).click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-email"))).clear();
                driver.findElement(By.id("recipient-email")).sendKeys(email);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-name"))).clear();
                driver.findElement(By.id("recipient-name")).sendKeys(name);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-text"))).clear();
                driver.findElement(By.id("message-text")).sendKeys(message);

                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Send message']"))).click();

                wait.until(ExpectedConditions.alertIsPresent());
                String alertText = driver.switchTo().alert().getText();
                driver.switchTo().alert().accept();

                result.append("Test ").append(i + 1)
                        .append(" => Alert: ").append(alertText).append("\n");

                if (alertText.contains("Thanks for the message")) {
                    if (email.trim().isEmpty() || name.trim().isEmpty() || message.trim().isEmpty()) {
                        result.append("Bug: Empty field accepted!\n");
                    } else if (!email.contains("@") || email.startsWith(" ")) {
                        result.append("Bug: Invalid email accepted!\n");
                    } else {
                        result.append("Valid input accepted as expected.\n");
                    }
                } else {
                    result.append("Unexpected alert or no confirmation message.\n");
                }

            } catch (Exception e) {
                result.append("Test ").append(i + 1).append(" => Exception: ").append(e.getMessage()).append("\n");
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
            System.out.println("Report saved to reportContact.txt");
        }
    }
}
