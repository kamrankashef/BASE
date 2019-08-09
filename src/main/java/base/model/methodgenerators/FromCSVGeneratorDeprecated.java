package base.model.methodgenerators;

import base.parsergen.csv.DelimitedParserGenerator;

public class FromCSVGeneratorDeprecated extends DelimitedGenerator {

    @Override
    protected String getEscapedDelimiter() {
        return DelimitedParserGenerator.ESCAPED_CSV_SPLIT_EXP;
    }

    @Override
    protected String getType() {
        return "CSV";
    }

}
