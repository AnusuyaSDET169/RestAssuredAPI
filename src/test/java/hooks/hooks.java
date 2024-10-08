package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import static io.restassured.RestAssured.*;
public class hooks {



public static String baseURI;

@Before
public void setup() {
    baseURI = System.getProperty("baseUrl", "https://userserviceapp-f5a54828541b.herokuapp.com/uap");
}
    @After
    public void teardown()
    {
    	
    	
    }
	
}