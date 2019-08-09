package base.model;

import java.util.stream.Collectors;

public class ModelUtil {

    public static PrimitiveField fieldLookup(final AbstractModel model, final String javaName) {
        for (final PrimitiveField field : model.getPrimitiveFieldsWithLinked()) {
            if (field.toJavaVariableName().equals(javaName)) {
                return field;
            }
        }

        final String allFields = model.getPrimitiveFieldsWithLinked()
                .stream()
                .map(field -> field.toJavaVariableName())
                .collect(Collectors.joining(", "));

        System.err.println("Could not find field '" + javaName + "' in model '" + model.getJavaClassName() + "'"
                + "\nAvailable fields: " + allFields);
        return null;
    }
}
