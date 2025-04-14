import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.*;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        // Initialize report file
        final PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter("./Reports/AllTests.txt"));
            writer.println("Combined Test Report - All Test Cases");
            writer.println("=====================================");
        } catch (IOException e) {
            System.err.println("Failed to initialize report file: " + e.getMessage());
            return;
        }

        // List of test classes to run
        List<Class<?>> testClasses = Lists.newArrayList(
                DeleteOrder.class,
                LoginWithReport.class,
                PlaceOrderWithReport.class,
                PriceValidation.class,
                SignupWithReport.class,
                TestAddAllProductsToCart.class,
                TestSlideNext.class,
                NextPreviousButtonsFunctionality.class,
                AboutOpenningClossingFunctionality.class,
                Categories_Logo.class,          // Added from image
                ContactFormTest.class
        );

// List of report files to aggregate
        String[] reportFiles = {
                "reportDeleteItems.txt",
                "reportlogin.txt",
                "reportPlaceOrder.txt",
                "ReportTestTotalPrice.txt",
                "reportSignup.txt",
                "ReportTestAddAllProductsToCart.txt",
                "reportTestSlide.txt",
                "NextPreviousButtonsFunctionality.txt",
                "AboutOpenningClossingFunctionality.txt",
                "reportCategoriesLOGO.txt",      // Following the pattern: report + simplified name + .txt
                "reportContactFormTest.txt"
        };

        // Create TestNG instance
        TestNG testNG = new TestNG();

        // Set test classes
        testNG.setTestClasses(testClasses.toArray(new Class[0]));

        // Ensure tests run sequentially
        testNG.setParallel("none");

        // Custom listener to log test status
        testNG.addListener(new TestListenerAdapter() {
            @Override
            public void onTestSuccess(ITestResult result) {
                writer.println("Test Passed: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());
                writer.println("------------------------------------------------");
            }

            @Override
            public void onTestFailure(ITestResult result) {
                writer.println("Test Failed: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());
                writer.println("Error: " + (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error"));
                writer.println("------------------------------------------------");
            }

            @Override
            public void onTestSkipped(ITestResult result) {
                writer.println("Test Skipped: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());
                writer.println("------------------------------------------------");
            }
        });

        // Run tests
        testNG.run();

        // Append individual report contents
        writer.println("\nDetailed Test Reports");
        writer.println("=====================");
        for (String reportFile : reportFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader("./Reports/" + reportFile))) {
                writer.println("\nReport: " + reportFile);
                writer.println("---------------------");
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
                writer.println("---------------------");
            } catch (IOException e) {
                writer.println("\nReport: " + reportFile);
                writer.println("Error: Could not read report file - " + e.getMessage());
                writer.println("---------------------");
            }
        }

        // Close report file
        writer.close();
        System.out.println("Combined report saved to AllTests.txt");
    }
}