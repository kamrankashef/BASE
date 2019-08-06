package com.base.parsergen.rules.impl;

import com.base.model.PrimitiveType;
import com.base.parsergen.rules.TypeSetsI;
import java.util.HashMap;
import java.util.Map;

public class StatefulTypeSetGuesser implements TypeSetsI {

    final Map<String, EvalState> evals = new HashMap<>();

    private static final class EvalState {

        boolean couldBeDouble = true;
        boolean couldBeLong = true;
        boolean couldBeBoolean = true;
        PrimitiveType bestGuess = null;
        boolean nullabe = false;
        boolean encounteredNonNull = false;
        int maxLength = 0;
    }

    boolean lastAllowedNull = false;

    @Override
    final public boolean lastAllowedNull() {
        return lastAllowedNull;
    }

    @Override
    public PrimitiveType nameToType(
            final String modelName,
            final String originalColName,
            final Object... sampleValues) {

        final String key = modelName + "::" + originalColName;
        if (!evals.containsKey(key)) {
            evals.put(key, new EvalState());
        }
        final EvalState evalState = evals.get(key);

        for (final Object sampleValue : sampleValues) {
            if (sampleValue == null || sampleValue.toString().trim().isEmpty()) {
                evalState.nullabe = true;
                lastAllowedNull = evalState.nullabe;
                continue;
            }

            evalState.encounteredNonNull = true;
            if (!(sampleValue instanceof Boolean)
                    && !"n".equalsIgnoreCase(sampleValue.toString())
                    && !"y".equalsIgnoreCase(sampleValue.toString())
                    && !"true".equalsIgnoreCase(sampleValue.toString())
                    && !"false".equalsIgnoreCase(sampleValue.toString())) {
                evalState.couldBeBoolean = false;
            }
            if (!sampleValue.toString().trim().matches("^-?\\d+$")) {
                evalState.couldBeLong = false;
            }
            if (!sampleValue.toString().trim().matches("^-?(\\d+)(\\.\\d+)?|-?(\\.\\d+)$")) {
                evalState.couldBeDouble = false;
            }
            evalState.maxLength = Math.max(evalState.maxLength, sampleValue.toString().length());
        }

        final String colToLower = originalColName.toLowerCase();

        if (!evalState.encounteredNonNull) {
            evalState.bestGuess = PrimitiveType.TINY_TEXT;
        } else if (evalState.couldBeBoolean) {
            evalState.bestGuess = PrimitiveType.BOOLEAN;
        } else if (evalState.couldBeLong) {
            if (colToLower.equals("id") || colToLower.matches("^.*[A-Za-z]id$")) {
                evalState.bestGuess = PrimitiveType.LONG;
            } else {
                evalState.bestGuess = PrimitiveType.INT;
            }
        } else if (evalState.couldBeDouble) {
            evalState.bestGuess = PrimitiveType.DOUBLE;
        } else {
            if (evalState.maxLength > 10000) {
                evalState.bestGuess = PrimitiveType.LONG_TEXT;
            } else if (evalState.maxLength > 1000) {
                evalState.bestGuess = PrimitiveType.MEDIUM_TEXT;
            } else {
                evalState.bestGuess = PrimitiveType.TEXT;
            }
        }
        lastAllowedNull = evalState.nullabe;
        return evalState.bestGuess;

    }

}
