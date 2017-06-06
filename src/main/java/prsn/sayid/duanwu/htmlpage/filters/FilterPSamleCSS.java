package prsn.sayid.duanwu.htmlpage.filters;

import com.ucap.duanwu.htmlpage.NodeType;
import org.jsoup.nodes.Element;
import prsn.sayid.duanwu.htmlpage.FilterUtil;
import prsn.sayid.duanwu.htmlpage.FrameNodeCapsule;

import static com.ucap.duanwu.htmlpage.NodeType.*;
import static prsn.sayid.duanwu.htmlpage.PageParserSayidImp.nodeTypeOfE;

/**
 * Created by emmet on 2017/5/25.
 */
public class FilterPSamleCSS implements FilterUtil {
    @Override
    public boolean doFilter(FrameNodeCapsule element) {
        if(element.leftSibling()== null) return true;
        Element e = element.leftSibling().getElment();
        NodeType t = nodeTypeOfE(e);
        if (t!= P_) return true;
        return !(element.getElment().classNames().equals(e.classNames()));
    }
}
