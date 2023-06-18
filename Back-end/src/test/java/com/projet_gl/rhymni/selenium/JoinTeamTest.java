package com.projet_gl.rhymni.selenium;

import java.util.List;

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


/**
 * WARNING: This test will fail if louis legendre is already in a team
 */
public class JoinTeamTest {
    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        JoinTeamTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        JoinTeamTest.driver.close();
    }

    @Test
    public void testJoinTeam() {
        // Navigate to the login page
        driver.get("http:///localhost:5173/auth");

        // Find the email and password input fields
        WebElement emailInput = driver.findElement(By.cssSelector("input[type='text']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        // Enter the email and password values
        emailInput.sendKeys("louis.legendre@reseau.eseo.fr");
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

        // Find and click the "Rejoindre" button
        WebElement joinButton = driver.findElements(By.className("smallButton")).get(1);
        joinButton.click();

        // Wait for the page to load after joining the team
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the voir equipe anchor tag
        WebElement seeLink = driver.findElements(By.className("smallLink")).get(1);
        seeLink.click();

        // Wait for the page to load after clicking the voir equipe link
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement studentListUl = driver.findElement(By.cssSelector("div.project-composition > ul"));

        // Find all student list items within the <ul> element
        List<WebElement> studentListItems = studentListUl.findElements(By.tagName("li"));

        boolean isLouisLegendreInList = false;

        // Iterate over the student list items
        for (WebElement listItem : studentListItems) {
            if (listItem.getText().contains("Louis Legendre")) {
                isLouisLegendreInList = true;
                break;
            }
        }

        // Verify if "Louis Legendre" is in the student list
        Assertions.assertTrue(isLouisLegendreInList, "Louis Legendre is not in the student list");
    }
}
