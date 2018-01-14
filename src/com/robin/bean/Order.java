package com.robin.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	
	/*`oid` VARCHAR(32) NOT NULL,
	  `ordertime` DATETIME DEFAULT NULL,
	  `total` DOUBLE DEFAULT NULL,
	  `state` INT(11) DEFAULT NULL,
	  `address` VARCHAR(30) DEFAULT NULL,
	  `name` VARCHAR(20) DEFAULT NULL,
	  `telephone` VARCHAR(20) DEFAULT NULL,
	  `uid` VARCHAR(32) DEFAULT NULL,*/
	
	private String oid;
	private Date ordertime;
	private double total;
	private int state;//订单状态 0:未付款	1:已付款	2:已发货	3.已完成
	private User user;
	private List<OrderItem> orderItems= new ArrayList<OrderItem>();
	
	//TODO
	private String address;
	private String name;
	private String telephone;
	
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", state=" + state + ", user="
				+ user + ", name=" + name + ", telephone=" + telephone + "]";
	}
	
	
	

}
