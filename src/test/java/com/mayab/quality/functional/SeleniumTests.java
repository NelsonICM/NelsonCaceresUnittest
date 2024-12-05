package com.mayab.quality.functional;

import static org.hamcrest.MatcherAssert.assertThat; // Updated import
import static org.junit.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    @Order(1)
    void AddRecord() {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(5000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Jhon Doe");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("example@gmail.com");
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
        assertThat(actualResult, is("Jhon Doe")); // Using updated assertThat
    }

    @Test
    @Order(2)
    public void Create_ExistingEmail() {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        pause(5000);
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Nelson");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("example@gmail.com");
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

    // The rest of the tests remain unchanged...
    // Ensure all assertThat calls reference the updated MatcherAssert class.
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
          //Comparación 
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
        
        // Verificar que el texto NO esté presente
        assertFalse("El registro 'John Doe' todavía está presente en la tabla.", isPresent);
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
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        pause(1000);
        driver.findElement(By.name("name")).sendKeys("Nelson");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("Nelson@gmail.com");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("23");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(3000);
        String firstResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[2]/td[1]")).getText();
        assertThat(firstResult, is("Mario"));
        String secondResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        assertThat(secondResult, is("Nelson"));

  

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
      driver.findElement(By.name("email")).sendKeys("Pedro@gmail.com");
      driver.findElement(By.name("age")).click();
      driver.findElement(By.name("age")).clear();
      driver.findElement(By.name("age")).sendKeys("19");
      takeScreenshot("prueba");
      driver.findElement(By.xpath("//div[2]/div/i")).click();
      driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::div[1]")).click();
      driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
      pause(2000);
      String actualResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
      
      //Comparación 
      System.out.println(actualResult);
      assertThat(actualResult, is("Pedro"));
    }
    public static void takeScreenshot (String filename) throws IOException{
    	File file= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    	FileUtils.copyFile(file, new File("src/screenshots/" + filename + ".jpeg"));
    	System.out.println("screenshot");
    	
    }
    

    private void pause(long mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}