package com.robin.service;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.Category;

public interface CategoryService {
	public List<Category> getAllCategory() throws SQLException;
	
}
