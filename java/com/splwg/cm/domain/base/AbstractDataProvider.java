package com.splwg.cm.domain.print.base;


public abstract class AbstractDataProvider implements DataProvider{
	protected DataHandler handler;
	
	public void setDataHandler(DataHandler handler) {
		this.handler = handler; 
	}
}
