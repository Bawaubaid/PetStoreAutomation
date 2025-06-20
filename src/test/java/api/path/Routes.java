package api.path;

	// In this class we will store all URL's. 

public class Routes {
	
	public static String base_url = "https://petstore.swagger.io/v2";
	
	
	// User Module URL's
	
	public static String user_post_url = base_url +"/user"; //Create user
	public static String user_post_url1 = base_url +"/user/createWithArray"; //Creates list of users with given input array
	public static String user_post_url2 = base_url +"/user/createWithList"; //Creates list of users with given input array
	public static String user_get_url = base_url +"/user/{username}"; //Get user by user name
	public static String user_get_url1 = base_url +"/user/login"; //Logs user into the system
	public static String user_get_url2 = base_url +"/user/logout"; //Logs out current logged in user session
	public static String user_put_url =  base_url +"/user/{username}"; //Updated user
	public static String user_delete_url = base_url +"/user/{username}"; //Delete user
	
	// Pet Module URL's
	
	public static String pet_post_url = base_url +"/pet"; //Add a new pet to the store
	public static String pet_post_url1 = base_url +"/pet/{petId}"; //Updates a pet in the store with form data
	public static String pet_post_url2 = base_url +"/pet/{petId}/uploadImage"; //uploads an image
	public static String pet_put_url =  base_url +"/pet"; //Update an existing pet
	public static String pet_get_url = base_url +"/pet/findByStatus"; //Finds Pets by status
	public static String pet_get_url2 = base_url +"/pet/findByTags"; //Finds Pets by tags
	public static String pet_get_url1 = base_url +"/pet/{petId}"; //Find pet by ID
	public static String pet_delete_url = base_url +"/pet/{petId}"; //Deletes a pet

	// Store Module URL's
	
	public static String store_post_url = base_url +"/store/order"; //Place an order for a pet
	public static String store_get_url = base_url +"/store/order/{orderId}"; //Find purchase order by ID
	public static String store_get_url1 = base_url +"/store/inventory"; //Returns pet inventories by status
	public static String store_delete_url = base_url +"/store/order/{orderId}"; //Delete purchase order by ID
	

	//We get method not allowed in response when we send wrong request like for get URL we send put request.
	
	
	

}
