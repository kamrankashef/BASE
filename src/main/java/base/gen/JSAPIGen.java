package base.gen;

import base.model.AbstractField;
import base.model.AbstractModel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JSAPIGen {

    public static String buildJSAPI(final Collection<AbstractModel> models) {
        final SourceBuilder bldr = new SourceBuilder();
        bldr.append("var API = {\n")
                .append(1, "API_ROOT: \"/api\",\n")
                .append(1, "DATA_FORMAT: 'json',\n")
                .append(1, "generic_post: function (end_point, params, callback) {\n")
                .append(2, "params['extra_data'] = {\n")
                .append(3, "'cache_buster': Math.floor(Math.random() * 999999999)\n")
                .append(2, "};\n")
                .append("\n")
                .append(2, "$.post(API.API_ROOT + '/' + end_point,\n")
                .append(3, "params,\n")
                .append(3, "callback,\n")
                .append(3, "API.DATA_FORMAT);\n")
                .append(1, "}\n")
                .append(1, ",get_captcha: function (image_tag) {\n")
                .append(2, "var rand_int = Math.floor(Math.random() * 1000000);\n")
                .append(2, "image_tag.attr('src', API.API_ROOT + '/get_captcha?blah=' + rand_int);\n")
                .append(2, "image_tag.focus();\n")
                .append(1, "}");
        for (final AbstractModel model : models) {
            for (final String func : JSAPIGen.genJSFunctions(model)) {
                bldr.append("\n").append(1, ",)").append(func);
            }
        }
        bldr.append("\n};\n");
        return bldr.toString();
    }

    private static List<String> genJSFunctions(final AbstractModel m) {

        final List<String> functions = new LinkedList<>();

        {
            // Get
            final SourceBuilder bldr = new SourceBuilder();

            final String endPoint = "get_" + m.toDBName();
            bldr.append(endPoint).append(": function (").append(m.getGuidField().toDBName()).append(", callback) {\n");
            bldr.append(2, "var params = {\n")
                    .append(3, m.getGuidField().toDBName()).append(": ").append(m.getGuidField().toDBName()).append("\n");
            bldr.append(2, "};\n");
            bldr.append(2, "API.generic_post('").append(endPoint).append("', params, callback);\n")
                    .append(1, "}");
            functions.add(bldr.toString());
        }

        {
            // Delete
            final SourceBuilder bldr = new SourceBuilder();

            final String endPoint = "delete_" + m.toDBName();
            bldr.append("").append(endPoint).append(": function (").append(m.getGuidField().toDBName()).append(", callback) {\n");
            bldr.append(2, "var params = {\n")
                    .append(3, m.getGuidField().toDBName()).append(": ").append(m.getGuidField().toDBName()).append("\n");
            bldr.append(2, "};\n");
            bldr.append(2, "API.generic_post('").append(endPoint).append("', params, callback);\n")
                    .append(1, "}");
            functions.add(bldr.toString());
        }

        {
            // Create
            final SourceBuilder bldr = new SourceBuilder();
            final String endPoint = "create_" + m.toDBName();
            bldr.append("").append(endPoint).append(": function (");
            boolean first = true;
            String params = "";
            for (final AbstractField field : m.allOriginalFields()) {
                if (!field.userGenerated || !field.shareWithClient) {
                    continue;
                }
                bldr.append(first ? "" : ", ").append(field.toDBName());
                params += (first ? "\n" : ",\n") + SourceBuilder.getIndents(4);
                params += field.toDBName() + ": " + field.toDBName();
                first = false;
            }
            bldr.append(", callback) {\n");
            bldr.append(3, "var params = {").append(params).append("\n").append("};\n\n");
            bldr.append(2, "API.generic_post('").append(endPoint).append("', params, callback);\n")
                    .append(1, "}");
            functions.add(bldr.toString());
        }
        {
            // Update
            final SourceBuilder bldr = new SourceBuilder();
            final String endPoint = "update_" + m.toDBName();
            bldr.append("").append(endPoint).append(": function (");
            boolean first = true;
            String params = "";
            for (final AbstractField field : m.allOriginalFields()) {
                if (!field.shareWithClient) {
                    continue;
                }
                bldr.append(first ? "" : ", ").append(field.toDBName());
                params += (first ? "\n" : ",\n") + SourceBuilder.getIndents(4);;
                params += field.toDBName() + ": " + field.toDBName();
                first = false;
            }
            bldr.append(", callback) {\n");
            bldr.append(3, "var params = {").append(params).append("\n").indent(3).append("};\n\n");
            bldr.append(2, "API.generic_post('").append(endPoint).append("', params, callback);\n")
                    .append(1, "}");
            functions.add(bldr.toString());
        }

        return functions;
    }

}
