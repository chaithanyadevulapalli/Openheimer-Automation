package org.example.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class BKdashboardPage extends BasePage{
    private By generateTaxReliefFile = By.id("tax_relief_btn");
    public BKdashboardPage(WebDriver driver) {
        super(driver);
       }

       public void generatetaxreliefFile() throws InterruptedException{
         click(generateTaxReliefFile);
          driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


       }
}
