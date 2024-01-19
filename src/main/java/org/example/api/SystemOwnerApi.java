package org.example.api;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
public class SystemOwnerApi {
    public static final String BASE_URI = "http://localhost:9997";
    public static final String ENDPOINT = "/api/v1/hero/owe-money";
    public static final String ENDPOINT_VOUCHER = "/api/v1/voucher/by-person-and-type";
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        // Add other setup configurations
    }
    @Test(priority = 1)
    public void testGetAPIWithValidNatid() {
        // Set the base URI for the API
        // Define the expected values

        int natidVal = 1;
        String expectedNatId = "natid-"+natidVal;
        //String expectedStatus = "OWE";
        // Make the GET request
        Response response = get(natidVal);

        // Validate the response status code
        assertEquals(response.getStatusCode(), 200, "Unexpected status code");

        // Validate the response body
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains(expectedNatId), "Unexpected natid in response body");
        if(responseBody.contains("OWE")|| responseBody.contains("NIL"))
        {
            assertTrue(true, "Success");
        }
        else{
            assertTrue(false,"invalid status value, other than OWE or NIL");
    }
    }
    @Test(priority = 2)
    public void testGetAPIWithInValidnatId() {
        // Set the base URI for the API
        // Define the expected values

        String natidVal = "A";
        String expectedNatId = "natid-"+natidVal;
        //String expectedStatus = "OWE";
        // Make the GET request
        Response response = given()
                .queryParam("natid", natidVal)
                .when()
                .get(ENDPOINT);

        // Validate the response status code
        assertEquals(response.getStatusCode(), 500, "Unexpected status code");

    }
    @Test(priority = 3)
    public void testGetVoucherAPI() {

        Response response = given()
                .when()
                .get(ENDPOINT_VOUCHER);

        // Validate the response status code
        response.then().statusCode(200);

        // Validate the response format using SoftAssert for multiple assertions
        SoftAssert softAssert = new SoftAssert();

         softAssert.assertTrue(response.body().asString().contains("data"), "Response doesn't contain 'data' field");
        // Check if response contains 'name' field
        softAssert.assertTrue(response.body().asString().contains("name"), "Response doesn't contain 'name' field");
        // Check if response contains 'voucherType' field
        softAssert.assertTrue(response.body().asString().contains("voucherType"), "Response doesn't contain 'voucherType' field");
        // Check if response contains 'count' field
        softAssert.assertTrue(response.body().asString().contains("count"), "Response doesn't contain 'count' field");
        // Assert all conditions
        softAssert.assertAll();
    }

     public static Response get(int val) {
        return given()
                .queryParam("natid", val)
                .when()
                .get(ENDPOINT);
    }
}

