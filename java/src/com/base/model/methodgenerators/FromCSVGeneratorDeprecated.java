package com.base.model.methodgenerators;

import com.base.gen.ModelGen;
import com.base.parsergen.csv.DelimitedParserGenerator;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ModelGen.ModelMethodGenerator.class)
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
