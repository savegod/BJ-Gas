package com.splwg.cm.domain.print.handler;

import javax.servlet.http.HttpServletResponse;

import com.splwg.cm.domain.print.base.DataWriter;

public class ServletResponseWriter implements DataWriter{
	HttpServletResponse res;
	public void setServletResponse(HttpServletResponse r){
		this.res = r;
	}
	
	public boolean write(String content) { 
		
		return true;
	}

}
