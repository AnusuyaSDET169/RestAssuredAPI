import pojo.userAddress;

public class TestDatafile {

	
	public static String AddUser()
	{
		AddUser a=new AddUser();
		a.setuser_first_name("Yara");
		a.setuser_last_name("Yaruuu");
		a.setuser_contact_number("2106204555");
		a.setuser_email_id("yarakutty.v@gmail.com")
		
		userAddress ua=new userAddress();
		ua.setPlotNumber("pp-01");
		ua.setStreet("NorthLake Avenue");
		ua.setState("Texas");
		ua.setCountry("USA");
		ua.setZipCode("78727");
		
		a.setUseraddress(ua);
		return a;
		
	}
	
}
