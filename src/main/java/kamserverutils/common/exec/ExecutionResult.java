package kamserverutils.common.exec;

final public class ExecutionResult<T extends Object> {

	
	final private T entity;
	final private AbstractMessageType errorType;
	public static final ErrorType UNKNOWN_ERROR =
					new ErrorType("unknown_error", "Unknown error");

	public static <K> ExecutionResult<K> errorResult() {
		return new ExecutionResult<>(null, UNKNOWN_ERROR);
	}

	public static <K> ExecutionResult<K> errorResult(final AbstractMessageType errorType) {
		return new ExecutionResult<>(null, errorType);
	}

	public static <K> ExecutionResult<K> successResult(final K value) {
		return new ExecutionResult<>(value, null);
	}

	private ExecutionResult(final T entity, final AbstractMessageType errorType) {
		this.entity = entity;
		this.errorType = errorType;
	}

	public boolean isError() {
		return this.errorType != null;
	}

	public boolean isSuccess() {
		return this.errorType == null;
	}

	public AbstractMessageType errorMsg() {
		if (!this.isError()) {
			throw new IllegalStateException("Not in error state");
		}
		return this.errorType;
	}

	public T entity() {
		if (this.isError()) {
			throw new IllegalStateException("In error state, cannot get entity");
		}
		return this.entity;
	}
}
