package service;

import dao.UserDAO;
import vo.UserVO;

public class LoginService {

	private static class SingletonHelper {
		private static final LoginService INSTANCE = new LoginService();
	}

	private final UserDAO userDAO;


	private LoginService() {
		this.userDAO = UserDAO.getInstance();
	}

	
	public static LoginService getInstance() {
		return SingletonHelper.INSTANCE;
	}
	public UserVO getUser(String userId) {
	    if (userId == null || userId.isBlank()) {
	        return null;
	    }
	    
	    return userDAO.getUserByUserId(userId);
	}
	/**
	 * 로그인 처리
	 * 
	 * @param userId   사용자 ID
	 * @param password 비밀번호(평문)
	 * @return 성공 시 true, 실패 시 false
	 */
	public boolean login(String userId, String password) {
		if (userId == null || password == null || userId.isBlank() || password.isBlank()) {
			return false;
		}
		return userDAO.login(userId, password);
	}

	/**
	 * 사용자 권한 조회
	 * 
	 * @param userId 사용자 ID
	 * @return 'admin' 또는 'user' (없으면 null)
	 */
	public String getRole(String userId) {
		return userDAO.getRole(userId);
	}
}