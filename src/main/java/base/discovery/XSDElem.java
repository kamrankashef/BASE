package base.discovery;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class XSDElem implements Comparable<XSDElem> {

    public abstract ElemType getType();

    public abstract String renderName();

    public enum ElemType {
        DATA,
        COMPLEX,
        SEQ,
        PARENT,
        REF
    }

    private final String name;
    private final List<XSDElem> children = new LinkedList<>();

    public XSDElem(final String name) {
        this.name = name;
    }

    public Collection<XSDElem> children() {
        return this.children;
    }

    public boolean hasChild(final XSDElem xsdElem) {
        for (final XSDElem elem : this.children()) {
            if (xsdElem.name.equals(elem.name)) {
                return true;
            }
        }
        return false;
    }

    public XSDElem getChild(final XSDElem xsdElem) {
        for (final XSDElem elem : this.children()) {
            if (xsdElem.name.equals(elem.name)) {
                return elem;
            }
        }
        return null;
    }

    public String name() {
        return this.name;
    }

    public XSDElem addChild(final XSDElem child) {
        this.children.add(child);
        return this;
    }

    @Override
    public String toString() {
        return prettyPrint(0);
    }

    public String prettyPrint(final int tabCount) {
        final StringBuilder bldr = new StringBuilder();

        IntStream.rangeClosed(0, tabCount).forEach(i -> {
            bldr.append("\t");
        });
        bldr.append(this.renderName())
                .append("\n");
        for (final XSDElem elem : children) {
            bldr.append(elem.prettyPrint(tabCount + 1));
        }
        return bldr.toString();
    }

    @Override
    public int compareTo(final XSDElem o) {
        if (o == null) {
            return -1;
        }
        return this.name.compareTo(o.name);
    }
}
