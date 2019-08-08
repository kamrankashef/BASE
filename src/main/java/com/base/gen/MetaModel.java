package com.base.gen;

import com.base.application.ApplicationBuilder;
import com.base.model.ForiegnKeyField;
import com.base.model.AbstractModel;
import com.base.model.Model;
import com.base.model.PrimitiveField;
import com.base.model.PrimitiveType;
import com.base.util.CaseConversion;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class MetaModel {

    public static AbstractModel genAppDescription(
            final String modelName,
            final String org,
            final Map<String, Object> sampleObject,
            final Map<String, PrimitiveType> hints,
            final Collection<String> additionalFS) {

        final Map<String, String> thirdPartyMapping = new HashMap<>();

        final Map<String, Object> map = new HashMap<>();

        final List<PrimitiveField> fields = new LinkedList<>();

        map.put("fields", fields);

        for (final Map.Entry<String, Object> entry : sampleObject.entrySet()) {
            final String nameFromSource = entry.getKey();
            final String name = CaseConversion.bustedUpperCamelUnderScore(nameFromSource);
            final Object value = entry.getValue();
            final PrimitiveType type;
            System.out.println("name: " + nameFromSource + ", " + value.getClass());
            if (hints.containsKey(nameFromSource)) {
                type = hints.get(nameFromSource);
            } else if (value instanceof Boolean) {
                type = PrimitiveType.BOOLEAN;
            } else if (value instanceof Long) {
                type = PrimitiveType.LONG;
            } else if (value instanceof Integer) {
                type = PrimitiveType.INT;
            } else if (value instanceof Double) {
                type = PrimitiveType.DOUBLE;
            } else {
                type = PrimitiveType.TEXT;
            }
            fields.add(new PrimitiveField(name, Collections.EMPTY_SET, type));
            thirdPartyMapping.put(name, nameFromSource);
        }

        final List<ForiegnKeyField> fkFields = new LinkedList<>();
        for (final String fkFieldName : additionalFS) {
            final AbstractModel fkModel = new Model(fkFieldName, Collections.EMPTY_LIST, Collections.EMPTY_LIST, org);
            final ForiegnKeyField fkField = new ForiegnKeyField(Collections.EMPTY_SET, fkModel, false);
            fkFields.add(fkField);
        }

        final AbstractModel model = new Model(modelName, fkFields, fields, org);
        model.setThirdPartyMapping(thirdPartyMapping);
        return model;
    }

    public final static void main(final String... args) throws IOException {
//        final String sampleInput = "{\"PK_Event\":\"60809063\",\"PK_Device\":\"45007077\",\"Timestamp\":\"1436872189\",\"LocalDate\":\"2015-07-14 06:56:21\",\"Read\":\"0\",\"EventType\":\"7\",\"IP\":\"73.191.141.163\",\"Locked\":\"0\",\"NewValue\":\"nm_paid_tunnels\",\"Description\":\"Remote access tunnel down\",\"Key\":\"3344025978\",\"Thumbnail\":\"0\",\"Severity\":0}";

        final String sampleInput = ""
                + "{\"DeviceID\":\"3\",\"Description\":\"floor sensor\",\"Room\":\"office\",\"Type\":\"urn:schemas-micasaverde-com:device:DoorSensor:1\",\"Category\":\"4\",\"SubCategory\":\"1\",\"ParentCategory\":\"19\",\"Configured\":\"1\",\"Manufacturer\":\"Aeon\",\"Model\":\"DSB29\",\"AltID\":\"2\",\"ManufInfo\":\"134,2,29\",\"VersInfo\":\"3,3,40,1,16\",\"Neighbors\":\"\",\"Capab\":\"83,156,0,4,32,1,R,B,RS,|48:1,112,113:1,114,128,132:2,133,134,\",\"PollOk\":\"3\",\"PK_KitDevice\":\"2202\"}";
        final Map<String, Object> asMap = new Gson().fromJson(sampleInput, Map.class);
        System.out.println("AS MAP: ");
        System.out.println(asMap);
        final Map<String, PrimitiveType> hints = new HashMap<>();

        // TODO Enable auto-dected on fields with time and date in them
//        hints.put("Timestamp", PrimitiveType.DATE);
//        hints.put("LocalDate", PrimitiveType.DATE);
        final Collection<String> fkNames = new LinkedList<>();
        fkNames.add("Gateway");
        final AbstractModel model = MetaModel.genAppDescription("Device", "com.nonnatech", asMap, hints, fkNames);
//        final Model gateway = new Model("Gateway")
//        model.fkFields.add(new ForiegnKeyField(new LinkedList<>(), model, false));
        final Map<String, String> map = new HashMap<>();
        map.put("Device.java", ModelGen.toModelClass(model));
        map.put("DeviceDL.java", DLGen.toDLClass(model));
        final SchemaGen schemaGen = new SchemaGen(SchemaGen.DBVendor.MYSQL);
        map.put("schema.sql", schemaGen.buildSchema("dummyschema", Collections.singleton(model)));
        ApplicationBuilder.fileExporter("/tmp", map);

//        System.out.println(DLGen.toDLClass(model));
    }
}
