package com.robin.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.robin.bean.User;
import com.robin.dao.UserDao;
import com.robin.utils.C3P0Utils;

public class UserDaoImpl implements UserDao {
	private boolean transAction_flag;
	private Connection conn;
	private ThreadLocal<Connection> local;
	
	
	public UserDaoImpl() throws SQLException {
		local = new ThreadLocal<>();
		conn = C3P0Utils.getConnection();
		local.set(conn);
		transAction_flag = false;
	}
	
	@Override
	protected void finalize() throws Throwable {
		conn.close();
	}

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

	@Override
	public int saveUserInTransAction(User user) throws SQLException {
		System.out.println("[UserDaoImpl]:saveUserInTransAction begin...");
		// 从ThreadLocal中获得connection,确保setAutoCommit,执行insert操作,commit的都是同一个connection
		Connection connection = local.get();
		if(connection == null)
		{
			connection = C3P0Utils.getConnection();
			local.set(connection);
		}

		if(transAction_flag != true)
		{
			// 开启事务
			System.out.println("[UserDaoImpl]:transAction_flag is false,set to True...");
			connection.setAutoCommit(false);
			transAction_flag = true;
		}
		QueryRunner runner = new QueryRunner();
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		Object[] userinfo = {user.getUid(),user.getUsername(),user.getPassword(),user.getName(),
							user.getEmail(),user.getTelephone(),user.getBirthday(),
							user.getSex(),user.getState(),user.getCode()};
		int update = runner.update(connection, sql, userinfo);
		return update;
	}

	@Override
	public void rollback() throws SQLException {
		// 从ThreadLocal中获得connection,确保setAutoCommit,执行insert操作,commit的都是同一个connection
		System.out.println("[UserDaoImpl]:rollback begin...");
		Connection connection = local.get();
		if(connection == null)
		{
			connection = C3P0Utils.getConnection();
			local.set(connection);
		}

		if(transAction_flag != true)
		{
			//说明没有开启过事务,就无需rollback
			System.out.println("transAction_flag is FALSE!No need to ROLLBACK!");
		}
		else
		{
			System.out.println("[UserDaoImpl]:rollback...");
			transAction_flag = false;
			connection.rollback();
		}

	}

	@Override
	public void commit() throws SQLException {
		// 从ThreadLocal中获得connection,确保setAutoCommit,执行insert操作,commit的都是同一个connection
		System.out.println("[UserDaoImpl]:commit begin...");
		Connection connection = local.get();
		if(connection == null)
		{
			connection = C3P0Utils.getConnection();
			local.set(connection);
		}

		if(transAction_flag != true)
		{
			//说明没有开启过事务,就无需rollback
			System.out.println("transAction_flag is FALSE!No need to COMMIT!");
		}
		else
		{
			System.out.println("[UserDaoImpl]:commit...");
			transAction_flag = false;
			connection.commit();
		}
		
	}

}
