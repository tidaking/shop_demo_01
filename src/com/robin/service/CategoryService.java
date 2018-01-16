package com.robin.service;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.Category;

public interface CategoryService {
	public List<Category> getAllCategory() throws SQLException;

	public boolean addCategory(String cname) throws SQLException;

	public Category findCategoryByCid(String cid) throws SQLException;
	
}
