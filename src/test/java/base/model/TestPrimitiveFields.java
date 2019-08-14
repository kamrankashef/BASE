package base.model;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestPrimitiveFields {

    @Test
    public void testFieldEquality() {

        final PrimitiveField field1 = new PrimitiveField("Field1", PrimitiveType.TIMESTAMP);
        final PrimitiveField field2 = new PrimitiveField("Field1", PrimitiveType.TIMESTAMP);
        final PrimitiveField field3 = new PrimitiveField("Field3", PrimitiveType.TIMESTAMP);
        final PrimitiveField field4 = new PrimitiveField("Field1", PrimitiveType.INT);
        Assert.assertEquals(field1, field2);
        Assert.assertNotEquals(field1, field3);
        Assert.assertNotEquals(field1, field4);
        Assert.assertNotEquals(field3, field4);
    }

    @Test
    public void testEqaulityOfSetsContainingPrimitiveFields() {
        final PrimitiveField field1 = new PrimitiveField("Field1", PrimitiveType.TIMESTAMP);
        final PrimitiveField field1Aug = new PrimitiveField("Field1Aug", PrimitiveType.TIMESTAMP);

        final Set<PrimitiveField> hashSet = new HashSet<>();
        hashSet.add(field1);
        hashSet.add(field1Aug);
        hashSet.add(field1);
        hashSet.add(field1Aug);
        assertEquals(2, hashSet.size());

        final Set<PrimitiveField> treeSet = new TreeSet<>();
        treeSet.add(field1);
        treeSet.add(field1Aug);
        treeSet.add(field1);
        treeSet.add(field1Aug);
        assertEquals(2, treeSet.size());

        assertEquals(field1, treeSet.iterator().next());

    }
}
