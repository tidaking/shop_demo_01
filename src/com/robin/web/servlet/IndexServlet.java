package com.robin.web.servlet;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.bean.Product;
import com.robin.service.ProductService;
import com.robin.service.impl.ProductServiceImpl;

/**
 */
public class IndexServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	public String gotoIndex(HttpServletRequest request,HttpServletResponse response) {
		// 1.向service层请求获得最新商品List
		List<Product> latest_products = null;
		ProductService service = new ProductServiceImpl();
		try {
			latest_products = service.getLatestProduct();
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常~无法获取到最新商品");
			return "/jsp/msg.jsp";
		}

		// 1.向service层请求获得热门商品List
		List<Product> hot_products = null;
		service = new ProductServiceImpl();
		try {
			hot_products = service.getHotestProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常~无法获取到热门商品");
			return "/jsp/msg.jsp";
		}

		request.setAttribute("new_list", latest_products);
		request.setAttribute("hot_list", hot_products);

		return "/jsp/index.jsp";
	}
}
