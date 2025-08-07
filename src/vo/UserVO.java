package vo;

import java.util.Date;

public class UserVO {
	private int userNo;
	private String userId;
	private String password;
	private String name;
	private String role;
	private Date regDate;

	public UserVO() {}

	public UserVO(int userNo, String userId, String password, String name, String role, Date regDate) {
		this.userNo = userNo;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.role = role;
		this.regDate = regDate;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getPassword() {
		return password;
	}

	public int getUserNo() {
		return userNo;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public Date getRegDate() {
		return regDate;
	}

	@Override
	public String toString() {
		return "User{" + "userNo=" + userNo + ", userId='" + userId + ", name='" + name + ", role='" + role
				+ ", regDate=" + regDate + '}';
	}
}