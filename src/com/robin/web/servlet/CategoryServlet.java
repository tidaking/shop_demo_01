package com.robin.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.bean.Category;
import com.robin.service.CategoryService;
import com.robin.service.impl.CategoryServiceImpl;
import com.robin.utils.JsonUtil;

import net.sf.json.JSONArray;

/**
 * 获取商品类别
 */
public class CategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String getCategory(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		// 1.从service层获取所有的CategoryBean(List)
		List<Category> categoryBeanList = null;
		CategoryService service = new CategoryServiceImpl();
		try {
			categoryBeanList = service.getAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("msg", "查询商品类别出错~~");
			return "/jsp/msg.jsp";
		}

		if(categoryBeanList != null)
		{
			// 2.将List封装成JSON
			String categoryJson = JsonUtil.list2json(categoryBeanList);

			// 3.将JSON送回给前端页面
			response.getWriter().print(categoryJson);
		}
		else
		{
			System.out.println("[CategoryServlet]:Get no category!");
			response.getWriter().print("");
		}

		return null;
	}
}




