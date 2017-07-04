package sayid.htmlpage.filters;

import sayid.htmlpage.FilterUtil;
import ucap.htmlpage.NodeType;
import static ucap.htmlpage.NodeType.*;
import org.jsoup.nodes.Element;
import sayid.htmlpage.FrameNodeCapsule;

import static sayid.htmlpage.PageParserSayidImp.nodeTypeOfE;

/**
 * Created by emmet on 2017/5/25.
 *
 * Filter out the P targets with same css name.
 */
public class FilterPWithSamleCSS implements FilterUtil {
    @Override
    public boolean doFilter(FrameNodeCapsule element) {
        if(element.leftSibling()== null) return true;
        Element e = element.leftSibling().getElment();
        NodeType t = nodeTypeOfE(e);
        if (t!= P_) return true;
        return !(element.getElment().classNames().equals(e.classNames()));
    }
}
