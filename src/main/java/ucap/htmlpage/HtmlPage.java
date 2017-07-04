package ucap.htmlpage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sayid.htmlpage.PageParserSayidImp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by emmet on 2017/5/23.
 */
public class HtmlPage {
    private static final Logger L = LoggerFactory.getLogger(HtmlPage.class);

    /**
     * It's a wrapper to give dynamic proxy object.
     */
    private static class PageParserWrap implements InvocationHandler {
        private final PageParser parser;

        /**
         * Instantiates a new Page parser wrap.
         *
         * @param parser the parser
         */
        PageParserWrap(PageParser parser) {
            this.parser = parser;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if ("doParse".equals(method.getName())) {
                long t0 = System.currentTimeMillis();
                Object r = method.invoke(parser, args);
                L.info(String.format("parser uses times: %d"
                        , System.currentTimeMillis() - t0));
                return r;
            } else {
                return method.invoke(parser, args);
            }
        }
    }

    /**
     * Make page parser page parser.
     *
     * @return the page parser
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    public static PageParser makePageParser()
            throws InstantiationException, IllegalAccessException {
        return (PageParser) Proxy.newProxyInstance(
                        PageParserSayidImp.class.getClassLoader()
                , PageParserSayidImp.class.getInterfaces()
                , new PageParserWrap(PageParserSayidImp.class.newInstance()));
    }
}
