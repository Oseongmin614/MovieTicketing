package service;

import dao.UserDAO;

public class LoginService {
    // 1. 내부 정적 헬퍼 클래스 사용 (지연 로딩)
    private static class SingletonHelper {
        private static final LoginService INSTANCE = new LoginService();
    }

    // 2. final로 선언된 DAO 인스턴스
    private final UserDAO userDAO;

    // 3. private 생성자
    private LoginService() {
        this.userDAO = UserDAO.getInstance(); // UserDAO도 싱글톤으로 구현됨
    }

    // 4. 인스턴스 반환 메서드 (동기화 불필요)
    public static LoginService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * 로그인 처리
     * @param userId 사용자 ID
     * @param password 비밀번호(평문)
     * @return 성공 시 true, 실패 시 false
     */
    public boolean login(String userId, String password) {
        if (userId == null || password == null || 
            userId.isBlank() || password.isBlank()) {
            return false;
        }
        return userDAO.login(userId, password);
    }

    /**
     * 사용자 권한 조회
     * @param userId 사용자 ID
     * @return 'admin' 또는 'user' (없으면 null)
     */
    public String getRole(String userId) {
        return userDAO.getRole(userId);
    }
}