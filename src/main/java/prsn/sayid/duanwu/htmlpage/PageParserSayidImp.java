package prsn.sayid.duanwu.htmlpage;

import com.ucap.duanwu.htmlpage.*;

import static com.ucap.duanwu.htmlpage.HtmlPage.mount;
import static com.ucap.duanwu.htmlpage.NodeType.nodeTypeOf;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import prsn.sayid.duanwu.htmlpage.filters.FiltersConfig;

import javax.servlet.FilterConfig;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by emmet on 2017/5/22.
 */
public final class PageParserSayidImp implements PageParser
{
    int deepth;
    boolean free = false;

    static
    {
        mount(PageParserSayidImp.class);
    }

    @Override
    public FramePage doParse(InputStream input, String charestName)
            throws PageParserException
    {
        return null;
    }

    @Override
    public void setParserDeepth(int deepth)
    {
        this.deepth = deepth;
    }

    @Override
    public int getParserDeepth()
    {
        return deepth;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public void free() {
        free = true;
    }

    private void paserDom(Document docment)
    {

    }

    private class Travering
    {
        private class Capsule implements FrameNodeCapsule
        {
            private final Element elment;
            private final int level;

            private List<Capsule> children = null;
            private FrameNodeCapsule leftSibling = null;
            private FrameNodeCapsule rightSibling = null;
            private FrameNodeCapsule father = null;
            private boolean keeping = true;

            Capsule (Element element, int level)
            {
                this.elment   = element;
                this.level    = level;
            }

            @Override
            public Element getElment() {
                return elment;
            }

            @Override
            public int getLevel() {
                return level;
            }

            @Override
            public FrameNodeCapsule getFather()
            {
                return father;
            }

            @Override
            public List<? extends FrameNodeCapsule> getChildren() {
                return children;
            }

            @Override
            public FrameNodeCapsule leftSibling() {
                return leftSibling;
            }

            @Override
            public FrameNodeCapsule rightSibling() {
                return rightSibling;
            }

            @Override
            public boolean isKeeping() {
                return keeping;
            }
        }

        public class _FN implements FrameNode
        {
            private final List<_FN> children = new ArrayList();
            final NodeType nodeType;
            final int level;

            _FN(Capsule c)
            {
                this.level = c.getLevel();
                this.nodeType = nodeTypeOfE(c.elment);
                synchronized (Travering.this.ndtpCount)
                {
                    Map<NodeType, Integer> ncnt = Travering.this.ndtpCount;
                    int i = ncnt.containsKey(nodeType)? 0:  ncnt.get(nodeType);
                    ncnt.put(nodeType, ++i);
                }
            }

            @Override
            public NodeType getNodeType()
            {
                return nodeType;
            }

            @Override
            public List<FrameNode> getChildren()
            {
                return Collections.unmodifiableList(children);
            }

            @Override
            public int getLevel() {
                return level;
            }
        }

        Map<NodeType, Integer> ndtpCount = Collections.synchronizedMap(new EnumMap<>(NodeType.class));

        Travering (Document document) throws PageParserException
        {
            Element element = document.body();
            if (element == null)
                throw new PageParserException("There isn't a body element.");
            Capsule root = new Capsule(element, 0);
            buildFrame(root);
            cleanFrame(root);
        }

        void buildFrame(Capsule c)
        {
            if (c.level == PageParserSayidImp.this.deepth) return;

            Element _e = c.elment;
            List<Element> _es = new LinkedList(_e.getAllElements().stream().collect(Collectors.toList()));
            if (_es.isEmpty()) return;

            List<Element> _o = new LinkedList<>();
            do {
                Element e = _es.remove(0);
                if (nodeTypeOfE(e) == null)
                    _es.addAll(e.getAllElements().stream().collect(Collectors.toList()));
                else
                    _o.add(e);
            } while (!_es.isEmpty());
            c.children = _o.stream().map(a -> new Capsule(a, c.level + 1)).collect(Collectors.toList());

            for (int i = 1; i < c.children.size(); i++)
            {
                Capsule ci_0 = c.children.get(i -1);
                Capsule ci_1 = c.children.get(i);
                ci_0.rightSibling = ci_1;
                ci_1.leftSibling  = ci_0;
                ci_0.father = ci_1.father = c;
            }
            c.children.forEach(a -> buildFrame(a));
        }

        void cleanFrame(Capsule c)
        {
            c.keeping = FiltersConfig.doFilter(c);
            if (c.children == null || c.children.isEmpty()) return;
            c.children.forEach(a -> cleanFrame(a));
        }

        _FN makeFramePage(Capsule c)
        {
            LinkedList<Capsule> fifo = new LinkedList<>();
            LinkedList<_FN> frame = new LinkedList<>();
            fifo.add(c); frame.add(new _FN(c));
            _FN _r = null;
            do{
                Capsule _c = fifo.removeLast();
                _r = frame.removeLast();

                List<Capsule> chi = _c.children.stream().filter(a->a.keeping).collect(Collectors.toList());
                List<_FN>     nds = chi.stream().map(_FN::new).collect(Collectors.toList());
                fifo.addAll(chi);
                frame.addAll(nds);
                _r.children.addAll(nds);
            }
            while (!fifo.isEmpty());
            return _r;
        }


    }

    public static NodeType nodeTypeOfE(Element element)
    {
        return nodeTypeOf(element.tag().getName());
    }
}
