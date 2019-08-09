package base.discovery.tagtypes;

import base.discovery.XSDElem;

public class ComplexElem extends XSDElem {

    public ComplexElem(String name) {
        super(name);
    }

    @Override
    public ElemType getType() {
        return ElemType.COMPLEX;
    }
    
    public String renderName() {
        return "COMPLEX TYPE";
    }

}
