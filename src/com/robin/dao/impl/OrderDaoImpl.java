package com.robin.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.dao.OrderDao;
import com.robin.utils.ConnectionManager;

public class OrderDaoImpl implements OrderDao {

	@Override
	public boolean saveOrder(Order order) throws SQLException {
		
		/*`oid` VARCHAR(32) NOT NULL,
		  `ordertime` DATETIME DEFAULT NULL,
		  `total` DOUBLE DEFAULT NULL,
		  `state` INT(11) DEFAULT NULL,
		  `address` VARCHAR(30) DEFAULT NULL,
		  `name` VARCHAR(20) DEFAULT NULL,
		  `telephone` VARCHAR(20) DEFAULT NULL,
		  `uid` VARCHAR(32) DEFAULT NULL,*/
		  
		  
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		
		Object[] params_order = {order.getOid(),order.getOrdertime(),order.getTotal(),
							order.getState(),order.getAddress(),order.getName(),
							order.getTelephone(),order.getUser().getUid()};
		Connection conn = ConnectionManager.getConnectionByLocalThread();

		int update = runner.update(conn, sql, params_order);

		if(update != 1)
		{
			System.out.println("[OrderDao]:saveOrder return "+update);
			return false;
		}

		return true;
	}

	@Override
	public boolean saveOrderItems(Order order) throws SQLException {
		
		/* `itemid` VARCHAR(32) NOT NULL,
		  `count` INT(11) DEFAULT NULL,
		  `subtotal` DOUBLE DEFAULT NULL,
		  `pid` VARCHAR(32) DEFAULT NULL,
		  `oid` VARCHAR(32) DEFAULT NULL,*/
		QueryRunner runner = new  QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		int update = 0;
		
		Connection conn = ConnectionManager.getConnectionByLocalThread();
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			Object[] params_orderitem = {orderItem.getItemid(),orderItem.getCount(),orderItem.getSubtotal(),
							 orderItem.getProduct().getPid(),orderItem.getOrder().getOid()};
			update = runner.update(conn, sql, params_orderitem);
			if(update != 1)
			{
				System.out.println("[OrderDaoImpl]:saveOrderItems return "+update);
				return false;
			}
		}
	
		return true;
	}
}
