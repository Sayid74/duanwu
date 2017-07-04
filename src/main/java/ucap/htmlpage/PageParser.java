package ucap.htmlpage;

import java.io.InputStream;

/**
 * Created by emmet on 2017/5/23.
 */
public interface PageParser {
    FrameDigest doParse(InputStream input, String charsetName, String baseUri)
        throws PageParserException;
    FrameDigest doParse(String url) throws PageParserException;
    void setParserDeepth(int deepth);
    int getParserDeepth();
    boolean isFree();
}
