package base.parsergen;

import base.parsergen.rules.SourceFiles.SourceFile;
import base.model.AbstractModel;
import base.model.Model;
import base.parsergen.rules.ParseRuleSet;
import base.parsergen.xml.XMLToModels;
import base.parsergen.xml.XMLParserGenerator;
import base.parsergen.xml.ModelHierarchy;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import kamserverutils.common.util.IOUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;


final public class XMLBuilder extends AbstractBuilderFromSource {

    private final XMLToModels scanner;

    public XMLBuilder(final ParseRuleSet parseRuleSet) // Source files used and disregarded
            throws IOException {
        super(parseRuleSet);
        this.scanner = new XMLToModels(parseRuleSet);
    }

    @Override
    protected void process() throws IOException {
        final StringBuilder buildMains = new StringBuilder();
        buildMains.append("<project name=\"build_mains\" basedir=\".\" >\n\n");

        for (final SourceFile sourceFile : parseRuleSet.sourceFiles.sourceFiles) {

            final ModelHierarchy xsdNesting;

            { // File to doc
                // TODO Use IOUtils
                final String xml = IOUtil.inputStreamToString(sourceFile.getInputStream());
                final Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
                xsdNesting = new ModelHierarchy(new Model("XML", Collections.EMPTY_LIST, Collections.EMPTY_LIST, parseRuleSet.org ), "FOO");
                scanner.createTypeDefs(xsdNesting, doc);
                System.out.println(XMLParserGenerator.prettyPrint(xsdNesting, 0));
            }

            // XML Parser
            final String parserName = sourceFile.getType() + "Parser";
            final String target = parserName;

            final String genParser = XMLParserGenerator.createNoOpParser(xsdNesting, parserName, scanner.models.values());
            genedParsers.put("src/main/java/main/" + parserName + ".java", genParser);

            buildMains.append(AbstractBuilderFromSource.createAntTarget(target,
                    "main." + parserName,
                    parserName,
                    sourceFile.getType() + " parser",
                    "compile,check-jdbc-url",
                    Collections.singletonList("${xml.file}"),
                    Collections.singletonList("JDBC_CONNECTION_STRING=${jdbc.connection.string}")));

        }

        buildMains.append("</project>");

        mainsBuildXML.add(buildMains.toString());
    }

    @Override
    public Collection<AbstractModel> getElemModels() {
        return new LinkedList<>(scanner.models.values());
    }

    @Override
    public Map<String, AbstractModel> getElemModelMap() {
        return new HashMap(scanner.models);
    }
}
