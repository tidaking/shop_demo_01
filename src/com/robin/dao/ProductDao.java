package com.robin.dao;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.PageBean;
import com.robin.bean.Product;

public interface ProductDao {

	public List<Product> getLatestProduct() throws SQLException;

	public List<Product> getHotestProduct() throws SQLException;

	public Product getProduct(String pid) throws SQLException;

	public PageBean<Product> getProducts(String cid, int curPage) throws SQLException;
	
	public int getProductConut(String cid) throws SQLException;

	public List<Product> getAllProducts() throws SQLException;

	public boolean addProduct(Product product) throws SQLException;

}
