package base.discovery;

import base.discovery.tagtypes.ComplexElem;
import base.discovery.tagtypes.DataElem;
import base.discovery.tagtypes.ParentElem;
import base.discovery.tagtypes.RefElem;
import base.discovery.tagtypes.SeqElem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XSDUtil {

    public static XSDElem parse(final Document elem) {
        final XSDElem root = new ParentElem("root");
        populate(root, elem.select("xs|schema").first().children());
        return root;
    }

    private static void populate(final XSDElem root, final Elements children) {
        for (final Element elem : children) {
            final XSDElem child;

            if ("xs:complexType".equals(elem.tagName())) {
                child = new ComplexElem("");
            } else if ("xs:sequence".equals(elem.tagName())) {
                child = new SeqElem("");
            } else if (elem.hasAttr("ref")) {
                child = new RefElem(elem.attr("ref"));
            } else if (elem.hasAttr("type")) {
                child = new DataElem(elem.attr("name"));
            } else {
                child = new ParentElem(elem.attr("name"));
            }

            populate(child, elem.children());
            root.addChild(child);
        }
    }
}
