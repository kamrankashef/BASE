package fullsuite;

import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertObjectBatch;
import base.dl.methodgenerators.InsertObjectDBUtil;
import base.dl.methodgenerators.InsertRaw;
import base.dl.methodgenerators.TruncateTable;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.FromCSVGenerator;
import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.CSVBuilder;
import base.parsergen.csv.CSVParserGenerator;
import base.parsergen.rules.ParseRuleSet;
import base.v3.AbstractApplicationBuilder;
import base.v3.CSVApplicationBuilder;
import base.v3.XMLApplicationBuilder;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class CSVGenTest extends XMLGenTest {

    @Override
    protected AbstractApplicationBuilder getApplicationBuilder() throws IOException {
        return new CSVApplicationBuilder(getOrg(), getSourceFiles(), getExportDir());
    }


    @Override
    protected void applyOverrides(final AbstractApplicationBuilder abstractApplicationBuilder) {
        abstractApplicationBuilder.addElemModelMethods(new FromCSVGenerator())
                .addElemModelMethods(new ConstructorGenerator())
                .addMergedDLModelMethod(new InsertRaw())
                .addMergedDLModelMethod(new TruncateTable())
                .addMergedDLModelMethod(new InsertObject())
                .addMergedDLModelMethod(new InsertObjectDBUtil())
                .addMergedDLModelMethod(new InsertObjectBatch());
    }

}
