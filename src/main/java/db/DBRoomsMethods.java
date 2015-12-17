package db;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.apache.log4j.Logger;
import org.bson.Document;
import java.util.ArrayList;
import static java.util.Arrays.asList;
/**
 * User: Jean Carlo Rodriguez
 * Date: 12/11/15
 * Time: 11:05 AM
 */
public class DBRoomsMethods {
    MongoDBManager mongoDBManager;
    final static Logger logger = Logger.getLogger(DBRoomsMethods.class);
    public DBRoomsMethods() {
        logger.info("Using DBRoomsMethods");
        mongoDBManager = MongoDBManager.getInstance();
    }
    /**
     * This method allows filter rooms by a criteria
     * @param criteria
     * @return an ArrayList of String
     */
    public ArrayList<String> filterRoomsByCriteria(String criteria) {
        final ArrayList<String> roomsList = new ArrayList<String>();
        MongoCollection<Document> roomsCollection = MongoDBManager.getInstance().getCollection("rooms");
        FindIterable<Document> rooms = roomsCollection
                .find(new Document("$or", asList( new Document(
                        "customDisplayName", new BasicDBObject("$regex", criteria)),
                        new Document("code", new BasicDBObject("$regex", criteria)),
                        new Document("DisplayName", new BasicDBObject("$regex", criteria)))));
        rooms.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                roomsList.add(document.get("customDisplayName").toString());
            }
        });
        mongoDBManager.close();
        return roomsList;
    }
    /**
     * This method allows get room id using the displayName
     * @param value
     * @return a String
     */
    public String getRoomId(String value) {
        mongoDBManager = MongoDBManager.getInstance();
        String roomId = mongoDBManager.getId("rooms", "customDisplayName", value);
        mongoDBManager.close();
        return roomId;
    }
}