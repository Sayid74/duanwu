package sayid.Persistence;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import ucap.commons.logger.LoggerAdapter;
import ucap.htmlpage.FrameDigest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.when;

/**
 * Created by emmet on 2017/6/26.
 */
public class ParseredPersistenceImpTest {
    @Mock
    LoggerAdapter L;
    @Mock
    LinkedList<ServerAddress> serverAddresses;
    @Mock
    LinkedBlockingQueue<ParseredPersistenceImp> processors;
    //Field _uuid of type UUID - was not mocked since Mockito doesn't mock a Final class
    @Mock
    MongoClient mongoClnt;

    @InjectMocks
    ParseredPersistenceImp spy;

    @Mock
    FrameDigest.PersistenceObj persistenceObj;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testPutServer() throws Exception {
        String _u1 = ParseredPersistenceImp.getGlobalUUID();
        ParseredPersistenceImp.putServer("192.168.1.142", 27017);
        String _u2 = ParseredPersistenceImp.getGlobalUUID();
        Assert.assertFalse(_u1.equals(_u2));

        ParseredPersistenceImp.putServer("192.168.1.142", 27017);
        String _u3 = ParseredPersistenceImp.getGlobalUUID();
        Assert.assertTrue(_u2.equals(_u3));
    }

    @Test
    public void testGetPersistence() throws Exception {
        ParseredPersistenceImp.putServer("192.168.1.142", 27017);
        spy = ParseredPersistenceImp.getPersistence(true);
        Assert.assertNotNull(spy);
    }

    @Ignore
    @Test
    public void testSaveValue() throws Exception {
        testGetPersistence();

        String websiteHost = "http://www.sina.com.cn";
        when(persistenceObj.uri()).thenReturn("http://www.sina.com.cn/index.html");
        when(persistenceObj.md5()).thenReturn(BigInteger.valueOf(123456789));
        when(persistenceObj.eigenvalue()).thenReturn(BigInteger.valueOf(987654321));

        long loadingDate = System.currentTimeMillis() - 1000 * 3600 * 24;
        spy.saveValue(persistenceObj, websiteHost, loadingDate);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme