package com.robin.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.bean.PageBean;
import com.robin.service.OrderService;
import com.robin.service.impl.OrderServiceImpl;
import com.robin.utils.JsonUtil;
import com.robin.utils.LogUtils;

/**
 * Servlet implementation class AdminOrderServlet
 */
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	public String updateState(HttpServletRequest request,HttpServletResponse response) {
		try {
			String oid = request.getParameter("oid");
			String state = request.getParameter("state");
			
			OrderService orderService = new OrderServiceImpl();
			Order order = orderService.findOrderByOid(oid);
			order.setState(Integer.parseInt(state));
			
			orderService.updateOrder(order);
			
			response.sendRedirect(request.getContextPath()+"/adminOrder?method=findByState&curPage=1&state="+state);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.error("update order state faild!");
			request.setAttribute("msg", "服务器异常!发货失败!");
			return "/admin/msg.jsp";
		}
	}

	public String findByState(HttpServletRequest request,HttpServletResponse response) {
		try {
			// 1.获取state/curPage
			String curPage = request.getParameter("curPage");
			String state = request.getParameter("state");
			
			// 2.获取PageBean
			OrderService orderService = new OrderServiceImpl();
			PageBean<Order> page = orderService.findOrderByState(state,Integer.parseInt(curPage));
			
			// 3.请求转发到list.jsp
			request.setAttribute("page", page);
			request.setAttribute("state", state);
			return "/admin/order/list.jsp";
		} catch (Exception e) {
			LogUtils.error("分页查找订单列表失败!服务器异常!");
			e.printStackTrace();
			return "/admin/msg.jsp";
		}
	}
	
	
	public String findItemByOid(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			// 1.获取Oid
			String oid = request.getParameter("oid");
			
			//
			OrderService orderService = new OrderServiceImpl();
			Order order = new Order();
			order.setOid(oid);
			List<OrderItem> items = orderService.findItemsByOrder(order);
			String itemsJson = JsonUtil.list2json(items);
			response.getWriter().print(itemsJson);
			LogUtils.debug(itemsJson);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常!订单详情查找失败");
			return "/admin/msg.jsp";
		}
	}

}
