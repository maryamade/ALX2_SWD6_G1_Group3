package org.depigroup3.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.depigroup3.utility.Const;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By HomeButton = By.xpath("//*[@id=\"navbarExample\"]/ul/li[1]/a");
    private final By placeOrderButton = By.xpath("//button[contains(text(),'Place Order')]");
    private final By nameField = By.id("name");
    private final By cardField = By.id("card");
    private final By countryField = By.id("country");
    private final By cityField = By.id("city");
    private final By monthField = By.id("month");
    private final By yearField = By.id("year");
    private final By purchaseButton = By.xpath("//button[contains(text(),'Purchase')]");
    private final By cartLink = By.id("cartur");


    //Constructor for CartPage
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Const.EXPLICIT_WAIT));
    }


    //Navigate to the cart page
    public CartPage navigateToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        return this;
    }



    //Navigate to the Home page
    public CartPage navigateToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(HomeButton)).click();
        return this;
    }

    // Verify if a product is in the cart
    public boolean isProductInCart(String productName) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//td[text()='" + productName + "']"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public CartPage placeOrder(String name, String cardNumber, String country, String city, String month, String year) {
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
        driver.findElement(cardField).sendKeys(cardNumber);
        driver.findElement(countryField).sendKeys(country);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(monthField).sendKeys(month);
        driver.findElement(yearField).sendKeys(year);

        driver.findElement(purchaseButton).click();
        return this;
    }

    //Click on all "Delete" links in the cart
    public void deleteAllItemsFromCart() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            while (true) {
                List<WebElement> deleteLinks = driver.findElements(By.cssSelector("a[onclick^='deleteItem']"));
                if (deleteLinks.isEmpty()) {
                    break;
                }

                WebElement firstDeleteLink = deleteLinks.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(firstDeleteLink));

                String itemDetails = firstDeleteLink.getAttribute("outerHTML");
                firstDeleteLink.click();

                System.out.println("Deleted item: " + itemDetails);

                // Wait for any delete link to disappear
                wait.until(ExpectedConditions.stalenessOf(firstDeleteLink));
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }






}
