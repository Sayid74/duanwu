package sayid.spider;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by emmet on 2017/6/15.
 */
public class Spider {

    /**
     * The type Node.
     */
    public final static class Node {
        public final int level;
        public final String url;

        /**
         * Instantiates a new Node.
         *
         * @param level the level
         * @param url   the url
         */
        Node(int level, String url) {
            this.level = level;
            this.url = url;
        }
    }

    /**
     * The constant MAX_SPIDER_COUNT.
     */
    public final static int MAX_SPIDER_COUNT = 10;
    /**
     * The constant MAX_LEVEL.
     */
    public final static int MAX_LEVEL = 8;

    private final static ArrayBlockingQueue<Spider> spiders
            = new ArrayBlockingQueue<Spider>(MAX_SPIDER_COUNT);
    private final static LinkedList<Node> nodes = new LinkedList<>();


    /**
     * Do crawl. Begin crawl.
     *
     * @param url       the url
     * @param action    the action
     */
    public static void doCrawl(String url, String baseUrl, CrawlingAction action) {

        for (int i = 0; i < MAX_SPIDER_COUNT; i++)
            spiders.add(new Spider(baseUrl));

        nodes.add(new Node(0, url));
        synchronized (nodes)
        {
            while (!nodes.isEmpty()) {
                Spider s = null;
                try {
                    s = spiders.poll(1, SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (s == null) continue;

                Node n = nodes.pollLast();
                nodes.addAll(s.crawlTo(n, action));
                spiders.add(s);
            }
        }
    }

    private final String baseUrl;
    private List<Node> crawlTo(Node n, CrawlingAction action) {
        int level = n.level + 1;
        if (level > MAX_LEVEL) return Collections.EMPTY_LIST;
        URLRetriever retriever;
        try {
            retriever = URLRetriever.mkRetrieverByUrl(n.url, baseUrl);
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }

        if ((action != null) && (! action.doAction(n, retriever.getDoc())))
            return Collections.EMPTY_LIST;

        List<Node> links = retriever.listLinks().stream()
                .map(a->new Node(level, a)).collect(Collectors.toList());
        List<Node> medias = retriever.listMedias().stream()
                .map(a->new Node(level, a)).collect(Collectors.toList());
        List<Node> imports = retriever.listImports().stream()
                .map(a->new Node(level, a)).collect(Collectors.toList());

        int size = links.size() + medias.size() + imports.size();
        ArrayList<Node> retList = new ArrayList<>(size);
        if (!links.isEmpty()) retList.addAll(links);
        if (!medias.isEmpty()) retList.addAll(medias);
        if (!imports.isEmpty()) retList.addAll(imports);

        return retList;
    }

    private Spider(String baseUrl){
        this.baseUrl = baseUrl;
    }

    /**
     * List imports in styles list.
     *
     * @return the list
     */
    private static void  listImportsInStyles(String baseUrl, Node n, Document doc) {
        System.out.println();
        System.out.println("=======================================");
        System.out.println("**** URL: " + n.url + " ****");
        // Retrieve file extension
        Elements filextElements = doc.select("span[class$=filext]");
        if (filextElements == null || filextElements.isEmpty()) return;
        Element filext = filextElements.first().parent();
        if (filext.childNodeSize() < 2) return;
        System.out.println("---------------------------------------");
        filext.children().forEach( a -> System.out.print("    " + a.text()));
        System.out.println();

        // Retrieve icon
        List<String> styles = doc.select("style").stream()
                .map(a->a.toString()).collect(Collectors.toList());
        if (styles.size() > 0) {
            LinkedList<String> retList = new LinkedList<>();
            styles.stream().map(a -> parserUrlsFromStyle(baseUrl, n.url, a))
                    .reduce(retList, (y, x)->{ y.addAll(x); return y; });
        }
        Elements headerInfo = doc.select("table[class$=headerInfo]");
        if (headerInfo == null || headerInfo.isEmpty()) return;
        System.out.println("---------------------------------------");

        // Retrieve abstract summary
        Elements lines = headerInfo.first().select("tr");
        for (Element e: lines)
        {
            Elements line = e.select("td");
            if (line.size() >= 2)
            {
                line.forEach(a -> System.out.print("    " + a.text()));
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");

        Elements questionElments = doc.select("h2[class=question]");
        if (questionElments == null || questionElments.isEmpty()) return;
        Elements infomations = doc.select("div[class$=infoBox]");
        infomations.forEach(a->{
            System.out.println("[" + a.previousElementSibling().text() + "]");
            System.out.println("    " + a.text());
        });
    }

    private static List<String> parserUrlsFromStyle(String baseUrl, String url, String style) {
        String s = style.substring("<style>".length()
                , style.length() - "</style>".length() - 1);

        //Pattern pattern = Pattern.compile("url\\s*\\(\\S*\\)");
        Pattern pattern = Pattern.compile("\\.f1\\s*\\{\\s*background-image:\\s*url\\s*\\(\\S*\\)\\s*;\\s*}");
        Matcher m = pattern.matcher(s);
        LinkedList<String> retList = new LinkedList<>();
        while (m.find()) {
            String g = m.group();
            g = g.substring(g.indexOf('(') + 1, g.length() - 1).trim();
            if (g.startsWith("//"))
                g = "https:" + g;
            else if (g.startsWith("/"))
                g = baseUrl.endsWith("/")?
                        baseUrl.substring(0, baseUrl.length() -2) + g: baseUrl + g;
            else
                g = url + g;
            System.out.println(g);
            retList.add(g);
        }
        return retList;
    }

    public static void main (String args[]) {
        //String url = "https://fileinfo.com/extension/csv";
        String url = "https://fileinfo.com/";
        final String baseUrl = "https://fileinfo.com";
        doCrawl(url, baseUrl, (node, doc)->{
            listImportsInStyles(baseUrl, node, doc);
            return true;
        });
    }

}
