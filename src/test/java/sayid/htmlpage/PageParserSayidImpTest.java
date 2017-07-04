package sayid.htmlpage;

import sayid.Persistence.ParseredPersistenceImp;
import sayid.spider.Spider;
import ucap.htmlpage.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

/**
 * The type Page parser sayid imp test.
 */
public class PageParserSayidImpTest {
    static {
        System.setProperty("com.ucap.logger",
                "prsn.sayid.common.LoggerAdapterSayid");
        System.setProperty("com.ucap.PageParser",
                "prsn.sayid.duanwu.htmlpage.PageParserSayidImp");
    }

    /**
     * The Page parser.
     */
    PageParser pageParser;
    /**
     * The Url.
     */
    String url = "http://www.sina.com.cn";

    /**
     * Sets up.
     */
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

    /**
     * Test do parse.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDoParse() throws Exception {
        FrameDigest frame = null;
        try(InputStream input = (new URL(url)).openStream())
        {
            frame = pageParser.doParse(input, "UTF-8", url);
            Assert.assertNotNull(frame);
            System.out.println("SimHash: " + frame.persistenceObj().eigenvalue());
        }
        if (frame != null)
        {
            ParseredPersistenceImp.putServer("192.168.1.142",27017);
            ParseredPersistenceImp p = ParseredPersistenceImp.getPersistence(true);
            p.saveValue(frame.persistenceObj(), "www.sina.com", System.currentTimeMillis());
        }
        Assert.assertTrue(true);
    }

    /**
     * Test do parse 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDoParse2() throws Exception {
        FrameDigest frame = pageParser.doParse(url);
        Assert.assertNotNull(frame);
        ParseredPersistenceImp.putServer("192.168.1.142", 27017);
        ParseredPersistenceImp.getPersistence(false)
                .saveValue(frame.persistenceObj()
                        , "www.sina.com", System.currentTimeMillis());
        Assert.assertTrue(true);
    }

    /**
     * Test node type of e.
     *
     * @throws Exception the exception
     */
    @Ignore
    @Test
    public void testNodeTypeOfE() throws Exception {
        NodeType result = PageParserSayidImp.nodeTypeOfE(new Element(null, "baseUri", new Attributes()));
        Assert.assertEquals(NodeType.BODY_, result);
    }

    @Ignore
    @Test
    public void testCrawlAndParser() throws Exception {
        String url = "https://fileinfo.com/extension/csv";
        String baseUrl = "https://fileinfo.com";
        ParseredPersistenceImp.putServer("192.168.1.142", 27017);
        Spider.doCrawl(url, baseUrl, (node, doc) ->{
            try {
                FrameDigest frame = pageParser.doParse(node.url);
                ParseredPersistenceImp.getPersistence(false)
                        .saveValue(frame.persistenceObj(), baseUrl, System.currentTimeMillis());
            } catch (PageParserException e) {
                e.printStackTrace();
            }
            return true;
        });
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme