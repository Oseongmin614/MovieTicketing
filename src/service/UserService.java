package service;

import java.util.List;

import dao.UserDAO;
import vo.UserVO;

public class UserService {

	private UserDAO userDAO;

	public UserService() {
		this.userDAO = UserDAO.getInstance();
	}

	public boolean registerUser(UserVO user) {
		if (userDAO.findByUserId(user.getUserId()) != null) {
			System.out.println("이미 존재하는 아이디입니다.");
			return false;
		}

		return userDAO.save(user) != null;
	}

	public List<UserVO> getAllUsers() {
		return userDAO.findAll();
	}

	public UserVO getUserById(String userId) {
		return userDAO.findByUserId(userId);
	}

	public boolean updateUser(UserVO user) {
		return userDAO.updateUser(user);
	}

	public boolean deleteUser(String userId) {
		return userDAO.deleteByUserId(userId);
	}
}