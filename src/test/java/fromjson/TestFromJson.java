package fromjson;

import com.base.application.ApplicationBuilder;
import com.base.application.ApplicationDescription;
import com.base.dl.methodgenerators.FromRs;
import com.base.dl.methodgenerators.GetByGuid;
import com.base.dl.methodgenerators.InsertObject;
import com.base.dl.methodgenerators.InsertRaw;
import com.base.gen.JSAPIGen;
import com.base.gen.SchemaGen;
import com.base.model.AbstractModel;
import com.base.model.methodgenerators.ConstructorGenerator;
import com.base.model.methodgenerators.FromThirdPartyGenerator;
import com.base.model.methodgenerators.ToStringGenerator;
import com.base.workflow.Helpers;
import com.kamserverutils.common.util.FileUtil;
import fullsuite.FullSuite;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;

public class TestFromJson {

    @Test
    public void fromJson() throws IOException, InterruptedException {

        final String exportDir = "/tmp/capfriendly";

        com.base.util.FileUtil.deleteDir(new File(exportDir));

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
