package com.robin.service.impl;

import java.sql.SQLException;

import com.robin.bean.Order;
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
}
