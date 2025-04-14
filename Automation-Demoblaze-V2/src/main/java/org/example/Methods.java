package org.example;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Methods {


        public static void smartWait(WebDriver driver) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

                // Wait for the page to load completely
                wait.until(webDriver -> 
                        ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
                );

                // Wait for jQuery AJAX calls to complete (if jQuery is present)
                try {
                        wait.until(webDriver -> 
                                ((JavascriptExecutor) webDriver).executeScript("return typeof jQuery !== 'undefined' && jQuery.active == 0").equals(true)
                        );
                } catch (Exception e) {
                        // Ignore if jQuery is not present
                }

                // Wait for all visible elements to be displayed
                wait.until(webDriver -> 
                        webDriver.findElements(By.xpath("//*[not(contains(@style,'display:none'))]")).size() > 0
                );

                // Wait for any alert to be present
                try {
                        wait.until(webDriver -> 
                                webDriver.switchTo().alert() != null
                        );
                } catch (Exception e) {
                        // Ignore if no alert is present
                }

                // Optionally, wait for a confirmation message (customize the locator as needed)
                try {
                        wait.until(webDriver -> 
                                webDriver.findElement(By.xpath("//*[contains(text(),'Confirmation')]")).isDisplayed()
                        );
                } catch (Exception e) {
                        // Ignore if no confirmation message is found
                }
        }
        

        
   




}
