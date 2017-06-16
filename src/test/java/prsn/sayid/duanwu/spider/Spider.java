package prsn.sayid.duanwu.spider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by emmet on 2017/6/15.
 */

public class Spider {

    public final static class Node {
        private final int level;
        private String url;
        Node(int level, String url) {
            this.level = level;
            this.url = url;
        }
    }

    public final static int MAX_SPIDER_COUNT = 10;
    public final static int MAX_LEVEL = 8;

    private final static ArrayBlockingQueue<Spider> spiders
            = new ArrayBlockingQueue<Spider>(MAX_SPIDER_COUNT);
    private final static LinkedList<Node> nodes = new LinkedList<>();


    static {
        for (int i = 0; i < MAX_SPIDER_COUNT; i++)
            spiders.add(new Spider());
    }

    public static void doCrawl(String url, int mastLevel, CrawlingAction action) {
        nodes.add(new Node(0, url));
        synchronized (nodes)
        {
            while (!nodes.isEmpty()) {
                Node n = nodes.pollLast();
                if (action != null) action.doAction(n);
                Spider s = null;
                try {
                    s = spiders.poll(1, SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nodes.addAll(s.crawlTo(n));
            }
        }
    }

    public List<Node> crawlTo(Node n) {
        int level = n.level + 1;
        if (level > MAX_LEVEL) return null;
        try {
            return URLRetriever.mkRetrieverByUrl(n.url).listLinks().stream()
                    .map(a->new Node(level, a)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Spider(){}
}
