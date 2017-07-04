package sayid.maths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucap.htmlpage.FrameNode;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigInteger.ZERO;

/**
 * Created by emmet on 2017/6/13.
 */
public class MD5 implements EigenvalueCalculator <List<FrameNode>>
{
    /**
     * The Nodes.
     */
    List<FrameNode> nodes = null;
    private static final Logger L = LoggerFactory.getLogger(MD5.class);
    /**
     * The Eigenvalue.
     */
    BigInteger eigenvalue = ZERO;

    @Override
    public BigInteger calculate(List<FrameNode> resouces)
    {
        this.nodes = new ArrayList(resouces);

        List<Byte> data = nodes.stream()
                .map(a->(byte)(a.getNodeType().ordinal()))
                .collect(Collectors.toList());
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte d[] = new byte[data.size()];
            for(int i = 0; i < d.length; i++) d[i] = data.get(i);
            eigenvalue = new BigInteger(md5.digest(d));
            return eigenvalue;
        }
        catch (NoSuchAlgorithmException e)
        {
            L.error("You should use oracle java sdk!", e);
            return eigenvalue;
        }
    }
}
