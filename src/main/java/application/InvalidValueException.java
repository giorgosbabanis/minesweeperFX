package application;

public class InvalidValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidValueException(String errorMessage) {
		super(errorMessage);
	}
	public InvalidValueException() {
		super("Some values are outside the expected range");
	}

}
