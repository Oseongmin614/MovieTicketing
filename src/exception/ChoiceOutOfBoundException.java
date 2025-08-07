package exception;

public class ChoiceOutOfBoundException extends RuntimeException {
	public ChoiceOutOfBoundException() {
		super("선택 가능한 범위를 벗어난 입력입니다.");
	}

	public ChoiceOutOfBoundException(String message) {
		super(message);
	}
}