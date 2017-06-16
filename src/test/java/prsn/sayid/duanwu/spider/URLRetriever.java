package prsn.sayid.duanwu.spider;

/**
 * Created by emmet on 2017/6/15.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class URLRetriever {

    private static final String LINK_FEATURE= "a[bref]";
    private static final String MEDIA_FEATURE= "[src]";
    private static final String IMPORT_FEATURE= "link[href]";

    public static URLRetriever mkRetrieverByUrl(String url) throws IOException{
        return new URLRetriever(url);
    }

    private final Document doc;
    private URLRetriever(String url) throws IOException {
        doc = Jsoup.connect(url).get();
    }

    public List<String> listLinks() { //Inner Link
        return doc.select(LINK_FEATURE).stream().map(a-> a.attr("abs:src"))
                .collect(Collectors.toList());
    }

    public List<String> listmedias() { //
        return doc.select(MEDIA_FEATURE).stream().map(a-> a.attr("abs:src"))
                .collect(Collectors.toList());

    }

    public List<String> listImports() { //Outter link
        return doc.select(IMPORT_FEATURE).stream().map(a-> a.attr("abs:src"))
                .collect(Collectors.toList());
    }
}
