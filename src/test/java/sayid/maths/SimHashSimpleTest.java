package sayid.maths;

import ucap.htmlpage.FrameNode;
import ucap.htmlpage.NodeType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by emmet on 2017/7/3.
 */
public class SimHashSimpleTest {
    @Mock
    Map<NodeType, Integer> vectors;
    @Mock
    List<FrameNode> nodes;
    @Mock
    BigInteger intSimHash;
    @InjectMocks
    PageFrameSimhash simHashSimple;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testCalculate() throws Exception {
    }

    @Test
    public void testDistance() throws Exception {
        when(intSimHash.xor(any())).thenReturn(new BigInteger("0001001000001",2));

        int result = simHashSimple.distance(any());
        Assert.assertEquals(3, result);
    }

    @Test
    public void testSegmentValue() throws Exception {

        when(intSimHash.bitLength()).thenReturn(64);
        when(intSimHash.testBit(anyInt())).thenReturn(true).thenReturn(false)
                .thenReturn(false).thenReturn(true);
        when(intSimHash.testBit(1)).thenReturn(false).thenReturn(true);
        List<BigInteger> result = simHashSimple.segmentValue(4);

        BigInteger _8191  = BigInteger.valueOf(8191);
        BigInteger _65535 = BigInteger.valueOf(65535);

        Assert.assertEquals(result.get(0), _8191);
        Assert.assertEquals(result.get(1), _65535);
        Assert.assertEquals(result.get(2), _65535);
        Assert.assertEquals(result.get(3), _65535);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
