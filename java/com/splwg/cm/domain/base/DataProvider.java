package com.splwg.cm.domain.print.base;



public interface DataProvider {
	public ThreadLocal<String> localBill = new ThreadLocal<String>();
	/**
	 * 设置数据处理实现类
	 * @param handler
	 */
	public void setDataHandler(DataHandler handler);
	
	/**
	 * 处理数据
	 */
	public void processData();
	
	/**
	 * 释放数据库连接
	 *
	 */
	public void releaseConn();
	
	/**
	 * 对billId的后续操作
	 * @param billId
	 * @return
	 */
	public boolean handleCallback(String billId);
}
