package config;
import java.io.InputStream;
import java.util.Properties;


	

	public class ConfigProperties {

		private Properties properties = new Properties();

	    public ConfigProperties() {
	        loadProperties();
	    }

	    private void loadProperties() {
	        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
	            if (input == null) {
	                System.out.println("Sorry, unable to find config.properties");
	                return;
	            }
	            properties.load(input);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    public String getUsername() {
	        return properties.getProperty("username");
	    }

	    public String getPassword() {
	        return properties.getProperty("password");
	    }	
		
		
	
}
