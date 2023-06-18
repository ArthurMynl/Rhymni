package com.projet_gl.rhymni.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * WARNING: This test will fail if presentation already exist
 */
public class AddModifyDeletePresentation {

    private static WebDriver driver;

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        AddModifyDeletePresentation.driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void cleanupWebDriver() {
        AddModifyDeletePresentation.driver.close();
    }

    @Test
    public void testJoinTeam() {
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find and click the "Soutenance" button
        WebElement presentationPageButton = driver.findElement(By.id("soutenances"));
        presentationPageButton.click();

        // Wait for the page to load after joining the team
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement soutenanceForm = driver.findElement(By.id("presentationCreate"));
        assertTrue(soutenanceForm.isDisplayed());

        WebElement soutenanceType = driver.findElement(By.id("presentationType"));
        soutenanceType.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTypeDropdown = new Select(soutenanceType);

        // Select "Soutenance intermédiaire" by visible text
        soutenanceTypeDropdown.selectByVisibleText("Soutenance intermédiaire");

        WebElement soutenanceTeam = driver.findElement(By.id("presentationTeam"));
        soutenanceTeam.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTeamDropdown = new Select(soutenanceTeam);

        // Select "Équipe 2 - Équipe 3" by visible text
        soutenanceTeamDropdown.selectByVisibleText("Équipe 2 - Équipe 3");

        WebElement soutenanceDate = driver.findElement(By.id("presentationDate"));
        soutenanceDate.clear();
        soutenanceDate.sendKeys("12072023");

        WebElement soutenanceTime = driver.findElement(By.id("presentationTime"));
        soutenanceTime.clear();
        soutenanceTime.sendKeys("1400");

        WebElement soutenanceRoom = driver.findElement(By.id("presentationRoom"));
        soutenanceRoom.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceRoomDropdown = new Select(soutenanceRoom);

        // Select "Joule" by visible text
        soutenanceRoomDropdown.selectByVisibleText("Joule");

        WebElement soutenanceTeacher = driver.findElement(By.id("presentationTeacher"));
        soutenanceTeacher.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTeacherDropdown = new Select(soutenanceTeacher);

        // Select "Rousseau Sophie - Jamet François" by visible text
        soutenanceTeacherDropdown.selectByVisibleText("Rousseau Sophie - Jamet François");

        WebElement soutenanceAdd = driver.findElement(By.id("presentationAjouter"));
        soutenanceAdd.click();

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

        WebElement soutenanceTypeFinal = driver.findElement(By.id("presentationType"));
        soutenanceTypeFinal.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTypeFinalDropdown = new Select(soutenanceTypeFinal);

        // Select "Soutenance finale" by visible text
        soutenanceTypeFinalDropdown.selectByVisibleText("Soutenance finale");

        WebElement soutenanceTeamFinal = driver.findElement(By.id("presentationTeam"));
        soutenanceTeamFinal.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTeamFinalDropdown = new Select(soutenanceTeamFinal);

        // Select "Équipe 2 - Équipe 3" by visible text
        soutenanceTeamFinalDropdown.selectByVisibleText("Équipe 2 - Équipe 3");

        WebElement soutenanceDateFinal = driver.findElement(By.id("presentationDate"));
        soutenanceDateFinal.clear();
        soutenanceDateFinal.sendKeys("23082023");

        WebElement soutenanceTimeFinal = driver.findElement(By.id("presentationTime"));
        soutenanceTimeFinal.clear();
        soutenanceTimeFinal.sendKeys("1600");

        WebElement soutenanceRoomFinal = driver.findElement(By.id("presentationRoom"));
        soutenanceRoomFinal.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceRoomFinalDropdown = new Select(soutenanceRoomFinal);

        // Select "Edison" by visible text
        soutenanceRoomFinalDropdown.selectByVisibleText("Edison");

        WebElement soutenanceTeacherFinal = driver.findElement(By.id("presentationTeacher"));
        soutenanceTeacherFinal.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTeacherFinalDropdown = new Select(soutenanceTeacherFinal);

        // Select "Rousseau Sophie - Jamet François" by visible text
        soutenanceTeacherFinalDropdown.selectByVisibleText("Rousseau Sophie - Jamet François");

        WebElement soutenanceAddFinal = driver.findElement(By.id("presentationAjouter"));
        soutenanceAddFinal.click();

        try {
            WebElement soutenanceChoose = driver.findElement(By.id("display-3-Soutenance intermédiaire"));
            soutenanceChoose.click();
        } catch (NoSuchElementException e) {
            WebElement soutenanceChoose = driver.findElement(By.id("display-2-Soutenance intermédiaire"));
            soutenanceChoose.click();
        }

        WebElement soutenanceCard = driver.findElement(By.id("presentationCard"));
        assertTrue(soutenanceCard.isDisplayed());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement soutenanceModify = driver.findElement(By.id("modifyPresentation"));
        soutenanceModify.click();

        WebElement soutenanceDateModify = driver.findElement(By.id("presentationDateModify"));
        soutenanceDateModify.clear();
        soutenanceDateModify.sendKeys("14072023");

        WebElement soutenanceTimeModify = driver.findElement(By.id("presentationTimeModify"));
        soutenanceTimeModify.clear();
        soutenanceTimeModify.sendKeys("1630");

        WebElement soutenanceRoomModify = driver.findElement(By.id("presentationRoomModify"));
        soutenanceRoomModify.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceRoomModifyDropdown = new Select(soutenanceRoomModify);

        // Select "Wiener" by visible text
        soutenanceRoomModifyDropdown.selectByVisibleText("Wiener");

        WebElement soutenanceTeacherModify = driver.findElement(By.id("presentationTeacherModify"));
        soutenanceTeacherModify.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceTeacherModifyDropdown = new Select(soutenanceTeacherModify);

        // Select "Rousseau Sophie - Jamet François" by visible text
        soutenanceTeacherModifyDropdown.selectByVisibleText("Rousseau Sophie - Jamet François");

        WebElement soutenanceModifyValidate = driver.findElement(By.id("modifyPresentation"));
        soutenanceModifyValidate.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebElement soutenanceChoose = driver.findElement(By.id("display-3-Soutenance finale"));
            soutenanceChoose.click();
        } catch (NoSuchElementException e) {
            WebElement soutenanceChoose = driver.findElement(By.id("display-2-Soutenance finale"));
            soutenanceChoose.click();
        }

        WebElement soutenanceCard4 = driver.findElement(By.id("presentationCard"));
        assertTrue(soutenanceCard4.isDisplayed());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement soutenanceFinaleModify = driver.findElement(By.id("modifyPresentation"));
        soutenanceFinaleModify.click();

        WebElement soutenanceFinaleDateModify = driver.findElement(By.id("presentationDateModify"));
        soutenanceFinaleDateModify.clear();
        soutenanceFinaleDateModify.sendKeys("14082023");

        WebElement soutenanceFinaleTimeModify = driver.findElement(By.id("presentationTimeModify"));
        soutenanceFinaleTimeModify.clear();
        soutenanceFinaleTimeModify.sendKeys("0930");

        WebElement soutenanceFinaleRoomModify = driver.findElement(By.id("presentationRoomModify"));
        soutenanceFinaleRoomModify.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceFinaleRoomModifyDropdown = new Select(soutenanceFinaleRoomModify);

        // Select "Wiener" by visible text
        soutenanceFinaleRoomModifyDropdown.selectByVisibleText("Jeanneteau");

        WebElement soutenanceFinaleTeacherModify = driver.findElement(By.id("presentationTeacherModify"));
        soutenanceFinaleTeacherModify.click();

        // Create a Select object with the soutenanceType WebElement
        Select soutenanceFinaleTeacherModifyDropdown = new Select(soutenanceFinaleTeacherModify);

        // Select "Rousseau Sophie - Jamet François" by visible text
        soutenanceFinaleTeacherModifyDropdown.selectByVisibleText("Rousseau Sophie - Jamet François");

        WebElement soutenanceFinaleModifyValidate = driver.findElement(By.id("modifyPresentation"));
        soutenanceFinaleModifyValidate.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebElement soutenanceChoose = driver.findElement(By.id("display-3-Soutenance finale"));
            soutenanceChoose.click();
        } catch (NoSuchElementException e) {
            WebElement soutenanceChoose = driver.findElement(By.id("display-2-Soutenance finale"));
            soutenanceChoose.click();
        }

        WebElement soutenanceCard3 = driver.findElement(By.id("presentationCard"));
        assertTrue(soutenanceCard3.isDisplayed());

        WebElement soutenanceFinalDelete = driver.findElement(By.id("deletePresentation"));
        soutenanceFinalDelete.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebElement soutenanceChoose = driver.findElement(By.id("display-3-Soutenance intermédiaire"));
            soutenanceChoose.click();
        } catch (NoSuchElementException e) {
            WebElement soutenanceChoose = driver.findElement(By.id("display-2-Soutenance intermédiaire"));
            soutenanceChoose.click();
        }

        WebElement soutenanceCard2 = driver.findElement(By.id("presentationCard"));
        assertTrue(soutenanceCard2.isDisplayed());

        WebElement soutenanceDelete = driver.findElement(By.id("deletePresentation"));
        soutenanceDelete.click();

    }
}
