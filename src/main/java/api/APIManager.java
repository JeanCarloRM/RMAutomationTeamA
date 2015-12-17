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

    public static APIManager instance;
    final static Logger logger = Logger.getLogger(APIManager.class);

    public static APIManager getInstance() {
        if(instance==null)
        {
            logger.info("API Manager initialized");
            instance = new APIManager();
            String baseURI = ExternalVariablesManager.getInstance().getRoomManagerService();
            RestAssured.baseURI = baseURI;
            RestAssured.useRelaxedHTTPSValidation();
        }
        return instance;
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
        String response = given().contentType("application/json").
                body("{\"username\":\""+user+"\",\"password\":\""+password+"\",\"authentication\": \"local\"}").
                when().
                post("/login").asString();
        JSONObject tokenJson = new JSONObject(response);
        return tokenJson.getString("token");
    }

    /**
     * send a deleteWithToken method to the API
     * @param endPoint
     */
    public void deleteWithToken(String endPoint){
        given().
                headers("Authorization", "jwt "+getToken()).
                when().delete(endPoint).
                then().
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
     * method deleteWithToken with basic authentication
     * @param endPoint
     * @param userAuthentication
     */
    public void deleteBasic(String endPoint, String userAuthentication){
        given()
            .header("Authorization", "Basic "+userAuthentication)
            .when().delete(endPoint);
    }

    /**
     * method for create a meeting with basic authentication
     * @param meeting
     * @param endPoint
     */
    public void postMeeting(JSONObject meeting, String endPoint, String userAuthentication){
        given()
            .contentType("application/json")
            .header("Authorization", "Basic " + userAuthentication)
            .content(meeting.toString())
            .when()
            .post(endPoint);
    }
}
