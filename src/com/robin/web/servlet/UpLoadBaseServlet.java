package com.robin.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.robin.utils.LogUtils;

/**
 * Servlet implementation class UpLoadBaseServlet
 */
public class UpLoadBaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 0.解决响应乱码问题
			//response.setContentType("text/html;charset=utf-8");
			
			// 1.获取方法名
			//String method_name = request.getParameter("method");
			// 1.1 获取磁盘文件项工厂
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			
			// 1.2 创建核心上传对象
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			
			// 1.3 解析请求,获取所有上传组件List<FileItem>
			 List<FileItem> fileItems = servletFileUpload.parseRequest(request);
			
			// 1.4 遍历List,获取每一个FileItem
			 String method_name = null;
			 for (FileItem fileItem : fileItems) {
				if(fileItem.isFormField())//普通field
				{
					String fieldName = fileItem.getFieldName();
					LogUtils.debug("fieldName:"+fieldName);
					if(fieldName.equals("method"))
					{
						method_name = fileItem.getString("utf-8");
					}
				}
			}
			 if(method_name != null)
			 {
				 callMethod(method_name,fileItems,request, response);
			 }
		} catch (Exception e) {
			LogUtils.error("BaseServlet exception..");
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常!");
			request.getRequestDispatcher("/admin/msg.jsp").forward(request, response);
		}
	}
	
	
	public boolean callMethod(String method_name,List<FileItem> fileItems,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		try {
			LogUtils.info(this.getClass().getName()+":"+method_name);
			// 2.获取子类的字节码文件对象
			Class clazz = this.getClass();
			Method method = null;
			if(clazz != null)
			{
				// 3.找到这个子类的特定方法
				if(method_name != null)
				{
					method = clazz.getMethod(method_name,List.class,HttpServletRequest.class,HttpServletResponse.class);
				}
				else
				{
					LogUtils.error("method = null!Redirect to index.jsp");
					response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
					return false;
				}
			}
			else
			{
				LogUtils.error("Not Found Class");
				return false;
			}
			// 4.通过反射,子类调用自己的方法,获取需要转发的路径
			String path = null;
			if(method != null)
			{
				path = (String)method.invoke(this,fileItems, request,response);
				LogUtils.debug(path);
			}
			// 5.如果方法有返回一个路径,就转发过去.否则由方法自行解决
			if(path != null)
			{
				LogUtils.debug("dispatcher:"+path);
				request.getRequestDispatcher(path).forward(request, response);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "服务器异常!");
			request.getRequestDispatcher("/admin/msg.jsp").forward(request, response);
			return false;
		}
	}

}
