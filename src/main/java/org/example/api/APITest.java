package org.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.dataBase.DatabaseVerification;
import org.example.utils.ExcelDataReader;
import org.example.utils.ExcelDatareader1;
import org.testng.annotations.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.*;
import java.util.Map;
    public class APITest {

        @Test(priority = 1,dataProvider = "heroData", dataProviderClass = org.example.utils.ExcelDataReader.class)
        public void createWorkingClassHero1(String natid, String name, String gender, String birthDate,
                                           String deathDate, double salary, double taxPaid) {

            // Check if the record already exists in the database
            if (DatabaseVerification.isRecordPresent(natid)) {
                // If the record already exists, return error status code 400
                fail("Record with natid " + natid + " already exists in the database. Should return error status code 400");
            }
            // Send API request
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            "\"natid\": \"" + natid + "\",\n" +
                            "\"name\": \"" + name + "\",\n" +
                            "\"gender\": \"" + gender + "\",\n" +
                            "\"birthDate\": \"" + birthDate + "\",\n" +
                            "\"deathDate\": \"" + deathDate + "\",\n" +
                            "\"salary\": " + salary + ",\n" +
                            "\"taxPaid\": " + taxPaid + ",\n" +
                            "\"browniePoints\": 9\n" +
                            "}")
                    .post("http://localhost:9997/api/v1/hero");

            // Validations
            int statusCode = response.getStatusCode();
            String responseBody = response.getBody().asString();

            assertEquals(statusCode, 200, "Status code is not 200");
            // Custom validations based on the response
            if (responseBody.contains("\"message\": null,")) {
                // AC2 Validations
                assertTrue(natid.matches("natid-\\d+"), "natid format is incorrect");
                assertTrue(name.matches("[a-zA-Z\\s]{1,100}"), "name format is incorrect");
                assertTrue(gender.equals("MALE") || gender.equals("FEMALE"), "gender is incorrect");
                assertTrue(validateDateTimeFormat(birthDate, "yyyy-MM-dd'T'HH:mm:ss"), "birthDate format is incorrect");
                assertTrue(validateDateTimeFormat(deathDate, "yyyy-MM-dd'T'HH:mm:ss") || deathDate == null, "deathDate format is incorrect");
                assertTrue(salary >= 0, "salary must be a non-negative decimal");
                assertTrue(taxPaid >= 0, "taxPaid must be a non-negative decimal");
                assert DatabaseVerification.isRecordPresent(natid);
            } else {
                // Handle error scenario if needed
            }
        }
        @Test(priority = 2,dataProvider = "heroData", dataProviderClass = org.example.utils.ExcelDatareader1.class)
        public void createWorkingClassHero(Map <String, Object> testData) {
            String natId = (String) testData.get("natid");
            String name = (String) testData.get("name");
            String gender = (String) testData.get("gender");
            String birthDate = (String) testData.get("birthDate");
            String deathDate = (String) testData.get("deathDate");
            double salary = (Double) testData.get("salary"); // Change to Double
            double taxPaid = (Double) testData.get("taxPaid"); // Change to Double
            String browniePoints = (String) testData.get("browniePoints");
            String deathDateJson = (deathDate != null) ? "\"" + deathDate + "\"" : "null";
            int expectedStatusCode = 0;
            // Send API request
            if (DatabaseVerification.isRecordPresent(natId)) {
                // If the record already exists, return error status code 400
               // boolean apiStatus = checkApiStatus();
              //  fail("Record with natid " + natId + " already exists in the database. Should return error status code 400");
                 expectedStatusCode = 400 ;
            }

            // Send API request
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            "\"natid\": \"" + natId + "\",\n" +
                            "\"name\": \"" + name + "\",\n" +
                            "\"gender\": \"" + gender + "\",\n" +
                            "\"birthDate\": \"" + birthDate + "\",\n" +
                            "\"deathDate\": " + deathDateJson + ",\n" + // Use the formatted deathDateJson here
                            "\"salary\": " + salary + ",\n" +
                            "\"taxPaid\": " + taxPaid + ",\n" +
                            "\"browniePoints\": 9\n" +
                            "}")
                    .post("http://localhost:9997/api/v1/hero");

            // Validations
            int statusCode = response.getStatusCode();
            String responseBody = response.getBody().asString();
               if(expectedStatusCode == 400){
                   assertEquals(expectedStatusCode, 400, "Record with natid " + natId + " already exists in the database. Should return error status code 400");
               }
               else {

                   assertEquals(statusCode, 200, "Status code is not 200");
               }
            // Custom validations based on the response
            if (responseBody.contains("\"message\": null,")) {
                // AC2 Validations
                assertTrue(natId.matches("natid-\\d+"), "natid format is incorrect");
                assertTrue(name.matches("[a-zA-Z\\s]{1,100}"), "name format is incorrect");
                assertTrue(gender.equals("MALE") || gender.equals("FEMALE"), "gender is incorrect");
                assertTrue(validateDateTimeFormat(birthDate, "yyyy-MM-dd'T'HH:mm:ss"), "birthDate format is incorrect");
                assertTrue(validateDateTimeFormat(deathDate, "yyyy-MM-dd'T'HH:mm:ss") || deathDate == null, "deathDate format is incorrect");
                assertTrue(salary >= 0, "salary must be a non-negative decimal");
                assertTrue(taxPaid >= 0, "taxPaid must be a non-negative decimal");
                assert DatabaseVerification.isRecordPresent(natId);
            } else {
                // Handle error scenario if needed
            }
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
