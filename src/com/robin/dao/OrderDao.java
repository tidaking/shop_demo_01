package com.robin.dao;

import java.sql.SQLException;

import com.robin.bean.Order;

public interface OrderDao {

	public boolean saveOrder(Order order) throws SQLException;

	public boolean saveOrderItems(Order order) throws SQLException;

}
