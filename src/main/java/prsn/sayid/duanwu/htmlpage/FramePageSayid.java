package prsn.sayid.duanwu.htmlpage;

import com.ucap.duanwu.htmlpage.FramePage;
import com.ucap.duanwu.htmlpage.NodeType;

import java.math.BigInteger;
import java.util.*;

import com.ucap.duanwu.htmlpage.FrameNode;
import static com.ucap.duanwu.htmlpage.NodeType.*;


/**
 * Created by emmet on 2017/5/22.
 */
public final class FramePageSayid implements FramePage
{
    @Override
    public int countGroupByNodeType(NodeType nodeType) {
        return 0;
    }

    @Override
    public FrameNode getRoot() {
        return null;
    }

    @Override
    public BigInteger simHash() {
        return null;
    }

    @Override
    public BigInteger md5() {
        return null;
    }

    @Override
    public List<FrameNode> wideFirstTravel() {
        return null;
    }
}
