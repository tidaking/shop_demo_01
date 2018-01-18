package com.robin.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.bean.PageBean;
import com.robin.bean.Product;
import com.robin.bean.User;
import com.robin.constant.Constant;
import com.robin.dao.OrderDao;
import com.robin.utils.C3P0Utils;
import com.robin.utils.ConnectionManager;
import com.robin.utils.LogUtils;
import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;

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

	@Override
	public PageBean<Order> findOrdersInPages(User user,int curPage) throws Exception {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		
		String sql_count = "select count(*) from orders where uid=?";
		Long count_long = (Long) runner.query(sql_count, new ScalarHandler(), user.getUid());
		int sumPage = 0;
		int count =count_long.intValue();
		if(count%Constant.ORDER_PAGE_SIZE != 0)
		{
			sumPage = count / Constant.ORDER_PAGE_SIZE +1;
		}else
		{
			sumPage = count / Constant.ORDER_PAGE_SIZE;
		}
		
		if(sumPage < curPage)
		{
			System.out.println("[OrderDao]:查询的页数超出范围");
			return null;
		}
		
		
		PageBean<Order> pageBean = new PageBean<>();
		pageBean.setCount(count);
		pageBean.setCurPage(curPage);
		pageBean.setCurSize(Constant.ORDER_PAGE_SIZE);
		pageBean.setSumPage(sumPage);
		
		String sql = "select * from orders where uid = ? limit ?,?";

		List<Order> orders = new ArrayList<>();
		int a,b;
		a = (curPage-1)*Constant.ORDER_PAGE_SIZE;
		b = Constant.ORDER_PAGE_SIZE;
		orders = runner.query(sql, new BeanListHandler<>(Order.class), user.getUid(),a,b);

		for (Order order : orders) {
			List<OrderItem> orderItems = findItemsByOrder(order);
			order.setOrderItems(orderItems);
			System.out.println("[OrderDao]:order:"+order.getOid());
		}
		pageBean.setList(orders);
		//return orders;
		return pageBean;
	}

	@Override
	public List<OrderItem> findItemsByOrder(Order order) throws Exception{
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		// 找出符合条件的orderitem,orderitem里面包含了pid
		String sql = "select * from orderitem o,product p where o.oid=? and o.pid=p.pid";
		// 同时查询product table,一并得到productBean
		// 每一个Map都是一条记录,key为字段名,value为字段值
		List<OrderItem> orderItems = new ArrayList<>();
		List<Map<String, Object>> list = runner.query(sql, new MapListHandler(), order.getOid());
		for (Map<String, Object> map : list) {
			OrderItem orderItem = new OrderItem();
			Product product = new Product();
			BeanUtils.populate(product, map);
			BeanUtils.populate(orderItem, map);
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}
		return orderItems;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders where oid=?";
		Order order = runner.query(sql, new BeanHandler<>(Order.class), oid);

		List<OrderItem> orderItems = findItemsByOrder(order);
		order.setOrderItems(orderItems);
		return order;
	}

	@Override
	public boolean updateOrder(Order order) throws Exception {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update orders set state=?,address = ?,name = ?,telephone = ? where oid = ?";
		Object[] params = {order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
		int update = runner.update(sql, params);
		if(update == 1)
		{
			return true;
		}
		return false;
	}

	@Override
	public PageBean<Order> findOrderByState(String state, int curPage) throws SQLException {
		LogUtils.info("entry");
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders";
		String sql_count = "select count(*) from orders";
		
		Long tempCount = (Long)runner.query(sql_count, new ScalarHandler());
		int count = tempCount.intValue();
		int sumPage = 0;
		if(count % Constant.ADMIN_ORDER_PAGE_SIZE != 0)
		{
			sumPage = count / Constant.ADMIN_ORDER_PAGE_SIZE +1;
		}else {
			sumPage = count / Constant.ADMIN_ORDER_PAGE_SIZE;
		}
		
		if(curPage > sumPage)
		{
			LogUtils.error("curPage is Out of Range!curPage:"+curPage+" sumPage:"+sumPage);
			return null;
		}
		PageBean<Order> page = new PageBean<>();
		page.setCount(count);
		page.setCurPage(curPage);
		page.setCurSize(Constant.ADMIN_ORDER_PAGE_SIZE);
		
		page.setSumPage(sumPage);
		int a,b;
		a = (curPage-1)*Constant.ADMIN_ORDER_PAGE_SIZE;
		b = Constant.ADMIN_ORDER_PAGE_SIZE;
		List<Order> list = new ArrayList<>();
		if(state != null || "".equals(state))
		{
			sql += " where state=? limit ?,?";
			list = runner.query(sql, new BeanListHandler<>(Order.class), state,a,b);
		}else {
			sql += " limit ?,?";
			list = runner.query(sql, new BeanListHandler<>(Order.class),a,b);
		}

		page.setList(list);
		LogUtils.info("exit");
		return page;
	}
}
