package com.base.parsergen.xml;

import java.util.Set;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class XMLTagUtil {

    /**
     * A text tag if no non-meta attributes and no children
     *
     * @param elem
     * @param xmlns
     * @return
     */
    public static boolean isATextTag(final Element elem, final Set<String> xmlns) {
        // If it has children, it's not text
        if (!elem.children().isEmpty()) {
            return false;
        }
        // If there is non-xmlns attribute, it is not a text tag
        TOP:
        for (final Attribute attr : elem.attributes()) {
            for (final String ns : xmlns) {
                if (attr.getKey().startsWith(ns + ":")) {
                    continue TOP;
                }
            }

            return false;
        }

        return true;
    }

}
