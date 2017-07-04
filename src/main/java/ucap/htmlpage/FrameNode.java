package ucap.htmlpage;

/**
 * Created by emmet on 2017/5/23.
 */

import java.util.List;
import java.util.Set;

/**
 * The interface Frame node.
 * Frame node represents page-frame node.
 * By most possible, node is described by tag of web page.
 */
public interface FrameNode {
    /**
     * Gets node type.
     *
     * @return the node type
     */
    NodeType getNodeType();

    /**
     * Gets children.
     *
     * @return the children
     */
    List<FrameNode> getChildren();

    /**
     * Gets level.
     *
     * @return the level
     */
    int getLevel();

    /**
     * Gets id.
     *
     * @return the id
     */
    String getID();

    /**
     * Gets tag class names.
     *
     * @return the tag class names
     */
    Set<String> getTagClassNames();
}
