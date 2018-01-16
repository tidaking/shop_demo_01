package com.robin.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.robin.bean.Category;
import com.robin.dao.CategoryDao;
import com.robin.utils.C3P0Utils;
import com.robin.utils.LogUtils;
import com.robin.utils.UUIDUtils;

public class CategoryDaoImpl implements CategoryDao {

	@Override
	public List<Category> getAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category";
		return  runner.query(sql, new BeanListHandler<>(Category.class));
	}

	@Override
	public boolean addCategory(String cname) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into category values(?,?)";
		String cid = UUIDUtils.getId();
		
		int update = runner.update(sql, cid,cname);
		if(update != 1)
		{
			LogUtils.error("Database update "+update+" lines!");
			return false;
		}
		return true;
	}

	@Override
	public Category getCategoryByCid(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category where cid=?";
		return runner.query(sql, new BeanHandler<>(Category.class), cid);
	}

}
