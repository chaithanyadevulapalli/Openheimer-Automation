package org.example.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BKdashboardPage extends BasePage{
    private By generateTaxReliefFile = By.id("tax_relief_btn");
    public BKdashboardPage(WebDriver driver) {
        super(driver);
       }

       public void generatetaxreliefFile() throws InterruptedException{
         click(generateTaxReliefFile);
           Thread.sleep(500);


       }
}
