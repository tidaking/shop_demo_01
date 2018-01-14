package com.robin.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;

import com.robin.bean.Cart;
import com.robin.bean.CartItem;
import com.robin.bean.Product;
import com.robin.bean.User;
import com.robin.service.ProductService;
import com.robin.service.impl.ProductServiceImpl;

public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	// 添加到购物车
	public String addToCart(HttpServletRequest request,HttpServletResponse response) {
		try {
			System.out.println("[CartServlet]:addToCart");
			// 0.获取要添加到购物车里面商品pid和数目count
			String pid = request.getParameter("pid");
			String count = request.getParameter("count");
			
			// 1.获取登录状态,如果未登录,则不允许加入购物车
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if(user == null)
			{
				System.out.println("[CartServlet]:Not logined~Please login first");
				request.setAttribute("msg", "请先登录!");
				return "/jsp/msg.jsp";
			}
			
			// 1.获取购物车对象
			
			Cart cart  = (Cart) session.getAttribute("cart");
			if(cart == null)//能够获取到用户购物车
			{
				cart = new Cart();
				session.setAttribute("cart", cart);
			}
			// 2.根据pid,从数据库中查找商品,并封装成一个Product对象
			ProductService productService = new ProductServiceImpl();
			Product product = productService.findByPid(pid);
			if(product != null)
			{
				// 3.往购物车里添加商品
				CartItem cartItem = new CartItem();
				
				cartItem.setCount(Integer.parseInt(count));
				cartItem.setProduct(product);
				cart.addToCart(cartItem);
				session.setAttribute("cart", cart);
				
				response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
				return null;
			}
			else {
				request.setAttribute("msg", "添加失败!找不到对应商品哦!");
				return "/jsp/msg.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "添加商品到购物车失败~");
			return "/jsp/msg.jsp";
		}
		
	}
	
	// 从购物车中删除
	public String  removeFromCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println("[CartServlet]:removeFromCart");
		// 1.获取要删除商品的pid
		String pid = request.getParameter("pid");
		
		// 2.获取购物车对象
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");

		if(cart == null)
		{
			cart = new Cart();
			session.setAttribute("cart", cart);
		}

		// 3.调用cart方法,从购物车中删除该商品
		cart.removeFromCart(pid);
		
		
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	
	// 清空购物车
	public String clearCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println("[CartServlet]:clearCart");
		// 1.获取购物车对象cart
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		
		if(cart == null)
		{
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		
		cart.clearCart();
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
}
