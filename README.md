This project contains an automated test suite for testing the Bigbank loan calculator feature. The test suite validates the correct functionality of the loan amount and period selection, dynamic calculation of the monthly payment, and APRC calculation.

Project Structure

src: Contains the test cases for the loan calculator functionality.

.gitignore: Specifies which files and directories to ignore when committing to Git.

pom.xml: Maven configuration file, listing all dependencies needed for the project.

Prerequisites

Java JDK (version 8 or higher)

Maven for build automation

Git for version control

How to Run the Tests

Clone the repository:

git clone https://github.com/yourusername/BigbankTests.git

cd BigbankTests

Run the tests using Maven: mvn test

Test reports are generated under the target/surefire-reports directory after the tests are executed.
