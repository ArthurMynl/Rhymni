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

public class SeeSpecialityTest {
    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        SeeSpecialityTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        SeeSpecialityTest.driver.close();
    }

    @Test
    public void testChangeStatusProjectToValidateTest() {
        driver.get("http:///localhost:5173/auth");

        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        emailInput.sendKeys("louis.legendre@reseau.eseo.fr");
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

        WebElement TeachersElement = driver.findElement(By.id("teachers"));

        TeachersElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement TeacherElement = driver.findElement(By.id("seeTeacher9"));

        TeacherElement.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedSpeciality = "Modélisation\nDéveloppement";

        WebElement specialitiesElement = driver.findElement(By.id("specialitiesTeacher"));
        String actualSpeciality = specialitiesElement.getText();
        Assertions.assertEquals(expectedSpeciality, actualSpeciality, "Validation mismatch");

    }
}
