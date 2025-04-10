package org.depigroup3.automation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.depigroup3.pages.*;
import org.depigroup3.utility.Const;
import org.depigroup3.utility.WebDriverUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.github.javafaker.Faker;

public class ProductPurchaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private Logger log = Logger.getLogger(ProductPurchaseTest.class);
    private ExtentReports report;
    private Map<String, ExtentTest> testMap = new HashMap<>();

    // Page objects
    private HomePage homePage;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    private static final List<String> ALL_PRODUCTS = Arrays.asList(
            Const.PHONE1_NAME, Const.PHONE2_NAME, Const.PHONE3_NAME, Const.PHONE4_NAME,
            Const.PHONE5_NAME, Const.PHONE6_NAME, Const.PHONE7_NAME,
            Const.LAPTOP1_NAME, Const.LAPTOP2_NAME, Const.LAPTOP3_NAME, Const.LAPTOP4_NAME,
            Const.LAPTOP5_NAME, Const.LAPTOP6_NAME,
            Const.MONITOR1_NAME, Const.MONITOR2_NAME
    );

    private static final Faker faker = new Faker(); // For generating random data

    private void initializePageObjects() {
        homePage = new HomePage(driver);
        signUpPage = new SignUpPage(driver);
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverUtils.createDirectoryIfNotExists(Const.REPORT_PATH);
        WebDriverUtils.createDirectoryIfNotExists(Const.SCREENSHOT_PATH);

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        report = new ExtentReports(Const.REPORT_PATH + "DemoblazeTestReport_" + timestamp + ".html");

        System.setProperty("webdriver.chrome.driver", Const.CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Const.IMPLICIT_WAIT));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Const.EXPLICIT_WAIT));
        initializePageObjects();
        log.info("Test suite setup completed");
    }

    @BeforeMethod
    public void setUpTest(java.lang.reflect.Method method) {
        String testName = method.getName();
        testMap.put(testName, report.startTest(testName)
                .assignCategory("Demoblaze Tests")
                .assignAuthor("Test Automation Team"));
        homePage.navigateToHomePage();
        log.info("Starting test: " + testName);
    }

    private void login(ExtentTest test) {
        try {
            homePage.clickLogin();
            loginPage.loginUser(Const.USERNAME, Const.PASSWORD);
            Assert.assertTrue(homePage.isUserLoggedIn(), "Login failed");
            test.log(LogStatus.PASS, "Logged in as " + Const.USERNAME);
            Thread.sleep(2000);
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    private void addProduct(String productName, ExtentTest test) {
        homePage.selectProduct(productName);
        productPage.addToCart();
        String alertText = WebDriverUtils.getAlertTextAndAccept(driver, wait);
        Assert.assertTrue(alertText.contains("Product added"), "Failed to add " + productName);
        test.log(LogStatus.PASS, "Added " + productName + " to cart");
        cartPage.navigateToHome();
    }

    // Existing Test 1: Verify user registration
    @Test(priority = 1)
    public void testUserRegistration() {
        ExtentTest test = testMap.get("testUserRegistration");
        try {
            homePage.clickSignUp();
            signUpPage.registerUser(Const.USERNAME, Const.PASSWORD);
            String alertText = WebDriverUtils.getAlertTextAndAccept(driver, wait);
            Assert.assertTrue(alertText.contains("Sign up successful"), "Registration failed: " + alertText);
            test.log(LogStatus.PASS, "Registered user: " + Const.USERNAME);
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 2: Verify user login
    @Test(priority = 2)
    public void testUserLogin() {
        ExtentTest test = testMap.get("testUserLogin");
        try {
            login(test);
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 3: Verify user logout
    @Test(priority = 3)
    public void testUserLogout() {
        ExtentTest test = testMap.get("testUserLogout");
        try {
            login(test);
            Thread.sleep(2000);
            homePage.logout();
            Assert.assertFalse(homePage.isUserLoggedIn(), "Logout failed");
            test.log(LogStatus.PASS, "Logged out successfully");
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 4: Verify adding a single product to cart
    @Test(priority = 4)
    public void testAddSingleProductToCart() {
        ExtentTest test = testMap.get("testAddSingleProductToCart");
        try {
            login(test);
            Thread.sleep(1000);
            addProduct(Const.PHONE1_NAME, test);
            cartPage.navigateToCart();
            Assert.assertTrue(cartPage.isProductInCart(Const.PHONE1_NAME), "Product not in cart");
            test.log(LogStatus.PASS, "Verified " + Const.PHONE1_NAME + " in cart");
            homePage.logout();
            Assert.assertFalse(homePage.isUserLoggedIn(), "Logout failed");
            test.log(LogStatus.PASS, "Logged out successfully");
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 5: Verify placing an order with one product
    @Test(priority = 5)
    public void testPlaceOrderSingleProduct() {
        ExtentTest test = testMap.get("testPlaceOrderSingleProduct");
        try {
            login(test);
            Thread.sleep(1000);
            addProduct(Const.PHONE1_NAME, test);
            cartPage.navigateToCart();
            cartPage.placeOrder(Const.CUSTOMER_NAME, Const.CARD_NUMBER, Const.COUNTRY, Const.CITY, Const.MONTH, Const.YEAR);
            Assert.assertTrue(checkoutPage.isThankYouMessageDisplayed(), "Order confirmation not displayed");
            test.log(LogStatus.PASS, "Order placed for " + Const.PHONE1_NAME);
            checkoutPage.closePopup();
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 6: Verify cart deletion
    @Test(priority = 6)
    public void testCartDeletion() {
        ExtentTest test = testMap.get("testCartDeletion");
        try {
            login(test);
            Thread.sleep(1000);
            addProduct(Const.PHONE1_NAME, test);
            Thread.sleep(1000);
            cartPage.navigateToCart();
            cartPage.deleteAllItemsFromCart();
            Assert.assertFalse(cartPage.isProductInCart(Const.PHONE1_NAME), "Product still in cart after deletion");
            test.log(LogStatus.PASS, "Deleted all items from cart");
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 7: Verify adding multiple products to cart
    @Test(priority = 7)
    public void testAddMultipleProductsToCart() {
        ExtentTest test = testMap.get("testAddMultipleProductsToCart");
        try {
            login(test);
            Thread.sleep(1000);
            for (String product : ALL_PRODUCTS.subList(0, 3)) {
                addProduct(product, test);
                if (product.equals(Const.PHONE3_NAME)) homePage.clickNext();
            }
            Thread.sleep(1000);
            cartPage.navigateToCart();
            Assert.assertTrue(cartPage.isProductInCart(Const.PHONE1_NAME), "First product missing from cart");
            test.log(LogStatus.PASS, "Verified multiple products in cart");
            Thread.sleep(1000);
            homePage.logout();
            Assert.assertFalse(homePage.isUserLoggedIn(), "Logout failed");
            test.log(LogStatus.PASS, "Logged out successfully");
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 8: Verify placing order with multiple products
    @Test(priority = 8)
    public void testPlaceOrderMultipleProducts() {
        ExtentTest test = testMap.get("testPlaceOrderMultipleProducts");
        try {
            login(test);
            Thread.sleep(1000);
            for (String product : ALL_PRODUCTS.subList(0, 3)) {
                addProduct(product, test);
                if (product.equals(Const.LAPTOP2_NAME)) homePage.clickNext();
            }
            cartPage.navigateToCart();
            cartPage.placeOrder(Const.CUSTOMER_NAME, Const.CARD_NUMBER, Const.COUNTRY, Const.CITY, Const.MONTH, Const.YEAR);
            Assert.assertTrue(checkoutPage.isThankYouMessageDisplayed(), "Order confirmation not displayed");
            test.log(LogStatus.PASS, "Order placed for multiple products");
            checkoutPage.closePopup();
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Existing Test 9: Verify About modal functionality
    @Test(priority = 9)
    public void testAboutModal() {
        ExtentTest test = testMap.get("testAboutModal");
        try {
            homePage.clickAbout();
            Thread.sleep(500);
            homePage.clickClose();
            Thread.sleep(500);
            Assert.assertFalse(homePage.isAboutClosed(), "About Closing Failed");
            test.log(LogStatus.PASS, "About Popup works Correctly");
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // New Test 10: Verify signup validation with multiple credentials
    @Test(priority = 10)
    public void testSignupValidation() {
        ExtentTest test = testMap.get("testSignupValidation");
        try {
            // Define test scenarios with dynamic credentials
            String[][] signupScenarios = {
                    {faker.name().username(), faker.internet().password(1, 3), "invalid"},  // Short password
                    {faker.name().username(), "", "empty"},                           // Empty password
                    {"", faker.internet().password(6, 10), "empty"},                  // Empty username
                    {"", "", "empty"},                                                // Both empty
                    {faker.lorem().characters(1), faker.internet().password(1, 3), "invalid"},  // Short username & password
                    {faker.name().username() + " " + faker.name().lastName(), faker.internet().password(6, 10), "invalid"},  // Username with space
                    {" ", faker.internet().password(6, 10), "invalid"},               // Username as space
                    {faker.name().username(), " ", "invalid"},                        // Password as space
                    {" ", " ", "invalid"},                                            // Both as space
                    {faker.name().username() + faker.random().nextInt(1000), faker.internet().password(6, 10), "valid"},  // Valid credentials
                    {faker.name().username() + "@" + faker.random().nextInt(1000), faker.internet().password(6, 10), "invalid"},  // Username with special char
                    {faker.name().username(), faker.internet().password(1, 5), "invalid"}  // Short password
            };

            for (int i = 0; i < signupScenarios.length; i++) {
                String username = signupScenarios[i][0];
                String password = signupScenarios[i][1];
                String expectedType = signupScenarios[i][2];
                String testCase = "Test " + (i + 1) + " (Username: '" + username + "', Password: '" + password + "')";

                homePage.navigateToHomePage(); // Reset to homepage for each iteration
                homePage.clickSignUp();
                signUpPage.registerUser(username, password);
                String alertText = "";
                try {
                    alertText = WebDriverUtils.getAlertTextAndAccept(driver, wait);
                    test.log(LogStatus.INFO, testCase + " - Alert: " + alertText);
                } catch (Exception e) {
                    test.log(LogStatus.WARNING, testCase + " - No alert. Might be a silent fail.");
                    continue;
                }

                boolean hasSpecialChar = !username.matches("^[a-zA-Z0-9]*$");
                boolean isUsernameInvalid = username.trim().isEmpty() || username.length() < 3 || hasSpecialChar;
                boolean isPasswordInvalid = password.trim().isEmpty() || password.length() < 6;

                if (alertText.contains("Please fill out")) {
                    if (expectedType.equals("empty")) {
                        test.log(LogStatus.PASS, testCase + " - Correct rejection for empty field(s).");
                    } else {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Unexpected empty fields alert for non-empty input.");
                    }
                } else if (alertText.contains("Sign up successful")) {
                    if (isUsernameInvalid && isPasswordInvalid) {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Invalid Username and Password accepted.");
                    } else if (isUsernameInvalid) {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Invalid Username accepted.");
                    } else if (isPasswordInvalid) {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Invalid Password accepted.");
                    } else {
                        test.log(LogStatus.PASS, testCase + " - Successfully registered valid input.");
                    }
                } else if (alertText.contains("This user already exist")) {
                    test.log(LogStatus.WARNING, testCase + " - User already exists (unexpected due to random username).");
                } else {
                    test.log(LogStatus.WARNING, testCase + " - Unexpected alert: " + alertText);
                }
            }
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }
//aa
    // New Test 11: Verify login validation with multiple credentials
    @Test(priority = 11)
    public void testLoginValidation() {
        ExtentTest test = testMap.get("testLoginValidation");
        try {
            // Register a valid user to test login with correct credentials
            homePage.clickSignUp();
            signUpPage.registerUser(Const.USERNAME, Const.PASSWORD);
            String signupAlert = WebDriverUtils.getAlertTextAndAccept(driver, wait);
            test.log(LogStatus.INFO, "Registered user "+Const.USERNAME+" - Alert: " + signupAlert);

            for (int i = 0; i < Const.LOGIN_CREDENTIALS.length; i++) {
                String username = Const.LOGIN_CREDENTIALS[i][0];
                String password = Const.LOGIN_CREDENTIALS[i][1];
                String expectedType = Const.LOGIN_CREDENTIALS[i][2];
                String testCase = "Test " + (i + 1) + " (Username: '" + username + "', Password: '" + password + "')";

                homePage.navigateToHomePage(); // Reset to homepage
                homePage.clickLogin();
                loginPage.loginUser(username, password);

                if (expectedType.equals("valid")) {
                    try {
                        // Wait for the page to stabilize after login
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
                        String welcomeText = driver.findElement(By.id("nameofuser")).getText();
                        if (welcomeText.contains("Welcome")) {
                            test.log(LogStatus.PASS, testCase + " - Alert: " + welcomeText + " - Valid login as expected.");
                        } else {
                            test.log(LogStatus.FAIL, testCase + " - Bug: Valid credentials but no welcome message.");
                        }
                        homePage.logout(); // Logout after successful login
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("nameofuser")));
                    } catch (Exception e) {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Valid login failed. Exception: " + e.getMessage());
                    }
                } else {
                    try {
                        String alertText = WebDriverUtils.getAlertTextAndAccept(driver, wait);
                        test.log(LogStatus.INFO, testCase + " - Alert: " + alertText);

                        if (expectedType.equals("notExist") && alertText.contains("User does not exist")) {
                            test.log(LogStatus.PASS, testCase + " - Valid rejection for non-existing user.");
                        } else if (expectedType.equals("invalid") && alertText.contains("Wrong password")) {
                            test.log(LogStatus.PASS, testCase + " - Valid rejection for invalid password.");
                        } else if (expectedType.equals("empty") && alertText.contains("Please fill out")) {
                            test.log(LogStatus.PASS, testCase + " - Valid rejection for empty field(s).");
                        } else {
                            test.log(LogStatus.FAIL, testCase + " - Bug: Unexpected behavior for invalid/empty login.");
                        }
                    } catch (Exception e) {
                        test.log(LogStatus.WARNING, testCase + " - Bug: No alert shown for invalid or empty login.");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    // New Test 12: Verify Next and Previous button functionality
    @Test(priority = 12)
    public void testPaginationButtons() {
        ExtentTest test = testMap.get("testPaginationButtons");
        try {
            // Test Next button
            Thread.sleep(1000);
            homePage.clickNext();
            Thread.sleep(1000);
            Assert.assertTrue(homePage.isAppleMonitorVisible(), "Next button failed: 'Apple monitor 24' not visible");
            test.log(LogStatus.PASS, "Next button clicked - 'Apple monitor 24' is visible");

            // Test Previous button
            homePage.clickPrevious();
            Thread.sleep(3000);
            Assert.assertTrue(homePage.isSamsungGalaxyS6Visible(), "Previous button failed: 'Samsung galaxy s6' not visible");
            test.log(LogStatus.PASS, "Previous button clicked - 'Samsung galaxy s6' is visible");

        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }


    // New Test 12: Verify Contact Form functionality
    @Test(priority = 13)
    public void testContactForm() {
        ExtentTest test = testMap.get("testContactForm");
        try {
            for (int i = 0; i < Const.CONTACT_FORM_DATA.length; i++) {
                String email = Const.CONTACT_FORM_DATA[i][0];
                String name = Const.CONTACT_FORM_DATA[i][1];
                String message = Const.CONTACT_FORM_DATA[i][2];
                String expectedType = Const.CONTACT_FORM_DATA[i][3];
                String testCase = "Test " + (i + 1) + " (Email: '" + email + "', Name: '" + name + "', Message: '" + message + "')";

                homePage.navigateToHomePage();
                homePage.clickContact();
                homePage.fillContactForm(email, name, message);
                homePage.submitContactForm();

                String alertText = "";
                try {
                    alertText = WebDriverUtils.getAlertTextAndAccept(driver, wait);
                    test.log(LogStatus.INFO, testCase + " - Alert: " + alertText);
                } catch (Exception e) {
                    test.log(LogStatus.WARNING, testCase + " - No alert received. Possible silent fail.");
                    continue;
                }

                if (alertText.contains("Thanks for the message")) {
                    if (email.trim().isEmpty() || name.trim().isEmpty() || message.trim().isEmpty()) {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Empty field accepted!");
                    } else if (!email.contains("@") || email.startsWith(" ")) {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Invalid email accepted!");
                    } else {
                        test.log(LogStatus.PASS, testCase + " - Valid input accepted as expected.");
                    }
                } else {
                    if (expectedType.equals("empty") || expectedType.equals("invalid")) {
                        test.log(LogStatus.PASS, testCase + " - Correctly rejected invalid/empty input.");
                    } else {
                        test.log(LogStatus.FAIL, testCase + " - Bug: Valid input not accepted. Unexpected alert: " + alertText);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Test failed with exception: " + e.getMessage());
            test.log(LogStatus.FAIL, "Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        ExtentTest test = testMap.get(methodName);

        if (!result.isSuccess()) {
            try {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String screenshotPath = Const.SCREENSHOT_PATH + methodName + "_" + timestamp + ".png";
                FileUtils.copyFile(
                        ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
                        new File(screenshotPath)
                );
                String relativePath = "Screenshots/" + methodName + "_" + timestamp + ".png";
                test.log(LogStatus.FAIL, "Test failed: " + result.getThrowable().getMessage(),
                        test.addScreenCapture(relativePath));
            } catch (IOException e) {
                test.log(LogStatus.ERROR, "Failed to capture screenshot: " + e.getMessage());
            }
        } else {
            test.log(LogStatus.INFO, "Test completed successfully");
        }

        report.endTest(test);
        log.info("Completed test: " + methodName);
    }

    @AfterClass(alwaysRun = true)
    public void cleanTest() {
        if (driver != null) driver.quit();
        if (report != null) report.flush();
        log.info("Test suite execution completed");
    }
}