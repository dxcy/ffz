package com.ue.auditmanage.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class encodingFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 1L;
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		arg0.setCharacterEncoding("utf-8");
		arg2.doFilter(arg0, arg1);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	@Override
	 public void destroy() {

	       System.out.println("encodingfilter关闭........");

	        

	    }

}
