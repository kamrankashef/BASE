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
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class CSVGenTest extends XMLGenTest {

    @Override
    final protected AbstractBuilderFromSource getBuilder() throws IOException {
        return AbstractBuilderFromSource.run(new CSVBuilder(
                new ParseRuleSet(getOrg(),
                        getModelAugmenterI(),
                        getElemModelMethods(),
                        getTypeSetsI(),
                        getTypeRenamerI(),
                        getSourceFiles(),
                        allowMissingAttributes(),
                        getConstraints()),
                new CSVParserGenerator()
        ));
    }

    @Override
    public Set<ModelGen.ModelMethodGenerator> getElemModelMethods() {
        final Set<ModelGen.ModelMethodGenerator> elemModelMethods = new LinkedHashSet<>();
        elemModelMethods.add(new FromCSVGenerator());
        elemModelMethods.add(new ConstructorGenerator());
        return elemModelMethods;
    }

    public Set<DLGen.DLMethodGenerator> getMergedDLMethods() {
        final Set<DLGen.DLMethodGenerator> mergedDLMethods = new LinkedHashSet<>();
        mergedDLMethods.add(new InsertRaw());
        mergedDLMethods.add(new TruncateTable());
        mergedDLMethods.add(new InsertObject());
        mergedDLMethods.add(new InsertObjectDBUtil());
        mergedDLMethods.add(new InsertObjectBatch());
        return mergedDLMethods;
    }

}
