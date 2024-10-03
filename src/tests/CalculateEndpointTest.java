package tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class CalculateEndpointTest {

    static {
        RestAssured.baseURI = "https://api.bigbank.com"; // replace with actual base URL
    }

    @Test
    public void testValidLoanAmountAndPeriod() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"loanAmount\": 5000, \"loanPeriod\": 60}")
                .when()
                .post("/calculate")
                .then()
                .statusCode(200)
                .body("monthlyPayment", notNullValue())
                .body("APRC", notNullValue());
    }

    @Test
    public void testInvalidLoanAmount() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"loanAmount\": -1000, \"loanPeriod\": 60}")
                .when()
                .post("/calculate")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid loan amount"));
    }

    @Test
    public void testBoundaryValues() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"loanAmount\": 1000, \"loanPeriod\": 6}")
                .when()
                .post("/calculate")
                .then()
                .statusCode(200)
                .body("monthlyPayment", greaterThan(0))
                .body("APRC", greaterThan(0.0));

        given()
                .contentType(ContentType.JSON)
                .body("{\"loanAmount\": 30000, \"loanPeriod\": 120}")
                .when()
                .post("/calculate")
                .then()
                .statusCode(200)
                .body("monthlyPayment", greaterThan(0))
                .body("APRC", greaterThan(0.0));
    }

    @Test
    public void testApiErrorHandling() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"loanAmount\": 5000, \"loanPeriod\": 60}")
                .when()
                .post("/invalid-endpoint")
                .then()
                .statusCode(404);
    }
}
