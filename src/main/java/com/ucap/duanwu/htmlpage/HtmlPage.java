package com.ucap.duanwu.htmlpage;

import com.ucap.commons.logger.LFactory;
import com.ucap.commons.logger.LoggerAdapter;
import prsn.sayid.duanwu.htmlpage.FramePageSayid;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;
import java.io.InputStream;

/**
 * Created by emmet on 2017/5/23.
 */
public class HtmlPage
{
    private static LoggerAdapter L = LFactory.makeL(HtmlPage.class);
    private static class PageParserWrap implements InvocationHandler
    {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable
        {
            if (method == PageParser.class.getMethod("doParse"
                    , InputStream.class, String.class))
            {
                long t0 = System.currentTimeMillis();
                Object r = method.invoke(proxy, args);
                L.info(String.format("parser uses times: %d"
                        , System.currentTimeMillis() - t0));
                return r;
            }
            else
            {
                return method.invoke(proxy, args);
            }
        }
    }

    public static Class<? extends PageParser> parserClass = null;

    public static PageParser makePageParser()
            throws InstantiationException, IllegalAccessException
    {
        return parserClass == null ? null
                :(PageParser) Proxy.newProxyInstance(
                        parserClass.getClassLoader()
                , PageParser.class.getInterfaces()
                , new PageParserWrap());
    }

    public static synchronized void mount(Class<? extends PageParser> c)
    {
        parserClass = c;
    }
}
