package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CalculateEndpointTest {

    private static final Logger logger = LogManager.getLogger(CalculateEndpointTest.class);

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://web-loan-application-staging.bigbank.lt";
        logger.info("Set up base URI: " + RestAssured.baseURI);
    }

    @Test
    public void testValidLoanCalculation() {
        logger.info("Starting test for valid loan calculation");

        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"loanAmount\": 1000, \"loanPeriod\": 6 }")
                .when()
                .post("/api/v1/loan/calculate");

        logger.info("Received response: " + response.asString());

        response.then()
                .statusCode(200)
                .body("monthlyPayment", greaterThan(0.0))
                .body("totalCost", greaterThan(0.0));

        logger.info("Test for valid loan calculation completed successfully");
    }

    @Test
    public void testBoundaryValues() {
        logger.info("Starting test for boundary values");

        //payload
        String requestBody = "{\n" +
                "    \"loanAmount\": 1000,\n" +
                "    \"loanPeriod\": 6,\n" +
                "    \"maturity\": 60,\n" +
                "    \"productType\": \"consumerLoan\",\n" +
                "    \"amount\": 1000,\n" +
                "    \"monthlyPaymentDay\": 10,\n" +
                "    \"interestRate\": 5.0,\n" +
                "    \"conclusionFee\": 100,\n" +
                "    \"administrationFee\": 50\n" +
                "}";

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/loan/calculate")
                .then()

                .statusCode(200)
                .extract().response();

        logger.info("Received response: " + response.asString());
    }

    @Test
    public void testInvalidLoanAmount() {
        logger.info("Starting test for invalid loan amount");

        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"loanAmount\": -5000, \"loanPeriod\": 60 }")
                .when()
                .post("/api/v1/loan/calculate");

        logger.info("Received response: " + response.asString());

        response.then()
                .statusCode(400);

        logger.info("Test for invalid loan amount completed successfully");
    }

    @Test
    public void testApiErrorHandling() {
        logger.info("Starting test for API error handling with an invalid endpoint");

        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"loanAmount\": 5000, \"loanPeriod\": 60 }")
                .when()
                .post("/invalid-endpoint");

        logger.info("Received response: " + response.asString());

        response.then()
                .statusCode(404);

        logger.info("Test for API error handling completed successfully");
    }
}
