package com.lazerycode.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.lazerycode.selenium.DriverBase;

public class SeleniumSimpleIT extends DriverBase {
	
    @Test
    public void googleTitleTest() throws Exception {
    	WebDriver driver = getDriver();

        driver.navigate().to("http://www.google.com");
    
        Assert.assertTrue(driver.getTitle().contains("Google"));
    }
    
    @Test
    public void googleSearchMilk() throws Exception {
    	WebDriver driver = getDriver();

        driver.navigate().to("http://www.google.com");
        
        WebElement searchButton = driver.findElement(By.name("btnK"));
    	WebElement textToSearch = driver.findElement(By.id("lst-ib"));
    	
    	textToSearch.sendKeys("Milk");
        searchButton.click();
        
        WebDriverWait wait = new WebDriverWait(driver, 10, 100);
        wait.until(d -> driver.getTitle().toLowerCase().startsWith("Milk".toLowerCase()));
    }

    @Test
    public void googleSearchMilk2() throws Exception {
        WebDriver driver = getDriver();

        driver.navigate().to("http://www.google.com");

        WebElement searchButton = driver.findElement(By.name("btnI"));
        WebElement textToSearch = driver.findElement(By.id("lst-ib"));

        textToSearch.sendKeys("Milk");
        searchButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 10, 100);
        wait.until(d -> driver.getTitle().toLowerCase().startsWith("Milk".toLowerCase()));
    }

    @Test
    public void googleImgTest() throws Exception {
        WebDriver driver = getDriver();

        driver.navigate().to("http://www.google.com");

        WebDriverWait wait = new WebDriverWait(driver, 10, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("hplogo")));

    }


}
