package base.model;

import java.util.HashMap;
import java.util.Map;

import base.parsergen.rules.ModelAugmenterI;
import org.junit.Assert;
import org.junit.Test;

public class TestAdjoinedModel {

    @Test
    public void testBasicModel() {
        final Model m1 = new Model("model1", "org");
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

        final Model m2 = new Model("model2", "org");
        final PrimitiveField fieldA = new PrimitiveField("FieldA", PrimitiveType.TEXT);
        m2.addPrimitiveField(fieldA);

        final Model[] models = {m1, m2};
        final String[] names = {"First", "Second"};
        final AbstractModel newModel = new Model(models, names, "new.org", "SomethingNew");

        Assert.assertEquals(4, newModel.allOriginalFields().size());

        Assert.assertEquals(3, newModel.allNonGuidOriginalFields().size());

        // TODO Circle back to these once additional regression tests are built out
        // TODO Why is the field "FirstField1" not "First_Field1" to indicated it is the result of a composition?
        // TODO More importantly, why is the third_party_identifier "FirstField1", not "Field1"?
        Assert.assertTrue(newModel.allNonGuidOriginalFields().contains(new PrimitiveField("FirstField1", PrimitiveType.TEXT)));
        Assert.assertTrue(newModel.allNonGuidOriginalFields().contains(new PrimitiveField("FirstField1Aug", PrimitiveType.INT)));
        Assert.assertTrue(newModel.allNonGuidOriginalFields().contains(new PrimitiveField("SecondFieldA", PrimitiveType.TEXT)));

        Assert.assertTrue(newModel.getAugmentedFields().isEmpty());
        Assert.assertEquals(3, newModel.getPrimitiveFieldsWithLinked().size());
        Assert.assertTrue(newModel.getFkFields().isEmpty());
    }
}

