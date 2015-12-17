package db;
import org.apache.log4j.Logger;
import java.util.ArrayList;
/**
 * User: Jean Carlo Rodriguez
 * Date: 12/11/15
 * Time: 10:44 AM
 */
public class DBResourcesMethods {
    MongoDBManager mongoDBManager;
    final static Logger logger = Logger.getLogger(DBResourcesMethods.class);
    public DBResourcesMethods() {
        logger.info("Using DBResourcesMethods");
        mongoDBManager = MongoDBManager.getInstance();
    }
    /**
     * Search resources in the DB by field applying the criteria
     * @param field
     * @param criteria
     * @return a string array
     */
    public ArrayList<String> filterResourcesByCriteria(String field, String criteria) {
        ArrayList<String> resourceList = mongoDBManager.filterByCriteria("resourcemodels",field,criteria);
        mongoDBManager.close();
        return resourceList;
    }
    /**
     * Get the resource id using the resource name
     * @param name
     * @return
     */
    public String getResourceId(String name) {
        String resourceId = MongoDBManager.getInstance().getId("resourcemodels", "name", name);
        return resourceId;
    }
}