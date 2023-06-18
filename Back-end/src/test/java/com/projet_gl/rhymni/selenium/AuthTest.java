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

class AuthTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        AuthTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        AuthTest.driver.close();
    }

    @Test
    public void testLoginStudent() {
        // Navigate to the login page
        driver.get("http:///localhost:5173/auth");

        // Find the email and password input fields
        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        // Enter the email and password values
        emailInput.sendKeys("arthur.meyniel@reseau.eseo.fr");
        passwordInput.sendKeys("network");

        // Submit the form
        WebElement loginButton = driver.findElement(By.id("btn-submit"));
        loginButton.click();

        // Wait for the page to load after login
        // Replace the "3000" value with an appropriate wait time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String location = driver.getCurrentUrl();
        Assertions.assertEquals("http://localhost:5173/project", location);
    }

    @Test
    public void testLoginTeacher() {
        // Navigate to the login page
        driver.get("http:///localhost:5173/auth");

        // Find the email and password input fields
        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        // Enter the email and password values
        emailInput.sendKeys("sophie.rousseau@eseo.fr");
        passwordInput.sendKeys("network");

        // Submit the form
        WebElement loginButton = driver.findElement(By.id("btn-submit"));
        loginButton.click();

        // Wait for the page to load after login
        // Replace the "3000" value with an appropriate wait time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // verify the location is the home page
        String location = driver.getCurrentUrl();
        Assertions.assertEquals("http://localhost:5173/teacher/9", location);
    }

    @Test
    public void testLoginPlanningAssistant() {
        // Navigate to the login page
        driver.get("http:///localhost:5173/auth");

        // Find the email and password input fields
        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        // Enter the email and password values
        emailInput.sendKeys("planning.assistant@eseo.fr");
        passwordInput.sendKeys("network");

        // Submit the form
        WebElement loginButton = driver.findElement(By.id("btn-submit"));
        loginButton.click();

        // Wait for the page to load after login
        // Replace the "3000" value with an appropriate wait time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // verify the location is the home page
        String location = driver.getCurrentUrl();
        Assertions.assertEquals("http://localhost:5173/student", location);
    }
}
