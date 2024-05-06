package application;

public class InvalidDescriptionException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidDescriptionException(String errorMessage) {
		super(errorMessage);
	}
	public InvalidDescriptionException() {
		super("The description did not have 4 values");
	}
}
