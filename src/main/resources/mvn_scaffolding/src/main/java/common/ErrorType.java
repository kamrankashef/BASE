package common;

final public class ErrorType extends AbstractMessageType {

	public ErrorType(final String errorCode, final String errorMsg) {
		super(errorCode, errorMsg);
		this.asMap.put("error_code", this.code);
		this.asMap.put("error_msg", this.msg);
		this.asString = "error_code=[" + errorCode + "],error_msg=[" + errorMsg + "]";
	}

	public static final ErrorType UNKNOWN_ERROR =
					new ErrorType("unknown_error", "Unknown error");

	@Override
	public String getStatus() {
		return "ERROR";
	}

	@Override
	public String getType() {
		return "error";
	}
}