package com.base.application.scratch;

import com.base.application.ApplicationBuilder;
import com.base.application.ApplicationDescription;
import com.base.dl.methodgenerators.DeleteByGuid;
import com.base.dl.methodgenerators.FromRs;
import com.base.dl.methodgenerators.GetByGuid;
import com.base.dl.methodgenerators.InsertObject;
import com.base.dl.methodgenerators.InsertRaw;
import com.base.dl.methodgenerators.UpdateObject;
import com.base.dl.methodgenerators.UpdateRaw;
import com.base.gen.JSAPIGen;
import com.base.gen.SchemaGen;
import com.base.model.AbstractModel;
import com.base.model.methodgenerators.ConstructorGenerator;
import com.base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import com.base.model.methodgenerators.FromThirdPartyGenerator;
import com.base.model.methodgenerators.IsShell;
import com.base.model.methodgenerators.ToMapGenerator;
import com.base.model.methodgenerators.ToStringGenerator;
import com.kamserverutils.common.util.FileUtil;
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
