package com.example.MyBookShopApp.selenium;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainPageSeleniumTests {

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
  public void testMainPageAccess() throws InterruptedException {
    MainPage mainPage = new MainPage(driver);
    mainPage
        .callPage()
        .pause();

    assertTrue(driver.getPageSource().contains("BOOKSHOP"));
  }

  @Test
  public void testMainPageSearchByQuery() throws InterruptedException {
    MainPage mainPage = new MainPage(driver);
    mainPage
        .callPage()
        .pause()
        .setUpSearchToken("Beautiful")
        .pause()
        .submit()
        .pause();

    assertTrue(driver.getPageSource().contains("Beautiful Boy"));
  }

  @Test
  public void navigateHeader() throws InterruptedException {
    MainPage mainPage = new MainPage(driver);
    mainPage
        .callPage()
        .pause()
        .openGenres()
        .pause();

    assertTrue(driver.getPageSource().contains("Easy reading"));

    mainPage.openRecent()
        .pause();

    assertEquals("http://localhost:8085/books/recent_page", driver.getCurrentUrl());

    mainPage.openPopular()
        .pause();

    assertEquals("http://localhost:8085/books/popular_page", driver.getCurrentUrl());

    mainPage.openAuthors()
        .pause();

    assertEquals("http://localhost:8085/authors", driver.getCurrentUrl());
  }

  @Test
  public void navigateFirstRecommendedFromMain() throws InterruptedException {
    MainPage mainPage = new MainPage(driver);
    mainPage
        .callPage()
        .pause()
        .openFirstRecommendedBook()
        .pause();

    assertEquals("http://localhost:8085/books/book-oml-573", driver.getCurrentUrl());
  }

  @Test
  public void navigateDetectivesGenreFromMain() throws InterruptedException {
    MainPage mainPage = new MainPage(driver);
    mainPage
        .callPage()
        .pause()
        .openGenres()
        .pause()
        .openByLinkPart("Detectives")
        .pause();

    assertTrue(driver.getPageSource().contains("Detectives"));
  }

  @Test
  public void addBookToCartAndRemove() throws InterruptedException {
    MainPage mainPage = new MainPage(driver);
    mainPage
        .callPage()
        .pause()
        .openFirstRecommendedBook()
        .pause()
        .openById("buy-button")
        .pause()
        .openCart()
        .pause();

    assertEquals("http://localhost:8085/books/cart", driver.getCurrentUrl());
    assertTrue(driver.getPageSource().contains("American Samurai"));

    mainPage.openCart()
        .openById("remove-button")
        .pause();

    assertEquals("",driver.manage().getCookieNamed("cartContents").getValue());
  }



}