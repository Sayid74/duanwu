package prsn.sayid.duanwu.molds;

/**
 * Created by emmet on 2017/6/7.
 */

import com.ucap.duanwu.htmlpage.FrameNode;
import com.ucap.duanwu.htmlpage.NodeType;

import java.math.BigInteger;
import java.util.*;

import static com.ucap.duanwu.htmlpage.NodeType.*;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class SimHashSimple implements EigenvalueCalculator
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
    private BigInteger intSimHash;
    private int hashbits = 64;

    public SimHashSimple(List<FrameNode> nodes)
    {
        this.nodes = nodes;
        calculate();
    }

    public SimHashSimple(List<FrameNode> tokens, int hashbits)
    {
        this.nodes = tokens;
        this.hashbits = hashbits;
        calculate();
    }

    @Override
    public BigInteger calculate()
    {
        int[] v = new int[hashbits];

        nodes.forEach(a->{
            BigInteger t = hash(a);
            for (int i = 0; i < hashbits; i++)
                v[i] += t.testBit(i) ? a.getLevel() : 0;
        });

        intSimHash = ZERO;
        for (int i = 0; i < this.hashbits; i++)
            intSimHash = intSimHash.setBit(i);

        return intSimHash;
    }

    @Override
    public int distance(BigInteger other)
    {
        BigInteger x = intSimHash.xor(other);
        int tot = 0;

        while (x.signum() != 0)
        {
            tot += 1;
            x = x.and(x.subtract(ONE));
        }
        return tot;
    }
    @Override
    public BigInteger getEigenvalue()
    {
        return intSimHash;
    }

    public List subByDistance(SimHashSimple simHash, int distance)
    {
        int numEach = hashbits / (distance + 1);
        List characters = new ArrayList();

        StringBuffer buffer = new StringBuffer();

        int k = 0;
        for (int i = 0; i < intSimHash.bitLength(); i++)
        {
            boolean sr = simHash.intSimHash.testBit(i);

            buffer.append(sr? 1 : 0);
            if ((i + 1) % numEach == 0)
            {
                BigInteger eachValue = new BigInteger(buffer.toString(), 2);
                System.out.println("------" + eachValue);
                buffer.delete(0, buffer.length());
                characters.add(eachValue);
            }
        }
        return characters;
    }

    private BigInteger hash(FrameNode node)
    {
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
        BigInteger m = BigInteger.valueOf(1000003); // A big prime number
        BigInteger mask = BigInteger.valueOf(2).pow(hashbits).subtract(ONE);

        for (char item: data)
        {
            BigInteger temp = BigInteger.valueOf((long)item);
            x = x.multiply(m).xor(temp).and(mask);
        }
        x = x.xor(BigInteger.valueOf(data.length));
        if (x.equals(ONE.negate()))
            x = BigInteger.valueOf(-2);
        return x;
    }

}
