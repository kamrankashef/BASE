package base.discovery.tagtypes;

import base.discovery.XSDElem;

public class RefElem extends XSDElem {

    public RefElem(String name) {
        super(name);
    }

    @Override
    public ElemType getType() {
        return ElemType.REF;
    }

    public String renderName() {
        return "-> " + name();
    }

}
