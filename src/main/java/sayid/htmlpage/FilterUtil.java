package sayid.htmlpage;

/**
 * Created by emmet on 2017/5/24.
 * Filter out some elements those haven't any useful for algorithm.
 */

@FunctionalInterface
public interface FilterUtil
{
    /**
     * Do filter boolean.
     * If you think the element isn't useful to algorithm
     * you should return false otherwise return true.
     *
     * @param element the element
     * @return the boolean. If true, the element isn't useful else return true.
     */
    boolean doFilter(FrameNodeCapsule element);
}
