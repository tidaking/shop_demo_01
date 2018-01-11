package com.robin.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.robin.bean.User;
import com.robin.constant.Constant;
import com.robin.service.UserService;
import com.robin.service.impl.UserServiceImpl;
import com.robin.utils.UUIDUtils;
import com.sun.xml.internal.ws.wsdl.writer.document.Service;

/**
 * Servlet implementation class UserServlet
 */

public class UserServlet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	
	public String loginUI(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("[UserServlet]login UI");
		return "/jsp/login.jsp";	
	}
	public String registUI(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("[UserServlet]regist UI");
		return "/jsp/register.jsp";	
	}



	// 登录 
	public String login(HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println("[UserServlet]login");

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String vcode = request.getParameter("vcode");
		String auto_login = request.getParameter("auto_login");
		String rem = request.getParameter("rem");

		UserService service = new UserServiceImpl();
		User user = null;
		try {
			user = service.login(username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//TODO:校验验证码
		// 1.检查用户名和密码是否匹配
		if(user != null)
		{
			// 2.检查该用户是否已激活
			if(user.getState() == Constant.USER_ACTIVED)
			{
				// 3.检查是否记住用户名
				Cookie username_cookie = new Cookie("username", username);
				if(rem != null)
				{
					username_cookie.setMaxAge(7*24*60*60);
					username_cookie.setPath(request.getContextPath());
				}
				else
				{
					username_cookie.setMaxAge(0);
					username_cookie.setPath(request.getContextPath());
				}
				response.addCookie(username_cookie);
				// 4.检查是否自动登录(保留)

				// 5.保存用户登录状态
				HttpSession session = request.getSession();
				session.setAttribute("user", user);

				// 6.跳转到首页
				response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
				return null;
			}
			else
			{
				//没有激活, 给用户提示
				request.setAttribute("msg", "您还没有激活,请先登录邮箱激活....");
				return "/jsp/msg.jsp";
			}
		}
		else//匹配失败,需要提醒客户失败信息
		{
			//请求转发
			request.setAttribute("msg", "登录失败!用户名跟密码不匹配");
			return "/jsp/login.jsp";
		}
	}

	public String logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		// 重定向到主页
		response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
		return null;
	}

	public String regist(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		String password = request.getParameter("password");
		String re_password = request.getParameter("re_password");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String birthday = request.getParameter("birthday");
		String vcode = request.getParameter("vcode");


		String form_commit_id = request.getParameter("form_commit_id");
		String session_commit_id = (String)request.getSession().getAttribute("session_commit_id");

		System.out.println("form_commit_id:"+form_commit_id);
		System.out.println("session_commit_id:"+session_commit_id);
		boolean valid_commit_flag = false;
		if(session_commit_id != null )
		{
			valid_commit_flag = session_commit_id.equals(form_commit_id);
		}

		if(valid_commit_flag == true)//说明是不是重复提交
		{
			System.out.println("[UserServlet][regist]:不是重复提交");
		}
		else {
			System.out.println("[UserServlet][regist]:重复提交");
		}


		if(valid_commit_flag == true)
		{
			request.getSession().removeAttribute("session_commit_id");
			Map<String, String[]> params = request.getParameterMap();
			User user = new User();
			BeanUtils.populate(user, params);

			System.out.println("[UserServlet][regist]:user:"+user);

			//TODO:校验验证码

			//TODO:校验是否有重复的username

			//校验username/password/email是否为空
			if(User.checkUserVaild(user) != true){
				request.setAttribute("msg", "注册失败!用户名/密码/email地址/姓名/生日不能为空!");
				return "/jsp/register.jsp";
			}

			//校验两次密码是否一样
			if(password.equals(re_password) != true)
			{
				request.setAttribute("msg", "注册失败!两次输入的密码不一致!");
				return "/jsp/register.jsp";
			}
			user.setUid(UUIDUtils.getId());
			user.setCode(UUIDUtils.getCode());
			UserService service = new UserServiceImpl();
			boolean ret = false;
			try {
				ret = service.regist(user);
			} catch (SQLException | MessagingException e) {
				e.printStackTrace();
				request.setAttribute("msg", "服务器异常");
				return "/jsp/msg.jsp";
			}

			if(ret)
			{
				//注册成功
				request.setAttribute("msg", "注册成功!赶快去邮箱激活吧~~~");
				return "/jsp/msg.jsp";
			}
			else {
				//注册失败
				request.setAttribute("msg", "注册失败!请重新注册!");
				return "/jsp/msg.jsp";
			}
		}
		else
		{
			request.setAttribute("msg", "上一次操作已完结,请勿直接刷新页面");
			return "/jsp/msg.jsp";
		}

	}

	public String active(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String code = request.getParameter("code");
		
		UserService service = new UserServiceImpl();
		boolean ret;
		try {
			ret = service.active(code);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常!");
			return "/jsp/msg.jsp";
		}

		if(ret != false)
		{
			//激活成功
			response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
			return null;
		}
		else
		{
			//激活失败
			request.setAttribute("msg", "激活失败!");
			return "/jsp/msg.jsp";
		}
	}
}