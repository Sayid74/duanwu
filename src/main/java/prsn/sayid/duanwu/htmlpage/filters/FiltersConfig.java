package prsn.sayid.duanwu.htmlpage.filters;

import com.ucap.commons.logger.LFactory;
import com.ucap.commons.logger.LoggerAdapter;
import com.ucap.duanwu.htmlpage.NodeType;
import org.jsoup.nodes.Element;
import prsn.sayid.duanwu.htmlpage.FilterUtil;
import prsn.sayid.duanwu.htmlpage.FrameNodeCapsule;

import static java.util.Collections.synchronizedMap;
import static com.ucap.duanwu.htmlpage.NodeType.*;
import static prsn.sayid.duanwu.htmlpage.PageParserSayidImp.nodeTypeOfE;

import java.util.*;

/**
 * Created by emmet on 2017/5/24.
 */
public class FiltersConfig
{
    /**
     * Configer
     */
    static
    {
        put(FilterPSamleCSS.class, P_);
    }

    /************************************************************************/

    private static  LoggerAdapter L = LFactory.makeL(FiltersConfig.class);
    private static Map<NodeType, Set<Class<? extends FilterUtil>>> config
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

    synchronized private static void put(Class<? extends FilterUtil> utilClass
            , NodeType type)
    {
        Set<Class<? extends FilterUtil>> utilClasses = config.get(type);
        if (utilClasses == null) {
            utilClasses = new LinkedHashSet();
            config.put(type, utilClasses);
        }
        utilClasses.add(utilClass);
    }

    private static Set<Class <? extends FilterUtil>>
        getUtilsByNodeType(NodeType nodeType)
    {
        Set<Class<? extends FilterUtil>> utils = config.get(nodeType);
        return utils == null? Collections.emptySet():
                Collections.unmodifiableSet(utils);
    }

}
