package org.example.ui.tests;

import org.example.dataBase.DatabaseVerification;
import org.example.ui.pages.BKdashboardPage;
import org.example.ui.pages.BasePage;
import org.example.ui.pages.HomePage;
import org.example.ui.pages.LoginPage;
//import org.example.ui.pages.UploadPage;
import org.example.ui.utils.CSVParser;
//import org.example.ui.utils.WebDriverFactory;
import org.example.ui.utils.WebDriverFactory;
import org.example.utils.FileVerification;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UITest extends BasePage {
    private String baseUrl = "http://localhost:9997/login";
    private String clUsername = "clerk";
    private String clPassword = "clerk";
    private String bkUserName = "bk";
    private String bkPassword = "bk";
    private String csvFilePath = "src/test/resources/clerkData.csv";
    private String textFilePath1 = System.getProperty("user.dir")+"\\DownloadedTaxFiles";
    private String textFilePath = getAbsolutePaths(textFilePath1);
    private String invalidcsvFilePath = "src/test/resources/clerkData.csv";

    private String absolutecsvFilepath = getAbsolutePaths(csvFilePath);

    private String invalidabsolutecsvFilepath = getAbsolutePaths(invalidcsvFilePath);
    private String jarpath = LoginPage.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedPath = new File(jarpath).getAbsolutePath();
    public UITest() {
        //super(null); // Pass null to the superclass constructor or use a default WebDriver.
        super(WebDriverFactory.createWebDriver());
    }

    @Test(priority=1)
    public void testCSVUpload()throws InterruptedException {
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("clerk", "clerk");
        Thread.sleep(300);
        HomePage homePage = new HomePage(driver);
        // Upload CSV
        homePage.uploadCSV(absolutecsvFilepath);
        Thread.sleep(300);
        // Database verification
        List<String[]> csvData = CSVParser.parseCSV(absolutecsvFilepath);
        System.out.println("csvData size is : " + csvData.size());
        for (int i = 0; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            // Assuming that the columns have a specific order, modify the indices accordingly
            String natid = data[0];
            System.out.println("natid value is :" + natid);
            // When natid is not present in Data base then loop will cancel
            boolean recordPresent = DatabaseVerification.isRecordPresent(natid);
            Assert.assertTrue(recordPresent, "Record not found in the database for natid: " + natid);
        }
    }
        @Test(priority=2)
        public void invalidCSVUpload()throws InterruptedException{
            driver.get(baseUrl);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login("clerk", "clerk");
            Thread.sleep(300);
            HomePage homePage = new HomePage(driver);
           //HomePage homepage;
            homePage.uploadInvalidCSV(invalidabsolutecsvFilepath);
            Thread.sleep(3000);
            // Database verification
            List<String[]> csvData = CSVParser.parseCSV(invalidabsolutecsvFilepath);
            System.out.println("csvData size is : "+csvData.size());
            for (int i = 0; i < csvData.size(); i++) {
                String[] data = csvData.get(i);
                // Assuming that the columns have a specific order, modify the indices accordingly
                String natid = data[0];
                System.out.println("natid value is :" + natid);
                // When natid is not present in Data base then loop will cancel----but it has to iterate the loop untill the size condition fails though assertion is fail during iteration
                boolean recordPresent = DatabaseVerification.isRecordPresent(natid);
                Assert.assertFalse(recordPresent, "Record found in the database for natid: " + natid);
            }

        // Additional assertions based on AC3 and AC4
            Thread.sleep(1000);

    }
    @Test(priority=3)
    public void downloadTaxFile() throws InterruptedException, IOException {
        DatabaseVerification.deleteRecordsWithStatusNew();
        FileVerification.cleanFolder(textFilePath);
        Thread.sleep(3000);
         // Initialize ChromeOptions
        ChromeOptions options  = new ChromeOptions();
        //set downloaded path
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", textFilePath);
        options.setExperimentalOption("prefs",prefs);
        //initialize chrome with chrome options
        WebDriver driver = new ChromeDriver(options);
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(bkUserName, bkPassword);
        Thread.sleep(300);
        BKdashboardPage dashboardPage = new BKdashboardPage(driver);
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        dashboardPage.generatetaxreliefFile();
        Thread.sleep(3000);
        System.out.println("file path location............. : "+textFilePath);
        boolean checkfileData = FileVerification.dataCheck(textFilePath+"\\tax_relief_file.txt");
        Assert.assertTrue(checkfileData,"Text file verification is failed");
        //Checking record in file table in DB
        boolean checkDBrecord = DatabaseVerification.hasRecordsWithStatusNew();
        Assert.assertTrue(checkDBrecord, "Record was not present in DB in file table");
           }
 // Method to get the absolute path
    private String getAbsolutePaths(String relativePath) {
        Path path = Paths.get(relativePath).toAbsolutePath();
        return path.toString();
    }


}

