package com.robin.web.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckReCommitServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String checkReCommit(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		System.out.println("checkReCommit!");
		
		double random = Math.random();
		String session_commit_id = String.valueOf(random);
		request.getSession().setAttribute("session_commit_id", session_commit_id);
		response.getWriter().println(session_commit_id);
		
		System.out.println("session_commit_id:"+session_commit_id);
		
		return null;
		
	}
	
	

}
