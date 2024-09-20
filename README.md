# Bigbank Loan Calculator API Tests

This project contains an automated test suite for testing the loan calculator feature of Bigbank. The test suite validates the correct functionality of:

- **Loan amount** and **loan period** selection
- **Dynamic calculation** of the monthly payment
- **APRC** (Annual Percentage Rate of Charge) calculation

## Project Structure

- `src`: Contains the test cases for the loan calculator functionality.
- `.gitignore`: Specifies which files and directories to ignore when committing to Git.
- `pom.xml`: Maven configuration file, listing all dependencies needed for the project.

## Prerequisites

Before running the tests, ensure you have the following installed:

- **Java JDK** (version 8 or higher)
- **Maven** for build automation
- **Git** for version control

## How to Run the Tests

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/BigbankTests.git
    cd BigbankTests
    ```

2. **Run the tests using Maven**:
    ```bash
    mvn test
    ```

3. **Test Reports**:
    - Test reports are generated under the `target/surefire-reports` directory after the tests are executed.
  
## Vision for the Test Automation Framework

The test automation framework should be scalable, maintainable, and easy to extend with new test cases.
It should support clear reporting and integrate with Continuous Integration (CI) tools, ensuring that tests are run automatically with each code change to deliver high-quality software.

## Test Cases

### 1. Valid Loan Amount and Period

Test Scenarios: 
- Valid Loan Amount and Period: Verify that a valid loan amount and period can be selected.
- Invalid Loan Amount: Test for negative, zero, or large loan amounts.
- Invalid Loan Period: Test for loan periods that are too short or too long compared to business rules.
- Boundary Values: Test the minimum and maximum loan amounts and periods.

```java
@Test
public void testValidLoanAmountAndPeriod() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 5000, \"loanPeriod\": 60}")\
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("loanAmount", equalTo(5000))
        .body("loanPeriod", equalTo(60));
}

@Test
public void testInvalidLoanAmountNegative() {
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
public void testBoundaryLoanAmount() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 1000, \"loanPeriod\": 12}")
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("loanAmount", equalTo(1000))
        .body("loanPeriod", equalTo(12));
}
```

## 2. Dynamic Calculation of the Monthly Payment

Test Scenarios:
- Correct Monthly Payment Calculation: Verify that the correct monthly payment is calculated based on the loan amount and loan period.
- Update in Real Time: Verify that any changes to the loan amount or period result in an updated monthly payment accordingly.
- Edge Case Calculations: Test for edge cases, like the smallest loan amount for the longest loan period.
```java
@Test
public void testMonthlyPaymentCalculation() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 5000, \"loanPeriod\": 60}")
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("monthlyPayment", equalTo(100.00));
}

@Test
public void testMonthlyPaymentUpdate() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 3000, \"loanPeriod\": 48}")
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("monthlyPayment", equalTo(80.00))
    .given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 6000, \"loanPeriod\": 48}")
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("monthlyPayment", equalTo(120.00));
}

```
## 3. APRC Calculation

Test Scenarios:
- Correct APRC Calculation: Verify that APRC is calculated correctly based on given data.
- Verify for Various Loan Amounts and Periods: Ensure APRC changes accordingly with varying loan amounts and periods.
- Edge Cases: Test APRC for the smallest loan amounts and longest loan periods.
```java
@Test
public void testAPRCCalculation() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 5000, \"loanPeriod\": 60}")
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("APRC", equalTo(5.5));
}

@Test
public void testAPRCMinMaxEdgeCase() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"loanAmount\": 1000, \"loanPeriod\": 120}")
    .when()
        .post("/calculate")
    .then()
        .statusCode(200)
        .body("APRC", equalTo(8.0));
}
```
