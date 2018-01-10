package com.robin.dao;

import java.sql.SQLException;

import com.robin.bean.User;

public interface UserDao {

	public User findUser(String username,String password) throws SQLException;

	public int saveUser(User user) throws SQLException;

	public User findUser(String code) throws SQLException;

	public int updateUser(User user) throws SQLException;

	public void rollback() throws SQLException;

	public void commit() throws SQLException;

	public int saveUserInTransAction(User user) throws SQLException;


}
