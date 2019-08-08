package com.base.parsergen;

import com.base.model.AbstractModel;
import com.base.model.Model;
import com.base.model.PrimitiveField;
import com.base.model.PrimitiveType;
import com.base.parsergen.csv.DelimitedParserGenerator;
import com.base.parsergen.rules.ParseRuleSet;
import com.base.parsergen.rules.SourceFiles;
import com.base.parsergen.rules.training.SourceFilesI;
import com.google.common.base.Splitter;
import com.kamserverutils.common.util.IOUtil;
import com.kamserverutils.common.util.StringUtil;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

// Expecting a single class defined by a delimited file
final public class DeprecatedDelimitedBuilder extends AbstractBuilderFromSource {

    Map<String, AbstractModel> models = new HashMap<>();
    final DelimitedParserGenerator delimitedParserGenerator;

    public DeprecatedDelimitedBuilder(final ParseRuleSet parseRuleSet, final DelimitedParserGenerator delimitedParserGenerator) throws IOException {
        super(parseRuleSet);
        this.delimitedParserGenerator = delimitedParserGenerator;
    }

    @Override
    public Collection<AbstractModel> getElemModels() {
        return new LinkedList(models.values());
    }

    @Override
    public Map<String, AbstractModel> getElemModelMap() {
        return new HashMap(models);
    }

    @Override
    protected void process() throws IOException {

        for (final SourceFilesI.SourceFileI sourceFile : parseRuleSet.sourceFiles.getFiles()) {

            final String fileAsStr = IOUtil.inputStreamToString(sourceFile.getSourceFile());

            final String modelName = sourceFile.getType();

            final String[] rows = fileAsStr.split("\n");
            final String[] originalColNames = rows[0].split(delimitedParserGenerator.getSplitExpression());

            final AbstractModel model = new Model(modelName, parseRuleSet.org);
            for (int colNum = 0; colNum < originalColNames.length; colNum++) {
                final String originalColName = originalColNames[colNum].trim();
                final String newColName = parseRuleSet.typeRenamer.rename(originalColName);
                final String[] sampleData = new String[rows.length - 1];
                boolean canBeNull = false;
                for (int rowNum = 1; rowNum < rows.length; rowNum++) {
                    sampleData[rowNum - 1] = Splitter.on(Pattern.compile(delimitedParserGenerator.getSplitExpression())).splitToList(rows[rowNum]).toArray(new String[0])[colNum];
                    if (sampleData[rowNum - 1] == null || StringUtil.isNullWhiteSpace(sampleData[rowNum - 1])) {
                        canBeNull = true;
                    }
                }

                final PrimitiveType type
                        = parseRuleSet.typeSets.nameToType(modelName, originalColName, sampleData);
                final PrimitiveField field = new PrimitiveField(newColName, originalColName, type);
                field.setNullable(canBeNull);
                model.addPrimitiveField(field);
            }

            this.models.put(modelName, model);

            final String parserName = modelName + "Parser";
            final String target = parserName;

            // Assert size is 1
            final String genParser = delimitedParserGenerator.createParser(
                    parserName,
                    models.values().iterator().next());

            genedParsers.put("src/main/" + parserName + ".java", genParser);

            mainsBuildXML.add(AbstractBuilderFromSource.createAntTarget(target,
                    "main." + parserName,
                    parserName,
                    sourceFile.getType() + " parser",
                    "compile,check-jdbc-url",
                    Collections.singletonList("${xml.file}"),
                    Collections.singletonList("JDBC_CONNECTION_STRING=${jdbc.connection.string}")));

        }
    }

}
