package sayid.htmlpage;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by emmet on 2017/5/25.
 * FrameNodeCapsule.
 * Its object will be used to relative between PageParser(traveling) and FilterUtil objects.
 */
public interface FrameNodeCapsule {
    /**
     * Gets elment.
     *
     * @return the elment
     */
    Element getElment();

    /**
     * Gets level.
     *
     * @return the level
     */
    int getLevel();

    /**
     * Gets father.
     * If node has Father linker, Deep Traveling should not use stack.
     *
     * @return the father
     */
    FrameNodeCapsule getFather();

    /**
     * Gets children.
     *
     * @return the children
     */
    List<? extends FrameNodeCapsule> getChildren();

    /**
     * Left sibling frame node capsule.
     *
     * @return the frame node capsule
     */
    FrameNodeCapsule leftSibling();

    /**
     * Right sibling frame node capsule.
     *
     * @return the frame node capsule
     */
    FrameNodeCapsule rightSibling();

    /**
     * Is keeping boolean.
     *
     * @return the boolean
     */
    boolean isKeeping();
}
