package base.model;

import org.junit.Assert;
import org.junit.Test;

public class TestModel {

    @Test
    public void testBasicModel() {
        final Model m = new Model("newModel", "org");
        Assert.assertEquals("newModel", m.toJavaVariableName());
        Assert.assertEquals("NewModel", m.getJavaClassName());
        Assert.assertEquals("new_model", m.toDBName());
        Assert.assertEquals("NewModelDL", m.dlName());
        Assert.assertEquals("org", m.getOrg());
        Assert.assertEquals(1, m.allOriginalFields().size());
        Assert.assertTrue(m.allNonGuidOriginalFields().isEmpty());
        Assert.assertTrue(m.getAugmentedFields().isEmpty());
        Assert.assertTrue(m.getPrimitiveFields().isEmpty());
        Assert.assertTrue(m.getFkFields().isEmpty());
//        Assert.assertTrue(m.getUnoriginalFields().isEmpty());

        m.addPrimitiveField(new PrimitiveField("Field1", PrimitiveType.TIMESTAMP));
        Assert.assertEquals(2, m.allOriginalFields().size());
        Assert.assertEquals(1, m.allNonGuidOriginalFields().size());
        Assert.assertTrue(m.getAugmentedFields().isEmpty());
        Assert.assertEquals(1, m.getPrimitiveFields().size());
        Assert.assertTrue(m.getFkFields().isEmpty());
//        Assert.assertTrue(m.getUnoriginalFields().isEmpty());
    }
}
