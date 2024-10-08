
package stepdefinition;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import Utils.LoggerLoad;
import config.ConfigProperties;
import hooks.hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pojo.Address;
import pojo.User;


public class commonRequests {

	private Response response;
	private static int storedUserId;
	private static String storedFirstName;

	public static int initialUserCount;
	public static int newUserCount;
	private ConfigProperties config;

	private Map<String, String> userData;

	public commonRequests() {
		config = new ConfigProperties();
	}

	@Given("I send a GET request to {string}")
	public void i_send_a_get_request_to(String endpoint) {
		String fullurl = hooks.baseURI + endpoint;
		response = given().auth().basic(config.getUsername(), config.getPassword()).when().get(fullurl).then().extract()
				.response();
	}

	@Then("I should receive a valid response with status code {int}")
	public void i_should_receive_a_valid_response_with_status_code(int statusCode) {
		String responseBody = response.asString();
		System.out.println("Response Body as String: " + responseBody);
		response.then().statusCode(statusCode);
		LoggerLoad.info("validated status code");
	}

	@Then("validate the number of users")
	public void validate_the_number_of_users() {
		initialUserCount = response.jsonPath().getList("users").size();
		System.out.println("Total number of users : " + initialUserCount);
		response.then().body("users.size()", greaterThan(0));
		LoggerLoad.info("validated users count");

	}

	@When("I store the userId from the POST response")
	public void i_store_the_userId_from_the_post_response() {
		// Extract and store the userId from the response
		storedUserId = response.jsonPath().getInt("user_id");
		System.out.println("User id for the newly created user is " + storedUserId);
	}

	@When("I store the firstname from the PUT response")
	public void i_store_the_firstname_from_the_put_response() {

		storedFirstName = response.jsonPath().getString("user_first_name");
		System.out.println("User first name for the updated user is " + storedFirstName);

	}

	@Given("I send a POST request to {string} with the following data:")
	public void i_send_a_post_request_to_with_the_following_data(String endpoint, DataTable dataTable) {
		// Convert the DataTable into a List of Maps
		List<Map<String, String>> dataList = dataTable.asMaps(String.class, String.class);

		// Assuming there's only one row of data
		if (!dataList.isEmpty()) {
			userData = dataList.get(0);

			User user = new User();
			Address address = new Address();

			// Set user details
			user.setUser_first_name(userData.get("user_first_name"));
			user.setUser_last_name(userData.get("user_last_name"));
			user.setUser_contact_number(userData.get("user_contact_number"));
			user.setUser_email_id(userData.get("user_email_id"));

			// Set address details
			address.setPlotNumber(userData.get("plotNumber"));
			address.setStreet(userData.get("street"));
			address.setState(userData.get("state"));
			address.setCountry(userData.get("country"));
			address.setZipCode(userData.get("zipCode"));

			user.setUserAddress(address);

			response = given().auth().basic(config.getUsername(), config.getPassword()).contentType(ContentType.JSON)
					.log().all().body(user).when().post(hooks.baseURI + endpoint).then().log().all().extract()
					.response();

		}
	}

	@Then("the user should be created successfully")
	public void the_user_should_be_created_successfully() {
		response.then().statusCode(201).body("user_first_name", equalTo(userData.get("user_first_name")))
				.body("user_last_name", equalTo(userData.get("user_last_name")));
		LoggerLoad.info("user created successfully");

	}

	@Then("the user count should be increased by 1")
	public void the_user_count_should_be_increased_by_1() {
		newUserCount = given().auth().basic(config.getUsername(), config.getPassword()).when()
				.get(hooks.baseURI + "/users").then().extract().jsonPath().getList("users").size();
		System.out.println("Total number of user before creation " + initialUserCount);
		System.out.println("Total number of user after creation " + newUserCount);
		Assert.assertEquals(newUserCount, initialUserCount + 1);
		LoggerLoad.info("After creation of user, total users count increase by 1");
	}

	@Given("I send a GET request to {string} with the stored user ID")
	public void i_send_a_get_request_with_stored_user_id(String endpoint) {

		response = given().auth().basic(config.getUsername(), config.getPassword()).pathParam("userId", storedUserId)
				.when().get(hooks.baseURI + endpoint).then().extract().response();
	}

	@Then("the response should contain the correct user details for the stored user ID")
	public void the_response_should_contain_the_correct_user_details_for_the_stored_user_ID() {
		// response.then().statusCode(200).body("userId", equalTo(userId));
		int responseUserId = response.jsonPath().getInt("user_id");
		assertEquals(storedUserId, responseUserId);

		String responseBody = response.asString();
		System.out.println("Response Body as String: " + responseBody);
		LoggerLoad.info("Validated user details for the created userID");
	}

	@Given("I send a PUT request to {string} with user ID {string} and updated data:")
	public void i_send_a_put_request_to_with_user_id_and_updated_data(String endpoint, String userId,
			DataTable dataTable1) {
		List<Map<String, String>> dataList1 = dataTable1.asMaps(String.class, String.class);
		// Assuming there's only one row of data
		if (!dataList1.isEmpty()) {
			userData = dataList1.get(0);
		}

		User user = new User();
		Address address = new Address();

		// Set user details
		user.setUser_first_name(userData.get("up_user_first_name"));
		user.setUser_last_name(userData.get("up_user_last_name"));
		user.setUser_contact_number(userData.get("up_user_contact_number"));
		user.setUser_email_id(userData.get("up_user_email_id"));

		// Set address details
		address.setPlotNumber(userData.get("plotNumber"));
		address.setStreet(userData.get("street"));
		address.setState(userData.get("state"));
		address.setCountry(userData.get("country"));
		address.setZipCode(userData.get("zipCode"));

		user.setUserAddress(address);

		response = given().auth().basic(config.getUsername(), config.getPassword()).pathParam("userId", storedUserId)
				.contentType(ContentType.JSON).log().all().body(user).when().put(hooks.baseURI + endpoint).then().log()
				.all().extract().response();
	}

	
	
	@Then("the response should contain the updated user details")
	public void the_response_should_contain_the_updated_user_details() {
		response.then().statusCode(200).body("user_first_name", equalTo(userData.get("up_user_first_name")))
				.body("user_last_name", equalTo(userData.get("up_user_last_name")))
				.body("user_email_id", equalTo(userData.get("up_user_email_id")));
		LoggerLoad.info("Validated user details for the updated userID");
	}

	@Given("I send a GET request to {string} with the stored user first name")
	public void i_send_a_get_request_to_with_the_stored_user_first_name(String endpoint) {
		response = given().auth().basic(config.getUsername(), config.getPassword())
				.pathParam("firstname", storedFirstName).log().all().when().get(hooks.baseURI + endpoint).then()
				.extract().response();
	}

	@Then("the response should contain the correct user details for the stored user first name")
	public void the_response_should_contain_the_correct_user_details_for_the_stored_user_first_name() {
		// response.then().statusCode(200).body("user_first_name", equalTo(firstName));
		String responseUserFirstName = response.jsonPath().getString("[0].user_first_name");
		System.out.println("check   responseUserFirstName " + responseUserFirstName);
		System.out.println("check    storedFirstName " + storedFirstName);
		assertEquals(storedFirstName, responseUserFirstName);
		String responseBody = response.asString();
		System.out.println("Response Body as String: " + responseBody);
		LoggerLoad.info("Validated user details for the stored user first name");
	}

	@Given("I send a DELETE request to {string} with the stored user ID")
	public void i_send_a_delete_request_to_with_stored_user_id(String endpoint) {
		response = given().auth().basic(config.getUsername(), config.getPassword()).pathParam("userId", storedUserId)
				.when().delete(hooks.baseURI + endpoint).then().extract().response();
	}

	@Then("I should receive a valid response with status code 200 and message {string}")
	public void i_should_receive_a_valid_response_with_status_code_and_message(String message) {
		response.then().statusCode(200).body("message", equalTo(message));
		LoggerLoad.info("validated status message");
	}

	@Then("the user count should be decreased by 1")
	public void the_user_count_should_be_decreased_by_1() {
		int updatedUserCount = given().auth().basic(config.getUsername(), config.getPassword()).when()
				.get(hooks.baseURI + "/users").then().extract().jsonPath().getList("users").size();
		System.out.println("Total number of user before deleting " + newUserCount);
		System.out.println("Total number of user after deleting " + updatedUserCount);
		Assert.assertEquals(updatedUserCount, newUserCount - 1);
		LoggerLoad.info("After deletion of user, total users count decreased by 1");
	}

}
/*	
	private Response response;
    private List<AddUser> users;
    private int currentUserIndex = 0; // To track the current user in the list
    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;
    
    @Before
    public void setup() {
        // Initialize RequestSpecification
        requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://userserviceapp-f5a54828541b.herokuapp.com/uap/")
            .setContentType("application/json")
            .build();

      
    }

    @Given("I have user data from the Excel CSV file {string}")
    public void i_have_user_data_from_csv(String csvFilePath) {
        // Read the user data from the CSV file
       // users = ExcelUtils.readUsersFromCSV("src/test/resources/userdata.csv");
        // Check if the file has been read successfully
        if (users.isEmpty()) {
            throw new RuntimeException("No users found in the CSV file");
        }
    
        given()
        .spec(requestSpec) // Use the requestSpec here
        .body(csvFilePath)
        .log().all(); // Optional: log the request for debugging
    }

    @When("I send a POST request to create the user from row {int}")
    public void i_send_post_request_to_create_user_from_row(Integer rowNumber) {
        // Get the user based on the row number (subtract 1 as list is 0-indexed)
        currentUserIndex = rowNumber - 1;
        AddUser user = users.get(currentUserIndex);
        
       

        // Send POST request with request body as JSON
        response = given().auth()
		        .basic("Numpy@gmail.com", "userapi@nn") // Replace with actual credentials
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/createusers");
//        response = given()
//            .contentType("application/json")
//            .body(user)
//        .when()
//            .post("/createusers");
    }

    @Then("I should get a status code {string}")
    public void i_should_get_a_status_code(String expectedStatusCode) {
	   // response.then().log().all().statusCode(expectedStatusCode);
    	int actualStatusCode = response.getStatusCode();
        
        // Log for debugging
        System.out.println("Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
        
        //assertEquals(actualStatusCode, Integer.parseInt(expectedStatusCode), "Status code mismatch");
    }
//    	 response.then()
//    	 .spec(responseSpec)
//        // .statusCode(expectedStatusCode) // Alternatively: .spec(responseSpec) if you set it up
//         .log().all(); // 
//    	
    	// Validate the status code
      //  assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCode));

       // assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCode));
    //}

    @Then("the status line should be {string}")
    public void the_status_line_should_be(String expectedStatusLine) {
        // Validate the status line
String actualStatusline = response.getStatusLine();
        
        // Log for debugging
        System.out.println("Expected: " + expectedStatusLine + ", Actual: " + actualStatusline);
       // assertEquals(response.getStatusLine(), expectedStatusLine);
    }

//    @Then("the user with row {int} should be successfully created")
//    public void validate_response_body(int rowNumber) {
//        // You can validate the response body, like checking user_id, creation_time, etc.
//        int userId = response.jsonPath().getInt("user_id");
//        String creationTime = response.jsonPath().getString("creation_time");
// 	   System.out.println(creationTime);
//
//        // Assuming you want to check if user ID and creation time exist
//        assertEquals(response.getStatusCode(), 201);
//        assertNotNull(userId, "User ID should not be null");
//       // assertNotNull(creationTime, "Creation time should not be null");
//    }
}

	
	
	
	
	/*
	
	public Response response;
    public UserAddress Address ;
    public User user;
    public String baseUri;
    
	@Given("The base URI is {string}")
	public void the_base_uri_is(String uri) {
	        baseUri = uri;
	        RestAssured.baseURI = baseUri;
		
        
	}
	@When("verify to perform Post request using data from {string}")
	public void verify_to_perform_post_request_using_data_from(String csvFilePath) {
		
		// Read the user data from the CSV file
        users = CSVUtil.readUsersFromCSV(csvFilePath);
        
        // Check if the file has been read successfully
        if (users.isEmpty()) {
            throw new RuntimeException("No users found in the CSV file");
        }
    }
	
	@Then("System should validate status code {string} and {string}")
	public void system_should_validate_status_code_and(String expectedStatusCode, String expectedStatusLine) {
	   System.out.println(expectedStatusCode);
       assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCode));
	   System.out.println(expectedStatusLine);
       assertEquals(response.getStatusLine(), expectedStatusLine);
	  */
	
