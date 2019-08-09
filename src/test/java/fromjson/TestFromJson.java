package fromjson;

import base.application.ApplicationBuilder;
import base.application.ApplicationDescription;
import base.dl.methodgenerators.FromRs;
import base.dl.methodgenerators.GetByGuid;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.gen.JSAPIGen;
import base.gen.SchemaGen;
import base.model.AbstractModel;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.FromThirdPartyGenerator;
import base.model.methodgenerators.ToStringGenerator;
import base.workflow.Helpers;
import kamserverutils.common.util.FileUtil;
import fullsuite.FullSuite;
import java.io.File;
import java.io.IOException;
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
        for (final AbstractModel model : app.models) {

            model.addDLMethodGenerator(new FromRs());
            model.addDLMethodGenerator(new GetByGuid());
            model.addDLMethodGenerator(new InsertObject());
            model.addDLMethodGenerator(new InsertRaw());

            model.addModelMethodGenerator(new ConstructorGenerator());
            model.addModelMethodGenerator(new FromThirdPartyGenerator());
            model.addModelMethodGenerator(new ToStringGenerator());
        }

        ApplicationBuilder appBldr = new ApplicationBuilder(app);

        final Map<String, String> genFiles = appBldr.buildClasses();

//        final String bcHome = System.getenv("BUILD_COMMONS_HOME");
//        runCommand(bcHome + "/bin/scaffolder.rb", "web", exportHome, "orgname,modfoo,1.0.2", "javax.mail,mail,1.4.5:com.google.guava,guava,18.0");
//
        final SchemaGen schemaGen = new SchemaGen(SchemaGen.DBVendor.MYSQL);
        genFiles.put("web/WEB-INF/schema.sql", schemaGen.buildSchema(app.name.toLowerCase(), app.models));
        genFiles.put("web/js/api.js", JSAPIGen.buildJSAPI(app.models));
        ApplicationBuilder.fileExporter(exportDir, genFiles);
        FullSuite.runDiff("expected_out/cap_friendly", exportDir);
    }

}
