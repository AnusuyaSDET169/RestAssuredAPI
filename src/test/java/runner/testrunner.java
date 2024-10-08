
package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features="src/test/resources/features/all.feature",
glue= {"stepdefinition","hooks"},
plugin={"pretty",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
		"html:target/cucumber-reports/restassured-api-testreport.html"}
		)

public class testrunner extends AbstractTestNGCucumberTests {
	 
}