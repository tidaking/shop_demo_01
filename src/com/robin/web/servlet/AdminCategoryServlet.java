package com.robin.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.bean.Category;
import com.robin.service.CategoryService;
import com.robin.service.impl.CategoryServiceImpl;
import com.robin.utils.LogUtils;

/**
 * Servlet implementation class AdminCategoryServlet
 */
public class AdminCategoryServlet extends BaseServlet
{
	private static final long serialVersionUID = -8008181460121623015L;
	
	public String addCategoryUI(HttpServletRequest request,HttpServletResponse response) {
		return "/admin/category/add.jsp";
	}
	
	public String addCategory(HttpServletRequest request,HttpServletResponse response) {
		try {
			String cname = request.getParameter("cname");
			
			CategoryService categoryService = new CategoryServiceImpl();
			boolean ret = categoryService.addCategory(cname);
			if(ret != true)
			{
				LogUtils.error("add Category failed!");
				request.setAttribute("msg", "服务器异常~~新增商品类别出错");
				return "/admin/msg.jsp";
			}
			
			List<Category> categories = categoryService.getAllCategory();
			request.setAttribute("list", categories);
			return "/admin/category/list.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.error("add Category failed!");
			request.setAttribute("msg", "服务器异常~~新增商品类别出错");
			return "/admin/msg.jsp";
		}
	}
	
	
	public String findAllCategory(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			CategoryService categoryService = new CategoryServiceImpl();

			List<Category> categories = categoryService.getAllCategory();
			request.setAttribute("list", categories);
			return "/admin/category/list.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.error("find all category failed...");
			request.setAttribute("msg", "服务器异常~~查询商品类别出错");
			return "/admin/msg.jsp";
		}
	}
	
	
}