package sayid.Persistence;

/**
 * Created by emmet on 2017/6/12.
 */
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucap.Persistence.FramePersistence;
import ucap.htmlpage.FrameDigest;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The type Parsered persistence.
 */
public class ParseredPersistenceImp implements FramePersistence {
    private static final Logger L = LoggerFactory.getLogger(FramePersistence.class);
    private static final String COLLECTION_NAME = "FramePages";
    private static final String DATABASE_NAME = "duanwu";
    private static final LinkedList<ServerAddress> serverAddresses = new LinkedList<>();
    private static final LinkedBlockingQueue<ParseredPersistenceImp> processors
            = new LinkedBlockingQueue<>();

    private static volatile UUID globalUUID = UUID.randomUUID();

    /**
     * Add a mongodb server host to a list recorder. The list structure
     * will used to select and connect to a mongodb server. If mongodb's
     * servers has been changed globalUUID value should be changed too.
     *
     * @param url  the uri or ip address represents host server address.
     * @param port the port bounds to the host server.
     */
    public static void putServer(String url, int port) {
        for(ServerAddress a: serverAddresses) {
            if ((url.equals(a.getHost())) && (port == a.getPort())) return;
        }
        serverAddresses.add(new ServerAddress(url, port));
        globalUUID = UUID.randomUUID();
    }

    public static String getGlobalUUID() {
        return globalUUID.toString();
    }

    /**
     * Gets persistence.
     *
     * @param makeNew If It is set true, method should make a new instance for result. Another else be set false,
     *                method select an old processor, if processors stack is null, method will create a new
     *                processor for result also.
     * @return the persistence
     */
    public static ParseredPersistenceImp getPersistence(boolean makeNew) {
        if ((processors.isEmpty()) || (makeNew))
            return new ParseredPersistenceImp(new MongoClient(serverAddresses));
        else {
            try {
                return processors.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                L.error(">>> getPersistence method with parameter makeNew: " + makeNew, e);
                return null;
            }
        }
    }

    private final UUID _uuid = globalUUID;
    private MongoClient mongoClnt ;

    private ParseredPersistenceImp(MongoClient mongoClnt) {
        this.mongoClnt = mongoClnt;
    }

    /**
     * Save the record value which represents the feature of a page frame.
     *
     * @param value       the value represents feature of a page frame.
     * @param websiteHost the website host
     * @param loadingDate the date when loading the page.
     */
    @Override
    public synchronized void saveValue(FrameDigest.PersistenceObj value
            , String websiteHost, long loadingDate) {
        Document doc = new Document();
        doc.put("website", websiteHost);
        doc.put("uri", value.uri());
        doc.put("md5", value.md5().toString());
        doc.put("eigenvalue", value.eigenvalue().toString());
        doc.put("loadingData", loadingDate);
        doc.put("savingDate", System.currentTimeMillis());
        MongoDatabase db = mongoClnt.getDatabase(DATABASE_NAME);
        db.getCollection(COLLECTION_NAME).insertOne(doc);

        // If mongodb's servers has been changed
        // globalUUID value should be changed too.
        // If globalUUID is changed, mongoClnt must
        // be closed immediately, and doesn't add
        // to processors list again.
        if (this._uuid == ParseredPersistenceImp.globalUUID)
            processors.add(this);
        else {
            mongoClnt.close();
            mongoClnt = null;
        }
    }

//    private static final String fillterWebName(String url) {
//        try {
//            return (new URL(url.trim())).getHost();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * MongoClnt it is mongodb client object. So it must be being
     * closed when finalize;
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        mongoClnt.close();
        super.finalize();
    }
}
