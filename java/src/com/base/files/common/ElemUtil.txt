package common;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ElemUtil {

    public static String getFirstSelectedText(
            final Element elem,
            final String query) {
        final Elements elems = elem.select(query);
        if (elems.isEmpty()) {
            return null;
        }
        return elems.first().text();
    }
}
