package prsn.sayid.duanwu.Persistence;

/**
 * Created by emmet on 2017/6/12.
 */
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.ucap.commons.logger.LFactory;
import com.ucap.commons.logger.LoggerAdapter;
import com.ucap.duanwu.htmlpage.FramePage;
import org.bson.Document;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ParseredPersistence
{
    private static final LoggerAdapter L = LFactory.makeL(ParseredPersistence.class);
    private static final String COLLECTION_NAME = "FramePages";
    private static final String DATABASE_NAME = "duanwu";
    private static final LinkedList<ServerAddress> serverAddresses = new LinkedList<>();
    private static final LinkedBlockingQueue<ParseredPersistence> processors
            = new LinkedBlockingQueue<>();

    private static UUID globalUUID = UUID.randomUUID();

    public static void putServer(String url, int port)
    {
        for(ServerAddress a: serverAddresses)
        {
            if ((url.equals(a.getHost())) && (port == a.getPort())) return;
        }
        serverAddresses.add(new ServerAddress(url, port));
        globalUUID = UUID.randomUUID();
    }

    public static ParseredPersistence getPersistence(boolean makeNew)
    {
        if ((processors.isEmpty()) || (makeNew))
            return new ParseredPersistence(new MongoClient(serverAddresses));
        else
        {
            try {
                return processors.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                L.error(e);
                return null;
            }
        }
    }

    private final UUID _uuid = globalUUID;
    private MongoClient mongoClnt ;

    private ParseredPersistence(MongoClient mongoClnt)
    {
        this.mongoClnt = mongoClnt;
    }

    public synchronized void saveFramePageValue(FramePage.ValueObj value, String websiteHost
            , long loadingDate)
    {
        Document doc = new Document();
        doc.put("website", websiteHost);
        doc.put("uri", value.uri());
        doc.put("md5", value.md5());
        doc.put("eigenvalue", value.eigenvalue());
        doc.put("loadingData", loadingDate);
        doc.put("savingDate", System.currentTimeMillis());
        MongoDatabase db = mongoClnt.getDatabase(DATABASE_NAME);
        db.getCollection(COLLECTION_NAME).insertOne(doc);

        if (this._uuid == ParseredPersistence.globalUUID)
            processors.add(this);
        else {
            mongoClnt.close();
            mongoClnt = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        mongoClnt.close();
        super.finalize();
    }
}
