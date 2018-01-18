package com.robin.web.filter;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.robin.utils.LogUtils;


/**
 * 统一编码
 * @author Administrator
 *
 */
public class EncodingFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		//1.强转
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) resp;
		
		//2.放行
		//Map<String, String[]> map = request.getParameterMap();
		//LogUtils.debug("map size:"+map.size());
		
//		for (String key : map.keySet()) {
//			for(String value:map.get(key))
//			{
//				//LogUtils.debug(key+":"+value);
//			}
//		}
		chain.doFilter(new MyRequest(request), response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
class MyRequest extends HttpServletRequestWrapper{
	private HttpServletRequest request;
	private boolean flag=true;
	
	
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.request=request;
		//LogUtils.debug(super.getClass().getName());
		//LogUtils.debug(this.getClass().getName());
		//LogUtils.debug("111 super uri"+super.getRequestURI());
		//LogUtils.debug("111 this uri"+this.getRequestURI());
	}
	
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		//HttpServletRequest My_Request = (HttpServletRequest)this;
		//HttpServletRequest this_Request = (HttpServletRequest)this.request;
		//LogUtils.debug("before My_Request uri:"+My_Request.getRequestURI());
		//LogUtils.debug("before this_Request uri:"+this_Request.getRequestURI());
		
		RequestDispatcher requestDispatcher = super.getRequestDispatcher(path);
		//LogUtils.debug("after My_Request uri:"+My_Request.getRequestURI());
		//LogUtils.debug("after this_Request uri:"+this_Request.getRequestURI());
		//LogUtils.debug("this:"+this.getRequest().getClass().getName());
		//LogUtils.debug("super:"+super.getRequest().getClass().getName());
		return requestDispatcher;
		
	}
	
	@Override
	public String getParameter(String name) {  
		//LogUtils.debug(name);
		//LogUtils.debug(super.getParameter(name));
		if(name==null || name.trim().length()==0){
			return null;
		}
		//LogUtils.debug(super.getParameterValues(name)[0]);
		String[] values = getParameterValues(name);
		if(values==null || values.length==0){
			return null;
		}
		
		return values[0];
	}
	
	@Override
	/**
	 * hobby=[eat,drink]
	 */
	public String[] getParameterValues(String name) {
		if(name==null || name.trim().length()==0){
			return null;
		}
		Map<String, String[]> map = getParameterMap();
		if(map==null || map.size()==0){
			return null;
		}
		
		return map.get(name);
	}
	
	@Override
	/**
	 * map{ username=[tom],password=[123],hobby=[eat,drink]}
	 */
	public Map<String,String[]> getParameterMap() {  
		
		/**
		 * 首先判断请求方式
		 * 若为post  request.setchar...(utf-8)
		 * 若为get 将map中的值遍历编码就可以了
		 */
		//String method = request.getMethod();
		String method = super.getMethod();
		if("post".equalsIgnoreCase(method))
		{
			try 
			{
				LogUtils.debug("post");
				//request.setCharacterEncoding("utf-8");
				//return request.getParameterMap();
				//String characterEncoding = super.getCharacterEncoding();
				//LogUtils.debug(characterEncoding);
				super.setCharacterEncoding("utf-8");
				return super.getParameterMap();
			} 
			catch (UnsupportedEncodingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("get".equalsIgnoreCase(method))
		{
			//LogUtils.debug("get 111:"+request);
			//Map<String,String[]> map = request.getParameterMap();
			//int size = map.entrySet().size();
			//System.out.println("entry size:"+size);
			Map<String,String[]> map = super.getParameterMap();
			//Map<String,String[]> super_map = super.getParameterMap();
			//LogUtils.debug("super request:"+super.getRequest().toString());
			//LogUtils.debug("this.request:"+this.request.toString());
//			if(map == null)
//			{
//				//LogUtils.debug("null");
//			}
			//LogUtils.debug("map size:"+map.size()+"");
			//LogUtils.debug("super map size:"+super_map.size()+"");
//			for (String key : map.keySet()) {
//				for(String value:map.get(key))
//				{
//					//LogUtils.debug("map:"+key+":"+value);
//				}
//			}
			
//			for (String key : super_map.keySet()) {
//				for(String value:super_map.get(key))
//				{
//					//LogUtils.debug("super_map:"+key+":"+value);
//				}
//			}
			//LogUtils.debug("request try get method:"+request.getParameter("method"));
			//LogUtils.debug("super try get method:"+super.getParameter("method"));
			//LogUtils.debug("this try get method:"+this.getParameter("method"));
			//LogUtils.debug("super:"+super.getClass().getName());
			//LogUtils.debug("this:"+this.getClass().getName());
			//LogUtils.debug("request uri:"+request.getRequestURI());
			//LogUtils.debug("super uri:"+super.getRequestURI());
			
			if(flag)
			{
				for (String key:map.keySet()) 
				{
					String[] arr = map.get(key);
					//继续遍历数组
					for(int i=0;i<arr.length;i++)
					{
						//编码
						try 
						{
							arr[i]=new String(arr[i].getBytes("iso8859-1"),"utf-8");
						} 
						catch (UnsupportedEncodingException e) 
						{
							e.printStackTrace();
						}
					}
				}
				flag=false;
			}
			//需要遍历map 修改value的每一个数据的编码
			//LogUtils.debug("return  111");
//			for (String key : map.keySet()) {
//				for(String value:map.get(key))
//				{
//					//LogUtils.debug(key+":"+value);
//				}
//			}
			return map;
		}
		//LogUtils.debug("return");
		return super.getParameterMap();
	}
	
}