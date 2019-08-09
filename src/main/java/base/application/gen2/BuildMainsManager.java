package base.application.gen2;

import kamserverutils.common.util.FileUtil;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class BuildMainsManager {

    private static final String TARGET = "target";
    private static final String PROJECT = "project";
    final Document doc;//new JsoupJsoup.parse("", "", Parser.xmlParser());

    public BuildMainsManager() {
        doc = new Document("");
        doc.outputSettings().prettyPrint(true);

        final Element projectMains = doc.prependElement(PROJECT);
        projectMains.attr("name", "build_names");
        projectMains.attr("basedir", ".");
    }

    public BuildMainsManager(final String path) throws IOException {
        final String xml = FileUtil.fileToString(path);
        doc = Jsoup.parse(xml, "", Parser.xmlParser());
        doc.outputSettings().prettyPrint(true);
    }

    @Override
    public String toString() {
        return doc.outerHtml();
    }

    public static void main(String[] args) {
        final BuildMainsManager buildMainsManager = new BuildMainsManager();
        buildMainsManager.addReplaceTarget("Something", "Sports data");
        buildMainsManager.addReplaceTarget("Something", "Sports data");
        System.out.println(buildMainsManager);
    }

    public void removeTarget(final String parserName) {

    }

    public Element getTarget(final String parserName) {
        return doc.select(TARGET + "[name=" + parserName + "]").first();
    }

    public void addReplaceTarget(final String parserName, final String description) {

        final Element project = doc.select(PROJECT).first();
        
        final Element target = getTarget(parserName) == null ? project.prependElement(TARGET) 
                :getTarget(parserName);
        
        target.text("");
       
        target.attr("name", parserName);
        target.attr("depends", "check-jdbc-url");
        target.attr("description", description);
        final Element java = target.prependElement("java");
        java.attr("classname", "main." + parserName);
        java.attr("fork", "true");
        java.attr("classpathref", "exec-classpath");
        final Element arg = java.prependElement("arg");
        arg.attr("value", "${input.file}");
        final Element jvmArg = java.prependElement("jvmarg");
        jvmArg.attr("value", "-DJDBC_CONNECTION_STRING=${jdbc.url}");
    }

}
