package com.base.normal.impl;

import com.base.model.PrimitiveField;
import com.base.model.PrimitiveType;
import com.base.normal.NodeType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TypeChecker {

    private boolean couldBeDouble = true;
    private boolean couldBeLong = true;
    private boolean couldBeBoolean = true;
    private PrimitiveType bestGuess = PrimitiveType.TINY_TEXT;
    private boolean couldBeNull = false;
    private int longestSoFar = 0;

    public static TypeChecker guessType(final Object o) {

        final TypeChecker checker = new TypeChecker();
        checker.register(o);
        return checker;
    }

    public static TypeChecker guessType(final Collection<Object> objects) {

        final TypeChecker checker = new TypeChecker();
        objects.forEach((object) -> {
            checker.register(object);
        });
        return checker;
    }

    public PrimitiveType getBestGuess() {
        return bestGuess;
    }

    public boolean nullable() {
        return this.couldBeNull;
    }

    public void register(final Object o) {

        if (o == null) {
            couldBeNull = true;
            return;
        } else if (couldBeLong && ((o instanceof Long) || (o instanceof Integer))) {
            couldBeBoolean = false;
            bestGuess = PrimitiveType.LONG;
        } else if (o instanceof Double && couldBeDouble) {
            couldBeLong = false;
            couldBeBoolean = false;
            bestGuess = PrimitiveType.DOUBLE;
        } else if (o instanceof Boolean && couldBeBoolean) {
            couldBeDouble = false;
            couldBeLong = false;
            bestGuess = PrimitiveType.BOOLEAN;
        } else if (o instanceof String) {
            final String asString = (String) o;
            if (("true".equalsIgnoreCase(asString) || "false".equalsIgnoreCase(asString)) && couldBeBoolean) {
                couldBeDouble = false;
                couldBeLong = false;
                bestGuess = PrimitiveType.BOOLEAN;
            } else if (asString.trim().matches("^-?\\d{1,14}$") && couldBeLong) {
//                if (asString.length() < 9) {
//                    bestGuess = PrimitiveType.LONG;
//                }
                couldBeBoolean = false;
                bestGuess = PrimitiveType.LONG;
            } else if (asString.trim().matches("^-?(\\d{1,14})(\\.\\d{1,14})?|-?(\\.\\d{1,30})$") && couldBeDouble) {
                couldBeLong = false;
                couldBeBoolean = false;
                bestGuess = PrimitiveType.DOUBLE;
            } else {
                couldBeBoolean = false;
                couldBeDouble = false;
                couldBeLong = false;
                longestSoFar = Math.max(asString.length(), longestSoFar);
                // Overly tight coupling with type implementations
                if (longestSoFar <= 128) {
                    bestGuess = PrimitiveType.TINY_TEXT;
                } else if (longestSoFar <= 1024) {
                    bestGuess = PrimitiveType.TEXT;
                } else if (longestSoFar <= 40000) {
                    bestGuess = PrimitiveType.MEDIUM_TEXT;
                } else if (longestSoFar <= 1000000) {
                    bestGuess = PrimitiveType.LONG_TEXT;
                } else {
                    throw new IllegalArgumentException("Cannot accomidate object of length " + longestSoFar);
                }
            }

        } else {
            throw new IllegalArgumentException("Unknown conversion for object: " + o + " of type " + o.getClass());
        }
    }

    static public boolean isLiteral(final Object o) {
        return o.getClass().equals(Boolean.class)
                || o.getClass().equals(Double.class)
                || o.getClass().equals(String.class);
    }

    static public NodeType getType(final Object o) {
        if (isLiteral(o)) {
            return NodeType.LITERAL;
        }
        if (o instanceof Map) {
            return NodeType.OBJECT_SLASH_MAP;
        }
        if (o instanceof ArrayList) {
            List<Object> list = (List) o;
            if (list.isEmpty()) {
                return NodeType.UNKNOWN_LIST;
            }
            boolean foundLiterals = false;
            boolean foundObjects = false;

            for (final Object item : list) {
                final boolean isLiteral = isLiteral(item);
                foundLiterals |= isLiteral;
                foundObjects |= !isLiteral;
            }

            // TODO What to do in the empty case?
            if (foundLiterals == foundObjects) {
                throw new IllegalStateException("Mixed set of types literals and objects in list: " + list);
            }

            if (foundLiterals) {
                return NodeType.LITERAL_LIST;
            } else {
                return NodeType.COMPLEX_LIST;
            }
        }
        throw new RuntimeException("Could not find a type for object: " + o + " of class " + o.getClass());
    }

    public static void mergeInto(
            final PrimitiveField mergeInto,
            final PrimitiveField mergeFrom) {
        final PrimitiveType type = merge(mergeInto.getPrimitiveType(), mergeFrom.getPrimitiveType());
        final boolean nullable = mergeInto.nullable() || mergeFrom.nullable();
        mergeInto.setPrimitiveType(type);
        mergeInto.setNullable(nullable);
    }

    public static PrimitiveType merge(
            final PrimitiveType type1,
            final PrimitiveType type2) {
        if (type1 == null) {
            return type2;
        }
        if (type2 == null) {
            return type1;
        }
        if (type1 == type2) {
            return type1;
        }

        if ((type1 == PrimitiveType.DOUBLE && type2 == PrimitiveType.LONG)
                || (type2 == PrimitiveType.DOUBLE || type1 == PrimitiveType.LONG)) {
            return PrimitiveType.DOUBLE;
        }
        if ((type1 == PrimitiveType.DOUBLE && type1 == PrimitiveType.LONG)
                || (type2 == PrimitiveType.DOUBLE || type1 == PrimitiveType.LONG)) {
            return PrimitiveType.DOUBLE;
        }

        if (type1 == PrimitiveType.LONG_TEXT || type2 == PrimitiveType.LONG_TEXT) {
            return PrimitiveType.LONG_TEXT;
        }
        if (type1 == PrimitiveType.MEDIUM_TEXT || type2 == PrimitiveType.MEDIUM_TEXT) {
            return PrimitiveType.MEDIUM_TEXT;
        }
        if (type1 == PrimitiveType.TEXT || type2 == PrimitiveType.TEXT) {
            return PrimitiveType.TEXT;
        }
        if (type1 == PrimitiveType.TINY_TEXT || type2 == PrimitiveType.TINY_TEXT) {
            return PrimitiveType.TINY_TEXT;
        }
        return PrimitiveType.TINY_TEXT;
    }

}
