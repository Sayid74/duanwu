package ucap;

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

        /*
        FrameDigest frame = null;
        try(InputStream input = (new URL(url)).openStream())
        {
            PageParser pageParser = HtmlPage.makePageParser();
            pageParser.setParserDeepth(5);
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
            p.saveValue(frame.persistenceObj(), "www.sina.com", System.currentTimeMillis());
        }
        */

    }
}
