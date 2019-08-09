package base.discovery.tagtypes;

import base.discovery.XSDElem;

public class ParentElem extends XSDElem {

    public ParentElem(String name) {
        super(name);
    }

    @Override
    public ElemType getType() {
        return ElemType.PARENT;
    }
    
    public String renderName() {
        return name();
    }

}
