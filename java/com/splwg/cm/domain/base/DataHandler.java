package com.splwg.cm.domain.print.base;

import org.dom4j.Document;

import com.splwg.cm.domain.print.DataHandlerException;

public interface DataHandler {
	/**
	 * sub-class should provide means to process the returned xml document
	 * 
	 * @param doc
	 */
	public String invoke(Document doc) throws DataHandlerException;

	/**
	 * 字符串形式的xml文档
	 * 
	 * @param doc
	 */
	public String  invoke(String doc) throws DataHandlerException;

	/**
	 * 设置特定属性
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	public void setAttribute(String attrName, Object attrValue);

	/**
	 * 数据输出
	 * @param wrt
	 */
	public void setDataWriter(DataWriter wrt);
}
