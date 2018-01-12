package com.robin.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.Category;
import com.robin.dao.CategoryDao;
import com.robin.dao.impl.CategoryDaoImpl;
import com.robin.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {

	@Override
	public List<Category> getAllCategory() throws SQLException {
		CategoryDao dao = new CategoryDaoImpl();
		return dao.getAllCategory();
	}
	
	
}
