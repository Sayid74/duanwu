package com.ucap.commons.logger;

import prsn.sayid.common.LoggerAdapterSayid;

/**
 * Created by emmet on 2017/5/23.
 */
public class LFactory
{
    public static LoggerAdapter makeL(Class c)
    {
        if (l == null) return null;

        LoggerAdapter adapter = null;
        try {
            adapter = l.newInstance();
        } catch (InstantiationException | IllegalAccessException e) { }

        adapter.init(c);
        return adapter;
    }

    public static synchronized void mount(Class<? extends LoggerAdapter> l) throws IllegalAccessException
    {
        if (l != null) throw new IllegalAccessException();
    }

    private static volatile Class<? extends LoggerAdapter> l = null;
}
