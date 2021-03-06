package com.robin.service;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.bean.PageBean;
import com.robin.bean.User;

public interface OrderService {

	public boolean newOrder(Order order) throws Exception;

	public PageBean<Order> findOrdersInPages(User user, int curPage) throws Exception;

	public List<OrderItem> findItemsByOrder(Order order) throws Exception;

	public Order findOrderByOid(String oid) throws Exception;

	public boolean updateOrder(Order order) throws Exception;

	public PageBean<Order> findOrderByState(String state, int curPage) throws SQLException;
}
