package org.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.dataBase.DatabaseVerification;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.testng.Assert.*;

public class Dummy {
    private final String baseUrl = "http://localhost:9997/api/v1/hero/vouchers";

    @Test(priority = 2, dataProvider = "heroData1withVoucher", dataProviderClass = org.example.utils.ExcelDatareader1.class)
    public void createWorkingClassHeroWithExistingNatId(Map<String, Object> testData) {
        String natId = (String) testData.get("natid");
        String name = (String) testData.get("name");
        String gender = (String) testData.get("gender");
        String birthDate = (String) testData.get("birthDate");
        String deathDate = (String) testData.get("deathDate");
        double salary = (Double) testData.get("salary"); // Change to Double
        double taxPaid = (Double) testData.get("taxPaid"); // Change to Double
        String browniePoints = (String) testData.get("browniePoints");
        String deathDateJson = (deathDate != null) ? "\"" + deathDate + "\"" : "null";
        String voucherName = (String) testData.get("voucherName");
        String voucherType = (String) testData.get("voucherType");
        int expectedStatusCode = 0;
        // Send API request
        if (DatabaseVerification.isRecordPresent(natId)) {
            expectedStatusCode = 400;
        } else {
            Assert.assertTrue(false, "record doesn't exist in DB");
        }
        String requestBody = getRequestBody(natId, name, gender, birthDate, deathDateJson, salary, taxPaid, browniePoints, voucherName, voucherType);
        // Send API request
        Response response = sendPostRequest(requestBody);
        Assert.assertEquals(expectedStatusCode, response.getStatusCode(), "Status code must be 400");

    }

    @Test(priority = 1, dataProvider = "heroData1withVoucher", dataProviderClass = org.example.utils.ExcelDatareader1.class)
    public void createWorkingClassHero(Map<String, Object> testData) {
        String natId = (String) testData.get("natid");
        String name = (String) testData.get("name");
        String gender = (String) testData.get("gender");
        String birthDate = (String) testData.get("birthDate");
        String deathDate = (String) testData.get("deathDate");
        double salary = (Double) testData.get("salary"); // Change to Double
        double taxPaid = (Double) testData.get("taxPaid"); // Change to Double
        String browniePoints = (String) testData.get("browniePoints");
        String deathDateJson = (deathDate != null) ? "\"" + deathDate + "\"" : "null";
        String voucherName = (String) testData.get("voucherName");
        String voucherType = (String) testData.get("voucherType");
        int expectedStatusCode = 0;
        if (DatabaseVerification.isRecordPresent(natId)) {
            expectedStatusCode = 400;
        }
        String requestBody = getRequestBody(natId, name, gender, birthDate, deathDateJson, salary, taxPaid, browniePoints, voucherName, voucherType);
        // Send API request
        System.out.print("json body is ........ : "+requestBody);
            Response response = sendPostRequest(requestBody);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        if(expectedStatusCode == 400){
            assertEquals(expectedStatusCode, statusCode, "Record with natid " + natId + " already exists in the database. Should return error status code 400");
        }
        else {

            assertEquals(statusCode, 200, "Status code is not 200");
        }
               // Custom validations based on the response
            if (response.statusCode() == 200) {
                // AC2 Validations
                assertTrue(natId.matches("natid-\\d+"), "natid format is incorrect");
                assertTrue(name.matches("[a-zA-Z\\s]{1,100}"), "name format is incorrect");
                assertTrue(gender.equals("MALE") || gender.equals("FEMALE"), "gender is incorrect");
                assertTrue(validateDateTimeFormat(birthDate, "yyyy-MM-dd'T'HH:mm:ss"), "birthDate format is incorrect");
                assertTrue(validateDateTimeFormat(deathDate, "yyyy-MM-dd'T'HH:mm:ss") || deathDate == null, "deathDate format is incorrect");
                assertTrue(salary >= 0, "salary must be a non-negative decimal");
                assertTrue(taxPaid >= 0, "taxPaid must be a non-negative decimal");
                //assert
                assert DatabaseVerification.isRecordPresent(natId);
            } else {
               // Assert.assertTrue(false, "request failed with different status code");
                // Handle error scenario if needed

        }
    }

    @Test(priority = 2, dataProvider = "heroData2withVoucher", dataProviderClass = org.example.utils.ExcelDatareader1.class)
    public void createWorkingClassHeroWithInvalidData(Map<String, Object> testData) {
        String natId = (String) testData.get("natid");
        String name = (String) testData.get("name");
        String gender = (String) testData.get("gender");
        String birthDate = (String) testData.get("birthDate");
        String deathDate = (String) testData.get("deathDate");
        double salary = (Double) testData.get("salary"); // Change to Double
        double taxPaid = (Double) testData.get("taxPaid"); // Change to Double
        String browniePoints = (String) testData.get("browniePoints");
        String deathDateJson = (deathDate != null) ? "\"" + deathDate + "\"" : "null";
        String voucherName = (String) testData.get("voucherName");
        String voucherType = (String) testData.get("voucherType");
        int expectedStatusCode = 0;
        // Send API request
        if (DatabaseVerification.isRecordPresent(natId)) {
            expectedStatusCode = 400;
        }
        String requestBody = getRequestBody(natId, name, gender, birthDate, deathDateJson, salary, taxPaid, browniePoints, voucherName, voucherType);
        // Send API request
        Response response = sendPostRequest(requestBody);
        int statusCode = response.getStatusCode();
        System.out.println("Invalid data API status code......... : "+statusCode);
        boolean databaseRecord = DatabaseVerification.isRecordPresent(natId);
        if(expectedStatusCode == 400){
            assertEquals(expectedStatusCode, 400, "Record with natid " + natId + " already exists in the database. Should return error status code 400");
        }
        else {
            Assert.assertEquals(statusCode, 400, "Status code must be 400");
            Assert.assertFalse(databaseRecord,"record should not be inserted");
        }
    }

    private Response sendPostRequest(String requestBody) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(baseUrl);
    }

    private String getRequestBody(String natId, String name, String gender, String birthDate, String deathDate, double salary, double taxPaid, String browniePoints, String voucherName, String voucherType) {
    return String.format("{\n" +
            "\"natid\": \"" + natId + "\",\n" +
            "\"name\": \"" + name + "\",\n" +
            "\"gender\": \"" + gender + "\",\n" +
            "\"birthDate\": \"" + birthDate + "\",\n" +
            "\"deathDate\": " + deathDate + ",\n" + // Use the formatted deathDateJson here
            "\"salary\": " + salary + ",\n" +
            "\"taxPaid\": " + taxPaid + ",\n" +
            "\"browniePoints\": 9,\n" +
            "\"vouchers\": ["
            + "{"
            + "\"voucherName\": \"VOUCHER 1\","
            + "\"voucherType\": \"TRAVEL\""
            + "}"
            + "]" +
            "}");

    }

    private boolean checkApiStatus() {
        return true;
    }

    private boolean validateDateTimeFormat(String dateTime, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime.parse(dateTime, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
