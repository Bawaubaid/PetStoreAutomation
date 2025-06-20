package api.endpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.ResourceBundle;

import api.path.Routes;
import api.payload.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserEndPoint2 {

public static ResourceBundle getURL() {
		
		ResourceBundle links = ResourceBundle.getBundle("links");
		return links;
	}
	
	public static Response createUser(User payload) {
		
		String postURL = getURL().getString("user_post_url");
		
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload).when()
				.post(postURL);
		return response;
	}

	public static Response readUser(String username)  {

		String getURL = getURL().getString("user_get_url");
		
		Response response = given().pathParam("username", username).when().get(getURL);
		
		return response;
	}

	public static Response updateUser(String username, User payload) {

		String putURL = getURL().getString("user_put_url");
		
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON)
				.pathParam("username", username).body(payload).when().put(putURL);
		return response;
	}
	
	public static Response deleteUser(String username) {

		String deleteURL = getURL().getString("user_delete_url");
		
		Response response = given().pathParam("username", username).when().delete(deleteURL);
		return response;
	}
	
	public static Response loginUser(String userName, String password)
	{
		String getURL1 = getURL().getString("user_get_url1");
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username", userName);
		data.put("password", password);

		Response response = RestAssured.given().queryParam("api_key", "special-key").body(data)
		.when().get(getURL1); 
		return response;
	}
	
	
	public static Response logoutUser()
	{
		
		String getURL2 = getURL().getString("user_get_url2");
		
		Response response = RestAssured.given()	
				.when().get(getURL2); 
		return response;
	}
	
	
}
