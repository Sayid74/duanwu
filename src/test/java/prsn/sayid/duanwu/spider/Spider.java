package prsn.sayid.duanwu.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
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
                if (action != null) {
                    if (! action.doAction(n)) continue;
                }
                nodes.addAll(s.crawlTo(n));
            }
        }
    }

    private final String baseUrl;
    private List<Node> crawlTo(Node n) {
        int level = n.level + 1;
        if (level > MAX_LEVEL) return null;
        URLRetriever retriever;
        try {
            retriever = URLRetriever.mkRetrieverByUrl(n.url, baseUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        List<Node> links = retriever.listLinks().stream()
                .map(a->new Node(level, a)).collect(Collectors.toList());
        List<Node> medias = retriever.listMedias().stream()
                .map(a->new Node(level, a)).collect(Collectors.toList());
        List<Node> imports = retriever.listImports().stream()
                .map(a->new Node(level, a)).collect(Collectors.toList());

        retriever.listImportsInStyles();

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

    /*
    public static void main (String args[]) {
        String url = "https://fileinfo.com/extension/csv";
        String baseUrl = "https://fileinfo.com";
        doCrawl(url, baseUrl, node ->{
            return true;
        });
    }
    */

}
