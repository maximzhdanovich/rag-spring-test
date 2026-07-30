package com.example.MyBookShopApp.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SignInSeleniumTests {

  private static ChromeDriver driver;

  @BeforeAll
  static void setup(){
    System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Desktop/Java_Spring/chromedriver.exe");
    driver = new ChromeDriver();
    driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
  }

  @AfterAll
  static void tearDown(){
    driver.quit();
  }

  @Test
  public void testLoginLogout() throws InterruptedException {
    SignInPage signInPage = new SignInPage(driver);
    signInPage
        .callPage()
        .pause()
        .clickEmailRadiobutton()
        .pause()
        .inputMail("aleksandr.baranov@skillbox.ru")
        .pause()
        .clickById("sendauth")
        .pause()
        .inputPass("123123")
        .pause()
        .clickById("toComeInMail")
        .pause();

    assertEquals("http://localhost:8085/my", driver.getCurrentUrl());

    signInPage.clickById("profile-link")
        .pause()
        .clickById("logout-link")
        .pause();

    assertEquals("http://localhost:8085/signin", driver.getCurrentUrl());
  }

}
