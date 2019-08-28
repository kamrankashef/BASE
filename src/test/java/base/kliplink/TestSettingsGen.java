package base.kliplink;

import base.application.ApplicationBuilder;
import base.dl.methodgenerators.FromRs;
import base.dl.methodgenerators.GetByGuid;
import base.dl.methodgenerators.HardDeleteByGuid;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.ForiegnKeyField;
import base.model.Model;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.model.methodgenerators.ToStringGenerator;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import base.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

// TODO This does not perform any assertions - compare generated assets to expected results
public class TestSettingsGen {

    private static final String ORG = "com.kliplink";
    private static final String EXPORT_DIR = "/tmp/kliplink_gen";

    @Test
    public void testSettingsGen() throws IOException {
        FileUtil.deleteDir(new File(EXPORT_DIR));
        final Map<String, AbstractModel> modelMap = new HashMap<>();

        final AbstractModel org = new Model("Org", ORG);

        org.addPrimitiveField(new PrimitiveField("Site", PrimitiveType.TINY_TEXT, false));
        org.addPrimitiveField(new PrimitiveField("FirstName", PrimitiveType.TINY_TEXT, false));
        org.addPrimitiveField(new PrimitiveField("LastName", PrimitiveType.TINY_TEXT, false));
        org.addPrimitiveField(new PrimitiveField("Email", PrimitiveType.TINY_TEXT, false));

        final AbstractModel settings = new Model("Setting", ORG);
        settings.addPrimitiveField(new PrimitiveField("SettingName", PrimitiveType.TINY_TEXT, false));
        settings.addPrimitiveField(new PrimitiveField("DataValue", PrimitiveType.TINY_TEXT, false));
        settings.addPrimitiveField(new PrimitiveField("DataType", PrimitiveType.TINY_TEXT, false));
        settings.addFKField(new ForiegnKeyField(Collections.EMPTY_SET, org, true));

        final AbstractModel creds = new Model("Cred", ORG);
        creds.addPrimitiveField(new PrimitiveField("AccessKey", PrimitiveType.TINY_TEXT, false));
        creds.addPrimitiveField(new PrimitiveField("SecretKey", PrimitiveType.TINY_TEXT, false));
        creds.addPrimitiveField(new PrimitiveField("type", PrimitiveType.TINY_TEXT, false));
        creds.addFKField(new ForiegnKeyField(Collections.EMPTY_SET, org, true));

        modelMap.put(org.getJavaClassName(), org);
        modelMap.put(settings.getJavaClassName(), settings);
        modelMap.put(creds.getJavaClassName(), creds);

        makeStandardSet(modelMap);
    }

    private static void makeStandardSet(
            final Map<String, AbstractModel> modelMap) throws IOException {

        final Set<ModelGen.ModelMethodGenerator> mergedModelMethods = new HashSet<>();
        mergedModelMethods.add(new DerivedModelConstructorGenerator());
        mergedModelMethods.add(new ConstructorGenerator());
        mergedModelMethods.add(new ToStringGenerator());

        final Set<DLGen.DLMethodGenerator> mergedDLMethods = new LinkedHashSet<>();

        mergedDLMethods.add(new InsertObject());
        mergedDLMethods.add(new FromRs());
        mergedDLMethods.add(new GetByGuid());
        mergedDLMethods.add(new HardDeleteByGuid()); // Need to change to delete by special keys
        mergedDLMethods.add(new InsertRaw());

        final Map<String, String> additionalJavaFiles = Collections.EMPTY_MAP;

        final String mainsBuildXML = "";

        ApplicationBuilder.buildElementParserAndLayerModels(ORG,
                modelMap,
                additionalJavaFiles,
                ModelAugmenterI.EMPTY_AUGMENTER,
                ModelTransformerI.EMPTY_TRANSFORMER,
                Collections.EMPTY_SET,
                mergedModelMethods,
                mergedDLMethods,
                mainsBuildXML,
                EXPORT_DIR);
    }
}

