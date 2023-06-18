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
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * WARNING: This test will fail if team pairs are already created
 */
public class CreateTeamPairTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        CreateTeamPairTest.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        CreateTeamPairTest.driver.close();
    }

    @Test
    public void testCreateTeamPair() {
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

        // Navigate to the create team page
        WebElement linkedTeamsElement = driver.findElement(By.id("linkedTeams"));

        // Click the element
        linkedTeamsElement.click();

        // Wait for the page to load after login
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Select the teams for the pair
        WebElement teamInput1 = driver.findElement(By.id("teamInput1"));
        Select selectTeam1 = new Select(teamInput1);
        selectTeam1.selectByValue("0");

        WebElement teamInput2 = driver.findElement(By.id("teamInput2"));
        Select selectTeam2 = new Select(teamInput2);
        selectTeam2.selectByValue("1");

        // Click the "Créer la paire" button
        WebElement createPairButton = driver.findElement(By.id("buttonCreate"));
        createPairButton.click();

        // Select the teams for the pair
        selectTeam1.selectByValue("2");
        selectTeam2.selectByValue("3");

        // Click the "Créer la paire" button
        createPairButton.click();

        List<WebElement> teamPairElements = driver.findElements(By.cssSelector(".team-pair"));

        // Find the delete button within the team pair element
        WebElement deleteButton = teamPairElements.get(0).findElement(By.className("smallButton"));
    
        // Click the delete button
        deleteButton.click();

        int pairCount = 0;

        // Iterate over the team pair elements
        for (WebElement teamPairElement : teamPairElements) {
            // Get the team pair text
            String teamPairText = teamPairElement.findElement(By.tagName("p")).getText();

            // Check if the team pair exists in the list
            if (teamPairText.equals("Team 0 - Team 1") || teamPairText.equals("Team 2 - Team 3")) {
                pairCount++;
            }
        }

        // Verify if the correct number of team pairs exists
        Assertions.assertEquals(1, pairCount, "The expected number of team pairs does not match");
    }
}
