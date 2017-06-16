package com.ucap.duanwu.htmlpage;

import java.io.InputStream;

/**
 * Created by emmet on 2017/5/23.
 */
public interface PageParser {
    FramePage doParse(InputStream input, String charsetName, String baseUri)
        throws PageParserException;
    FramePage doParse(String url) throws PageParserException;
    void setParserDeepth(int deepth);
    int getParserDeepth();
    boolean isFree();
}
