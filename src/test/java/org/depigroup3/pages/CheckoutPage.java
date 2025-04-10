package org.depigroup3.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.depigroup3.utility.Const;


public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By confirmButton = By.xpath("//button[contains(@class, 'confirm btn btn-lg btn-primary')]");
    private final By closeButton = By.xpath("//button[text()='Purchase']//preceding-sibling::button");
    private final By thankYouMessage = By.xpath("//h2[text()='Thank you for your purchase!']");

    //Constructor for CheckoutPage
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Const.EXPLICIT_WAIT));
    }

    //Confirm the checkout
    public CheckoutPage confirmCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
        return this;
    }

    //Close the checkout popup
    public CheckoutPage closePopup() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
        return this;
    }


    // Verify if the thank you message is displayed
    public boolean isThankYouMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(thankYouMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
