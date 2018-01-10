package com.robin.service;

import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.robin.bean.User;

public interface UserService {
	public User login(String username,String password) throws SQLException;

	public boolean regist(User user) throws SQLException, AddressException, MessagingException;

	public boolean active(String code) throws SQLException;
}
