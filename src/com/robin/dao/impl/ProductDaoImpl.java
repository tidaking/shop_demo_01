package com.robin.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.robin.bean.PageBean;
import com.robin.bean.Product;
import com.robin.constant.Constant;
import com.robin.dao.ProductDao;
import com.robin.utils.C3P0Utils;

public class ProductDaoImpl implements ProductDao {

	@Override
	public List<Product> getLatestProduct() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product order by pdate DESC limit ?,?";
		return runner.query(sql, new BeanListHandler<>(Product.class),0,9);
	}

	@Override
	public List<Product> getHotestProduct() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		return runner.query(sql, new BeanListHandler<>(Product.class),Constant.PRODUCT_IS_HOT,0,9);
	}

	@Override
	public Product getProduct(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where pid=?";
		return runner.query(sql, new BeanHandler<>(Product.class),pid);
	}

	@Override
	public PageBean<Product> getProducts(String cid, int curPage) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		int a,b;
		int count = getProductConut(cid);
		int sumPage = 0;
		int curSize = Constant.PAGE_PAGE_SIZE;
		if(count % curSize == 0)
		{
			sumPage = count/curSize;//TODO:dont write hard code
		}
		else
		{
			sumPage = count/curSize +1;
		}
		
		if(curPage > sumPage)
		{
			System.out.println("[ProductDao]:request curPage("+curPage+") is larger than sumPage("+sumPage+")");
			return null;
		}

		PageBean<Product> page= new PageBean<>();
		page.setCount(count);
		page.setCurSize(curSize);
		page.setSumPage(sumPage);
		page.setCurPage(curPage);

		a = (curPage - 1)*curSize;
		b = curSize;
		String sql = "select * from product where cid=? limit ?,? ";
		List<Product> list = runner.query(sql, new BeanListHandler<>(Product.class),cid,a,b);

		page.setList(list);
		return page;
	}


	@Override
	public int getProductConut(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from product where cid=?";
		Long count = (Long) runner.query(sql, new ScalarHandler(),cid);
		return count.intValue();
	}
}
