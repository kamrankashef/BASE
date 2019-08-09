package base.parsergen;

import base.gen.ModelGen;
import base.model.AbstractModel;
import base.parsergen.rules.ParseRuleSet;
import base.parsergen.rules.SourceFiles.SourceFile;
import kamserverutils.common.util.FileUtil;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractBuilderFromSource {

    protected final Map<String, String> genedParsers = new HashMap<>();
    protected final Collection<String> mainsBuildXML = new TreeSet<>();
    public final ParseRuleSet parseRuleSet;
    public final AtomicBoolean inited = new AtomicBoolean(false);
    
    static final public AbstractBuilderFromSource run(
            final AbstractBuilderFromSource builder)
            throws IOException {
        if(builder.inited.getAndSet(true)) {
            throw new IllegalStateException("Cannot run init twice");
        }
        builder.process();
        builder.applyAugmentAndMethods();
        return builder;
    }

    AbstractBuilderFromSource(
            final ParseRuleSet parseRuleSet
    ) throws IOException {
        this.parseRuleSet = parseRuleSet;
    }

    public Map<String, String> getGenedParsers() {
        return new HashMap(genedParsers);
    }

    public Collection<String> getAntEntries() {
        return new TreeSet(mainsBuildXML);
    }

    abstract protected void process() throws IOException;

    protected final String sourceFileToString(final String rootDir,
            final SourceFile sourceFile) throws IOException {
        final String fileName = rootDir + "/" + sourceFile.fileName;
        System.out.println("Processing file: " + fileName);
        System.out.println("file name is " + fileName);
        return FileUtil.fileToString(fileName);
    }

    protected static String createAntTarget(final String target,
            final String className,
            final String parserName,
            final String description,
            final String depends,
            final Collection<String> args,
            final Collection<String> jvmArgs) {

        String argsStr = "";
        for (final String arg : args) {
            argsStr += "      <arg value=\"" + arg + "\"/>\n";
        }
        String jvmArgsStr = "";

        for (final String arg : jvmArgs) {
            jvmArgsStr += "      <jvmarg value=\"-D" + arg + "\"/>\n";
        }

        return ""
                + "  <target name=\"" + target + "\" depends=\"" + depends + "\" description=\"" + description + "\">\n"
                + "    <java classname=\"main." + parserName + "\"\n"
                + "        fork=\"true\"\n"
                + "        classpathref=\"test-classpath\">\n"
                + argsStr
                + jvmArgsStr
                + "    </java>\n"
                + "  </target>\n\n";
    }

    final protected void applyAugmentAndMethods() {
        for (final AbstractModel model : getElemModels()) {
            model.applyAugmented(parseRuleSet.modelAugmenter);

            for (final ModelGen.ModelMethodGenerator modelMethodGen : parseRuleSet.elemModelMethods) {
                model.addModelMethodGenerator(modelMethodGen);
            }
        }
    }

    public abstract Collection<AbstractModel> getElemModels();

    public abstract Map<String, AbstractModel> getElemModelMap();
    
}
