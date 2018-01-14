package com.robin.service;

import java.sql.SQLException;

import com.robin.bean.Order;

public interface OrderService {

	public boolean newOrder(Order order) throws Exception;

}
