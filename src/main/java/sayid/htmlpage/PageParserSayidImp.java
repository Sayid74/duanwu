package sayid.htmlpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sayid.htmlpage.filters.FiltersConfig;
import sayid.maths.EigenvalueCalculator;
import sayid.maths.MD5;
import sayid.maths.PageFrameSimhash;
import ucap.htmlpage.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigInteger.ZERO;
import static ucap.htmlpage.NodeType.nodeTypeOf;


/**
 * The type Page parser Sayid imp.
 */
public final class PageParserSayidImp implements PageParser
{
    private static final Logger L = LoggerFactory.getLogger(PageParserSayidImp.class);
    private static final Class<? extends EigenvalueCalculator> hyperplaneHash;
    private static final Class<? extends EigenvalueCalculator> md5Calculator;
    private int deepth;
    private boolean free = false;
    private String baseUri;

    static {
        hyperplaneHash = PageFrameSimhash.class;
        md5Calculator  = MD5.class;
    }

    /**
     * Node type of e node type.
     *
     * @param element the element
     * @return the node type
     */
    public static NodeType nodeTypeOfE(Element element) {
        return nodeTypeOf(element.tag().getName());
    }

    @Override
    public FrameDigest doParse(InputStream input, String charsetName, String baseUri)
            throws PageParserException {
        Document d;
        try {
            d = Jsoup.parse(input, charsetName, baseUri);
        } catch (IOException e) {
            throw new PageParserException(e);
        }
        FrameDigest r = parserDom(d);
        free();
        return r;
    }

    @Override
    public FrameDigest doParse(String url) throws PageParserException {
        Document d;
        try {
            d = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new PageParserException(e);
        }
        FrameDigest r = parserDom(d);
        free();
        return r;
    }

    @Override
    public void setParserDeepth(int deepth) {
        this.deepth = deepth;
    }

    @Override
    public int getParserDeepth() {
        return deepth;
    }

    @Override
    public boolean isFree() {
        return free;
    }

    private void free() {
        free = true;
    }

    private FrameDigest parserDom(Document document) throws PageParserException
    {
        final String uri = document.baseUri();
        final Traveling _t = new Traveling(document);

        class FD implements FrameDigest {
            class PO implements PersistenceObj {
                @Override
                public String uri() {
                    return uri;
                }

                @Override
                public BigInteger md5()
                {
                    try {
                        return md5Calculator.newInstance().calculate(_t.nodes);
                    } catch (InstantiationException | IllegalAccessException e) {
                        L.error("Can't calculate a md5 number, maybe calculator" +
                                " causes problems.", e);
                        return ZERO;
                    }
                }

                @Override
                public BigInteger eigenvalue() {
                    try {
                        return hyperplaneHash.newInstance().calculate(_t.nodes);
                    } catch (InstantiationException | IllegalAccessException e) {
                        L.error("Can't calculate an eigenvalue, because hyperplane" +
                                " hash occurs some mistake", e);
                        return ZERO;
                    }
                }
            }

            @Override
            public int countGroupByNodeType(NodeType nodeType) {
                return _t.ndtpCount.get(nodeType);
            }

            @Override
            public FrameNode getRoot() {
                return _t.rootNode;
            }

            @Override
            public List<FrameNode> wideFirstTravel() {
                return new ArrayList<>(_t.nodes);
            }

            @Override
            public PersistenceObj persistenceObj() {
                return new PO();
            }

            @Override
            public long distance(FrameDigest other) throws PageParserException {
                if (other == null)
                    throw new NullPointerException(
                            "Other Frame Digest illegal set to null.");
                try {
                    return hyperplaneHash.newInstance()
                            .distance(other.persistenceObj().eigenvalue());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new PageParserException(e);
                }
            }

        }
        return new FD();
    }

    /**
     * Traveling encapsulates actions, those done at traveling between tags in the web-page.
     */
    private class Traveling {
        /**
         * The Capsule object will be given to FilterUtil object.
         */
        private class Capsule implements FrameNodeCapsule {
            private final Element elment;
            private final int level;

            private List<Capsule> children = null;
            private FrameNodeCapsule leftSibling = null;
            private FrameNodeCapsule rightSibling = null;
            private FrameNodeCapsule father = null;
            private boolean keeping = true;

            /**
             * Instantiates a new Capsule.
             *
             * @param element the element
             * @param level   the level
             */
            Capsule (Element element, int level) {
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
            public FrameNodeCapsule getFather() {
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

        /**
         * The type Fn.
         */
        public class _FN implements FrameNode {
            private final List<_FN> children = new ArrayList<>();
            /**
             * The Node type.
             */
            final NodeType nodeType;
            /**
             * The Level.
             */
            final int level;
            /**
             * The Id.
             */
            String id;
            /**
             * The Class names.
             */
            Set<String> classNames;

            /**
             * Instantiates a new Fn.
             *
             * @param c the c is object of type FrameNodeCapsule
             */
            _FN(Capsule c) {
                this.level = c.getLevel();
                this.nodeType = nodeTypeOfE(c.elment);
                this.id = c.elment.id();
                this.classNames = c.elment.classNames();

                synchronized (Traveling.this.ndtpCount) {
                    Map<NodeType, Integer> ncnt = Traveling.this.ndtpCount;
                    int i = ncnt.getOrDefault(nodeType, 0);
                    ncnt.put(nodeType, ++i);
                }
            }

            @Override
            public NodeType getNodeType() {
                return nodeType;
            }

            @Override
            public List<FrameNode> getChildren() {
                return Collections.unmodifiableList(children);
            }

            @Override
            public int getLevel() {
                return level;
            }

            @Override
            public String getID() {
                return id;
            }

            @Override
            public Set<String> getTagClassNames() {
                return classNames;
            }
        }

        /**
         * The Ndtp count. Record count of every node type.
         */
        final Map<NodeType, Integer> ndtpCount = Collections.synchronizedMap(
                new EnumMap<>(NodeType.class));
        /**
         * Entry node.
         */
        private _FN rootNode;
        /**
         * The nodes will be used to calculate eigenvalue of the traveling page.
         */
        private LinkedList<FrameNode> nodes = new LinkedList<>();

        /**
         * Instantiates a new Traveling.
         *
         * @param document the document
         * @throws PageParserException the page parser exception
         */
        Traveling(Document document) throws PageParserException {
            Element element = document.body();
            if (element == null)
                throw new PageParserException("There isn't a body element.");
            Capsule root = new Capsule(element, 0);
            buildFrame(root);
            cleanFrame(root);

            LinkedList<Capsule> fifo = new LinkedList<>();
            LinkedList<_FN> frame = new LinkedList<>();
            rootNode = new _FN(root);
            fifo.add(root); frame.add(rootNode); // Use to wide traveling
            _FN _r;

            /**
             * Collects all nodes whose keeping flag is true.
             */
            do {
                Capsule _c = fifo.removeLast();
                _r = frame.removeLast();
                nodes.add(_r);

                if (_c.children == null || _c.children.isEmpty()) continue;

                List<Capsule> chi = _c.children.stream().filter(a->a.keeping)
                        .collect(Collectors.toList());
                List<_FN> nds = chi.stream().map(_FN::new).collect(Collectors.toList());
                fifo.addAll(chi);
                frame.addAll(nds);
                _r.children.addAll(nds);
            }
            while (!fifo.isEmpty());
        }

        /**
         * Build frame.
         * Use deep traveling method to drawing the frame with capsule node.
         * The traveling use recurrence style. So it should rewrite to iteration.
         *
         * @param c the c
         */
        void buildFrame(Capsule c) {
            if (c.level == PageParserSayidImp.this.deepth) return;

            Element _e = c.elment;
            List<Element> _es = new ArrayList<>(new ArrayList<>(_e.children()));
            if (_es.isEmpty()) return;

            List<Element> _o = new LinkedList<>();
            do {
                Element e = _es.remove(0);
                if (nodeTypeOfE(e) == null)
                    _es.addAll(new ArrayList<>(e.children()));
                else
                    _o.add(e);
            } while (!_es.isEmpty());
            c.children = _o.stream().map(a -> new Capsule(a, c.level + 1))
                .collect(Collectors.toList());

            for (int i = 1; i < c.children.size(); i++) {
                Capsule ci_0 = c.children.get(i -1);
                Capsule ci_1 = c.children.get(i);
                ci_0.rightSibling = ci_1;
                ci_1.leftSibling  = ci_0;
                ci_0.father = ci_1.father = c;
            }
            c.children.forEach(this::buildFrame);
        }

        /**
         * Clean frame.
         * For example: If some p tag side by side and tey has same css name
         * , they can be combined to one tag. So that one of them should be
         * retain, others should be left.
         *
         * @param c the c
         */
        void cleanFrame(Capsule c) {
            c.keeping = FiltersConfig.doFilter(c); //It don't remove from tree, only marks keeping flag to false;
            if (c.children == null || c.children.isEmpty()) return;
            c.children.forEach(this::cleanFrame);
        }
    }
}