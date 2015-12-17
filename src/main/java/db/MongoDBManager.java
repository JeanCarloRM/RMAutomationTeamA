package db;
import Framework.ExternalVariablesManager;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.eq;
/**
 * User: Jean Carlo Rodriguez
 * Date: 12/7/15
 * Time: 11:13 AM
 */
public class MongoDBManager {
    private static MongoDBManager instance;
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    public static MongoDBManager getInstance(){
        if(instance==null)
        {
            instance = new MongoDBManager();
            mongoClient = new MongoClient(ExternalVariablesManager.getInstance().getRoomManagerIP(), 27017);
            database = mongoClient.getDatabase("roommanager");
        }
        return instance;
    }
    /**
     * Gets a collection from the Data Base
     * @param collectionName
     * @return
     */
    public MongoCollection getCollection(String collectionName){
        return database.getCollection(collectionName);
    }
    /**
     * close the data base connection
     */
    public void close(){
        mongoClient.close();
        instance = null;
    }
    /**
     * Search in the data base with some criteria
     * @param collection
     * @param field
     * @param criteria
     * @return
     */
    public ArrayList<String> filterByCriteria(String collection, String field, String criteria) {
        final String fieldName = field;
        final ArrayList<String> list = new ArrayList<String>();
        MongoCollection<Document> documentsCollection = getCollection(collection);
        FindIterable<Document> rooms = documentsCollection
                .find(new Document(fieldName, new BasicDBObject("$regex", criteria)));
        rooms.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                list.add(document.get(fieldName).toString());
            }
        });
        return list;
    }
    /**
     * This method allows get Id
     * @param collection
     * @param findBy
     * @param value
     * @return a String
     */
    public String getId(String collection, String findBy, String value) {
        final Document[] idObject = new Document[1];
        String id;
        MongoCollection<Document> roomsCollection = getCollection(collection);
        FindIterable<Document> idList = roomsCollection.find(eq(findBy, value));
        idList.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                idObject[0] = document;
            }
        });
        if(idObject[0]!=null)
            id = idObject[0].get("_id").toString();
        else
            id= "";
        return id;
    }
}