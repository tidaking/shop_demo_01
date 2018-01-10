package com.robin.dao.impl;

import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.robin.bean.User;
import com.robin.dao.UserDao;
import com.robin.utils.C3P0Utils;

public class UserDaoImpl implements UserDao {

	@Override
	public User findUser(String username, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from user where username=? and password=?";
		User user = null;
		user = runner.query(sql, new BeanHandler<>(User.class), username,password);
		return user;
	}

	@Override
	public int saveUser(User user) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		Object[] userinfo = {user.getUid(),user.getUsername(),user.getPassword(),user.getName(),
							user.getEmail(),user.getTelephone(),user.getBirthday(),
							user.getSex(),user.getState(),user.getCode()};
		int update = runner.update(sql, userinfo);
		return update;
	}

	@Override
	public User findUser(String code) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from user where code=?";
		User user = null;
		user = runner.query(sql, new BeanHandler<>(User.class), code);
		return user;
	}

	@Override
	public int updateUser(User user) throws SQLException{
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update user set username = ?,password = ?,name = ?,email = ?,telephone = ?,birthday =?,sex =?, state = ?,code = ? where uid = ?";
		Object[] params= { user.getUsername(),user.getPassword(),
				user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),
				user.getSex(),user.getState(),user.getCode(),user.getUid()
		};
		int update = runner.update(sql,params);
		return update;
	}

}
