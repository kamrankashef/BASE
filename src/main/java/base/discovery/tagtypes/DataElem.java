package base.discovery.tagtypes;

import base.discovery.XSDElem;

public class DataElem extends XSDElem {

    public DataElem(String name) {
        super(name);
    }

    @Override
    public ElemType getType() {
        return ElemType.DATA;
    }

    public String renderName() {
        return "[" + name() + "]";
    }

}
