package prsn.sayid.duanwu.molds;

import com.ucap.duanwu.htmlpage.FrameNode;
import com.ucap.duanwu.htmlpage.NodeType;

import java.math.BigInteger;
import java.util.*;

import static com.ucap.duanwu.htmlpage.NodeType.*;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Created by emmet on 2017/5/26.
 */
public class SimHashOfFrameNode {
    private static final Map<NodeType, BigInteger> vectors =
            new EnumMap<NodeType, BigInteger>(NodeType.class){{
                put(BODY_,        ZERO);
                put(DIV_,         ONE);
                put(P_,           ONE.shiftLeft(4  ));
                put(TABLE_,       ONE.shiftLeft(8  ));
                put(TD_,          ONE.shiftLeft(12 ));
                put(TR_,          ONE.shiftLeft(16 ));
                put(TH_,          ONE.shiftLeft(20 ));
                put(THEAD_,       ONE.shiftLeft(24 ));
                put(TBODY_,       ONE.shiftLeft(28 ));
                put(TFOOT_,       ONE.shiftLeft(32 ));
                put(COL_,         ONE.shiftLeft(36 ));
                put(COLGROUP_,    ONE.shiftLeft(40 ));
                put(FORM_,        ONE.shiftLeft(44 ));
                put(FRAME_,       ONE.shiftLeft(48 ));
                put(FRAMESET_,    ONE.shiftLeft(52 ));
                put(IFRAME_,      ONE.shiftLeft(56 ));
                put(UL_,          ONE.shiftLeft(60 ));
                put(OL_,          ONE.shiftLeft(64 ));
                put(LI_,          ONE.shiftLeft(68 ));
                put(DL_,          ONE.shiftLeft(72 ));
                put(DT_,          ONE.shiftLeft(76 ));
                put(DD_,          ONE.shiftLeft(80 ));
                put(ARTICLE_,     ONE.shiftLeft(84 ));
                put(ASIDE_,       ONE.shiftLeft(88 ));
                put(CAPTION_,     ONE.shiftLeft(92 ));
                put(DETAILS_,     ONE.shiftLeft(96 ));
                put(MAP_,         ONE.shiftLeft(100));
                put(IMAG_,        ONE.shiftLeft(104));
                put(VIDEO_,       ONE.shiftLeft(108));
                put(FIGURE_,      ONE.shiftLeft(112));
                put(FIGCAPTION_,  ONE.shiftLeft(116));
            }};

    //private Function<FrameNode, Integer> powerCal;
    private List<FrameNode> nodes;
    private int vectorValues[] = new int[vectors.size()];

    public SimHashOfFrameNode(List<? extends FrameNode> nodes)
    {
        this.nodes = Collections.unmodifiableList(nodes);
        vectorValues = new int[vectors.size()];
    }

    public BigInteger calculateSimhash()
    {
        BigInteger r = ZERO;
        System.out.println("nodes count: " + nodes.size());
        nodes.forEach(a -> vectorValues[a.getNodeType().ordinal()]
                += a.getLevel());

        for (int i = 0; i < vectorValues.length; i++)
            System.out.println ("vector[" + i + "] = " + vectorValues[i]) ;

        for(int i = 0; i < vectorValues.length; i++)
        {
            int value = vectorValues[i];
            if (value >= 85)
                value = 15;
            else
            {
                int v1 = value & 3;
                int v2 = (value & (15 -  3)) == 0? 0 : 1;
                int v3 = (value & (63 - 15)) == 0? 0 : 1;
                value = (v3 << 4) + (v2 << 3) + v1;
            }

            BigInteger bv = BigInteger.valueOf(value);
            NodeType t = NodeType.values()[i];
            BigInteger pow = vectors.get(t);
            r = r.add(bv.multiply(pow));
        }
        return r;
    }

    public long distance(BigInteger other)
    {
        long r = 0;
        BigInteger x0 = calculateSimhash();
        BigInteger x1 = other;
        BigInteger m = BigInteger.valueOf(15);

        while (!x0.equals(x1))
        {
            BigInteger _x0 = x0.and(m);
            BigInteger _x1 = x1.and(m);

            r += _x0.subtract(_x1).pow(2).intValue();

            x0 = x0.compareTo(ZERO) > 0 ? x0.shiftRight(4) : ZERO;
            x1 = x1.compareTo(ZERO) > 0 ? x1.shiftRight(4) : ZERO;
        }

        return r;
    }
}
