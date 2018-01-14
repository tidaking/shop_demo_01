package com.robin.web.servlet;


import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.robin.bean.Cart;
import com.robin.bean.CartItem;
import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.bean.User;
import com.robin.constant.Constant;
import com.robin.service.OrderService;
import com.robin.service.impl.OrderServiceImpl;
import com.robin.utils.ConnectionManager;
import com.robin.utils.UUIDUtils;

/**
 * 处理订单
 */
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	// 将购物车里面的CartItem放进订单里
	public String newOrder(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			// 1.从Session中获取购物车对象cart
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			if(cart == null)
			{
				request.setAttribute("msg", "购物车是空的哦~~~并没有东西可以提交订单呢~");
				return "/jsp/msg.jsp";
			}
			
			User user = (User) session.getAttribute("user");
			if(user == null)
			{
				request.setAttribute("msg", "请先登录再提交订单呢~~");
				return "/jsp/msg.jsp";
			}
			
			// 2.创建Order对象
			Order order = new Order();
			
			// 2.从cart中获取总金额,并设进order中,然后存进数据库,这里要用事务
			order.setOid(UUIDUtils.getId());
			
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//String date = format.format(new java.util.Date());
			//java.util.Date date = new java.util.Date();
			//Timestamp timestamp = new Timestamp(0);

			order.setOrdertime(new Date());
			order.setState(Constant.ORDER_UNPAYED);
			order.setTotal(cart.getTotal());
			order.setUser(user);
	
			List<OrderItem> orderItems = new ArrayList<>();
			
			// 3.从cart中获取CartItemMap,遍历CartItemMap,将cartitem存进数据库order_item表中
			Map<String, CartItem> cartItemMap = cart.getCartItemMap();
			Collection<CartItem> cartItems = cartItemMap.values();
			for (CartItem cartItem : cartItems) {
				OrderItem orderItem = new OrderItem();
				orderItem.setItemid(UUIDUtils.getId());
				orderItem.setCount(cartItem.getCount());
				orderItem.setProduct(cartItem.getProduct());
				orderItem.setSubtotal(cartItem.getSubtotal());
				orderItem.setOrder(order);
				//将orderItem存进List
				orderItems.add(orderItem);
			}
			
			order.setOrderItems(orderItems);
			
			boolean ret = false;
			OrderService orderService = new OrderServiceImpl();
			ret = orderService.newOrder(order);
			if(ret != true)
			{
				request.setAttribute("msg", "创建订单失败!");
				return "/jsp/msg.jsp";
			}
			session.setAttribute("order", order);
			response.sendRedirect(request.getContextPath()+"/jsp/order_info.jsp");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "创建订单失败!");
			return "/jsp/msg.jsp";
		}
		
	}
	
	//删除订单
	public String delOrder(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	
	//为订单付款订单
	public String payOrder(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}

}
