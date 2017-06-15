package com.ucap.duanwu;

import com.ucap.duanwu.htmlpage.FramePage;
import com.ucap.duanwu.htmlpage.HtmlPage;
import com.ucap.duanwu.htmlpage.PageParser;
import prsn.sayid.duanwu.Persistence.ParseredPersistence;

import java.io.InputStream;
import java.net.URL;

public class App
{
    static
    {
        System.setProperty("com.ucap.logger",
                "prsn.sayid.common.LoggerAdapterSayid");
        System.setProperty("com.ucap.PageParser",
                "prsn.sayid.duanwu.htmlpage.PageParserSayidImp");
    }
    public static void main(String args[]) throws Exception
    {
        String url = null;
        if (args.length == 0)
        {

            url = System.console().readLine();System.out.println("Input a url: ");
        }
        else
        {
            url = args[0];
        }

        FramePage frame = null;
        try(InputStream input = (new URL(url)).openStream())
        {
            PageParser pageParser = HtmlPage.makePageParser();
            pageParser.setParserDeepth(5);
            frame = pageParser.doParse(input, "UTF-8", url);
            if (frame == null)
                System.out.println("Parser resualt is null!");
            else {
                System.out.println("SimHash: " + frame.eigenvalue());
                System.out.println("Distance : " + frame.distance(null));
            }
        }
        if (frame != null)
        {
            ParseredPersistence.putServer("192.168.1.142",27017);
            ParseredPersistence p = ParseredPersistence.getPersistence(true);
            p.saveFramePageValue(frame.vo(), "www.sina.com", System.currentTimeMillis());
        }
    }
}
