package com.base.discovery.tagtypes;

import com.base.discovery.XSDElem;

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
