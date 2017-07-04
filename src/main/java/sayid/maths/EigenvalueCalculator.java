package sayid.maths;

import java.math.BigInteger;

/**
 * Created by emmet on 2017/6/9.
 *
 * @param <T> the type parameter
 */
public interface EigenvalueCalculator <T>
{

    /**
     * Calculate big integer.
     *
     * @param resources the resources
     * @return the big integer
     */
    BigInteger calculate(T resources);

    /**
     * Distance int.
     *
     * @param other the other
     * @return the int
     */
    default int distance(BigInteger other)
    {
        throw new UnsupportedOperationException();
    }
}
