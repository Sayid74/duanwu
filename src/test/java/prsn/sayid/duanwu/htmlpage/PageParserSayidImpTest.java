package prsn.sayid.duanwu.htmlpage;

import com.ucap.duanwu.htmlpage.FrameDigest;
import com.ucap.duanwu.htmlpage.HtmlPage;
import com.ucap.duanwu.htmlpage.NodeType;
import com.ucap.duanwu.htmlpage.PageParser;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import prsn.sayid.duanwu.Persistence.ParseredPersistenceImp;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by emmet on 2017/6/20.
 */
public class PageParserSayidImpTest {
    static
    {
        System.setProperty("com.ucap.logger",
                "prsn.sayid.common.LoggerAdapterSayid");
        System.setProperty("com.ucap.PageParser",
                "prsn.sayid.duanwu.htmlpage.PageParserSayidImp");
    }

    PageParser pageParser;
    String url = "http://www.sina.com.cn";
    @Before
    public void setUp() {
        try {
            pageParser = HtmlPage.makePageParser();
        } catch (InstantiationException | IllegalAccessException e) {
            Assert.fail(e.getLocalizedMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(pageParser);
        pageParser.setParserDeepth(20);
    }

    @Test
    public void testDoParse() throws Exception {
        FrameDigest frame = null;
        try(InputStream input = (new URL(url)).openStream())
        {
            frame = pageParser.doParse(input, "UTF-8", url);
            if (frame == null)
                System.out.println("Parser resualt is null!");
            else {
                System.out.println("SimHash: " + frame.persistenceObj().eigenvalue());
                System.out.println("Distance : " + frame.distance(null));
            }
        }
        if (frame != null)
        {
            ParseredPersistenceImp.putServer("192.168.1.142",27017);
            ParseredPersistenceImp p = ParseredPersistenceImp.getPersistence(true);
            p.saveFramePageValue(frame.persistenceObj(), "www.sina.com", System.currentTimeMillis());
        }
        Assert.assertTrue(true);
    }

    @Test
    public void testDoParse2() throws Exception {
        FrameDigest frame = pageParser.doParse(url);
        Assert.assertTrue(true);
    }

    @Ignore
    @Test
    public void testNodeTypeOfE() throws Exception {
        NodeType result = PageParserSayidImp.nodeTypeOfE(new Element(null, "baseUri", new Attributes()));
        Assert.assertEquals(NodeType.BODY_, result);
    }


}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme