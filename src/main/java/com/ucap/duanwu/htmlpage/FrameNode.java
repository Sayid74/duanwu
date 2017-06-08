package com.ucap.duanwu.htmlpage;

/**
 * Created by emmet on 2017/5/23.
 */

import java.util.List;
import java.util.Set;

public interface FrameNode {
    NodeType getNodeType();
    List<FrameNode> getChildren();
    int getLevel();
    String getID();
    Set<String> getTagClassNames();
}
