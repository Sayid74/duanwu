package sayid.htmlpage.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sayid.htmlpage.FilterUtil;
import sayid.htmlpage.FrameNodeCapsule;
import ucap.htmlpage.NodeType;

import java.util.*;

import static java.util.Collections.synchronizedMap;
import static sayid.htmlpage.PageParserSayidImp.nodeTypeOfE;
import static ucap.htmlpage.NodeType.P_;

/**
 * Created by emmet on 2017/5/24.
 */
public class FiltersConfig {

    private final static FiltersConfig _SELF;
    static {
        synchronized (FiltersConfig.class) {
            _SELF = new FiltersConfig();
            _SELF.put(FilterPWithSamleCSS.class, P_);
        }
    }

    /************************************************************************/

    private static final Logger L = LoggerFactory.getLogger(FiltersConfig.class);
    /**
     * Holds filters. In the map, every key is a NodeType and value is a set
     * contains a serials filter.
     */
    private final Map<NodeType, Set<Class<? extends FilterUtil>>> config
        = synchronizedMap(new EnumMap(NodeType.class));

    /**
     * Do filter boolean. Pickup a set contains a set filters and do it
     * until when a filter return false, than return false. When go to
     * the set end, all filter method return true, it will return true.
     *
     * @param element the element will been judgment.
     * @return the boolean. True all method return true.
     */
    public static boolean doFilter(FrameNodeCapsule element) {
        NodeType nodeType = nodeTypeOfE(element.getElment());
        if (nodeType == null) return false;

        for (Class<? extends FilterUtil> T: getUtilsByNodeType(nodeType)) {
            try {
                FilterUtil t = T.newInstance();
                if (! t.doFilter(element)) return false;
            } catch (InstantiationException | IllegalAccessException e) {
                L.error("node type: " + nodeType.name() + "    text: "
                        + element.getElment().text(), e);
            }
        }
        return true;
    }

    private void put(Class<? extends FilterUtil> utilClass, NodeType type) {
        Set<Class<? extends FilterUtil>> utilClasses = config.get(type);
        if (utilClasses == null) {
            utilClasses = new LinkedHashSet();
            config.put(type, utilClasses);
        }
        utilClasses.add(utilClass);
    }

    private static Set<Class <? extends FilterUtil>> getUtilsByNodeType(NodeType nodeType) {
        Set<Class<? extends FilterUtil>> utils = _SELF.config.get(nodeType);
        return utils == null? Collections.emptySet():
                Collections.unmodifiableSet(utils);
    }

}
