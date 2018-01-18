package com.robin.web.servlet;


import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.utils.LogUtils;

/**
 * Servlet基类
 */
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 0.解决响应乱码问题
			response.setContentType("text/html;charset=utf-8");
			
			// 1.获取方法名
			String method_name = request.getParameter("method");
			LogUtils.info(this.getClass().getName()+":"+method_name);
			// 2.获取子类的字节码文件对象
			Class clazz = this.getClass();
			Method method = null;
			if(clazz != null)
			{
				// 3.找到这个子类的特定方法
				if(method_name != null)
				{
					method = clazz.getMethod(method_name,HttpServletRequest.class,HttpServletResponse.class);
				}
				else
				{
					LogUtils.error("method = null!Redirect to index.jsp");
					//LogUtils.error("method = null!Redirect to msg.jsp");
					response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
				}
			}
			else
			{
				LogUtils.error("Not Found Class");
			}
			// 4.通过反射,子类调用自己的方法,获取需要转发的路径
			String path = null;
			if(method != null)
			{
				path = (String)method.invoke(this, request,response);
			}
			// 5.如果方法有返回一个路径,就转发过去.否则由方法自行解决
			if(path != null)
			{
				request.getRequestDispatcher(path).forward(request, response);
			}
		} catch (Exception e) {
			LogUtils.error("BaseServlet exception..");
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常!");
			request.getRequestDispatcher("/jsp/msg.jsp").forward(request, response);
		}
	}
}