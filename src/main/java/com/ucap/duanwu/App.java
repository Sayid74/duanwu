package com.ucap.duanwu;

import com.ucap.duanwu.htmlpage.FramePage;
import com.ucap.duanwu.htmlpage.HtmlPage;
import com.ucap.duanwu.htmlpage.PageParser;

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
            System.out.println("Input a url: ");
            url = System.console().readLine();
        }
        else
        {
            url = args[0];
        }

        try(InputStream input = (new URL(url)).openStream())
        {
            PageParser pageParser = HtmlPage.makePageParser();
            pageParser.setParserDeepth(5);
            FramePage frame = pageParser.doParse(input, "UTF-8", url);
            if (frame == null)
                System.out.println("Parser resualt is null!");
            else {
                System.out.println("SimHash: " + frame.simHash());
                System.out.println("Distance : " + frame.distance(null));
            }
        }
    }
}
