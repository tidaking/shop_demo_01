package com.robin.web.servlet;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.bean.Category;
import com.robin.bean.Product;
import com.robin.service.CategoryService;
import com.robin.service.ProductService;
import com.robin.service.impl.CategoryServiceImpl;
import com.robin.service.impl.ProductServiceImpl;
import com.robin.utils.LogUtils;

/**
 * Servlet implementation class AdminProductServlet
 */
public class AdminProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String addProductUI(HttpServletRequest request,HttpServletResponse response) {
		try {
			CategoryService categoryService = new CategoryServiceImpl();
			List<Category> categories = categoryService.getAllCategory();
			request.setAttribute("list", categories);
			return "/admin/product/add.jsp";
		} catch (Exception e) {
			LogUtils.error("add product faild");
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常!");
			return "/admin/msg.jsp";
		}
	}

	public String findAll(HttpServletRequest request,HttpServletResponse response) {
		try {
			ProductService productService = new ProductServiceImpl();
			List<Product> products = productService.findAll();
			if(products == null)
			{
				LogUtils.warning("No products");
				request.setAttribute("msg", "暂无商品");
				return "/admin/msg.jsp";
			}
			request.setAttribute("list", products);
			return "/admin/product/list.jsp";
		} catch (SQLException e) {
			LogUtils.error("find all product faild");
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常~~查找商品失败");
			return "/admin/msg.jsp";
		}
	}
}
