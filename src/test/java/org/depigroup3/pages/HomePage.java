package org.depigroup3.pages;

import org.depigroup3.utility.Const;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By userNameLink = By.id("nameofuser");
    private final By aboutPopup = By.id("videoModal");
    private final By signUpLink = By.id("signin2");
    private final By loginLink = By.id("login2");
    private final By logoutLink = By.id("logout2");
    private final By NextButton = By.id("next2");
    private final By PreviousButton = By.id("prev2");
    private final By closey = By.xpath("//*[@id=\"videoModal\"]/div/div/div[1]/button");
    private final By abouty = By.xpath("//*[@id=\"navbarExample\"]/ul/li[3]/a");

    // New locators for contact form
    private final By contactLink = By.xpath("//a[text()='Contact']");
    private final By recipientEmailField = By.id("recipient-email");
    private final By recipientNameField = By.id("recipient-name");
    private final By messageTextField = By.id("message-text");
    private final By sendMessageButton = By.xpath("//button[text()='Send message']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Const.EXPLICIT_WAIT));
    }

    public HomePage navigateToHomePage() {
        driver.get(Const.BASE_URL);
        driver.manage().window().maximize();
        return this;
    }

    public HomePage clickSignUp() {
        wait.until(ExpectedConditions.elementToBeClickable(signUpLink)).click();
        return this;
    }

    public HomePage clickClose() {
        wait.until(ExpectedConditions.elementToBeClickable(closey)).click();
        return this;
    }

    public HomePage clickAbout() {
        wait.until(ExpectedConditions.elementToBeClickable(abouty)).click();
        return this;
    }

    public HomePage clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(NextButton)).click();
        return this;
    }

    public HomePage clickPrevious() {
        wait.until(ExpectedConditions.elementToBeClickable(PreviousButton)).click();
        return this;
    }

    public HomePage clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
        return this;
    }

    public HomePage selectProduct(String productName) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(productName))).click();
        return this;
    }

    public HomePage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        return this;
    }

    public boolean isUserLoggedIn() {
        try {
            // Wait for the element to be visible and re-locate it to avoid staleness
            WebElement userNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(userNameLink));
            String userNameText = userNameElement.getText().trim();
            return !userNameText.isEmpty() && userNameText.startsWith("Welcome");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAboutClosed() {
        try {
            String displayStyle = wait.until(ExpectedConditions.visibilityOfElementLocated(aboutPopup))
                    .getAttribute("style");
            return displayStyle.contains("display: none");
        } catch (Exception e) {
            return false;
        }
    }



    public boolean isAppleMonitorVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(PreviousButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSamsungGalaxyS6Visible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(NextButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    // New methods for contact form
    public HomePage clickContact() {
        wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();
        return this;
    }

    public HomePage fillContactForm(String email, String name, String message) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(recipientEmailField)).clear();
        driver.findElement(recipientEmailField).sendKeys(email);

        wait.until(ExpectedConditions.visibilityOfElementLocated(recipientNameField)).clear();
        driver.findElement(recipientNameField).sendKeys(name);

        wait.until(ExpectedConditions.visibilityOfElementLocated(messageTextField)).clear();
        driver.findElement(messageTextField).sendKeys(message);

        return this;
    }

    public HomePage submitContactForm() {
        wait.until(ExpectedConditions.elementToBeClickable(sendMessageButton)).click();
        return this;
    }


}