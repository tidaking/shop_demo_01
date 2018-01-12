package com.robin.web.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robin.utils.JsonUtil;

public class CheckReCommitServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String checkReSubmit(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		System.out.println("checkReSubmit!");
		
		double random = Math.random();
		String session_commit_id = String.valueOf(random);
		request.getSession().setAttribute("session_commit_id", session_commit_id);
		//response.getWriter().write(session_commit_id);

		String JsonCommitId = "{\"commit_id\":\""+session_commit_id+"\"}";

		System.out.println("session_commit_id:"+session_commit_id);
		System.out.println("JsonCommitId:"+JsonCommitId);
		response.getWriter().write(JsonCommitId);
		return null;
	}
}
