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
import com.robin.utils.LogUtils;

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
		int curSize = Constant.PRODUCT_PAGE_SIZE;
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

	@Override
	public List<Product> getAllProducts() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product";
		return runner.query(sql, new BeanListHandler<>(Product.class));
	}

	@Override
	public boolean addProduct(Product product) throws SQLException {
		
		/*`pid` VARCHAR(32) NOT NULL,
		  `pname` VARCHAR(50) DEFAULT NULL,
		  `market_price` DOUBLE DEFAULT NULL,
		  `shop_price` DOUBLE DEFAULT NULL,
		  `pimage` VARCHAR(200) DEFAULT NULL,
		  `pdate` DATE DEFAULT NULL,
		  `is_hot` INT(11) DEFAULT NULL,
		  `pdesc` VARCHAR(255) DEFAULT NULL,
		  `pflag` INT(11) DEFAULT NULL,
		  `cid` VARCHAR(32) DEFAULT NULL,*/
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {product.getPid(),product.getPname(),product.getMarket_price(),
							product.getShop_price(),product.getPimage(),product.getPdate(),
							product.getIs_hot(),product.getPdesc(),product.getPflag(),
							product.getCategory().getCid()};
		int update = runner.update(sql, params);
		if(update != 1)
		{
			LogUtils.error("Database update "+update+" lines");
			return false;
		}
		return true;
	}
}
