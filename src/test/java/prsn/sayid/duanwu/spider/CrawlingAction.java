package prsn.sayid.duanwu.spider;

import java.lang.FunctionalInterface;

/**
 * Created by emmet on 2017/6/16.
 */
@FunctionalInterface
public interface CrawlingAction {
    /**
     * Do action occurs before crawl the web-page.
     * It gives programmer an opportunity to
     * decide if does crawl the web-page.
     *
     * @param node the node represents the web-page.
     * @return the boolean. If return true, the web-page
     * should be crawl, and false, shouldn't crawl.
     */
    boolean doAction(Spider.Node node);
}
