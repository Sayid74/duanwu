package prsn.sayid.duanwu.Persistence;

/**
 * Created by emmet on 2017/6/12.
 */
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ucap.duanwu.htmlpage.FramePage;
import org.bson.Document;

public class ParseredPersistence
{
    public static void main(String[] rgs)
        throws UnknownHostException, MongoException
    {
        MongoClient clnt  = new MongoClient(Arrays.asList(
                new ServerAddress("192.168.1.142", 27017)
        ));
        MongoDatabase db = clnt.getDatabase("test");
//        db.createCollection("table2");
        MongoCollection parseredContext = db.getCollection("table2");
        Document doc = new Document();
        doc.append("name", "test");
        doc.append("time", System.currentTimeMillis());
        parseredContext.insertOne(doc);
    }

    public void setFramePageValue(FramePage.ValueObj value, long loadingDate, long savingDate)
    {
        Document doc = new Document();
        doc.put("uri", value.uri());
        doc.put("md5", value.md5());
        doc.put("eigenvalue", value.eigenvalue());
    }
}
