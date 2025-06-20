package api.tests;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndPoint;
import api.endpoints.UserEndPoint2;
import api.payload.User;
import api.utilities.ExtentReportManager;
import api.utilities.RetryAnalyzer;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class UserTests2 {

	Faker faker;
	User userPayload;
	Logger logger;

	
	@BeforeClass
	public void setup() {

		faker = new Faker();
		userPayload = new User();
		logger = LogManager.getLogger(this.getClass());

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setPassword(faker.internet().password());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
	}

	@Test(priority = 1)
	public void test_createUser() {
		
		logger.info("****************************************************************");
	    logger.info("***********<<<<<Starting test_createUser>>>>>*******************");
		
	    try {
	    	
		// Sending POST request to create a new user
	    	logger.info("Sending POST request to create a new user");
	    	Response postUserResponse = UserEndPoint2.createUser(userPayload);
       
        // Extracting and logging response time
        long responseTime = postUserResponse.getTime();
       
        // Assertion for response time
        Assert.assertTrue(responseTime >= 200 && responseTime <= 5000, "Response time is outside the expected range (200-2000ms).");
        Assert.assertEquals(postUserResponse.getStatusCode(), 200, "Expected status code is 200.");
        Assert.assertEquals(postUserResponse.getHeader("Content-Type"), "application/json", "Expected content type is application/json.");

        // Extracting and validating field types from response
        String jsonBody = postUserResponse.getBody().asString();
        JSONObject jsonObj = new JSONObject(jsonBody);
        Object codeField = jsonObj.get("code");
        Object typeField = jsonObj.get("type");
        Object messageField = jsonObj.get("message");

        logger.info("Asserting response field types");
        Assert.assertTrue(codeField instanceof Integer, "Code field should be Integer.");
        Assert.assertTrue(typeField instanceof String, "Type field should be String.");
        Assert.assertTrue(messageField instanceof String, "Message field should be String.");
        
     // Extracting and logging field values
        int code = postUserResponse.then().extract().path("code");
        String type = postUserResponse.then().extract().path("type");

        // Assertion for field values
        Assert.assertEquals(code, 200, "Expected code is 200.");
        Assert.assertEquals(type, "unknown", "Expected type is 'unknown'.");
        
     // Path to JSON schema
        String jsonFilePath = "C:\\Users\\ubaidullah.bawa\\eclipse-workspace\\PetStoreAutomation\\test-data\\postUserSchema.json";
        File schemaFile = new File(jsonFilePath);

        // Schema validation
        postUserResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
        
    } catch (AssertionError e) {
        logger.error("Test assertion failed: {}", e.getMessage());
        ExtentReportManager.getTest().fail("Test failed: " + e.getMessage());
        throw e;
        
    } catch (Exception e) {
        logger.error("Unexpected exception occurred: {}", e.getMessage());
        ExtentReportManager.getTest().fail("Unexpected exception: " + e.getMessage());
        throw e;
        
    } finally {
        logger.info("***** Finished test_createUser *****");
        logger.info("********************************************");
        ExtentReportManager.getTest().info("Test finished");
    }
        
	}

	@Test(priority = 2, retryAnalyzer = RetryAnalyzer.class)
	public void test_getUserByName() {
		logger.info("***** Starting test_getUserByName *****");
	    ExtentReportManager.getTest().info("Test for GET user response time started");
	
	    try {
	        Response getUserResponse = UserEndPoint2.readUser(userPayload.getUsername());

	        // Log and extract response time
	        long responseTime = getUserResponse.getTime();
	        String contentType = getUserResponse.getHeader("Content-Type");
	        String jsonBody = getUserResponse.body().asString();
	        JSONObject jsonObj = new JSONObject(jsonBody);
	        logger.info("Response body: " + jsonBody);

	        // Assertion for response time
	        Assert.assertTrue(responseTime >= 200 && responseTime <= 5000, "Response time is outside the expected range.");
	        Assert.assertEquals(contentType, "application/json", "Expected Content-Type is application/json.");
	        Assert.assertTrue(jsonObj.get("id") instanceof Integer, "Id field should be integer.");
	        Assert.assertTrue(jsonObj.get("username") instanceof String, "Username field should be String.");
	        Assert.assertTrue(jsonObj.get("firstName") instanceof String, "FirstName field should be String.");
	        Assert.assertTrue(jsonObj.get("lastName") instanceof String, "LastName field should be String.");
	        Assert.assertTrue(jsonObj.get("email") instanceof String, "Email field should be String.");
	        Assert.assertTrue(jsonObj.get("password") instanceof String, "Password field should be String.");
	        Assert.assertTrue(jsonObj.get("phone") instanceof String, "Phone field should be String.");
	        Assert.assertTrue(jsonObj.get("userStatus") instanceof Integer, "UserStatus field should be Integer.");
	       
	        String username = getUserResponse.then().extract().path("username");
	        String firstName = getUserResponse.then().extract().path("firstName");
	        String lastName = getUserResponse.then().extract().path("lastName");
	        String email = getUserResponse.then().extract().path("email");
	        String password = getUserResponse.then().extract().path("password");
	        String phone = getUserResponse.then().extract().path("phone");
	        int userStatus = getUserResponse.then().extract().path("userStatus");
	
	        // Assertions for field values
	        Assert.assertEquals(username, userPayload.getUsername(), "Username mismatch.");
	        Assert.assertEquals(firstName, userPayload.getFirstName(), "FirstName mismatch.");
	        Assert.assertEquals(lastName, userPayload.getLastName(), "LastName mismatch.");
	        Assert.assertEquals(email, userPayload.getEmail(), "Email mismatch.");
	        Assert.assertEquals(password, userPayload.getPassword(), "Password mismatch.");
	        Assert.assertEquals(phone, userPayload.getPhone(), "Phone mismatch.");
	        Assert.assertEquals(userStatus, userPayload.getUserStatus(), "UserStatus mismatch.");
	        
	    } 
	    catch (Exception e) {
	        logger.error("Unexpected exception occurred: {}", e.getMessage());
	        ExtentReportManager.getTest().fail("Unexpected exception: " + e.getMessage());
	        throw e;
	    } 
	    finally {
	        logger.info("***** Finished test_getUserByName *****");
	        ExtentReportManager.getTest().info("Test finished");
	    }
	}

	@Test(priority = 3,  retryAnalyzer = RetryAnalyzer.class)
	public void test_updateUserByName() {
		// Start the test and log the initial info
	    ExtentReportManager.getTest().info("Test for updating user started");
	    logger.info("********************************************");
	    logger.info("***** Starting test_updateUser *****");
	
	    try {
	        // Update the user details with random data
	        userPayload.setFirstName(faker.name().firstName());
	        userPayload.setLastName(faker.name().lastName());
	        userPayload.setEmail(faker.internet().safeEmailAddress());
	        
	        // Send PUT request to update user
	       Response putUserResponse = UserEndPoint2.updateUser(userPayload.getUsername(), userPayload);
	
	        // Log and verify the response
	        int statusCode = putUserResponse.getStatusCode();
	        long resTime = putUserResponse.getTime();	
	        String contentType = putUserResponse.getHeader("Content-Type");
	        
	        // Assert status code
	        Assert.assertEquals(statusCode, 200, "Expected status code is 200.");
	        Assert.assertTrue(resTime >= 200 && resTime <= 5000, "Response time is outside the expected range (200-2000ms).");
	    	Assert.assertEquals(contentType, "application/json", "Expected Content-Type is application/json.");
	        
	        String jsonFilePath = "C:\\Users\\ubaidullah.bawa\\eclipse-workspace\\PetStoreAutomation\\test-data\\putUserSchema.json";
	        File file = new File(jsonFilePath);
	
	        putUserResponse.then().body(JsonSchemaValidator.matchesJsonSchema(file));
	
	
	    } 
	    catch (AssertionError e) {
	        logger.error("Test assertion failed: {}", e.getMessage());
	        ExtentReportManager.getTest().fail("Test failed: " + e.getMessage());
	        throw e;
	    } 
	    catch (Exception e) {
	        logger.error("Unexpected exception: {}", e.getMessage());
	        ExtentReportManager.getTest().fail("Unexpected exception: " + e.getMessage());
	        throw e;
	    } 
	    finally {
	        logger.info("***** Finished test_updateUser *****");
	        logger.info("********************************************");
	        ExtentReportManager.getTest().info("Test finished");
	    }
	}

	@Test(priority = 4, retryAnalyzer = RetryAnalyzer.class)
	public void test_deleteUserByName() {
		Response deleteUserResponse = UserEndPoint2.deleteUser(this.userPayload.getUsername());
		deleteUserResponse.then().log().all();

		Assert.assertEquals(deleteUserResponse.getStatusCode(), 200);
		// Path to JSON schema
        String jsonFilePath = "C:\\Users\\ubaidullah.bawa\\eclipse-workspace\\PetStoreAutomation\\test-data\\deleteUserSchema.json";
        File schemaFile = new File(jsonFilePath);

        // Schema validation
        deleteUserResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
	}
	
	@Test(priority = 5, description = "This test verifies that logging in a user returns a 200 status code")
	public void test_loginUser() {
	    logger.info("********************************************");
	    logger.info("********* Starting test_loginUser **********");
	
	    try {
	        // Send login request
	        Response getLoginUserResponse = UserEndPoint2.loginUser(userPayload.getUsername(), userPayload.getPassword());
	        
	        long resTime = getLoginUserResponse.getTime();
	        String jsonBody = getLoginUserResponse.body().asString();

	        // Parse JSON and check field types
	        JSONObject jsonObj = new JSONObject(jsonBody);
	        Object codeField = jsonObj.get("code");
	        Object typeField = jsonObj.get("type");
	        Object messageField = jsonObj.get("message");


	        // Log response status and body
	      	Assert.assertEquals(getLoginUserResponse.getStatusCode(), 200);
	        Assert.assertTrue(resTime >= 200 && resTime <= 5000, "Response time is outside the expected range");
	        Assert.assertEquals(getLoginUserResponse.getHeader("Content-Type"), "application/json", "Expected Content-Type is application/json");

	        Assert.assertTrue(codeField instanceof Integer, "Code field should be an Integer.");
	        Assert.assertTrue(typeField instanceof String, "Type field should be a String.");
	        Assert.assertTrue(messageField instanceof String, "Message field should be a String.");
	        
	     // Extract field values from the response
	        int code = getLoginUserResponse.then().extract().path("code");
	        String type = getLoginUserResponse.then().extract().path("type");
	        String message = getLoginUserResponse.then().extract().path("message");
	
	        // Assert field values
	        Assert.assertEquals(code, 200, "Expected code is 200");
	        Assert.assertEquals(type, "unknown", "Expected type is 'unknown'");
	        Assert.assertTrue(message.contains("logged in"), "Message should contain 'logged in'");
	    
	        // Load JSON schema file and validate response
	        String jsonFilePath = "C:\\Users\\ubaidullah.bawa\\eclipse-workspace\\PetStoreAutomation\\test-data\\getUserLoginSchema.json";
	        File file = new File(jsonFilePath);
	        logger.info("Asserting that the response matches the JSON schema from {}", jsonFilePath);
	        getLoginUserResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
	       
	    } 
	    catch (AssertionError e) {
	        logger.error("Test assertion failed: {}", e.getMessage());
	        ExtentReportManager.getTest().fail("Test failed: " + e.getMessage());
	        throw e;
	    } 
	    finally {
	        logger.info("***** Finished test_loginUser***************");
	        logger.info("********************************************");
	    }
	}
	
	//Logout user api	
	@Test(priority = 6, description = "This test verifies that the logout response returns a 200 status code")
	public void test_logoutUser() 
	{
	    logger.info("********************************************");
	    logger.info("******** Starting test_logoutUser***********");
	    
	    try {
	        // Log the start of the test
	        ExtentReportManager.getTest().info("Testing logout user for correct status code");
	        
	        // Sending the logout request
	        Response getLogoutUsertResponse = UserEndPoint2.logoutUser();
	        long resTime = getLogoutUsertResponse.getTime();

	        
	        // Asserting status code
	        Assert.assertEquals(getLogoutUsertResponse.getStatusCode(), 200, "Expected status code is 200.");
	        Assert.assertTrue(resTime >= 200 && resTime <= 5000, "Expected response time between 200 and 2000ms.");
	        Assert.assertEquals(getLogoutUsertResponse.getHeader("Content-Type"), "application/json", "Expected content type is application/json.");

	        // Extract response body and assert field types
	        String jsonBody = getLogoutUsertResponse.body().asString();
	        JSONObject jsonObj = new JSONObject(jsonBody);
	        
	        Assert.assertTrue(jsonObj.get("code") instanceof Integer, "Code field should be an integer.");
	        Assert.assertTrue(jsonObj.get("type") instanceof String, "Type field should be a string.");
	        Assert.assertTrue(jsonObj.get("message") instanceof String, "Message field should be a string.");
	      
	        // Extract and assert specific field values
	        int code = getLogoutUsertResponse.then().extract().path("code");
	        String type = getLogoutUsertResponse.then().extract().path("type");

	        Assert.assertEquals(code, 200, "Expected code is 200.");
	        Assert.assertEquals(type, "unknown", "Expected type is 'unknown'.");
	    
	        // Validate JSON schema
	        String jsonFilePath = "C:\\Users\\ubaidullah.bawa\\eclipse-workspace\\PetStoreAutomation\\test-data\\getUserLogoutSchema.json";
	        File file = new File(jsonFilePath);
	        getLogoutUsertResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
	    
	    } 
	    catch (AssertionError e) 
	    {
	        logger.error("Assertion failed: {}", e.getMessage());
	        ExtentReportManager.getTest().fail("Test failed: " + e.getMessage());
	        throw e;  // Rethrow for test failure
	    } 
	    finally 
	    {
	        logger.info("***** Finished test_logoutUser *****");
	        logger.info("************************************");
	        ExtentReportManager.getTest().info("Test finished");
	    }
	}
}
