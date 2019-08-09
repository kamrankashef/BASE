package base.gen;

import base.model.AbstractField;
import base.model.AbstractModel;
import base.model.CRUDAction;

public class APIGen {

    final private static String[] SERVICE_IMPORTS = {
        "com.kamserverutils.common.exec.ErrorType",
        "com.kamserverutils.common.exec.ExecutionResult",
        "com.kamserverutils.common.util.ServiceUtil",
        "com.kamserverutils.common.util.StringUtil",
        "java.util.Map",
        "java.util.logging.Logger",
        "javax.servlet.annotation.WebServlet",
        "javax.servlet.http.HttpServletResponse",
        "javax.servlet.http.HttpServletRequest",
        "java.sql.Connection"
    };

    public static String buildAPIClass(final AbstractModel m, final CRUDAction action) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append("package ").append(m.getServicePackage()).append(";\n\n");

        final String apiClassName = action.getAPIClassName(m);
        final String endPoint = action.getAPIServiceName(m);

        bldr.append("import ").append(m.getModelPackage()).append(".").append(m.getJavaClassName()).append(";\n");
        bldr.append("import ").append(m.getDlPackage()).append(".").append(m.dlName()).append(";\n");

        for (final String path : APIGen.SERVICE_IMPORTS) {
            bldr.append("import ").append(path).append(";\n");
        }

        bldr.append("\n// Auto-generated\n");
        bldr.append("@WebServlet(\"/api/").append(endPoint).append("\")\n");
        bldr.append("final public class ").append(apiClassName)
                .append(" extends AuthenticatedServlet { \n\n")
                .append(1, "private static final Logger LOGGER = Logger.getLogger(")
                .append(apiClassName)
                .append(".class.getName());\n\n")
                .append(1, "@Override\n")
                .append(1, "protected Map<String, Object> doGetBody(\n")
                .append(2, "final HttpServletRequest req,\n")
                .append(2, "final HttpServletResponse resp)\n")
                .append(2, "throws Exception {\n\n");

        if (action.requiresGuid) {
            bldr.append(2, m.getGuidField().toJavaDeclaration())
                    .append(" = (String) ")
                    .append(" req.getParameter(\"")
                    .append(m.getGuidField().toDBName()).append("\");\n");
            bldr.append(2, "if(StringUtil.isNullOrEmptyStr(").append(m.getGuidField().toJavaVariableName()).append(")) {\n");
            bldr.append(3, "return ServiceUtil.messageMap(new ErrorType(\"").append(m.getGuidField().toDBName()).append("_missing\", \"")
                    .append(m.getGuidField().toEnglish())
                    .append(" is a required field\"));\n");
            bldr.append(2, "}\n\n");
        }

        if (action.includeObjParams) {
            for (final AbstractField field : m.allNonGuidOriginalFields()) {
                if (!field.shareWithClient) {
                    bldr.append(2, "// TODO Set this\n");
                    bldr.append(2, field.toJavaDeclaration()).append(" = null;\n\n");
                } else if (field.isUserGenerated()) {
                        bldr.append(2, field.toJavaDeclaration())
                                .append(" = (String) ")
                                .append(" req.getParameter(\"")
                                .append(field.toDBName()).append("\");\n");
                        if (!field.nullable()) {
                            bldr.append(2, "if(StringUtil.isNullOrEmptyStr(").append(field.toJavaVariableName()).append(")) {\n");
                            bldr.append(3, "return ServiceUtil.messageMap(new ErrorType(\"").append(field.toDBName()).append("_missing\", \"")
                                    .append(m.getGuidField().toEnglish())
                                    .append(" is a required field\"));\n");
                            bldr.append(2, "}\n");
                        }
                    
                }
            }
        }

        bldr.append("\n").append(2, "try (final Connection conn = getNoAutoCommitConnection()) {\n");
        bldr.append(3, "final ExecutionResult<").append(action.returnType(m))
                .append("> er = ").append(m.dlName())
                .append(".").append(action.toString()).append("(conn");
        if (action.requiresGuid) {
            bldr.append("\n").append(4, ", ").append(m.getGuidField().toJavaVariableName());
        }
        
        if (action.includeObjParams) {
            for (final AbstractField field : m.allNonGuidOriginalFields()) {
                bldr.append("\n").append(4, ", ").append(field.toJavaVariableName());
            }
        }

        bldr.append(");\n");
        bldr.append(3, "if(er.isError()) {\n");
        if(action.requiresCommit) {
            bldr.append(4, "conn.rollback();\n");
        }
        bldr.append(4, "return ServiceUtil.messageMap(er.errorMsg());\n");
        bldr.append(3, "}\n");
        if(action.requiresCommit) {
            bldr.append(3, "conn.commit();\n");
        }
        bldr.append("\n").append(3, "return ServiceUtil.successMap(");
        if (action.returnsObj) {
            bldr.append("\"").append(m.toDBName()).append("\", ").append("er.entity().toMap()");
        }
        bldr.append(");\n");
        bldr.append(2, "}\n");
        bldr.append(1, "}\n\n");
        bldr.append("}\n");
        bldr.append("\n");
        return bldr.toString();
    }

}
