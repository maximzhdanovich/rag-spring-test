package com.example.MyBookShopApp.selenium;

import liquibase.sdk.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainPage {

  private String url = "http://localhost:8085/";
  private ChromeDriver driver;

  public MainPage(ChromeDriver driver) {
    this.driver = driver;
  }

  public MainPage callPage() {
    driver.get(url);
    return this;
  }


  public MainPage pause() throws InterruptedException {
    Thread.sleep(2000);
    return this;
  }

  public MainPage setUpSearchToken(String token) {
    WebElement element = driver.findElement(By.id("query"));
    element.sendKeys(token);
    return this;
  }

  public MainPage openGenres() {
    WebElement element = driver.findElement(By.id("genres"));
    element.click();
    return this;
  }

  public MainPage openRecent() {
    WebElement element = driver.findElement(By.id("recent"));
    element.click();
    return this;
  }

  public MainPage openPopular() {
    WebElement element = driver.findElement(By.id("popular"));
    element.click();
    return this;
  }

  public MainPage openAuthors() {
    WebElement element = driver.findElement(By.id("authors"));
    element.click();
    return this;
  }

  public MainPage openFirstRecommendedBook() {
    WebElement element = driver.findElement(By.partialLinkText("American Samurai"));
    element.click();
    return this;
  }

  public MainPage openByLinkPart(String linkTextContains) {
    WebElement element = driver.findElement(By.partialLinkText(linkTextContains));
    element.click();
    return this;
  }

  public MainPage openById(String id) {
    WebElement element = driver.findElement(By.id(id));
    element.click();
    return this;
  }

  public MainPage openCart() {
    WebElement element = driver.findElement(By.id("book_cart"));
    element.click();
    return this;
  }


  public MainPage submit() {
    WebElement element = driver.findElement(By.id("search"));
    element.submit();
    return this;
  }
}
