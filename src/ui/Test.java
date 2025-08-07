package ui;

import dao.UserDAO;
import service.LoginService;
import vo.UserVO;

public class Test {

	public static void main(String[] args) {
		try {
			new Test().runLoginTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runLoginTest() throws Exception {
		System.out.println("===== 로그인 기능 테스트 시작 =====");

		String testUserId = "testUser";
		String testPassword = "testPassword";
		String testName = "테스트유저";

		UserDAO userDAO = UserDAO.getInstance();
		LoginService loginService = LoginService.getInstance();

		System.out.println("\n--- 테스트 시작 전 기존 테스트 사용자 삭제 ---");
		if (userDAO.deleteByUserId(testUserId)) {
			System.out.println("기존 테스트 사용자(" + testUserId + ") 삭제 성공.");
		} else {
			System.out.println("기존 테스트 사용자(" + testUserId + ") 없음 또는 삭제 실패.");
		}

		System.out.println("\n--- 1. 테스트 사용자 생성 또는 확인 ---");
		UserVO existingUser = userDAO.findByUserId(testUserId);
		System.out.println(
				"findByUserId(" + testUserId + ") 결과: " + (existingUser != null ? existingUser.getUserId() : "null"));
		UserVO savedUser = null;

		if (existingUser == null) {
			System.out.println("기존 사용자가 없으므로 새로 생성합니다.");
			UserVO testUser = new UserVO();
			testUser.setUserId(testUserId);
			testUser.setPassword(testPassword);
			testUser.setName(testName);
			testUser.setRole("USER");
			savedUser = userDAO.save(testUser);
			if (savedUser != null) {
				System.out.println("새로운 사용자 생성 성공: " + savedUser.getUserId());
			} else {
				System.out.println("새로운 사용자 생성 실패");
				return;
			}
		} else {
			System.out.println("기존 테스트 사용자(" + testUserId + ")가 존재합니다. 새로 생성하지 않고 진행합니다.");
			savedUser = existingUser;
		}
		System.out.println("최종 savedUser: " + (savedUser != null ? savedUser.getUserId() : "null"));

		System.out.println("\n--- 2. 로그인 테스트 ---");
		System.out.println("테스트 아이디: " + testUserId);
		System.out.println("테스트 비밀번호: " + testPassword);

		boolean loginSuccess = loginService.login(testUserId, testPassword);

		if (loginSuccess) {
			System.out.println("로그인 성공!");
		} else {
			System.out.println("로그인 실패!");
		}

		System.out.println("\n--- 3. 잘못된 비밀번호 테스트 ---");
		System.out.println("테스트 아이디: " + testUserId);
		System.out.println("잘못된 비밀번호: wrongPassword");

		loginSuccess = loginService.login(testUserId, "wrongPassword");

		if (!loginSuccess) {
			System.out.println("로그인 실패 (예상된 결과)");
		} else {
			System.out.println("로그인 성공 (예상치 못한 결과)");
		}

		System.out.println("\n===== 로그인 기능 테스트 종료 =====");
	}
}