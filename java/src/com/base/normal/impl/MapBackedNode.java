package com.base.normal.impl;

import com.base.model.PrimitiveType;
import static com.base.model.PrimitiveType.LONG;
import com.base.normal.NodeType;
import com.base.parsergen.rules.TypeRenamerI;
import com.base.parsergen.rules.TypeSetsI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapBackedNode {

    final String name;
    final Map<String, Object> backingMap;
    final List<Object> backingList;
    final NodeType nodeType;
    final PrimitiveType underlyingType;
    final public boolean isNull;
    final TypeSetsI typeSets;
    final TypeRenamerI renamer;

    public MapBackedNode(final String name,
            final Object backingObject,
            final TypeSetsI typeSets,
            final TypeRenamerI renamer) {

        this.typeSets = typeSets;
        this.renamer = renamer;
        this.name = renamer.rename(name);

        if (backingObject == null) {
            this.nodeType = null;
            this.isNull = true;
            this.underlyingType = null;
            this.backingList = null;
            this.backingMap = null;
            return;
        }

        this.nodeType = TypeChecker.getType(backingObject);
        this.isNull = false;

        switch (this.nodeType) {
            case COMPLEX_LIST:
                backingMap = null;
                backingList = (List<Object>) backingObject;
                underlyingType = null;
                break;
            case LITERAL_LIST:
                backingMap = null;
                backingList = (List<Object>) backingObject;
                underlyingType = typeSets.nameToType(null, this.name, backingList);// TypeChecker.guessType(backingList).getBestGuess();
                break;
            case OBJECT_SLASH_MAP:
                backingMap = (Map<String, Object>) backingObject;
                backingList = null;
                underlyingType = null;
                break;
            case LITERAL:
                backingMap = null;
                backingList = null;
                underlyingType = typeSets.nameToType(null, this.name, backingObject); // TypeChecker.guessType(backingList).getBestGuess();TypeChecker.guessType(backingObject).getBestGuess();
                break;
            case UNKNOWN_LIST:
                backingMap = null;
                backingList = null;
                underlyingType = null;
                break;
            default:
                System.err.println(nodeType);
                throw new UnsupportedOperationException("Unsupported type " + backingObject.getClass());
        }

    }

    public String getName() {
        return this.name;
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public boolean hasSubNode(String name) {
        return backingMap.containsKey(name);
    }

    public PrimitiveType getUnderlyingType() {
        return this.underlyingType;
    }

    public Collection<String> getChildName() {
        return new HashSet<>(backingMap.keySet());
    }

    public MapBackedNode getChild(final String name) {
        return new MapBackedNode(name, backingMap.get(name), this.typeSets, this.renamer);
    }

    public Collection<MapBackedNode> getBackingList() {
        return backingList
                .stream()
                .map((entry) -> {
//                    return new MapBackedNode("*", entry, this.typeSets);
// TODO Consider using a special List index
// Not a huge problem b/c literal list will not have named children
                    return new MapBackedNode(getName(), entry, this.typeSets, this.renamer);
                }).collect(Collectors.toList());

    }

    public Collection<Object> getPrimitiveList() {
        return new LinkedList<>(backingList);
    }

    public Map<String,Object> getBackingMap() {
        return new HashMap<>(backingMap);
    }
}
