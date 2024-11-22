package com.mayab.quality.functional;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CRUDSeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Suppress unnecessary logs
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.SEVERE);

        // Set ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\nelso\\Downloads\\chromedriver-win64\\chromedriver.exe"); // Replace with your actual path

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions", "--start-maximized");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--remote-allow-origins=*");

        // Retry driver initialization in case of socket issues
        int retries = 3;
        while (retries > 0) {
            try {
                driver = new ChromeDriver(options);
                break;
            } catch (WebDriverException e) {
                System.out.println("Retrying WebDriver initialization...");
                retries--;
                if (retries == 0) {
                    throw e;
                }
            }
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Navigate to the application
        driver.get("https://mern-crud-mpfr.onrender.com/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private void waitForPageToLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    @Test
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
    public void testSearchRecordByName() {
        waitForPageToLoad();

        WebElement searchBox = waitForElement(By.xpath("//input[@placeholder='Search']"));
        searchBox.clear();
        searchBox.sendKeys("Mario");

        WebElement searchResult = waitForElement(By.xpath("//tr[td[contains(text(),'Mario')]]"));
        Assertions.assertNotNull(searchResult, "The record was not found.");
    }

    @Test
    public void testSearchAllRecords() {
        waitForPageToLoad();

        WebElement tableBody = waitForElement(By.xpath("//table/tbody"));
        Assertions.assertFalse(tableBody.findElements(By.tagName("tr")).isEmpty(), "No records found.");
    }
}
