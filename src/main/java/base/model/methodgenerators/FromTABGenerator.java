package base.model.methodgenerators;

import base.parsergen.csv.DelimitedParserGenerator;

public class FromTABGenerator extends DelimitedGenerator {

    @Override
    protected String getEscapedDelimiter() {
        return DelimitedParserGenerator.ESCAPED_TAB_SPLIT_EXP;
    }

    @Override
    protected String getType() {
        return "TAB";
    }

}
