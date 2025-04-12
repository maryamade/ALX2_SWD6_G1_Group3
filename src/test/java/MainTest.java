import com.relevantcodes.extentreports.ExtentReports;
import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        // Initialize ExtentReports
        ExtentReports extentReport = new ExtentReports("./Reports/alltest.html", true);
        extentReport.addSystemInfo("Environment", "Demoblaze")
                .addSystemInfo("Tester", "QA Team");

        // List of test classes to run
        List<Class<?>> testClasses = Lists.newArrayList(
                DeleteOrder.class,
                LoginWithReport.class,
//                NextPreviousButtonsFunctionality.class,
//                PlaceOrderWithReport.class,
//                PriceValidation.class,
                TestAddAllProductsToCart.class
        );

        // Create TestNG instance
        TestNG testNG = new TestNG();
        testNG.setTestClasses(testClasses.toArray(new Class[0]));
        testNG.setParallel("none");

        // Run tests
        testNG.run();

        // Flush and close the report
        extentReport.flush();
        extentReport.close();

        System.out.println("HTML report generated at ./Reports/alltest.html");
    }
}