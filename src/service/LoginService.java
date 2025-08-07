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

		return userDAO.findByUserId(userId);
	}

	public boolean login(String userId, String password) {
		if (userId == null || password == null || userId.isBlank() || password.isBlank()) {
			return false;
		}
		return userDAO.login(userId, password);
	}

	public String getRole(String userId) {
		return userDAO.getRole(userId);
	}
}
