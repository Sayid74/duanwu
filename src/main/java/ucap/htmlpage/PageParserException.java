package ucap.htmlpage;

/**
 * Created by emmet on 2017/5/22.
 */
public class PageParserException extends Exception
{
    public PageParserException() { }

    public PageParserException(String message)
    {
        super(message);
    }

    public PageParserException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PageParserException(Throwable cause)
    {
        super(cause);
    }

    public PageParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
