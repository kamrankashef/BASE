package base.model;


import base.gen.ModelGen;
import base.parsergen.rules.ModelAugmenterI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/**
 * TODO Get rid of this test and create targed tests for applyAugmented and ModelGen.ModelMethodGenerator
 * @deprecated
 */
public class TestSpecialConstructor {

    @Test
    public void testSpecialConstructor() {
        final Model m1 = new Model("model1", "org.one");
        final PrimitiveField field1 = new PrimitiveField("Field1", PrimitiveType.TEXT);
        m1.addPrimitiveField(field1);
        final PrimitiveField field1Aug = new PrimitiveField("Field1Aug", PrimitiveType.INT);

        m1.applyAugmented(new ModelAugmenterI() {
            @Override
            public Map<PrimitiveField, String> getAugmentedFields(AbstractModel model) {
                final Map<PrimitiveField, String> map = new HashMap<>();
                map.put(field1Aug, "null");
                return map;
            }
        });

        final Model m2 = new Model("model2", "org.two");
        final PrimitiveField fieldA = new PrimitiveField("FieldA", PrimitiveType.TEXT);
        m2.addPrimitiveField(fieldA);

        final Model[] models = {m1, m2};
        final String[] names = {"First", "Second"};
        final AbstractModel newModel = new Model(models, names, "new.org", "SomethingNew");

        // What would really be needed is a
        // merged model type that keeps around both the
        // elem models and additional fields
        final ModelGen.ModelMethodGenerator specialConstructor = new ModelGen.ModelMethodGenerator() {
            @Override
            public String genMethod(AbstractModel model, final String parentPackage) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Set<String> requiredImports() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
//        newModel.getModelGenerators().add()

    }
}

