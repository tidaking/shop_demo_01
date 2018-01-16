package com.robin.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.PageBean;
import com.robin.bean.Product;
import com.robin.dao.ProductDao;
import com.robin.dao.impl.ProductDaoImpl;
import com.robin.service.ProductService;

public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> getLatestProduct() throws SQLException {
		ProductDao dao = new ProductDaoImpl();
		return dao.getLatestProduct();
	}

	@Override
	public List<Product> getHotestProduct() throws SQLException {
		ProductDao dao = new ProductDaoImpl();
		return dao.getHotestProduct();
	}

	@Override
	public Product findByPid(String pid) throws SQLException {
		ProductDao dao = new ProductDaoImpl();
		return dao.getProduct(pid);
	}

	@Override
	public PageBean<Product> findByCidInPages(String cid, int curPage) throws SQLException {
		ProductDao dao = new ProductDaoImpl();
		return dao.getProducts(cid,curPage);
	}

	@Override
	public List<Product> findAll() throws SQLException {
		ProductDao productDao = new ProductDaoImpl();
		return productDao.getAllProducts();
	}

	@Override
	public boolean addProduct(Product product) throws SQLException {
		ProductDao productDao = new ProductDaoImpl();
		return productDao.addProduct(product);
	}

}
