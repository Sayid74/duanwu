package prsn.sayid.duanwu.htmlpage;

import com.ucap.duanwu.htmlpage.FrameNode;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by emmet on 2017/5/25.
 */
public interface FrameNodeCapsule {
    Element getElment();
    int getLevel();
    FrameNodeCapsule getFather();
    List<? extends FrameNodeCapsule> getChildren();
    FrameNodeCapsule leftSibling();
    FrameNodeCapsule rightSibling();
    boolean isKeeping();
}
