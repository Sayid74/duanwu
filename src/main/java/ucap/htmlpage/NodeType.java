package ucap.htmlpage;

/**
 * Created by emmet on 2017/5/23.
 */
public enum NodeType
{
    BODY_
    , DIV_, P_
    , TABLE_, TD_, TR_, TH_, THEAD_, TBODY_, TFOOT_, COL_, COLGROUP_
    , FORM_, FRAME_, FRAMESET_, IFRAME_
    , UL_, OL_, LI_, DL_, DT_, DD_
    , ARTICLE_, ASIDE_, CAPTION_, DETAILS_
    , MAP_, IMAG_, VIDEO_
    , FIGURE_, FIGCAPTION_;

    public static NodeType nodeTypeOf(String s)
    {
        if (s.toLowerCase().equals("body"))            return BODY_;
        else if (s.toLowerCase().equals("div"))        return DIV_;
        else if (s.toLowerCase().equals("p"))          return P_;
        else if (s.toLowerCase().equals("table"))      return TABLE_;
        else if (s.toLowerCase().equals("td"))         return TD_;
        else if (s.toLowerCase().equals("tr"))         return TR_;
        else if (s.toLowerCase().equals("th"))         return TH_;
        else if (s.toLowerCase().equals("thead"))      return THEAD_;
        else if (s.toLowerCase().equals("tbody"))      return TBODY_;
        else if (s.toLowerCase().equals("tfoot"))      return TFOOT_;
        else if (s.toLowerCase().equals("col"))        return COL_;
        else if (s.toLowerCase().equals("colgroup"))   return COLGROUP_;
        else if (s.toLowerCase().equals("form"))       return FORM_;
        else if (s.toLowerCase().equals("frame"))      return FRAME_;
        else if (s.toLowerCase().equals("frameset"))   return FRAMESET_;
        else if (s.toLowerCase().equals("iframe"))     return IFRAME_;
        else if (s.toLowerCase().equals("ul"))         return UL_;
        else if (s.toLowerCase().equals("ol"))         return OL_;
        else if (s.toLowerCase().equals("li"))         return LI_;
        else if (s.toLowerCase().equals("dl"))         return DL_;
        else if (s.toLowerCase().equals("dt"))         return DT_;
        else if (s.toLowerCase().equals("dd"))         return DD_;
        else if (s.toLowerCase().equals("article"))    return ARTICLE_;
        else if (s.toLowerCase().equals("aside"))      return ASIDE_;
        else if (s.toLowerCase().equals("caption"))    return CAPTION_;
        else if (s.toLowerCase().equals("details"))    return DETAILS_;
        else if (s.toLowerCase().equals("map"))        return MAP_;
        else if (s.toLowerCase().equals("imag"))       return IMAG_;
        else if (s.toLowerCase().equals("video"))      return VIDEO_;
        else if (s.toLowerCase().equals("figure"))     return FIGURE_;
        else if (s.toLowerCase().equals("figcaption")) return FIGCAPTION_;
        else return null;
    }
}
