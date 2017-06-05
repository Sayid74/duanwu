package com.ucap.commons.logger;

import prsn.sayid.common.LoggerAdapterSayid;

/**
 * Created by emmet on 2017/5/23.
 */
public class LFactory
{
    private static volatile Class<? extends LoggerAdapter> loggerClass;
    static
    {
        try
        {
            String lcPrp = System.getProperty("com.ucap.logger");
            ClassLoader.getSystemClassLoader().loadClass(lcPrp);
        }
        catch (ClassNotFoundException ex) 
        {
            ex.printStackTrace();
        }
    }

    public static LoggerAdapter makeL(Class c)
    {
        if (loggerClass == null) return null;

        LoggerAdapter adapter = null;
        try {
            adapter = loggerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) { }

        adapter.init(c);
        return adapter;
    }

    static
    {
        try {
            LoggerAdapterSayid.class.newInstance();
            loggerClass = LoggerAdapterSayid.class;
        } catch (InstantiationException|IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
