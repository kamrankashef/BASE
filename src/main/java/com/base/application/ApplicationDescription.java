package com.base.application;

import com.base.model.AbstractField;
import com.base.model.Constraint;
import com.base.model.ForiegnKeyField;
import com.base.model.AbstractModel;
import com.base.model.Model;
import com.base.model.PrimitiveField;
import com.base.model.PrimitiveType;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//CREATE TABLE user (
//  user_guid CHAR(10) PRIMARY KEY
// ,role ENUM('superuser', 'admin', 'caregiver')
// ,org_guid CHAR(10) NOT NULL
// ,FOREIGN KEY (org_guid) REFERENCES org(org_guid) ON DELETE CASCADE
// ,pass_hash VARCHAR(40)
// ,email VARCHAR(63) UNIQUE
// ,last_name VARCHAR(63) NOT NULL
// ,created_at TIMESTAMP DEFAULT 0
// ,modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
// ,deleted_at TIMESTAMP NULL DEFAULT NULL
//) ENGINE=InnoDB;
public class ApplicationDescription {

    public final String name;
    public final List<AbstractModel> models;
    public final String org;

    public ApplicationDescription(
            final String name,
            final String org,
            final List<AbstractModel> models
    ) {
        this.name = name;
        this.org = org;
        this.models = models;
    }

    // Currently only has basic support - no contraints or FKs
    public String toJson() {
        
        final Map<String, Object> asMap = new HashMap<>();
        asMap.put("name", this.name);
        asMap.put("org", this.org);
        final List<Map<String, Object>> localModels = new LinkedList<>();
        asMap.put("models", localModels);
        
        for(final AbstractModel model:this.models)  {
            final Map<String, Object> modelAsMap = new HashMap<>();
            localModels.add(modelAsMap);
            
            modelAsMap.put("name", model.getJavaClassName());
            final List<Map<String, Object>> localFields = new LinkedList<>();
            modelAsMap.put("fields", localFields);
            
            
            for(final PrimitiveField field:model.getPrimitiveFields()) {
                final Map<String, Object> fieldAsMap = new HashMap<>();
                localFields.add(fieldAsMap);
                fieldAsMap.put("type", field.getPrimitiveType().name());
                fieldAsMap.put("name", field.getJavaClassName());
            }
            
        }
        
        return new Gson().toJson(asMap);
    }
    public static ApplicationDescription fromJson(final String str) {
        final Map<String, Object> asMap = new Gson().fromJson(str, Map.class);
        System.out.println("Parsing description " + asMap + "\n");

        final String name = (String) asMap.get("name");
        final String org = (String) asMap.get("org");
        final List<Map<String, Object>> modelsJson = (List<Map<String, Object>>) asMap.get("models");
        final List<AbstractModel> models = new LinkedList<>();
        final Map<String, AbstractModel> modelMap = new HashMap<>();

        for (final Map<String, Object> modelJson : modelsJson) {
            final String modelName = (String) modelJson.get("name");
            final List<PrimitiveField> primitiveFields = new LinkedList<>();
            final List<ForiegnKeyField> fkFields = new LinkedList<>();

            final List<Map<String, Object>> fieldsJson
                    = (List<Map<String, Object>>) modelJson.get("fields");
            for (final Map<String, Object> fieldJson : fieldsJson) {
                final AbstractField field;
                final Set<Constraint> contstraints = new HashSet<>();
                if (null == fieldJson) {
                    System.err.println("Null on " + fieldsJson);
                }
                if ("fk".equalsIgnoreCase((String) fieldJson.get("type"))) {
                    final boolean cascadeOnDelete
                            = !fieldJson.containsKey("cascade_on_delete")
                            || "false".equalsIgnoreCase((String) fieldJson.get("cascade_on_delete"));
                    final ForiegnKeyField fkField = new ForiegnKeyField(contstraints, modelMap.get((String) fieldJson.get("fk_model")), cascadeOnDelete);
                    fkFields.add(fkField);
                    fkField.setNullable(false);
                    field = fkField;

                } else {
                    final PrimitiveType type = PrimitiveType.fromString(
                            (String) fieldJson.get("type"));
                    final PrimitiveField primitiveField = new PrimitiveField((String) fieldJson.get("name"), contstraints, type);
                    primitiveFields.add(primitiveField);
                    field = primitiveField;
                }
                if (Boolean.FALSE.equals(fieldJson.get("nullable"))) {
                    field.setNullable(false);
                }
                if ("false".equals(fieldJson.get("share_with_client"))) {
                    field.setDontShareWithClient();
                }

            }
            final AbstractModel model = new Model(modelName, fkFields, primitiveFields, org);
            models.add(model);
            modelMap.put(modelName, model);

        }

        return new ApplicationDescription(name, org, models);
    }
}
