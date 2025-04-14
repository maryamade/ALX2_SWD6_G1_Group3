import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AllTests {
    ExtentReports report;

    @BeforeSuite
    public void setupReport() {
        report = new ExtentReports("./Reports/TestReport.html", false);
        report.addSystemInfo("User Name", "Team_G3");
        report.addSystemInfo("Test Suite", "Demoblaze Tests");
        report.addSystemInfo("Environment", "Demoblaze");
        report.addSystemInfo("Browser", "ChromeDriver");
        report.addSystemInfo("Host Name", "DEPI");
    }

    @Test(priority = 1)
    public void testSignupWithReport() {
        ExtentTest test = report.startTest("Signup Test");
        SignupWithReport signupTest = new SignupWithReport(test);
        try {
            signupTest.setup();
            signupTest.testSignupValidation();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Signup test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            signupTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 2)
    public void testLoginWithReport() {
        ExtentTest test = report.startTest("Login Test");
        LoginWithReport loginTest = new LoginWithReport(test);
        try {
            loginTest.setup();
            loginTest.testLogin();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Login test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            loginTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 3)
    public void testCategoriesLogo() {
        ExtentTest test = report.startTest("Categories Logo Test");
        Categories_Logo categoriesLogoTest = new Categories_Logo(test);
        try {
            categoriesLogoTest.setup();
            categoriesLogoTest.testCategoriesLogo();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Categories logo test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            categoriesLogoTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 4)
    public void TestAddAllProductsToCart() {
        ExtentTest test = report.startTest("Add ALL products Test");
        TestAddAllProductsToCart TestAddAllProductsToCart = new TestAddAllProductsToCart(test);
        try {
            TestAddAllProductsToCart.setup();
            TestAddAllProductsToCart.testAddProductsToCart();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Add Products ToCart test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            TestAddAllProductsToCart.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 5)
    public void PlaceOrder() {
        ExtentTest test = report.startTest("Place Order");
        PlaceOrderWithReport placeOrder = new PlaceOrderWithReport(test);
        try {
            placeOrder.setup();
            placeOrder.testPlaceOrder();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Place Order Failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            placeOrder.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 6)
    public void testPriceValidation() {
        ExtentTest test = report.startTest("Price Validation Test");
        PriceValidation priceValidationTest = new PriceValidation(test);
        try {
            priceValidationTest.setup();
            priceValidationTest.testCartTotal();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Price validation test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            priceValidationTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 7)
    public void testDeleteOrder() {
        ExtentTest test = report.startTest("Delete Order Test");
        DeleteOrder deleteOrderTest = new DeleteOrder(test);
        try {
            deleteOrderTest.setup();
            deleteOrderTest.testDeleteOrder();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Delete order test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            deleteOrderTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 8)
    public void testSlideShowNext() {
        ExtentTest test = report.startTest("Carousel Next Button Test");
        TestSlideNext slideTest = new TestSlideNext(test);
        try {
            slideTest.setup();
            slideTest.SlideNextTest();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Carousel slide test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            slideTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 9)
    public void testContactForm() {
        ExtentTest test = report.startTest("Contact Form Test");
        ContactFormTest contactFormTest = new ContactFormTest(test);
        try {
            contactFormTest.setup();
            contactFormTest.TestContactForm();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Contact form test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            contactFormTest.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 10)
    public void testNextPreviousButtonsFunctionality() {
        ExtentTest test = report.startTest("NextPreviousButtons Buttons Test");
        NextPreviousButtonsFunctionality NextPreviousButtons = new NextPreviousButtonsFunctionality(test);
        try {
            NextPreviousButtons.setup();
            NextPreviousButtons.testNextPreviousButtonsFunctionality();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "NextPreviousButtons buttons test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            NextPreviousButtons.tearDown();
            report.endTest(test);
        }
    }

    @Test(priority = 11)
    public void testAboutOpeningClosingFunctionality() {
        ExtentTest test = report.startTest("About Modal Test");
        AboutOpenningClossingFunctionality aboutTest = new AboutOpenningClossingFunctionality(test);
        try {
            aboutTest.setup();
            aboutTest.TestAboutOpenningClossingFunctionality();
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "About modal test failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            aboutTest.tearDown();
            report.endTest(test);
        }
    }



    @AfterSuite
    public void closeReport() {
        report.flush();
        report.close();
        System.out.println("Report saved to TestReport.html");

    }
}