package base.normal.scratchspace;

import base.normal.Node;
import base.normal.NodeType;
import static base.normal.NodeType.COMPLEX_LIST;
import static base.normal.NodeType.LITERAL;
import static base.normal.NodeType.OBJECT_SLASH_MAP;
import base.normal.impl.MapBackedNode;
import base.normal.impl.TypeChecker;
import java.util.Collection;

public class NodeSchemaParser {

    final Node rnode;

    public NodeSchemaParser(
            final String name,
            final MapBackedNode exampleNode
    ) {
        rnode = new Node(name, NodeType.OBJECT_SLASH_MAP);
        mergeNode(rnode, exampleNode);
    }

    public Node getStructuredNode() {
        return rnode;
    }

    // Merge the node struture into rootNode
    private void mergeNode(final Node tallyNode, final MapBackedNode exampleNode) {

        if (tallyNode.getNodeType() == null) {
            System.out.println(tallyNode.getName());
            tallyNode.setUnderlyingType(exampleNode.getUnderlyingType());
            tallyNode.setNodeType(exampleNode.getNodeType());
            tallyNode.setNullable(true);
            return;
        }

        final NodeType nodeType = exampleNode.getNodeType();

        if (nodeType != tallyNode.getNodeType()) {
            // Ensure structurally the same
            throw new RuntimeException("Non-matching NodeTypes: " + nodeType + " != " + tallyNode.getNodeType());
        }

        if (nodeType == LITERAL) {
            tallyNode.setNullable(tallyNode.nullable() || exampleNode.isNull);
            tallyNode.setUnderlyingType(TypeChecker.merge(tallyNode.underlyingType(), exampleNode.getUnderlyingType()));
            return;
        } else if (nodeType == OBJECT_SLASH_MAP) {
            // Set nullable if expected childen are not in example
            tallyNode
                    .childrenNames()
                    .forEach((childName) -> {
                        // TODO This seems to be be operating as COMPLEX_LIST
                        if (!exampleNode.hasSubNode(childName)) {
                            System.out.println("NULLING " + childName);
                            tallyNode.getSubNode(childName).setNullable(true);
                        }
                    });
            // For each child add it to referecen if it doesn't exist
            // and recurse in

            mergeTallyAndExample(tallyNode, exampleNode);

            return;
        } else if (nodeType != NodeType.UNKNOWN_LIST) { // List type(nodeType == LITERAL_LIST || nodeType == LITERAL_LIST) {

            // Check is 
            // Loop on each example and try to merge it in
            // 1 - get each element
            final Collection<MapBackedNode> elements = exampleNode
                    .getBackingList();
            // Create entry
            final Node mergeIntoNode;
            final MapBackedNode exampleChild = elements.iterator().next();
            final String exampleChildName = exampleChild.getName();
            if (tallyNode.hasSubNode(exampleChildName)) {
                mergeIntoNode = tallyNode.getSubNode(exampleChildName);
            } else {
                mergeIntoNode = new Node(exampleChildName, exampleChild.getNodeType());
                tallyNode.addSubNode(mergeIntoNode, false);
            }
            // Apply values
            if (nodeType == COMPLEX_LIST) {
                elements.forEach((childMember) -> {

                    mergeTallyAndExample(mergeIntoNode, childMember);

                });
            } else {
                // For Literal List
//                final TypeChecker checker = TypeChecker.guessType(exampleNode.getPrimitiveList());
                mergeIntoNode.setNullable(mergeIntoNode.nullable() || exampleNode.isNull);
                mergeIntoNode.setUnderlyingType(TypeChecker.merge(exampleNode.getUnderlyingType(), mergeIntoNode.underlyingType()));
            }
        }

    }

    /**
     * Merge an example node in the tally node
     */
    private void mergeTallyAndExample(final Node tallyNode, final MapBackedNode exampleNode) {
//        final NodeType nodeType = tallyNode.getNodeType();
        final int tallyInitialSize = tallyNode.childrenNames().size();
        exampleNode
                .getChildName()
                .forEach((childName) -> {

                    final Node childTally;
                    final MapBackedNode exampleChild = exampleNode.getChild(childName);
                    if (!tallyNode.hasSubNode(childName)) {
                        // Add
                        final NodeType newType = exampleChild.getNodeType();
                        // Create - add new reference
                        childTally = new Node(childName, exampleChild.getNodeType());
                        childTally.setNullable(tallyInitialSize != 0);
                        tallyNode.addSubNode(childTally, false);
                    } else {
                        // Merge - pull existing reference
                        childTally = tallyNode.getSubNode(childName);
                    }

                    if (!exampleChild.isNull) {
                        mergeNode(childTally, exampleChild);
                    }
                });
    }

}
