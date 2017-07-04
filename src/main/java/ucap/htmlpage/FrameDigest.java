package ucap.htmlpage;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by emmet on 2017/5/23.
 */
public interface FrameDigest {
    /**
     * The interface Persistence obj.
     */
    public interface PersistenceObj
    {
        /**
         * Uri string of the page should be persisted.
         *
         * @return the string
         */
        String uri();

        /**
         * Md 5 big integer. It is md5 eigenvalue of the persistence page.
         *
         * @return the big integer
         */
        BigInteger md5();

        /**
         * Eigenvalue big integer.
         *
         * @return the big integer
         */
        BigInteger eigenvalue();
    }

    /**
     * Count group by node type int.
     *
     * @param nodeType the node type
     * @return the int
     */
    int countGroupByNodeType(NodeType nodeType);

    /**
     * Gets root.
     *
     * @return the root
     */
    FrameNode getRoot();

    /**
     * Distance long.
     *
     * @param other the other
     * @return the long
     * @throws PageParserException the page parser exception
     */
    long distance(FrameDigest other) throws PageParserException;

    /**
     * Wide first travel list.
     *
     * @return the list
     */
    List<FrameNode> wideFirstTravel();

    /**
     * Persistence obj.
     *
     * @return the persistence obj
     */
    PersistenceObj persistenceObj();
}
