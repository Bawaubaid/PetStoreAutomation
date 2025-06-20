package api.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoint;
import api.payload.User;
import api.utilities.DataProviders;
import api.utilities.RetryAnalyzer;
import io.restassured.response.Response;

public class DDTests {
	
	Logger logger = LogManager.getLogger(this.getClass());
	
	@Test(priority = 1, dataProvider = "ExcelData",dataProviderClass = DataProviders.class)
	public void test_DDT_createUser(String userID, String userName, String fName, String lName, String email, String pwd, String phone) throws InterruptedException {
		
		logger.info("***************Starting DDT create user tests***************");
		User userPayload = new User();
		
		userPayload.setId(Integer.parseInt(userID));
		userPayload.setUsername(userName);
		userPayload.setFirstName(fName);
		userPayload.setLastName(lName);
		userPayload.setEmail(email);
		userPayload.setPassword(pwd);
		userPayload.setPhone(phone);
		
		
		Response response= UserEndPoint.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***************Finished DDT create user tests***************");
	}
	
	@Test(priority = 4, dataProvider = "UserNames", dataProviderClass = DataProviders.class,retryAnalyzer = RetryAnalyzer.class)
	public void test_DDT_deleteUsers(String userName) throws InterruptedException {
		Response response = UserEndPoint.deleteUser(userName);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}
