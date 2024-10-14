package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CalculateEndpointTest {

    private static final Logger logger = LogManager.getLogger(CalculateEndpointTest.class);

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://taotlus.bigbank.ee";
        logger.info("Set up base URI: " + RestAssured.baseURI);
    }

    @Test
    public void testValidLoanCalculation() {
        logger.info("Starting test for valid loan calculation");

        String requestBody = "{\n" +
                "  \"currency\": \"EUR\",\n" +
                "  \"productType\": \"SMALL_LOAN_EE01\",\n" +
                "  \"maturity\": 54,\n" +
                "  \"administrationFee\": 3.49,\n" +
                "  \"amount\": 5000,\n" +
                "  \"conclusionFee\": 100,\n" +
                "  \"interestRate\": 16.8,\n" +
                "  \"monthlyPaymentDay\": 15\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/loan/calculate");

        logger.info("Received response: " + response.asString());

        response.then()
                .statusCode(200)
                .body("monthlyPayment", equalTo(136.79f))
                .body("totalRepayableAmount", equalTo(7386.17f))
                .body("apr", equalTo(21.19f));

        logger.info("Test for valid loan calculation completed successfully");
    }

    @Test
    public void testBoundaryValues() {
        logger.info("Starting test for boundary values");

        String requestBody = "{\n" +
                "  \"currency\": \"EUR\",\n" +
                "  \"productType\": \"SMALL_LOAN_EE01\",\n" +
                "  \"maturity\": 6,\n" +
                "  \"administrationFee\": 1.49,\n" +
                "  \"amount\": 1000,\n" +
                "  \"conclusionFee\": 50,\n" +
                "  \"interestRate\": 5.0,\n" +
                "  \"monthlyPaymentDay\": 10\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
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

        String requestBody = "{\n" +
                "  \"currency\": \"EUR\",\n" +
                "  \"productType\": \"SMALL_LOAN_EE01\",\n" +
                "  \"maturity\": 54,\n" +
                "  \"administrationFee\": 3.49,\n" +
                "  \"amount\": -5000,\n" +
                "  \"conclusionFee\": 100,\n" +
                "  \"interestRate\": 16.8,\n" +
                "  \"monthlyPaymentDay\": 15\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/loan/calculate");

        logger.info("Received response: " + response.asString());

        response.then()
                .statusCode(500);

        logger.info("Test for invalid loan amount completed successfully");
    }

    @Test
    public void testApiErrorHandling() {
        logger.info("Starting test for API error handling with an invalid endpoint");

        String requestBody = "{\n" +
                "  \"currency\": \"EUR\",\n" +
                "  \"productType\": \"SMALL_LOAN_EE01\",\n" +
                "  \"maturity\": 54,\n" +
                "  \"administrationFee\": 3.49,\n" +
                "  \"amount\": 5000,\n" +
                "  \"conclusionFee\": 100,\n" +
                "  \"interestRate\": 16.8,\n" +
                "  \"monthlyPaymentDay\": 15\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/invalid-endpoint");

        logger.info("Received response: " + response.asString());

        response.then()
                .statusCode(200)
                .body(containsString("We're sorry but application doesn't work properly without JavaScript enabled"));

        logger.info("Test for API error handling completed successfully");
    }
}
