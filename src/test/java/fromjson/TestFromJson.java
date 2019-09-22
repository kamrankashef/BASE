package fromjson;

import base.application.ApplicationBuilder;
import base.application.ApplicationDescription;
import base.dl.methodgenerators.FromRs;
import base.dl.methodgenerators.GetByGuid;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.gen.DLGen;
import base.gen.JSAPIGen;
import base.gen.ModelGen;
import base.gen.SchemaGen;
import base.model.AbstractModel;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.FromThirdPartyGenerator;
import base.model.methodgenerators.ToStringGenerator;
import base.model.sql.MySql;
import base.workflow.Helpers;
import kamserverutils.common.util.FileUtil;
import fullsuite.FullSuite;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import org.junit.Test;

public class TestFromJson {

    @Test
    public void fromJson() throws IOException, InterruptedException {

        final String exportDir = "/tmp/capfriendly";

        base.util.FileUtil.deleteDir(new File(exportDir));

        final String pathToJson = Helpers.classToTestPath(TestFromJson.class, "fromjson/application.json");
        final String asJson = FileUtil.fileToString(pathToJson);

        final ApplicationDescription app = ApplicationDescription.fromJson(asJson);

        final Collection<ModelGen.ModelMethodGenerator> derivedModelMethods = new LinkedList<>();
        derivedModelMethods.add(new ConstructorGenerator());
        derivedModelMethods.add(new FromThirdPartyGenerator());
        derivedModelMethods.add(new ToStringGenerator());

        final Collection<DLGen.DLMethodGenerator> derivedModelDLMethods = new LinkedList<>();
        derivedModelDLMethods.add(new FromRs());
        derivedModelDLMethods.add(new GetByGuid());
        derivedModelDLMethods.add(new InsertObject());
        derivedModelDLMethods.add(new InsertRaw());


        final Map<String, String> genFiles = ApplicationBuilder.buildClasses(Collections.EMPTY_SET, Collections.EMPTY_SET,
                app.models, derivedModelMethods, derivedModelDLMethods,true);

//        final String bcHome = System.getenv("BUILD_COMMONS_HOME");
//        runCommand(bcHome + "/bin/scaffolder.rb", "web", exportHome, "orgname,modfoo,1.0.2", "javax.mail,mail,1.4.5:com.google.guava,guava,18.0");
//
        final SchemaGen schemaGen = new SchemaGen(new MySql());
        genFiles.put("web/WEB-INF/schema.sql", schemaGen.buildSchema(app.name.toLowerCase(), app.models));
        genFiles.put("web/js/api.js", JSAPIGen.buildJSAPI(app.models));
        ApplicationBuilder.fileExporter(exportDir, genFiles);
        FullSuite.runDiff("expected_out/cap_friendly", exportDir);
    }

}
