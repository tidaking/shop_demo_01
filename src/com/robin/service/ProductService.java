package com.robin.service;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.PageBean;
import com.robin.bean.Product;

public interface ProductService {

	public List<Product> getLatestProduct() throws SQLException;

	public List<Product> getHotestProduct() throws SQLException;

	public Product findByPid(String pid) throws SQLException;

	public PageBean<Product> findByCidInPages(String cid, int curPage) throws SQLException;

	public List<Product> findAll() throws SQLException;

	public boolean addProduct(Product product) throws SQLException;

}
