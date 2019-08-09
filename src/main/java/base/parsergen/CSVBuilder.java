package base.parsergen;

import base.model.AbstractModel;
import base.model.Model;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.parsergen.csv.CSVParserGenerator;
import base.parsergen.rules.ParseRuleSet;
import base.parsergen.rules.training.SourceFilesI;
import kamserverutils.common.util.StringUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

final public class CSVBuilder extends AbstractBuilderFromSource {

    final Map<String, AbstractModel> models = new HashMap<>();
    final CSVParserGenerator csvParserGenerator;

    public CSVBuilder(
            final ParseRuleSet parseRuleSet,
            final CSVParserGenerator csvParserGenerator)
            throws IOException {
        super(parseRuleSet);
        this.csvParserGenerator = csvParserGenerator;
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

            final String modelName = sourceFile.getType();

            final Reader in = new InputStreamReader(sourceFile.getSourceFile());
            final CSVParser records = CSVFormat.EXCEL.withHeader().parse(in);
            final Set<String> headers = records.getHeaderMap().keySet();

            headers.remove("");
            final AbstractModel model = new Model(modelName, parseRuleSet.org);
            List<CSVRecord> recordsList = records.getRecords();
            int rowCount = (int)recordsList.size();
            
            for (final String originalColName : headers) {

                final String newColName = parseRuleSet.typeRenamer.rename(originalColName);
                final String[] sampleData = new String[rowCount];
                boolean canBeNull = false;
                int index = 0;
                for (final CSVRecord record : recordsList) {
                    sampleData[index] = record.get(originalColName);
                    if (sampleData[index] == null || StringUtil.isNullWhiteSpace(sampleData[index])) {
                        canBeNull = true;
                    }
                    index++;
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

            final String genParser = csvParserGenerator.createParser(
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
