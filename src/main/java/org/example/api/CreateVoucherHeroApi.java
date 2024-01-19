package org.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.dataBase.DatabaseVerification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateVoucherHeroApi {
    private static String uri = "http://localhost:9997";
    private static String createheroEndpoint = "/api/v1/hero/vouchers";
    private static String jsonFilesPath = "src/test/resources/jsonFiles";
    private static String jsonFilepathName = "src/test/resources/jsonFiles/CreateVoucherValidHero.json";
    private static String invalidjsonFilepathName = "src/test/resources/jsonFiles/InvalidVoucherJsondata.json";
    // Set the base URI once before running the tests
    static {
        RestAssured.baseURI = uri;
    }

    @Test(priority = 1, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void verifyVoucherValidHero(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String name = (String)testData.get("name");
        String voucherName = (String)testData.get("voucherName");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        // Modify specific attributes based on the provided values
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "name", name);
        jsonData = modifyJsonData(jsonData, "voucherName", voucherName);
        System.out.println("Json data is ......: "+jsonData);
        // Make the API request with the modified JSON data
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);

        // Validate the response status code
        response.then().statusCode(200);
        String responseBody = response.getBody().asString();
        Assert.assertEquals(response.getStatusCode(), 200, "Api returned InvalidStatus Code : "+response.getStatusCode());
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertTrue(checkDBrecord, "record doesn't inserted in DB for Working class heros");
        boolean checkVoucherrecord = DatabaseVerification.isVoucherRecordPresent(voucherName);
        Assert.assertTrue(checkVoucherrecord, "record doesn't inserted in DB for Vouchers table");
    }
    @Test(priority = 2, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void verifyVoucherwithInvalidnatId(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");

        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid natId");
    }
    @Test(priority = 3, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyVoucherWithExistingNatId(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));

        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertTrue(checkDBrecord,"Given natId doesn't exist in DB");
        jsonData = modifyJsonData(jsonData, "natid", natId);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
    }
    @Test(priority = 4, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyVoucherWithInvalidName(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String name = (String)testData.get("name");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "name", name);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid name");
    }
    @Test(priority = 5, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyVoucherWithInvalidGender(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String gender = (String)testData.get("gender");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "gender", gender);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
       boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid name");
    }

    @Test(priority = 6, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyVoucherWithInvalidBirthDate(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String birthDate = (String)testData.get("birthDate");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilepathName)));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "birthDate", birthDate);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid Date");
    }

    @Test(priority = 7, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyVoucherWithNegativeSalary(Map<String, Object> testData) throws IOException {
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
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid Salary");
    }

    @Test(priority = 8, dataProvider = "excelDataprovider", dataProviderClass = org.example.utils.ExcelDataProvider.class)
    public void VerifyVoucherNameIsNull(Map<String, Object> testData) throws IOException {
        String natId = (String)testData.get("natid");
        String voucherName = (String)testData.get("voucherName");
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilesPath+"/NullVoucherjsonFile.json")));
        jsonData = modifyJsonData(jsonData, "natid", natId);
        jsonData = modifyJsonData(jsonData, "voucherName", voucherName);
        System.out.println("JsonBody ........... is :" + jsonData);
        Response response = given().contentType("application/json").body(jsonData).when().post(createheroEndpoint);
        // Validate the response status code
        response.then().statusCode(400);
        String responseBody = response.getBody().asString();
        boolean checkDBrecord =  DatabaseVerification.isRecordPresent(natId);
        Assert.assertFalse(checkDBrecord, "record  inserted in DB with invalid voucherName");
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

