package common;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractMessageType {

    final protected String code;
    final protected String msg;
    protected String asString;

    public AbstractMessageType(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
        this.asString = "[" + getStatus().toLowerCase() +"_code:'" + code + "', " + getStatus().toLowerCase() + "_msg:'" + msg + "']";
    }

    final protected Map<String, Object> asMap = new TreeMap<>();

    public abstract String getStatus();

    public abstract String getType();

    public String getErrorCode() {
        return this.code;
    }

    public String getErrorMsg() {
        return this.msg;
    }

    public Map<String, Object> toMap() {
        return this.asMap;
    }

    @Override
    public String toString() {
        return this.asString;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof ErrorType)) {
            return false;
        }
        final ErrorType that = (ErrorType) object;
        return this.code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return this.code.hashCode();
    }
}
