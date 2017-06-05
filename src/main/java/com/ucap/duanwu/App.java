package com.ucap.duanwu;

import com.ucap.duanwu.htmlpage.HtmlPage;
import com.ucap.duanwu.htmlpage.PageParser;
import com.ucap.duanwu.htmlpage.FramePage;
import prsn.sayid.duanwu.htmlpage.PageParserSayidImp;
import java.net.URL;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Console;

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
            FramePage frame = pageParser.doParse(input, "UTF-8");
            if (frame == null)
                System.out.println("Parser resualt is null!");
            else
                System.out.println("SimHash: " + frame.simHash());
        }
    }
}
