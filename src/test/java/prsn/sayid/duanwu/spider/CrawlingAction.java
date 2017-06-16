package prsn.sayid.duanwu.spider;

import java.lang.FunctionalInterface;

/**
 * Created by emmet on 2017/6/16.
 */
@FunctionalInterface
public interface CrawlingAction {
    void doAction(Spider.Node node);
}
