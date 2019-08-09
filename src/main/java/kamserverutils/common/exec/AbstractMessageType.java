package kamserverutils.common.exec;

import kamserverutils.common.containers.Mapable;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractMessageType implements Mapable {

	// TODO Would prefer if these were final
	protected String code;
	protected String msg;
	protected String asString;
	final protected Map<String, Object> asMap = new TreeMap<String, Object>();

	public abstract String getStatus();

	public abstract String getType();

	public String getErrorCode() {
		return this.code;
	}

	public String getErrorMsg() {
		return this.msg;
	}

	@Override
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