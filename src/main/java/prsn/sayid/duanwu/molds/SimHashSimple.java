package prsn.sayid.duanwu.molds;

/**
 * Created by emmet on 2017/6/7.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class SimHashSimple
{
    private String tokens;
    private BigInteger intSimHash;
    private String strSimHash;
    private int hashbits = 64;

    public SimHashSimple(String tokens)
    {
        this.tokens = tokens;
        simHash();
    }

    public SimHashSimple(String tokens, int hashbits)
    {
        this.tokens = tokens;
        this.hashbits = hashbits;
        simHash();
    }

    HashMap wordMap = new HashMap();

    public void simHash()
    {
        int[] v = new int[hashbits];
        StringTokenizer stringTokens = new StringTokenizer(tokens);
        while(stringTokens.hasMoreTokens())
        {
            String temp = stringTokens.nextToken();
            BigInteger t = hash(temp);
            for (int i = 0; i < this.hashbits; i++)
            {
                BigInteger bitmask = ONE.shiftLeft(i);
                if (t.and(bitmask).signum() != 0)
                {
                    v[i] += 1;
                }
                else
                {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = ZERO;
        StringBuffer simHashBuffer = new StringBuffer();
        for (int i = 0; i < this.hashbits; i++)
        {
            if (v[i] >= 0)
            {
                fingerprint = fingerprint.add(ONE.shiftLeft(i));
                simHashBuffer.append(1);
            }
            else
            {
                simHashBuffer.append(0);
            }
        }
        strSimHash = simHashBuffer.toString();
        System.out.println(strSimHash + " length " + strSimHash.length());
        intSimHash = fingerprint;
    }

    private BigInteger hash(String source)
    {
        if (source == null || source.isEmpty())
            return ZERO;

        char[] sourceArray = source.toCharArray();
        BigInteger x = BigInteger.valueOf((long)sourceArray[0]).shiftLeft(7);
        BigInteger m = BigInteger.valueOf(1000003);
        BigInteger mask = BigInteger.valueOf(2).pow(hashbits).subtract(ONE);

        for (char item: sourceArray)
        {
            BigInteger temp = BigInteger.valueOf((long)item);
            x = x.multiply(m).xor(temp).and(mask);
        }
        x = x.xor(BigInteger.valueOf(source.length()));
        if (x.equals(ONE.negate()))
            x = BigInteger.valueOf(-2);
        return x;
    }

    private int hammingDistance(SimHashSimple other)
    {
        BigInteger x = intSimHash.xor(other.intSimHash);
        int tot = 0;

        while (x.signum() != 0)
        {
            tot += 1;
            x = x.and(x.subtract(ONE));
        }
        return tot;
    }

    public int getDistance(String str1, String str2)
    {
        int distance;
        if (str1.length() != str2.length())
        {
            distance = -1;
        }
        else
        {
            distance = 0;
            for (int i = 0; i < str1.length(); i++)
            {
                if (str1.charAt(i) != str2.charAt(i))
                    distance++;
            }
        }
        return distance;
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

    public static void main(String[] args)
    {
        String s = "This is a test string for testing";
        SimHashSimple hash1 = new SimHashSimple(s, 128);
        System.out.println(hash1.intSimHash + " " + hash1.intSimHash.bitCount());
        hash1.subByDistance(hash1, 3);

        s = "This is a test string for testing , This is a test string for testing abcdef";
        SimHashSimple hash2 = new SimHashSimple(s, 128);
        System.out.println(hash2.intSimHash + " " + hash2.intSimHash.bitCount());
        hash1.subByDistance(hash2, 3);

        s = "This is a test string for testing als";
        SimHashSimple hash3 = new SimHashSimple(s, 64);
        System.out.println(hash3.intSimHash + " " + hash3.intSimHash.bitCount());
        hash1.subByDistance(hash3, 4);

        System.out.println("==========================================");
        int dis = hash1.getDistance(hash1.strSimHash, hash2.strSimHash);
        System.out.println(hash1.hammingDistance(hash2) + " " + dis);

        int dis2 = hash1.getDistance(hash1.strSimHash, hash3.strSimHash);
        System.out.print(hash1.hammingDistance(hash3) + " " + dis2);
    }
}
