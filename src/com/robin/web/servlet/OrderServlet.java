package com.robin.web.servlet;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.robin.bean.Cart;
import com.robin.bean.CartItem;
import com.robin.bean.Order;
import com.robin.bean.OrderItem;
import com.robin.bean.PageBean;
import com.robin.bean.User;
import com.robin.constant.Constant;
import com.robin.service.OrderService;
import com.robin.service.impl.OrderServiceImpl;
import com.robin.utils.PaymentUtil;
import com.robin.utils.UUIDUtils;

/**
 * 处理订单
 */
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	

	public String findOrderByOid(HttpServletRequest request,HttpServletResponse response) {
		try {
			String oid = request.getParameter("oid");

			OrderService orderService = new OrderServiceImpl();
			Order order = orderService.findOrderByOid(oid);
			if(order != null)
			{
				request.getSession().setAttribute("order", order);
				response.sendRedirect(request.getContextPath()+"/jsp/order_info.jsp");
				return null;
			}
			else {
				request.setAttribute("msg", "找不到订单!");
				return "/jsp/msg.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常");
			return "/jsp/msg.jsp";
		}
	}
	
	public String findOrderInPage(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			// 客户端需要
			// order:oid/state(状态)/total(总额)
			// orderitem:product(商品)/count(商品数量)/subtotal(小结)
			// product:pid/pname/price
			// 1.通过session获取用户uid
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if(user == null)
			{
				request.setAttribute("msg", "请先登录");
				return "/jsp/msg.jsp";
			}
			String curPage = request.getParameter("curPage");
			// 2.根据uid,找出order table里面符合条件的订单(oid)
			OrderService orderService = new OrderServiceImpl();
			PageBean<Order> pageBean = new PageBean<>();

			//orders = orderService.findOrdersInPages(user,Integer.parseInt(curPage));
			pageBean = orderService.findOrdersInPages(user,Integer.parseInt(curPage));
			
			// 5.OrderBean里面其实都有这些东西了,所以PageBean里面放的数据结构就是OrderBean就好了
			
			/*pageBean.setList(orders);
			pageBean.setCount(count);
			pageBean.setCurPage(curPage);
			pageBean.setCurSize(curSize);
			pageBean.setSumPage(sumPage);*/
			
			// 6.保存进session里面
			session.setAttribute("order_pageBean", pageBean);
			response.sendRedirect(request.getContextPath()+"/jsp/order_list.jsp");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "找不到订单");
			return "/jsp/msg.jsp";
		}
	}
	

	
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
			
			//添加完订单之后,要把购物车清空
			session.setAttribute("cart", null);
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
		//System.out.println("22222");
		//request.setAttribute("msg", "FUCK!");
		return "/jsp/msg.jsp";
	}
	
	//为订单付款订单
	public String payOrder(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String oid = request.getParameter("oid");
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		String address = request.getParameter("address");
		
		
		OrderService orderService = new OrderServiceImpl();
		Order order = orderService.findOrderByOid(oid);
		
		order.setAddress(address);
		order.setTelephone(telephone);
		order.setName(name);
		
		boolean ret = orderService.updateOrder(order);
		if(ret != true)
		{
			request.setAttribute("msg", "更新订单失败!");
			return "/jsp/msg.jsp";
		}
		
		

		// 组织发送支付公司需要哪些数据
		String pd_FrpId = request.getParameter("pd_FrpId");
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = oid;
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
	
		
		//发送给第三方
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		System.out.println("fuck!");
		System.out.println("url:"+sb.toString());
		
		response.sendRedirect(sb.toString());
		return null;
	}
	
	public String yeePaycallback(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		// 身份校验 --- 判断是不是支付公司通知你
		String hmac = request.getParameter("hmac");
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");

		// 自己对上面数据进行加密 --- 比较支付公司发过来hamc
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 响应数据有效
			if (r9_BType.equals("1")) {
				// 浏览器重定向
				System.out.println("111");
				request.setAttribute("msg", "您的订单号为:"+r6_Order+",金额为:"+r3_Amt+"已经支付成功,等待发货~~");
				
			} else if (r9_BType.equals("2")) {
				// 服务器点对点 --- 支付公司通知你
				System.out.println("付款成功！222");
				// 修改订单状态 为已付款
				// 回复支付公司
				response.getWriter().print("success");
			}
			
			//修改订单状态
			//OrderService s=(OrderService) BeanFactory.getBean("OrderService");
			//Order order = s.getOrderByOid(r6_Order);
			//s.updateOrder(order);
			OrderService orderService = new OrderServiceImpl();
			Order order = orderService.findOrderByOid(r6_Order);
			order.setState(1);
			orderService.updateOrder(order);
		} else {
			// 数据无效
			System.out.println("数据被篡改！");
		}
		return "/jsp/msg.jsp";
	}
	

}
