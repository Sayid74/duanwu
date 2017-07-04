package sayid.maths;

/**
 * Created by emmet on 2017/6/7.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucap.htmlpage.FrameNode;
import ucap.htmlpage.NodeType;
import static ucap.htmlpage.NodeType.*;

import java.math.BigInteger;
import java.util.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * The type PageFrameSimhash. It used to calculate a same simhase number by
 * a web-page, which described by a list of frame node.
 */
public final class PageFrameSimhash implements EigenvalueCalculator<List<FrameNode>>
{
    private static final Map<NodeType, Integer> vectors =
            new EnumMap<NodeType, Integer>(NodeType.class){{
                put(BODY_,        101);
                put(DIV_,         103);
                put(P_,           107);
                put(TABLE_,       109);
                put(TD_,          113);
                put(TR_,          127);
                put(TH_,          131);
                put(THEAD_,       139);
                put(TBODY_,       149);
                put(TFOOT_,       151);
                put(COL_,         163);
                put(COLGROUP_,    167);
                put(FORM_,        173);
                put(FRAME_,       179);
                put(FRAMESET_,    181);
                put(IFRAME_,      191);
                put(UL_,          193);
                put(OL_,          199);
                put(LI_,          211);
                put(DL_,          223);
                put(DT_,          227);
                put(DD_,          229);
                put(ARTICLE_,     233);
                put(ASIDE_,       239);
                put(CAPTION_,     241);
                put(DETAILS_,     251);
                put(MAP_,         257);
                put(IMAG_,        263);
                put(VIDEO_,       269);
                put(FIGURE_,      271);
                put(FIGCAPTION_,  281);
            }};

    private List<FrameNode> nodes;
    private BigInteger intSimHash = ZERO;
    private final static int HASH_BITS = 64;
    private final static long BIG_PRIME = 9223372036854775783l;
    private final static int  MIDDLE_PRIME = 2147483629;
    private static final Logger L = LoggerFactory.getLogger(PageFrameSimhash.class);

    /**
     * Instantiates a new Page frame sim hash.
     */
    public PageFrameSimhash() {
        L.info("Simhash length: " + HASH_BITS);
        L.info("Prime number: " + BIG_PRIME);
    }

    @Override
    public BigInteger calculate(List<FrameNode> resources) {
        this.nodes = new ArrayList(resources);
        final int[] v = new int[HASH_BITS];
        nodes.forEach(a->{
            BigInteger t = hash(a);
            int l = a.getLevel();
            for (int i = 0; i < HASH_BITS; i++)
                v[i] += t.testBit(i) ? l : -l;
        });

        for (int i = 0; i < HASH_BITS; i++) {
            intSimHash = intSimHash.setBit(v[i]> 0 ? 1: 0);
        }

        return intSimHash;
    }

    @Override
    public int distance(BigInteger other) {
        BigInteger x = intSimHash.xor(other);
        int tot = 0;

        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(ONE));
        }
        return tot;
    }

    private BigInteger hash(FrameNode node) {
        NodeType nodeTp = node.getNodeType();
        String id = node.getID();
        Set<String> clzNms = node.getTagClassNames();
        int len = clzNms.stream().mapToInt(String::length).sum();
        final StringBuffer clzNmsBuf = new StringBuffer(len);
        clzNms.stream().forEach(clzNmsBuf::append);
        len += (id.length() + 1);
        char data[] = new char[len];
        data[0] = (char) Math.pow(vectors.get(nodeTp), 2.0);
        System.arraycopy(id.toCharArray(), 0, data, 1, id.length());
        clzNmsBuf.getChars(0,clzNmsBuf.length(), data, id.length() + 1);

        BigInteger x = BigInteger.valueOf((long)data[0]).shiftLeft(7);
        BigInteger m = BigInteger.valueOf(BIG_PRIME); // A big prime number
        BigInteger mask = ONE.shiftLeft(HASH_BITS + 1).subtract(ONE);

        for (char item: data) {
            BigInteger temp = BigInteger.valueOf((long)item);
            x = x.multiply(m).xor(temp).and(mask);
        }

        x = x.xor(BigInteger.valueOf(data.length));
        if (x.equals(mask))
            x = mask.subtract(ONE);
        return x;
    }
}
