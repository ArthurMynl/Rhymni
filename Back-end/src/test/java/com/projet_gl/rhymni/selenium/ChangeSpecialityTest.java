package com.projet_gl.rhymni.selenium;

import java.util.List;

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

public class ChangeSpecialityTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        ChangeSpecialityTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        ChangeSpecialityTest.driver.close();
    }

    @Test
    public void testChangeSpeciality() {
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Wait for the page to load after login
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement addSpecialitySection = driver.findElement(By.cssSelector(".add-speciality"));

        // Click on the "Ajouter modélisation" button
        WebElement addModelisationButton = addSpecialitySection.findElement(By.xpath("//button[contains(text(), 'Ajouter modélisation')]"));
        addModelisationButton.click();

        // Wait for the page to load after login
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the "Retirer des spécialités" section
        WebElement removeSpecialitySection = driver.findElement(By.cssSelector(".remove-speciality"));

        // Click on the "Retirer modélisation" button
        WebElement removeInfrastructureButton = removeSpecialitySection.findElement(By.xpath("//button[contains(text(), 'Retirer infrastructure')]"));
        removeInfrastructureButton.click();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the teacher card element
        WebElement teacherCard = driver.findElement(By.cssSelector(".card-teacher"));

        // Get the text of the specialities
        WebElement specialitiesElement = teacherCard.findElement(By.cssSelector(".list-specialities"));
        List<WebElement> specialityElements = specialitiesElement.findElements(By.tagName("p"));

        // Create an array of expected specialities
        String[] expectedSpecialities = {"Modélisation", "Développement"};

        // Verify if the expected specialities are displayed
        for (int i = 0; i < expectedSpecialities.length; i++) {
            String expectedSpeciality = expectedSpecialities[i];
            WebElement specialityElement = specialityElements.get(i);
            String actualSpeciality = specialityElement.getText();
            Assertions.assertEquals(expectedSpeciality, actualSpeciality, "Speciality mismatch at index " + i);
        }
    }

}
