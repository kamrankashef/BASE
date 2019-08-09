package fullsuite;

import com.base.dl.methodgenerators.InsertObject;
import com.base.dl.methodgenerators.InsertObjectBatch;
import com.base.dl.methodgenerators.InsertObjectDBUtil;
import com.base.dl.methodgenerators.InsertRaw;
import com.base.dl.methodgenerators.TruncateTable;
import com.base.gen.DLGen;
import com.base.gen.ModelGen;
import com.base.model.methodgenerators.ConstructorGenerator;
import com.base.model.methodgenerators.FromCSVGenerator;
import com.base.parsergen.AbstractBuilderFromSource;
import com.base.parsergen.CSVBuilder;
import com.base.parsergen.csv.CSVParserGenerator;
import com.base.parsergen.rules.ParseRuleSet;
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
