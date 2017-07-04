
import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Created by emmet on 2017/7/3.
 */
public class TestBigInteger {
    public static void main(String args[]){
        /*
        BigInteger i = BigInteger.valueOf( 65536);
        i = i.multiply(BigInteger.valueOf( 65536));
        i = i.add(BigInteger.valueOf(-2));
        i = i.add(ONE);
        byte[] bs = i.toByteArray();
        System.out.println("Bits Length: " + i.bitLength());
        System.out.println("Bits count: " + i.bitCount());
        BigInteger x = i.add(ZERO);
        BigInteger y = i.add(ZERO);
        for (int n = 0; n < 64; n++) {
            y = y.shiftRight(1);
            for (int m = 0; m < y.bitLength(); m++) {
                System.out.print(y.testBit(m)? "1": "0");
            }
            System.out.println();
        }
        System.out.println();
        for (int n=0; n < bs.length; n++) {
            System.out.println(String.format("%x",bs[n]));
        }
        */
        BigInteger bi = ONE.shiftLeft(16).subtract(ONE);
        byte[] bs = bi.toByteArray();
        for (int n=0; n < bs.length; n++) {
            System.out.println(String.format("%x",bs[n]));
        }
    }
}
