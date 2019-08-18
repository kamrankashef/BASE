package base.v3;

import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.CSVBuilder;
import base.parsergen.csv.CSVParserGenerator;
import base.parsergen.rules.ParseRuleSet;
import base.parsergen.rules.SourceFiles;

import java.io.IOException;

public class CSVApplicationBuilder extends AbstractApplicationBuilder {


    public CSVApplicationBuilder(String org, SourceFiles sourceFiles, String exportDir) {
        super(org, sourceFiles, exportDir);
    }

    protected AbstractBuilderFromSource getBuilderFromSource() throws IOException {
        return new CSVBuilder(
                new ParseRuleSet(this.org,
                        this.modelAugmenter,
                        this.elemModelMethods,
                        this.typeSets,
                        this.typeRenamer,
                        this.sourceFiles,
                        this.allowMissing,
                        this.constraints),
                new CSVParserGenerator()
        );
    }

}
