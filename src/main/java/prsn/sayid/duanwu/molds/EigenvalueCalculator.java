package prsn.sayid.duanwu.molds;

import java.math.BigInteger;

/**
 * Created by emmet on 2017/6/9.
 */
public interface EigenvalueCalculator
{
    /**
     * Calculate big integer.
     *
     * @return the big integer
     */
    BigInteger calculate();

    /**
     * Distance int.
     *
     * @param other the other
     * @return the int
     */
    int distance(BigInteger other);

    /**
     * Gets eigenvalue.
     *
     * @return the eigenvalue
     */
    BigInteger getEigenvalue();
}
