package prsn.sayid.duanwu.spider;

/**
 * Created by emmet on 2017/6/15.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static URLRetriever mkRetrieverByUrl(String url, String baseUrl) throws IOException{
        return new URLRetriever(url, baseUrl);
    }

    private final Document doc;
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

    public List<String> listImportsInStyles() {
        List<String> styles = doc.select("style").stream()
                .map(a->a.toString()).collect(Collectors.toList());
        if (styles.size() > 0) {
            System.out.println("There are some styles: ");
            LinkedList<String> retList = new LinkedList<>();
            List ret = styles.stream().map(a -> parserUrlsFromStyle(a)).reduce(retList, (y, x)->{
                y.addAll(x);
                return y;
            });
            return ret;
        }
        return new ArrayList<>();
    }

    private List<String> parserUrlsFromStyle(String style) {
        String s = style.substring("<style>".length(), style.length() - "</style>".length() - 1);

        System.out.println(s);
        Pattern pattern = Pattern.compile("url\\s*\\(\\S*\\)");
        Matcher m = pattern.matcher(s);
        LinkedList<String> retList = new LinkedList<>();
        while (m.find()) {
            String g = m.group();
            g = g.substring(g.indexOf('(') + 1, g.length() - 1).trim();
            if (g.startsWith("/")) g=baseUrl + g;
            System.out.println(g);
            retList.add(g);
        }
        return retList;
    }
 }
