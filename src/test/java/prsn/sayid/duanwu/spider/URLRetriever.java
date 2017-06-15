package prsn.sayid.duanwu.spider;

/**
 * Created by emmet on 2017/6/15.
 */

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static jdk.nashorn.internal.objects.Global.print;

import java.io.IOException;


public class URLRetriever {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = args[0];
        print("Feching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src: media) {
            if (src.tagName().equals("img"))
                print("* %s: <%s> %sx%s (%s) \n",
                        src.tagName() ,
                        src.attr("abs:src"),
                        src.attr("width"),
                        src.attr("height"),
                        src.attr("alt").substring(0, 20));
            else
                print(" * %s: <%s> \n", src.tagName(),
                        src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link: imports) {
            print("* %s <%s> (%s)",
                    link.tagName(),
                    link.attr("abs:href"),
                    link.text().substring(0, 35));
        }
    }
}
