package api.endpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.util.HashMap;

import api.path.Routes;
import api.payload.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserEndPoint {

	public static Response createUser(User payload) {
		
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload).when()
				.post(Routes.user_post_url);
		return response;
	}

	public static Response readUser(String username)  {

		Response response = given().pathParam("username", username).when().get(Routes.user_get_url);
		
		return response;
	}

	public static Response updateUser(String username, User payload) {

		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON)
				.pathParam("username", username).body(payload).when().put(Routes.user_put_url);
		return response;
	}
	
	public static Response deleteUser(String username) {

		Response response = given().pathParam("username", username).when().delete(Routes.user_delete_url);
		return response;
	}
	
	public static Response loginUser(String userName, String password)
	{
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username", userName);
		data.put("password", password);

		Response response = RestAssured.given().queryParam("api_key", "special-key").body(data)
		.when().get(Routes.user_get_url1); 
		return response;
	}
	
	
	public static Response logoutUser()
	{
		Response response = RestAssured.given()	
				.when().get(Routes.user_get_url2); 
		return response;
	}
	
	
}
