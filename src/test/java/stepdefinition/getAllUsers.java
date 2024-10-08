package stepdefinition;
import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import Utils.LoggerLoad;
import config.ConfigProperties;
import hooks.hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;


public class getAllUsers {



    private Response response;
    private ConfigProperties configProperties = new ConfigProperties();
    private SoftAssert softAssert = new SoftAssert(); // For soft assertions
    
    @Given("base URI is invoked")
    public void base_uri_is_invoked() {
	   
    	  // Set the baseURI from hooks class
        baseURI = hooks.baseURI; // Fetching baseURI from hooks
     //   LoggerLoad.info("Base URL is set to: " + baseURI);
    }
	@When("Verify to send {string} request to the {string} with {string}")
	public void verify_to_send_request_to_the_with(String method, String endpoint, String authType) {
		if ("Basic".equalsIgnoreCase(authType)) {
            PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
            auth.setUserName(configProperties.getUsername());
            auth.setPassword(configProperties.getPassword());
            authentication = auth; // Set the authentication method
        }
       

        String fullEndpoint = baseURI + (endpoint == null ? "" : endpoint);
        LoggerLoad.info("Sending " + method + " request to: " + fullEndpoint);

        switch (method.toUpperCase()) {
            case "GET":
                response = given().when().log().all().get(fullEndpoint);
                break;
            case "POST":
                response = given().when().log().all().post(fullEndpoint);
                break;
            case "PUT":
                response = given().when().log().all().put(fullEndpoint);
                break;
            case "DELETE":
                response = given().when().log().all().delete(fullEndpoint);
                break;
            default:
                throw new IllegalArgumentException("Invalid method: " + method);
        }

        // Log the response for debugging
        LoggerLoad.info("Response received: " + response.asString());
    }

	@Then("Validate {string} and other validations for scenario name as {string}")
	public void validate_and_other_validations_for_scenario_name_as(String expectedStatusCodeStr, String scenarioName) {
		 
		 
		 SoftAssert softAssert = new SoftAssert(); // Initialize SoftAssert
	 	    int expectedStatusCode = Integer.parseInt(expectedStatusCodeStr);
	 	    int actualStatusCode = response.getStatusCode();

	 	    // Retrieve error status and error message from the response (if available)
	 	    String errorStatus = response.jsonPath().getString("error.status");  // Assuming the error status field in JSON
	 	    String errorMessage = response.jsonPath().getString("error.message"); // Assuming the error message field in JSON

	    //    // Validate status code and status line
	   //     softAssert.assertEquals(response.getStatusCode(), statusCode, "Expected status code did not match");
	      //  softAssert.assertTrue(response.getStatusLine().contains(expStatusLine), "Expected status line did not match");
	        if (actualStatusCode == expectedStatusCode) {
		        response.then().log().all().statusCode(expectedStatusCode);
		        LoggerLoad.info("Scenario: " + scenarioName + " passed with status code " + actualStatusCode);
		       
	        if (response.getStatusCode() == 200) {
	            int userCount = response.jsonPath().getList("$").size();
	            LoggerLoad.info("Number of users: " + userCount);
	        }
	        } else {
	        	 // Build the failure message
		        String failureMessage = "Failure in scenario: " + scenarioName 
		                + " | Expected status code: " + expectedStatusCode 
		                + " but got: " + actualStatusCode;

		        // Append error status and error message if they are not null or empty
		        if (errorStatus != null && !errorStatus.isEmpty()) {
		            failureMessage += " | Error Status: " + errorStatus;
		        }
		        if (errorMessage != null && !errorMessage.isEmpty()) {
		            failureMessage += " | Error Message: " + errorMessage;
		        }

		        // Log failure in the report and console
		        Reporter.log(failureMessage);
		        System.out.println(failureMessage);

		        // Log failure in Logger
		        LoggerLoad.error("Scenario: " + scenarioName + " failed. Expected status code " 
		                + expectedStatusCode + " but received " + actualStatusCode);

		        // Soft assert failure to allow test to continue
		        softAssert.fail(failureMessage);
	        }

	        // Report logging for assertions
	      //  Reporter.log("Validation complete for status code: " + statusCode + " and status line: " + expStatusLine);

	        // At the end, assert all to check for any failures
	        softAssert.assertAll();
	    
	 }
}


