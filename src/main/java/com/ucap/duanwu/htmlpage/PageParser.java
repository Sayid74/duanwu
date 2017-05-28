package com.ucap.duanwu.htmlpage;

import java.io.InputStream;

/**
 * Created by emmet on 2017/5/23.
 */
public interface PageParser {
    public FramePage doParse(InputStream input, String charestName) throws PageParserException;
    public void setParserDeepth(int deepth);
    public int getParserDeepth();
    public boolean isFree();
    public void free();
}
