package com.mayab.quality.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SeleniumTests {

    static WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu"); // Disable GPU acceleration
        options.addArguments("--no-sandbox"); // Required for containerized environments
        options.addArguments("--disable-dev-shm-usage"); // Use shared memory
        options.addArguments("--remote-allow-origins=*"); // Allow all origins
        options.addArguments("--window-size=1920,1080"); // Set the window size for headless mode

        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void AddRecord() {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(5000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Jhon Doe");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("example@gmail.com");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("5");
        pause(1000);
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(2000);

        String actualResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        pause(1000);
        System.out.println(actualResult);
        assertThat(actualResult, is("Jhon Doe"));
    }

    @Test
    @Order(2)
    public void Create_ExistingEmail() {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(5000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Mario");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("example@gmail.com");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("22");
        driver.findElement(By.xpath("//div[2]/div/i")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(2000);

        String actualResult = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")).getText();
        System.out.println(actualResult);
        assertThat(actualResult, is("That email is already taken."));
    }

    @Test
    @Order(3)
    public void ModifyAge() {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(5000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button")).click();
        driver.findElement(By.name("age")).clear();
        pause(2000);
        driver.findElement(By.name("age")).sendKeys("1");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(2000);

        String actualResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[3]")).getText();
        System.out.println(actualResult);
        assertThat(actualResult, is("51"));
    }

    @Test
    @Order(4)
    public void DeleteRecord() {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(5000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Jhon Doe'])[2]/following::button[1]")).click();
        boolean isPresent = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody")).getText().contains("Jhon Doe");

        assertFalse(isPresent, "The record 'Jhon Doe' is still present in the table.");
    }

    @Test
    @Order(5)
    public void findOnebyName() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Pedro");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("pedro@gmail.com");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("19");
        takeScreenshot("prueba");
        driver.findElement(By.xpath("//div[2]/div/i")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(2000);

        String actualResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        System.out.println(actualResult);
        assertThat(actualResult, is("Pedro"));
    }

    @Test
    @Order(6)
    public void findAll() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(4000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Mario");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("Mario@gmail.com");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("20");
        driver.findElement(By.xpath("//div[2]/div/i")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(3000);

        String firstResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[2]/td[1]")).getText();
        assertThat(firstResult, is("Pedro"));
        String secondResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        assertThat(secondResult, is("Mario"));
    }

    public static void takeScreenshot(String filename) throws IOException {
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("src/screenshots/" + filename + ".jpeg"));
        System.out.println("Screenshot saved as " + filename);
    }

    private void pause(long mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
