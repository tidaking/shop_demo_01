package com.robin.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import sun.security.ssl.SSLContextImpl.TLS11Context;

public class ConnectionManager {

	private static ThreadLocal<Connection> t1 = new ThreadLocal<Connection>();

	// 获取连接
	public static Connection getConnectionByLocalThread() throws SQLException {
		Connection connection = t1.get();

		if (connection == null) {
			connection = C3P0Utils.getConnection();
			t1.set(connection);
		}
		return connection;
	}

	// 开启事物
	public static void startTransaction() throws SQLException {
		Connection connection = getConnectionByLocalThread();
		connection.setAutoCommit(false);
	}

	// 提交事物
	public static void commit() throws SQLException {
		Connection connection = getConnectionByLocalThread();
		connection.commit();
	}

	// 回滚事物
	public static void rollback() throws SQLException {
		Connection connection = getConnectionByLocalThread();
		connection.rollback();
	}
	
	//关闭资源
	
	public static void close() throws SQLException {
		Connection connection = getConnectionByLocalThread();
		connection.close();
	}

}
