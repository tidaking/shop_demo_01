package com.robin.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Request;

import com.robin.bean.PageBean;
import com.robin.bean.Product;
import com.robin.service.ProductService;
import com.robin.service.impl.ProductServiceImpl;
import com.robin.utils.JsonUtil;

/**
 * 商品模块
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	public String findByCidInPages(HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("[ProductServlet]:findByCidInPages");
		// 1.从请求参数中获取cid/curPage
		String cid = request.getParameter("cid");
		String curPage = request.getParameter("curPage");
		
		// 2.向service层根据cid/curPage获取一个PageBean
		ProductService service = new ProductServiceImpl();
		PageBean<Product> page = null;
		try {
			page = service.findByCidInPages(cid,Integer.parseInt(curPage));
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "分页查询失败!");
			return "/jsp/msg.jsp";
		}
		

		if(page != null)
		{
			// 3.存进request域中
			request.setAttribute("page", page);
			
			// 4.请求转发到product_list.jsp中
			return "/jsp/product_list.jsp";
		}
		else
		{
			request.setAttribute("msg", "没有这类商品哦");
			return "/jsp/msg.jsp";
		}
	}
	public String findByPid(HttpServletRequest request,HttpServletResponse response)
	{
		// 0.从请求参数中获取pid
		String pid = request.getParameter("pid");
		
		// 1.向service层根据pid获取商品
		ProductService service = new ProductServiceImpl();
		Product product = null;
		try {
			product = service.findByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "查询指定商品失败~");
			return "/jsp/msg.jsp";
		}
		if(product != null)
		{
			// 2.将ProductBean保存到request中
			request.setAttribute("product", product);
			// 3.请求转发到/jsp/prodruct_info.jsp中
			return "/jsp/product_info.jsp";
		}
		else
		{
			request.setAttribute("msg", "查询指定商品失败~");
			return "/jsp/msg.jsp";
		}
	}

	public String findLatestProduct(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		// 1.向service层请求获得最新商品List
		List<Product> products = null;
		ProductService service = new ProductServiceImpl();
		try {
			products = service.getLatestProduct();
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常~无法获取到最新商品");
			return "/jsp/msg.jsp";
		}
		request.setAttribute("new_list", products);
		return "/jsp/index.jsp";
	}

	public String findHotestProduct(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		// 1.向service层请求获得热门商品List
		List<Product> products = null;
		ProductService service = new ProductServiceImpl();
		try {
			products = service.getHotestProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常~无法获取到热门商品");
			return "/jsp/msg.jsp";
		}
		request.setAttribute("hot_list", products);
		return "/jsp/index.jsp";
	}
}
