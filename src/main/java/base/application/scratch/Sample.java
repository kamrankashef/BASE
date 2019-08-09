package base.application.scratch;

import base.application.ApplicationBuilder;
import base.application.ApplicationDescription;
import base.dl.methodgenerators.DeleteByGuid;
import base.dl.methodgenerators.FromRs;
import base.dl.methodgenerators.GetByGuid;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.dl.methodgenerators.UpdateObject;
import base.dl.methodgenerators.UpdateRaw;
import base.gen.JSAPIGen;
import base.gen.SchemaGen;
import base.model.AbstractModel;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import base.model.methodgenerators.FromThirdPartyGenerator;
import base.model.methodgenerators.IsShell;
import base.model.methodgenerators.ToMapGenerator;
import base.model.methodgenerators.ToStringGenerator;
import kamserverutils.common.util.FileUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Sample {

    public static void main(final String... args) throws IOException {

        if (args.length == 0) {
            System.err.println("Missing path to application.json");
            System.exit(1);
        }

        final String pathToJson = args[0];
        final String asJson = FileUtil.fileToString(pathToJson);
        final ApplicationDescription app = ApplicationDescription.fromJson(asJson);

        for (final AbstractModel model : app.models) {

            model.addDLMethodGenerator(new FromRs());
            model.addDLMethodGenerator(new GetByGuid());
            model.addDLMethodGenerator(new InsertObject());
            model.addDLMethodGenerator(new InsertRaw());
            model.addDLMethodGenerator(new UpdateRaw());
            model.addDLMethodGenerator(new UpdateObject());
            model.addDLMethodGenerator(new DeleteByGuid());

            model.addModelMethodGenerator(new ConstructorGenerator());
            model.addModelMethodGenerator(new AttributeBasedFromElemMethodGenerator());
            model.addModelMethodGenerator(new FromThirdPartyGenerator());
            model.addModelMethodGenerator(new ToMapGenerator());
            model.addModelMethodGenerator(new ToStringGenerator());
            model.addModelMethodGenerator(new IsShell());
        }

        ApplicationBuilder appBldr = new ApplicationBuilder(app);

        final Map<String, String> genFiles = appBldr.buildClasses();
        final String exportHome = "/tmp/" + app.name;

        final String bcHome = System.getenv("BUILD_COMMONS_HOME");
        runCommand(bcHome + "/bin/scaffolder.rb", "web", exportHome, "orgname,modfoo,1.0.2", "javax.mail,mail,1.4.5:com.google.guava,guava,18.0");

        final SchemaGen schemaGen = new SchemaGen(SchemaGen.DBVendor.MYSQL);
        genFiles.put("web/WEB-INF/schema.sql", schemaGen.buildSchema(app.name.toLowerCase(), app.models));
        genFiles.put("web/js/api.js", JSAPIGen.buildJSAPI(app.models));
        ApplicationBuilder.fileExporter(exportHome, genFiles);
//        "/Users/anon/mnt_points/personal_dev/src/build-commons/bin/scaffolder.rb web /tmp/foo orgname,modfoo,1.0.2 javax.mail,mail,1.4.5:com.google.guava,guava,18.0"

    }

    public static void runCommand(final String script, final String... args) throws IOException {
        String[] command = new String[args.length + 1];
        command[0] = script;

        for (int i = 0; i < args.length; i++) {
            command[i + 1] = args[i];
        }

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            System.out.println("Script output: " + s);
        }
    }
}
