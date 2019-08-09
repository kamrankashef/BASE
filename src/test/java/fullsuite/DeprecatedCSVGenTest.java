package fullsuite;

import base.model.methodgenerators.FromCSVGeneratorDeprecated;
import base.gen.ModelGen;
import base.model.methodgenerators.ConstructorGenerator;
import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.DeprecatedDelimitedBuilder;
import base.parsergen.csv.DeprecatedCSVParserGenerator;
import base.parsergen.rules.ParseRuleSet;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class DeprecatedCSVGenTest extends XMLGenTest {

    @Override
    final protected AbstractBuilderFromSource getBuilder() throws IOException {
        return AbstractBuilderFromSource.run(new DeprecatedDelimitedBuilder(
                new ParseRuleSet(getOrg(),
                        getModelAugmenterI(),
                        getElemModelMethods(),
                        getTypeSetsI(),
                        getTypeRenamerI(),
                        getSourceFiles(),
                        allowMissingAttributes(),
                        getConstraints()),
                 new DeprecatedCSVParserGenerator()
        ));
    }

    @Override
    public Set<ModelGen.ModelMethodGenerator> getElemModelMethods() {
        final Set<ModelGen.ModelMethodGenerator> elemModelMethods = new LinkedHashSet<>();
        elemModelMethods.add(new FromCSVGeneratorDeprecated());
        elemModelMethods.add(new ConstructorGenerator());
        return elemModelMethods;
    }
}
