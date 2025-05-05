package exception;

/**
 * 메뉴 선택이 허용된 범위를 벗어났을 때 발생하는 예외.
 */
public class ChoiceOutOfBoundException extends RuntimeException {
    // 기본 생성자
    public ChoiceOutOfBoundException() {
        super("선택 가능한 범위를 벗어난 입력입니다.");
    }

    // 커스텀 메시지 생성자
    public ChoiceOutOfBoundException(String message) {
        super(message);
    }
}
