#Author: Anusuya Selvaraj

@AddUser 
Feature: Validating user creation with positive and negative data 

  @AddUserpositiveandnegative
  Scenario:  Verify is user is created succesfully using AddUserAPI excel
  Given   the base URL
  When    Verifying POST request with user data from all Excel
  Then    the response data should match the request from all Excel data