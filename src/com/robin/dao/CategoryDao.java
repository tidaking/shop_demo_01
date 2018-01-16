package com.robin.dao;

import java.sql.SQLException;
import java.util.List;

import com.robin.bean.Category;

public interface CategoryDao {

	List<Category> getAllCategory() throws SQLException;

	boolean addCategory(String cname) throws SQLException;

	Category getCategoryByCid(String cid) throws SQLException;

}
