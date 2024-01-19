package org.example.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class HomePage extends BasePage {
    private By addaHero = By.id("dropdownMenuButton2");
    private By uploadFile = By.xpath("//a[text()='Upload a csv file']");
    private By chooseFileInput = By.id("upload-csv-file");
    private By createButton = By.xpath("//button[text()='Create']");
    private By notificationElement = By.xpath("//div[@id='notification-block']//h3");
   // WebElement notificationElement = driver.findElement(By.xpath("//div[@id='notification-block']//h3"));

    // Get the text from the element
       public HomePage(WebDriver driver) {
        super(driver);
    }

    public void uploadCSV(String filePath) throws InterruptedException {
        driver.findElement(addaHero).click();
        driver.findElement(uploadFile).click();
         Thread.sleep(3000);
        driver.findElement(chooseFileInput).sendKeys(filePath);
        Thread.sleep(3000);
        driver.findElement(createButton).click();
    }
    public void uploadInvalidCSV(String filePath) throws InterruptedException {
        driver.findElement(addaHero).click();
        driver.findElement(uploadFile).click();
        Thread.sleep(3000);
        System.out.println("check.........1");
        driver.findElement(chooseFileInput).sendKeys(filePath);
        System.out.println("check.........2");
        Thread.sleep(3000);
        driver.findElement(createButton).click();
        //Thread.sleep(3000);
        System.out.println("check.........3");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement cheknotificationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationElement));
        String notificationText = driver.findElement(notificationElement).getText();
        Assert.assertEquals(notificationText,"Unable to create hero!","User able to create Hero");
    }

}
