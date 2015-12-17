package db;

import org.apache.log4j.Logger;

/**
 * Created by ArielWagner on 14/12/2015.
 */
public class DBOutOfOrdersMethods {
    MongoDBManager mongoDBManager;
    final static Logger logger = Logger.getLogger(DBOutOfOrdersMethods.class);

    public DBOutOfOrdersMethods() {
        logger.info("Using DBOutOfOrdersMethods");
        mongoDBManager = MongoDBManager.getInstance();
    }

    /**
     * This method allows get the out of order Id
     * @param value
     * @return a String (id)
     */
    public String getOutOfOrderId(String value) {
        String outOfOrderId = mongoDBManager.getId("outoforders", "title", value);
        return outOfOrderId;
    }
}
