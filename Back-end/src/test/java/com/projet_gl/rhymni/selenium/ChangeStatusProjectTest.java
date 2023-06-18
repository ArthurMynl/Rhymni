package com.projet_gl.rhymni.selenium;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ChangeStatusProjectTest {
    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        ChangeStatusProjectTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        ChangeStatusProjectTest.driver.close();
    }

    @Test
    public void testChangeStatusProjectToValidateTest() {
        driver.get("http:///localhost:5173/auth");

        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        emailInput.sendKeys("sophie.rousseau@eseo.fr");
        passwordInput.sendKeys("network");

        WebElement loginButton = driver.findElement(By.id("btn-submit"));
        loginButton.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement ProjectsElement = driver.findElement(By.id("projects"));

        ProjectsElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement ProjectElement = driver.findElement(By.id("seeProject1"));

        ProjectElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement ValidateSubjectElement = driver.findElement(By.id("validateSubject"));

        ValidateSubjectElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedSubject = "Validé\nRefuser";

        WebElement subjectProjectElement = driver.findElement(By.id("TrueValidation"));
        String actualSpeciality = subjectProjectElement.getText();
        Assertions.assertEquals(expectedSubject, actualSpeciality, "Validation mismatch");

    }

    @Test
    public void testChangeStatusProjectToRefuseTest() {
        driver.get("http:///localhost:5173/auth");

        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        emailInput.sendKeys("sophie.rousseau@eseo.fr");
        passwordInput.sendKeys("network");

        WebElement loginButton = driver.findElement(By.id("btn-submit"));
        loginButton.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement ProjectsElement = driver.findElement(By.id("projects"));

        ProjectsElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement ProjectElement = driver.findElement(By.id("seeProject1"));

        ProjectElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement RefuseSubjectElement = driver.findElement(By.id("refuseSubject"));

        RefuseSubjectElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedSubject = "Refusé\nValider";

        WebElement subjectProjectElement = driver.findElement(By.id("TrueRefuse"));
        String actualSpeciality = subjectProjectElement.getText();
        Assertions.assertEquals(expectedSubject, actualSpeciality, "Validation mismatch");

    }

}

