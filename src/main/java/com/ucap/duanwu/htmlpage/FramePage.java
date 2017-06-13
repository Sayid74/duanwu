package com.ucap.duanwu.htmlpage;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by emmet on 2017/5/23.
 */
public interface FramePage {
    public interface ValueObj
    {
        String uri();
        BigInteger md5();
        BigInteger eigenvalue();
    }

    int countGroupByNodeType(NodeType nodeType);
    FrameNode getRoot();
    long distance(FramePage other) throws PageParserException;
    List<FrameNode> wideFirstTravel();
    ValueObj vo();
}
