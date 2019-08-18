package base.v3;


import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.XMLBuilder;
import base.parsergen.rules.ParseRuleSet;
import base.parsergen.rules.SourceFiles;

import java.io.IOException;

public class XMLApplicationBuilder extends AbstractApplicationBuilder {


    public XMLApplicationBuilder(String org, SourceFiles sourceFiles, String exportDir) {
        super(org, sourceFiles, exportDir);
    }

    protected AbstractBuilderFromSource getBuilderFromSource() throws IOException {
        return new XMLBuilder(
                new ParseRuleSet(this.org,
                        this.modelAugmenter,
                        this.elemModelMethods,
                        this.typeSets,
                        this.typeRenamer,
                        this.sourceFiles,
                        this.allowMissing,
                        this.constraints)
        );
    }

}
