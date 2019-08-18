package fullsuite;

import base.model.methodgenerators.FromCSVGeneratorDeprecated;
import base.gen.ModelGen;
import base.model.methodgenerators.ConstructorGenerator;
import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.DeprecatedDelimitedBuilder;
import base.parsergen.csv.DeprecatedCSVParserGenerator;
import base.parsergen.rules.ParseRuleSet;
import base.v3.AbstractApplicationBuilder;
import base.v3.CSVApplicationBuilder;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class DeprecatedCSVGenTest extends XMLGenTest {

    @Override
    protected AbstractApplicationBuilder getApplicationBuilder() throws IOException {
        // TODO May need to use the deprecated version
        return new CSVApplicationBuilder(getOrg(), getSourceFiles(), getExportDir());
    }


    @Override
    protected void applyOverrides(final AbstractApplicationBuilder abstractApplicationBuilder) {
        abstractApplicationBuilder.addElemModelMethods(new FromCSVGeneratorDeprecated())
                .addElemModelMethods(new ConstructorGenerator());
    }

}

