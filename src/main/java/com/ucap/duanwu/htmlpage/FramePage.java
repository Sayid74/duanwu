package com.ucap.duanwu.htmlpage;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by emmet on 2017/5/23.
 */
public interface FramePage {
    int countGroupByNodeType(NodeType nodeType);
    FrameNode getRoot();
    BigInteger simHash();
    long distance(FramePage other);
    BigInteger md5();
    List<FrameNode> wideFirstTravel();
}
