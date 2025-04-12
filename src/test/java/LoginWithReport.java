import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Duration;

public class LoginWithReport {

    WebDriver driver;
    PrintWriter writer;
    WebDriverWait wait;

    String[][] loginData = {
            {"123456", "abcdef", "invalid"}, // Wrong password.
            {"wrongUser", "abcdef", "notExist"}, // User does not exist.
            {"123456", "wrongPass", "invalid"}, // Wrong password.
            {"", "", "empty"}, // Please fill out Username and Password.
            {"123456", "", "empty"}, // Please fill out Username and Password.
            {"", "abcdef", "empty"}, // Please fill out Username and Password.
            {"MaryamSara", "1234567", "valid"}, // Correct
            {"wrongUser2", "abcdef", "notExist"}, // User does not exist.
    };

    @BeforeTest
    public void setupReport() throws Exception {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        writer = new PrintWriter(new FileWriter("./Reports/reportlogin.txt"));
    }

    @BeforeMethod
    public void setupDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    private boolean isUserLogin()
    {
        try {
            WebElement userNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
            String usernameText = userNameElement.getText().trim();
            return !usernameText.isEmpty() && usernameText.startsWith("Welcome");
        }catch (Exception e){
            return false;
        }
    }

    @Test
    public void loginTest() throws Exception {
        // Loop through loginData like in SignUp
        for (int i = 0; i < loginData.length; i++) {
            String username = loginData[i][0];
            String password = loginData[i][1];
            String expectedType = loginData[i][2];

            StringBuilder result = new StringBuilder();
            result.append("Test ").append(i + 1).append(" => ");

            try {
                driver.get("https://demoblaze.com/index.html");

                // Open login modal
                wait.until(ExpectedConditions.elementToBeClickable(By.id("login2"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).clear();
                driver.findElement(By.id("loginusername")).sendKeys(username);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword"))).clear();
                driver.findElement(By.id("loginpassword")).sendKeys(password);

                // Click login button
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='logInModal']/div/div/div[3]/button[2]"))).click();

                // Check for valid login
                if (expectedType.equals("valid")) {
                    try {
                        // Wait for the welcome message to appear
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
                        String welcomeText = driver.findElement(By.id("nameofuser")).getText();
                        if (welcomeText.contains("Welcome")) {
                            result.append("Valid login as expected.\n");

                            // Logout after successful login
                            driver.findElement(By.id("logout2")).click();

                            if(!isUserLogin()) {
                                result.append("Logout successful. \n");
                            }else{
                                result.append("Bug: Logout Failed! user still logged in. \n");
                            }

                        } else {
                            result.append("Bug: Valid credentials but no welcome message.\n");
                        }
                    } catch (Exception e) {
                        result.append("Bug: Valid login failed. Exception: ").append(e.getMessage()).append("\n");
                    }
                } else {
                    try {
                        // Check for alerts or messages
                        wait.until(ExpectedConditions.alertIsPresent());
                        String alertText = driver.switchTo().alert().getText();
                        result.append("Alert: ").append(alertText).append("\n");
                        driver.switchTo().alert().accept();

                        // Check for specific rejection messages
                        if (expectedType.equals("notExist")) {
                            result.append("Valid rejection for non-existing user.\n");  // Correct rejection message
                        } else if (expectedType.equals("invalid")) {
                            result.append("Bug: Invalid Password accepted.\n");
                        } else if (expectedType.equals("empty")) {
                            result.append("Bug: Empty fields allowed.\n");
                        }

                    } catch (Exception e) {
                        result.append("Bug: No alert shown for invalid or empty login.\n");
                    }
                }

            } catch (Exception e) {
                result.append("Test ").append(i + 1).append(" => Bug: Exception during test: ").append(e.getMessage()).append("\n");
            }

            result.append("------------------------------------------------\n");

            // Print to console and file
            System.out.println(result.toString());
            writer.write(result.toString());
        }
    }

    @AfterMethod
    public void tearDownDriver() {
        // Ensure the driver is quit after each test method
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void closeReport() {
        // Close the writer after all tests are done
        if (writer != null) {
            writer.close();
            System.out.println("Report saved to reportlogin.txt");
        }
    }
}
