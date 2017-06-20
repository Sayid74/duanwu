package com.ucap.duanwu.htmlpage;

import com.ucap.commons.logger.LFactory;
import com.ucap.commons.logger.LoggerAdapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by emmet on 2017/5/23.
 */
public class HtmlPage {
    private static LoggerAdapter L = LFactory.makeL(HtmlPage.class);
    private static Class<? extends PageParser> parserClass = null;
    static {
        try {
            String ppPrp = System.getProperty("com.ucap.PageParser");
            Class c = ClassLoader.getSystemClassLoader().loadClass(ppPrp);
            if (Arrays.asList(c.getInterfaces()).contains(PageParser.class)) {
                L.info("PageParser is implemented by:" + c.getName());
                parserClass = (Class<PageParser>) c;
            } else {
                throw new ClassNotFoundException(
                        "com.ucap.PageParser property isn't" +
                        " a com.ucap.duanwu.html.PageParser instance.");
            }
        }
        catch (ClassNotFoundException ex) {
            L.error(ex);
        }
    }

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
        return parserClass == null ? null
                :(PageParser) Proxy.newProxyInstance(
                        parserClass.getClassLoader()
                , parserClass.getInterfaces()
                , new PageParserWrap(parserClass.newInstance()));
    }

    /**
     * Mount.
     *
     * @param parserClass the parser class
     */
    public static synchronized void mount(Class<? extends PageParser> parserClass) {
        HtmlPage.parserClass = parserClass;
    }
}
