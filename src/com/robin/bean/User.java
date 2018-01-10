package com.robin.bean;

public class User {
/*	`uid` varchar(32) NOT NULL,  
	 `username` varchar(20) DEFAULT NULL,  
	 `password` varchar(20) DEFAULT NULL,
    `name` varchar(20) DEFAULT NULL,
	 `email` varchar(30) DEFAULT NULL,
	 `telephone` varchar(20) DEFAULT NULL,
	 `birthday` date DEFAULT NULL,
	 `sex` varchar(10) DEFAULT NULL,
	 `state` int(11) DEFAULT NULL,
	 `code` varchar(64) DEFAULT NULL,*/
	
	private String uid;
	private String username;
	private String password;
	
	private String name;
	private String email;
	private String telephone;
	
	//如果表单里面有和时间相关的数据, 通常用String, 避免转换;
	private String birthday;
	private String sex;
	private int state;
	
	private String code;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", name=" + name + ", email="
				+ email + ", telephone=" + telephone + ", birthday=" + birthday + ", sex=" + sex + ", state=" + state
				+ ", code=" + code + "]";
	}
	
	
	public static boolean checkUserVaild(User user)
	{
		if(user.getUsername() == null || 
		   user.getPassword() == null ||
		   user.getEmail() == null ||
		   user.getName() == null ||
		   user.getBirthday() == null)
		{
			return false;
		}
		return true;
	}
	
	
	
	

}
