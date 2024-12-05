package com.mayab.quality.functional;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CRUDSeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Suppress unnecessary logs
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.SEVERE);

        // Use WebDriverManager to set up ChromeDriver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions", "--start-maximized");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://mern-crud-mpfr.onrender.com/");
        waitForPageToLoad();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void waitForPageToLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Test
    @Order(1)
    public void testCreateNewRecord() {
        waitForPageToLoad();

        WebElement addButton = waitForElement(By.xpath("//button[contains(text(),'Add')]"));
        addButton.click();

        WebElement nameField = waitForElement(By.name("name"));
        nameField.sendKeys("Mario");

        WebElement emailField = waitForElement(By.name("email"));
        emailField.sendKeys("mario.cruz.nicm@gmail.com");

        WebElement ageField = waitForElement(By.name("age"));
        ageField.sendKeys("46");

        WebElement submitButton = waitForElement(By.xpath("//button[@type='submit' and text()='Submit']"));
        submitButton.click();

        WebElement successMessage = waitForElement(By.xpath("//div[contains(@class,'alert-success')]"));
        Assertions.assertTrue(successMessage.isDisplayed(), "Record creation failed.");
    }

    @Test
    @Order(2)
    public void testCreateDuplicateEmailRecord() {
        waitForPageToLoad();

        WebElement addButton = waitForElement(By.xpath("//button[contains(text(),'Add')]"));
        addButton.click();

        WebElement nameField = waitForElement(By.name("name"));
        nameField.sendKeys("Mario");

        WebElement emailField = waitForElement(By.name("email"));
        emailField.sendKeys("mario.cruz.nicm@gmail.com");

        WebElement ageField = waitForElement(By.name("age"));
        ageField.sendKeys("46");

        WebElement submitButton = waitForElement(By.xpath("//button[@type='submit' and text()='Submit']"));
        submitButton.click();

        WebElement errorMessage = waitForElement(By.xpath("//div[contains(@class,'alert-danger')]"));
        Assertions.assertTrue(errorMessage.isDisplayed(), "Error message for duplicate email not displayed.");
    }

    @Test
    @Order(3)
    public void testModifyRecordAge() {
        waitForPageToLoad();

        WebElement editButton = waitForElement(By.xpath("//tr[td[contains(text(),'Mario')]]//button[contains(text(),'Edit')]"));
        editButton.click();

        WebElement ageField = waitForElement(By.name("age"));
        ageField.clear();
        ageField.sendKeys("35");

        WebElement submitButton = waitForElement(By.xpath("//button[@type='submit' and text()='Submit']"));
        submitButton.click();

        WebElement updatedAge = waitForElement(By.xpath("//tr[td[contains(text(),'Mario')]]//td[3]"));
        Assertions.assertEquals("35", updatedAge.getText(), "Age was not updated.");
    }

    @Test
    @Order(4)
    public void testDeleteRecord() {
        waitForPageToLoad();

        WebElement deleteButton = waitForElement(By.xpath("//tr[td[contains(text(),'Mario')]]//button[contains(text(),'Delete')]"));
        deleteButton.click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath("//tr[td[contains(text(),'Mario')]]"));
        }, "The record was not deleted.");
    }

    @Test
    @Order(5)
    public void testSearchRecordByName() {
        waitForPageToLoad();

        WebElement searchBox = waitForElement(By.xpath("//input[@placeholder='Search']"));
        searchBox.clear();
        searchBox.sendKeys("Mario");

        WebElement searchResult = waitForElement(By.xpath("//tr[td[contains(text(),'Mario')]]"));
        Assertions.assertNotNull(searchResult, "The record was not found.");
    }

    @Test
    @Order(6)
    public void testSearchAllRecords() {
        waitForPageToLoad();

        WebElement tableBody = waitForElement(By.xpath("//table/tbody"));
        int rowCount = tableBody.findElements(By.tagName("tr")).size();

        // Assert that the table has at least two rows
        Assertions.assertTrue(rowCount >= 2, "Expected multiple records, but found fewer.");
    }
}
