package org.example.api;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.dataBase.DatabaseVerification;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateHeroAPI {
    private static String uri = "http://localhost:9997";
    private static String createheroEndpoint = "/api/v1/hero";
    private static String jsonFilesPath = "src/test/resources/jsonFiles";
    private static String jsonFilepathName = "src/test/resources/jsonFiles/CreatevalidHero.json";
    private static String invalidjsonFilepathName = "src/test/resources/jsonFiles/InvalidJsondata.json";
    // Set the base URI once before running the tests
    static {
        RestAssured.baseURI = uri;
    }

    @Test(priority = 1, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void verifyValidHero(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String name = (String)testData.get("name");
        // Read JSON data from file
        //String jsonFilepathName = jsonFilesPath+"/CreatevalidHero.json";
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        // Modify specific attributes based on the provided values
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "name", name);
        System.out.println("Json data is ......: "+jsonData);
        // Make the API request with the modified JSON data
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);

        // Validate the response status code
        response.then().statusCode(200);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("null"), "Response does not contain the expected word is null ");
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertTrue(checkDBrecord, "record doesn't inserted in DB");
    }
    @Test(priority = 2, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void verifywithInvalidnatId(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");

        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
         jsonData = modifyJsonData(jsonData, "natid", natId);
          Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Invalid natid"), "Response does not contain the expected word is null ");
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid natId");
    }
    @Test(priority = 3, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyWithExistingNatId(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));

        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        System.out.println("status value is ........:  " +checkDBrecord);
        System.out.println("natId value is ........:  " +natId);
        Assert.assertTrue(checkDBrecord,"Given natId doesn't exist in DB");
        jsonData = modifyJsonData(jsonData, "natid", natId);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("already exists"), "Response does not contain the expected error message");

    }
    @Test(priority = 4, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyWithInvalidName(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String name = (String)testData.get("name");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "name", name);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Invalid name"), "Response does not contain the expected error Message ");
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid name");
    }
    @Test(priority = 5, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyWithInvalidGender(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String gender = (String)testData.get("gender");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "gender", gender);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Invalid gender"), "Response does not contain the expected error Message ");
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid name");
    }

    @Test(priority = 6, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyWithInvalidBirthDate(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String birthDate = (String)testData.get("birthDate");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "birthDate", birthDate);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
       // Assert.assertTrue(responseBody.contains("There are some issues"), "Response does not contain the expected error Message ");
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid Date");
    }

    @Test(priority = 7, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyWithNegativeSalary(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        double salary = (double)testData.get("salary");
        String jsonData = new String(Files.readAllBytes(Paths.get(invalidjsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData1(jsonData, "birthDate", salary);
        System.out.println("JsonBody ........... is :" + jsonData);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Salary must be greater than or equals to zero"), "Response does not contain the expected error Message ");
    }
    private String modifyJsonData(String jsonData, String attribute, String newValue) {
        // Modify the JSON data based on the provided attribute and new value
        return jsonData.replaceAll("\"" + attribute + "\":\\s*\"[^\"]+\"", "\"" + attribute + "\": \"" + newValue + "\"");
    }
    private String modifyJsonData1(String jsonData, String attribute, double newValue) {
        // Modify the JSON data based on the provided attribute and new value
        return jsonData.replaceAll("\"" + attribute + "\":\\s*\"[^\"]+\"", "\"" + attribute + "\": \"" + newValue + "\"");
    }
}

