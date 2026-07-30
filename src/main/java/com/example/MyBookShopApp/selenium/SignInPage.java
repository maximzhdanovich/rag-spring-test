package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SignInPage {

  private String url = "http://localhost:8085/signin/";
  private ChromeDriver driver;

  public SignInPage(ChromeDriver driver) {
    this.driver = driver;
  }

  public SignInPage callPage() {
    driver.get(url);
    return this;
  }

  public SignInPage pause() throws InterruptedException {
    Thread.sleep(2000);
    return this;
  }

  public SignInPage clickEmailRadiobutton() {
    WebElement element = driver.findElement(By.id("mailtype-check"));
    element.click();
    return this;
  }

  public SignInPage inputMail(String mail) {
    WebElement element = driver.findElement(By.id("mail"));
    element.sendKeys(mail);
    return this;
  }

  public SignInPage clickById(String id) {  //sendauth toComeInMail
    WebElement element = driver.findElement(By.id(id));
    element.click();
    return this;
  }

  public SignInPage inputPass(String pass) {
    WebElement element = driver.findElement(By.id("mailcode"));
    element.sendKeys(pass);
    return this;
  }




}
