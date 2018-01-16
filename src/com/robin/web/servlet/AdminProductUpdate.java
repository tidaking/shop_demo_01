package com.robin.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.robin.bean.Category;
import com.robin.bean.Product;
import com.robin.service.CategoryService;
import com.robin.service.ProductService;
import com.robin.service.impl.CategoryServiceImpl;
import com.robin.service.impl.ProductServiceImpl;
import com.robin.utils.LogUtils;
import com.robin.utils.UUIDUtils;
import com.robin.utils.UploadUtils;

/**
 * Servlet implementation class AdminProductUpdate
 */
public class AdminProductUpdate extends UpLoadBaseServlet {
	private static final long serialVersionUID = 1L;
	

	public String addProduct(List<FileItem> fileItems, HttpServletRequest request,HttpServletResponse response) {
		try {
			// 1.1 获取磁盘文件项工厂
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			
			// 1.2 创建核心上传对象
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			
			// 1.3 解析请求,获取所有上传组件List<FileItem>
			//List<FileItem> fileItems = servletFileUpload.parseRequest(request);
			
			// 1.4 遍历List,获取每一个FileItem
			Map<String, String> map = new HashMap<>();
			
			ProductService productService = new ProductServiceImpl();
			CategoryService categoryService = new CategoryServiceImpl();
			Product product = new Product();
			for (FileItem fileItem : fileItems) {
				if(fileItem.isFormField())//普通field
				{
					String fieldName = fileItem.getFieldName();
					String value = fileItem.getString("utf-8");
					map.put(fieldName, value);
					LogUtils.debug("fieldName:"+fieldName);
					LogUtils.debug("map get:"+map.get(fieldName));
				}
				else//文件
				{
					// 1.获取文件名
					String fileName = fileItem.getName();
					// 2.将文件名用UUID代替
					String realName = UploadUtils.getRealName(fileName);
					String uuidName = UploadUtils.getUUIDName(realName);
					// 3.设置负载均衡随机文件夹
					String dir = UploadUtils.getDir();
					
					// 4.保存文件
					InputStream inputStream = fileItem.getInputStream();
					String realPath = request.getServletContext().getRealPath("/products");
					
					File fileDir = new File(realPath, dir);
					if(!fileDir.exists()){
						fileDir.mkdirs();
					}
					product.setPimage("/products"+dir+"/"+uuidName);
					
					FileOutputStream outputStream = new FileOutputStream(new File(fileDir, uuidName));
					
					IOUtils.copy(inputStream, outputStream);
					
					// 删除临时文件
					fileItem.delete();
					
					IOUtils.closeQuietly(inputStream);
					IOUtils.closeQuietly(outputStream);
				}
			}
			
			

			String cid = map.get("cid");
			if(cid == null)
			{
				LogUtils.error("add product faild");
				request.setAttribute("msg", "添加商品失败!没有对应类别!");
				return "/admin/msg.jsp";
			}
			Category category = categoryService.findCategoryByCid(cid);
			BeanUtils.populate(product, map);
			product.setCategory(category);
			product.setPid(UUIDUtils.getId());
			product.setPdate(new Date());

			boolean ret = productService.addProduct(product);
			if(ret != true)
			{
				LogUtils.error("add product faild");
				request.setAttribute("msg", "添加商品失败!");
				return "/admin/msg.jsp";
			}

			LogUtils.debug("add product success");
			//return "/adminProduct?method=findAll";
			response.sendRedirect(request.getContextPath()+"/adminProduct?method=findAll");
			return null;
		} catch (Exception e) {
			LogUtils.error("add product exception");
			e.printStackTrace();

			request.setAttribute("msg", "添加商品失败!服务器异常!");
			return "/admin/msg.jsp";
		}
	}
}
