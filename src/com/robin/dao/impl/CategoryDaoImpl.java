package com.robin.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.robin.bean.Category;
import com.robin.dao.CategoryDao;
import com.robin.utils.C3P0Utils;

public class CategoryDaoImpl implements CategoryDao {

	@Override
	public List<Category> getAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category";
		return  runner.query(sql, new BeanListHandler<>(Category.class));
	}

}
