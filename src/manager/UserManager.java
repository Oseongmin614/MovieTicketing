package manager;

import java.util.List;

import service.UserService;
import vo.UserVO;

public class UserManager {

	private static UserManager instance;
	private final UserService userService;

	private UserManager() {
		this.userService = new UserService();
	}

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public List<UserVO> getAllUsers() {
		return userService.getAllUsers();
	}

	public UserVO getUserById(String userId) {
		return userService.getUserById(userId);
	}

	public boolean updateUser(UserVO user) {
		return userService.updateUser(user);
	}

	public boolean deleteUser(String userId) {
		return userService.deleteUser(userId);
	}
}