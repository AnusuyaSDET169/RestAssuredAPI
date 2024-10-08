#Author: Anusuya S

@GetAll
Feature: Verify to Get All users

 
  @GetAllpositiveandnegative
    
  Scenario Outline: Get All Users scenario - verification
  Given base URI is invoked
  When  Verify to send "<method>" request to the "<endpoint>" with "<auth>"
  Then Validate "<status_code>" and other validations for scenario name as "<scenario_name>"

  Examples: 
      | method | endpoint | auth  | status_code | status_line   | scenario_name |
      | GET    | /users   | Basic |         200 | HTTP/1.1 200  | valid 			|
      | GET    | /invalid | Basic |         404 | HTTP/1.1 404  |   invalid			|
      | GET    |          | Basic |         404 | HTTP/1.1 404  |   empty				|
      | GET    | /users   | No Auth |         200 | HTTP/1.1 200  |		no auth			|
      | POST   | /users   | Basic |         405 | HTTP/1.1 405  | different method	|
      | PUT    | /users   | Basic |         405 | HTTP/1.1 405  |	different method	|
      | DELETE | /users   | Basic |         405 | HTTP/1.1 405  |	different method	|