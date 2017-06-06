package prsn.sayid.duanwu.htmlpage.filters;

import com.ucap.commons.logger.LFactory;
import com.ucap.commons.logger.LoggerAdapter;
import com.ucap.duanwu.htmlpage.NodeType;
import prsn.sayid.duanwu.htmlpage.FilterUtil;
import prsn.sayid.duanwu.htmlpage.FrameNodeCapsule;

import java.util.*;

import static com.ucap.duanwu.htmlpage.NodeType.P_;
import static java.util.Collections.synchronizedMap;
import static prsn.sayid.duanwu.htmlpage.PageParserSayidImp.nodeTypeOfE;

/**
 * Created by emmet on 2017/5/24.
 */
public class FiltersConfig
{
    /**
     * Configer
     */

    private final static FiltersConfig _SELF;
    static
    {
        synchronized (FiltersConfig.class)
        {
            _SELF = new FiltersConfig();
            _SELF.put(FilterPSamleCSS.class, P_);
        }
    }

    /************************************************************************/

    private static LoggerAdapter L = LFactory.makeL(FiltersConfig.class);
    private final Map<NodeType, Set<Class<? extends FilterUtil>>> config
        = synchronizedMap(new EnumMap(NodeType.class));

    public static boolean doFilter(FrameNodeCapsule element)
    {
        NodeType nodeType = nodeTypeOfE(element.getElment());
        if (nodeType == null) return false;

        for (Class<? extends FilterUtil> T: getUtilsByNodeType(nodeType))
        {
            try {
                FilterUtil t = T.newInstance();
                if (! t.doFilter(element)) return false;
            } catch (InstantiationException | IllegalAccessException e) {
                L.error(e);
            }
        }
        return true;
    }

    private void put(Class<? extends FilterUtil> utilClass
            , NodeType type)
    {
        Set<Class<? extends FilterUtil>> utilClasses = config.get(type);
        if (utilClasses == null) {
            utilClasses = new LinkedHashSet();
            config.put(type, utilClasses);
        }
        utilClasses.add(utilClass);
    }

    private static Set<Class <? extends FilterUtil>> getUtilsByNodeType(NodeType nodeType)
    {
        Set<Class<? extends FilterUtil>> utils = _SELF.config.get(nodeType);
        return utils == null? Collections.emptySet():
                Collections.unmodifiableSet(utils);
    }

}
