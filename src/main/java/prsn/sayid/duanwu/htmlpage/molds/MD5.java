package prsn.sayid.duanwu.htmlpage.molds;

import com.sun.istack.internal.NotNull;
import com.ucap.commons.logger.LFactory;
import com.ucap.commons.logger.LoggerAdapter;
import com.ucap.duanwu.htmlpage.FrameNode;

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
    List<FrameNode> nodes = null;
    private static LoggerAdapter L = LFactory.makeL(MD5.class);
    BigInteger eigenvalue = ZERO;

    @Override
    public BigInteger calculate(@NotNull List<FrameNode> resouces)
    {
        this.nodes = new ArrayList(nodes);

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
            L.error(e);
            return eigenvalue;
        }
    }
}
