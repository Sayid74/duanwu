package prsn.sayid.duanwu.spider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by emmet on 2017/6/15.
 */

public class Spider {

    static class Node {
        private final int level;
        private String url;
        Node(int level, String url) {
            this.level = level;
            this.url = url;
        }
    }

    public static ArrayBlockingQueue<Spider> spiders;
    public static LinkedBlockingQueue<List<Node>> nodes;

    public static int MAX_SPIDER_COUNT = 10;

    static {
        spiders = new ArrayBlockingQueue<Spider>(MAX_SPIDER_COUNT);
        nodes = new LinkedBlockingQueue<>(1);
        for (int i = 0; i < MAX_SPIDER_COUNT; i++)
            spiders.add(new Spider());
    }

    public static void doCrawl(String url, int mastLevel) {

        nodes.add(Arrays.asList(new Node(0, url)));
        while (!nodes.isEmpty()) {
            try {
                Spider s = spiders.poll(1, TimeUnit.SECONDS);
                nodes.put(s.listNodes());
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private List<Node> listNodes() {
    }

    Spider() {
    }

}
