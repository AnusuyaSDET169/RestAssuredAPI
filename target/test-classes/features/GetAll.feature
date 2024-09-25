#Author: Anusuya S

@GetAll
Feature: Verify to Get All users

 
  @GetAllpositiveandnegative
    
   Scenario Outline: Get All Users scenario
   
    Given the base URI is "https://userserviceapp-f5a54828541b.herokuapp.com/uap/"
    When  Verify to send "<method>" request to the "<endpoint>" with "<auth>" 
    Then  the response status code should be <status_code> 
    And   the response statusline should be "<status_line>"
    

  Examples:
     |method   | endpoint | auth   |status_code|status_line|
     |  GET    |  users   | Basic  |200        |  OK       |
     |  GET    |  invalid | Basic  |404        | Not Found |
     |  GET    |          | Basic  |404        | Not Found |
     |  GET    |   null   | Basic  |404        | Not Found |
     |  GET    |   users  |        |200        |  OK       |
     |  POST   |   users  | Basic  |405        | Method Not Allowed |
     |  PUT    |   users  | Basic  |405        | Method Not Allowed |
     |  DELETE |   users  | Basic  |405        | Method Not Allowed |

