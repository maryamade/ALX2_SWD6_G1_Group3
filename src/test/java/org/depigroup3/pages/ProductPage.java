package org.depigroup3.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.depigroup3.utility.Const;


public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By addToCartButton = By.linkText("Add to cart");

    //Constructor for ProductPage
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Const.EXPLICIT_WAIT));
    }

    //Add the current product to cart
    public ProductPage addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        return this;
    }


}

