package com.base.normal;

import com.base.model.PrimitiveType;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Node {

    final private String name;
    private boolean nullable = false;
    private NodeType nodeType;
    PrimitiveType underlyingType = null;

    // "refs" in XSD speak
    final Map<String, Node> children = new TreeMap<>();

    // Real hack here
    // TODO do not nest children under * in gernerator
    public Node hackedNode() {

        if (nodeType == NodeType.COMPLEX_LIST) {
            final Node n = new Node(name, NodeType.OBJECT_SLASH_MAP);
            n.children.putAll(children.get(getName()).children);
            return n;
        }
//        else if (nodeType == NodeType.LITERAL_LIST) {
//            final Node n = new Node(name, NodeType.LITERAL);
//            System.out.println(children.get("*").children.keySet());
//            n.children.putAll(children.get("*").children);
////            n.setUnderlyingType(this.underlyingType);
//            return n;
//        }
        return this;

    }

    public Node(
            final String name,
            final NodeType nodeType) {
        this.name = name;
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getName() {
        return name;
    }

    public void addSubNode(final Node child, final boolean allowOverwrite) {
        final String key = child.getName();
        if (children.containsKey(key) && !allowOverwrite) {
            throw new RuntimeException("Overwrite not allowed");
        }
        children.put(key, child);
    }

    public boolean hasSubNode(final String name) {
        return children.containsKey(name);
    }

    public Node getSubNode(final String name) {
        return children.get(name);
    }

    public Node getSingleSubNode() {
        if (nodeType != NodeType.LITERAL_LIST) {
            throw new RuntimeException("Node type is " + nodeType + " LITERAL_LIST expected");
        }
        if (children.size() != 1) {
            throw new RuntimeException("Children size != 1");
        }
        return children.entrySet().iterator().next().getValue();
    }

    public Collection<Node> getSubNodeByType(final NodeType type) {
        final List<Node> nodes = new LinkedList<>();

        children.forEach((name, node) -> {
            if (node.getNodeType() == nodeType) {
                nodes.add(node);
            }
        });

        return nodes;
    }

    public PrimitiveType underlyingType() {
        return underlyingType;
    }

    public void setUnderlyingType(final PrimitiveType underlyingType) {
        this.underlyingType = underlyingType;
    }

    public boolean nullable() {
        return nullable;
    }

    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    public Collection<String> childrenNames() {
        return new HashSet<>(children.keySet());
    }

    public Collection<String> childNamesByType(final NodeType type) {

        final Set<String> childrenNames = new TreeSet<>();
        for (final String childName : childrenNames()) {
            if (getSubNode(childName).getNodeType() == type) {
                childrenNames.add(childName);
            }
        }
        return childrenNames;
    }

    @Override
    public String toString() {
        return "Node{" + "name=" + name
                + ", nullable=" + nullable
                + ", nodeType=" + nodeType
                + ", underlyingType=" + underlyingType
                + ", children={"
                + children.values().toString()
                + "}}";
    }

    public void setNodeType(final NodeType nodeType) {
        this.nodeType = nodeType;
    }

}
