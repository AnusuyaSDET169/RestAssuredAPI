package stepdefinition;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;



public class getAllUsers {
	
  public Response response;
	
    public String baseUri;
	@Given("the base URI is {string}")
	public void the_base_uri_is(String uri) {
	   
		
		     baseUri = uri;
	        RestAssured.baseURI = baseUri;
		
	}
	
	@When("Verify to send {string} request to the {string} with {string}")
	public void verify_to_send_request_to_the_with(String method, String endpoint, String auth) {

		
		if (auth.equalsIgnoreCase("Basic")) {
			
           response = given().auth()
        		        .basic("Numpy@gmail.com", "userapi@nn") // Replace with actual credentials
        		        .header("Content-Type", "application/json")
                        .when()
                        .request(method, endpoint);
       
		
		   } 
		
		

		//else {
		 else if (auth.equalsIgnoreCase("None")) {
            // If no authentication is needed (auth = "None")
            response = given()
                    .header("Content-Type", "application/json")
                    .when()
                    .request(method, endpoint);
        } 
		// Log the response for debugging purposes
	   // System.out.println("Response: " + response.asString());
       
        }
		
//	    makeApiRequest(method, endpoint, auth);
	
//		if (auth.equalsIgnoreCase("Basic")) {
//            response = given()
//                        .auth().basic("Numpy@gmail.com", "userapi@nn") // Replace with actual credentials
//                        .when()
//                        .request(method, endpoint);
//        } else {
//            response = given()
//                        .when()
//                        .request(method, endpoint);
//        }
    

	@Then("the response status code should be {int}")
	public void the_response_status_code_should_be(int expectedStatusCode) {
	    response.then().log().all().statusCode(expectedStatusCode);
	    
        //assertEquals(response.statusCode(), expectedStatusCode, "Status code does not match!");
	}
	
	

	@Then("the response statusline should be {string}")
	public void the_response_status_line_should_be(String expectedStatusLine) {
		

		expectedStatusLine = response.asString();
//	    JsonPath jp = new JsonPath(expectedReasonPhrase);
//	    String userId = jp.get("user_id");
//	    System.out.println("Extracted User ID: " + userId);
		System.out.println(expectedStatusLine);
		//Assert.assertEquals(response.getStatusLine(), expectedReasonPhrase);
		
		
		//String actualReasonPhrase = response.getStatusLine().split(" ", 3)[2].trim(); // Extract the reason phrase (e.g., "OK")
	    //Assert.assertEquals(actualReasonPhrase, expectedReasonPhrase, "Reason phrase does not match!");
	}

	}
	
	
