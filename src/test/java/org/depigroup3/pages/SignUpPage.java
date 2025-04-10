package org.depigroup3.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.depigroup3.utility.Const;


public class SignUpPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("sign-username");
    private final By passwordField = By.id("sign-password");
    private final By signUpButton = By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]");

    //Constructor for SignUpPage
    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Const.EXPLICIT_WAIT));
    }

    //Register a new user
    public SignUpPage registerUser(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(signUpButton).click();
        return this;
    }


}
