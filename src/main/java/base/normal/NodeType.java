package base.normal;

public enum NodeType {

    // TODO consider typing literal, and literal list
    LITERAL(false, true),
    OBJECT_SLASH_MAP(false, false),
    COMPLEX_LIST(true, false),
    LITERAL_LIST(true, true),
    UNKNOWN_LIST(true, null);
    
    final public Boolean isListType;
    final public Boolean isLiteralValued;

    NodeType(Boolean isListType,
            Boolean isLiteralValued) {
        this.isListType = isListType;
        this.isLiteralValued = isLiteralValued;
    }
}
