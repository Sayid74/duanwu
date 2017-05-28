package com.ucap.duanwu.htmlpage;

/**
 * Created by emmet on 2017/5/23.
 */

import java.util.List;

public interface FrameNode {
    public NodeType getNodeType();
    public List<FrameNode> getChildren();
    public int getLevel();
}
