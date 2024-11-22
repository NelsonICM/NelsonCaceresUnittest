package com.mayab.quality.functional;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
	WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    baseUrl = "https://www.facebook.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testUntitledTestCase() throws Exception {
      driver.get("https://www.facebook.com/");
      pause(5000);

      driver.findElement(By.id("email")).clear();
      driver.findElement(By.id("email")).sendKeys("puppies");

      driver.findElement(By.id("pass")).clear();
      driver.findElement(By.id("pass")).sendKeys("password");

      driver.findElement(By.name("login")).click();

      pause(5000);

      String actualResult = driver.findElement(By.xpath("/html/body/div[1]/div")).getText();
      String expectedText = "The email or mobile number you entered isn’t connected to an account.";
      assertThat(actualResult, containsString(expectedText)); // Verifica que el texto contiene la cadena esperada
  }


  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
  
  private void pause(long mils) {
      try {
          Thread.sleep(mils);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
	    try {
	        Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        if (acceptNextAlert) {
	            alert.accept();
	        } else {
	            alert.dismiss();
	        }
	        return alertText;
	    } finally {
	        acceptNextAlert = true;
  }
}
}