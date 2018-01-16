package com.robin.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.bean.PageBean;
import com.robin.bean.User;
import com.robin.dao.OrderDao;
import com.robin.dao.impl.OrderDaoImpl;
import com.robin.service.OrderService;
import com.robin.utils.ConnectionManager;

public class OrderServiceImpl implements OrderService {

	@Override
	public boolean newOrder(Order order) throws Exception{
		OrderDao orderDao = new OrderDaoImpl();
		
		try 
		{
			ConnectionManager.startTransaction();
			orderDao.saveOrder(order);
			orderDao.saveOrderItems(order);
			ConnectionManager.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ConnectionManager.rollback();
			throw e;
		}
		return true;
	}

	@Override
	public PageBean<Order> findOrdersInPages(User user,int curPage) throws Exception {
		OrderDao orderDao = new OrderDaoImpl();
		return orderDao.findOrdersInPages(user,curPage);
	}

	@Override
	public List<OrderItem> findItemsByOrder(Order order) throws Exception {
		OrderDao orderDao = new OrderDaoImpl();
		return orderDao.findItemsByOrder(order);
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		OrderDao orderDao = new OrderDaoImpl();
		return orderDao.findOrderByOid(oid);
	}

	@Override
	public boolean updateOrder(Order order) throws Exception {
		OrderDao orderDao = new OrderDaoImpl();
		return orderDao.updateOrder(order);
	}
}
