package com.base.discovery.tagtypes;

import com.base.discovery.XSDElem;

public class SeqElem extends XSDElem {

    public SeqElem(String name) {
        super(name);
    }

    @Override
    public ElemType getType() {
        return ElemType.SEQ;
    }
    
    public String renderName() {
        return "*";
    }

}
