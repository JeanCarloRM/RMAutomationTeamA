package api;

import Framework.ExternalVariablesManager;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import entities.ResourceEntity;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by ArielWagner on 10/12/2015.
 */
public class APIManager {

    final static Logger logger = Logger.getLogger(APIManager.class);

    public APIManager() {
        logger.info("API Manager initialized");
        String baseURI = ExternalVariablesManager.getInstance().getRoomManagerService();
        RestAssured.baseURI = baseURI;
        RestAssured.useRelaxedHTTPSValidation();
    }

    /**
     * This method allows get a JSONObject
     * @param endPoint
     * @return a JSONObject
     */
    public JSONObject getJson(String endPoint) {
        Response response = given().when().get(endPoint);
        JSONObject jsonObject = new JSONObject(response.asString());
        return jsonObject;
    }

    //Todo
    public String getToken()
    {
        String user = ExternalVariablesManager.getInstance().getAdminUserName();
        String password = ExternalVariablesManager.getInstance().getAdminUserPassword();
        String response = given().log().all().contentType("application/json").
                body("{\"username\":\""+user+"\",\"password\":\""+password+"\",\"authentication\": \"local\"}").
                when().
                post("/login").asString();
        JSONObject tokenJson = new JSONObject(response);
        return tokenJson.getString("token");
    }

    /**
     * send a delete method to the API
     * @param endPoint
     */
    public void delete(String endPoint){
        given().log().all().
                headers("Authorization", "jwt "+getToken()).
                when().delete(endPoint).
                then().log().all().
                statusCode(200);
    }

    /**
     * Create a resource using token
     * @param resource
     */
    public void postResource(ResourceEntity resource){
        given()
                .header("Authorization", "jwt " + getToken())
                .parameters("name", resource.getName(), "description", resource.getDisplayName(),
                        "customName", resource.getDisplayName(), "from", "",
                        "fontIcon", "fa " + resource.getIconName())
                .post("/resources");
    }

    /**
     * return a JSon array from a request
     * @param endPoint
     * @return
     */
    public JSONArray getArrayJson(String endPoint) {
        Response response = given().when().get(endPoint);
        JSONArray array = new JSONArray(response.asString());
        return array;
    }

    /**
     * method delete with basic authentication
     * @param endPoint
     * @param userName
     * @param userPassword
     */
    public void deleteBasic(String endPoint, String userName, String userPassword){
        given().log().all().
                auth().basic(userName, userPassword).
                when().delete(endPoint).
                then().log().all().
                statusCode(200);
    }

}