package com.projet_gl.rhymni.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ConsultingTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        ConsultingTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        ConsultingTest.driver.close();
    }

    @Test
    public void teamMemberAskConsulting() {
        // Navigate to the login page
        driver.get("http:///localhost:5173/auth");

        // Find the email and password input fields
        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        // Enter the email and password values
        emailInput.sendKeys("nathan.lafrogne@reseau.eseo.fr");
        passwordInput.sendKeys("network");

        // Submit the form
        WebElement loginButton = driver.findElement(By.id("btn-submit"));
        loginButton.click();

        // Wait for the page to load after login
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Go to Consulting page
        WebElement consultingPageButton = driver.findElement(By.id("consultings"));
        consultingPageButton.click();

        // Wait for the page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the planning card element
        WebElement planningSlot = driver.findElement(By.className("ec-event-body"));
        System.out.println(planningSlot.getText());
        Assertions.assertNotNull(planningSlot, "Planning slot not found");

        planningSlot.click();

        WebElement sidePanel = driver.findElement(By.id("info-consulting"));
        Assertions.assertNotNull(sidePanel, "Side panel not found");

        WebElement askConsultingButton = driver.findElement(By.id("ask-consulting-button"));
        Assertions.assertNotNull(askConsultingButton, "Ask consulting button not found");

        askConsultingButton.click();

    }

}
