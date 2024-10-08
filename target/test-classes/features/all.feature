#Author: Anusuya Selvaraj



   @CRUD_Positive
 
Feature: All CRUD operation in one 
  
  Scenario: Get All Users
    Given I send a GET request to "/users"
    Then I should receive a valid response with status code 200
    And validate the number of users
  
  Scenario: Create User
   Given I send a POST request to "/createusers" with the following data:
    | user_first_name | user_last_name | user_contact_number | user_email_id      | plotNumber | street | state  | country | zipCode |
    | Anusuya          | Vinod         | 2029936199          | yuryu@example.com| pl-10       | North glendrive | TX   | USA     | 75067   |
   Then I should receive a valid response with status code 201
    And the user should be created successfully
    And the user count should be increased by 1
    When I store the userId from the POST response
  
  Scenario: Get User by ID
    Given I send a GET request to "/user/{userId}" with the stored user ID
    Then I should receive a valid response with status code 200
    And the response should contain the correct user details for the stored user ID
  
  Scenario: Update User by ID
    Given I send a PUT request to "/updateuser/{userId}" with user ID "<userId>" and updated data:
   | up_user_first_name| up_user_last_name| up_user_contact_number | up_user_email_id        |plotNumber | street          | state   | country | zipCode |
   | Vinod             | Vinz             | 2019936199             | mathiwwwb@example.com     | pl-10     | North glendrive | TX      | USA     | 75067   |
    Then I should receive a valid response with status code 200
    And the response should contain the updated user details
    When I store the firstname from the PUT response
  
  Scenario: Get User by First Name
    Given I send a GET request to "/users/username/{firstname}" with the stored user first name
    Then I should receive a valid response with status code 200
    And the response should contain the correct user details for the stored user first name
  
  Scenario: Delete User by ID
    Given I send a DELETE request to "/deleteuser/{userId}" with the stored user ID
    Then I should receive a valid response with status code 200 and message "User is deleted successfully"
    And the user count should be decreased by 1