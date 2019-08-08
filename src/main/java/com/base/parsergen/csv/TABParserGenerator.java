package com.base.parsergen.csv;

import static com.base.parsergen.csv.DelimitedParserGenerator.ESCAPED_TAB_SPLIT_EXP;

public class TABParserGenerator extends DelimitedParserGenerator {

    public TABParserGenerator() {
        super(ESCAPED_TAB_SPLIT_EXP);
    }

}
