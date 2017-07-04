package sayid.spider;

/**
 * Created by emmet on 2017/6/15.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The type Url retriever.
 */
public class URLRetriever {

    private static final String LINK_FEATURE= "a[href]";
    private static final String MEDIA_FEATURE= "[src]";
    private static final String IMPORT_FEATURE= "link[href]";

    /**
     * Mk retriever by url.
     *
     * @param url the url
     * @return the url retriever
     * @throws IOException the io exception
     */

    private final String url;
    private final String baseUrl;
    private final Document doc;

    /**
     * Mk retriever by url url retriever.
     *
     * @param url     the url
     * @param baseUrl the base url
     * @return the url retriever
     * @throws IOException the io exception
     */
    public static URLRetriever mkRetrieverByUrl(String url, String baseUrl) throws IOException{
        return new URLRetriever(url, baseUrl);
    }

    private URLRetriever(String url, String baseUrl) throws IOException {
        this.url = url;
        this.baseUrl= baseUrl;
        doc = Jsoup.connect(url).get();
    }

    /**
     * List links list.
     *
     * @return the list contains some links.
     */
    public List<String> listLinks() { //Inner Link
        return doc.select(LINK_FEATURE).stream().map(a-> a.attr("abs:href"))
                .collect(Collectors.toList());
    }

    /**
     * Listmedias list.
     *
     * @return the list contains some media source links.
     */
    public List<String> listMedias() { //
        return doc.select(MEDIA_FEATURE).stream().map(a-> a.attr("abs:src"))
                .collect(Collectors.toList());

    }

    /**
     * List imports list.
     *
     * @return the list
     */
    public List<String> listImports() { //Outter link
        return doc.select(IMPORT_FEATURE).stream().map(a-> a.attr("abs:href"))
                .collect(Collectors.toList());
    }

    /**
     * Gets doc.
     *
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

 }
