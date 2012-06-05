package com.splwg.cm.domain.print.base;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;

import com.splwg.cm.domain.print.DataHandlerException;
import com.splwg.cm.domain.print.trans.BUSTrans;
import com.splwg.cm.domain.print.trans.RESTrans;

public class AbstractDataHandler implements DataHandler {
	protected Map<String, Object> attrs = new HashMap<String, Object>();
	protected DataWriter dataWriter;
	
	public void setDataWriter(DataWriter wrt){
		this.dataWriter = wrt;
	}
	
	public String invoke(String doc) throws DataHandlerException {
		throw new DataHandlerException("Not Implemented!");
	}

	public void setAttribute(String attrName, Object attrValue) {
		this.attrs.put(attrName, attrValue);
	}

	public String  invoke(Document doc) throws DataHandlerException {
		throw new DataHandlerException("Not Implemented!");
	}

	/**
	 * 根据模板类型获得转换处理器 (BUS/RES/...)
	 * @param template
	 * @return
	 */
	protected CMTransformer getTransformer(String template){
		if("BUS".equalsIgnoreCase(template)){
			return new BUSTrans();
		}
		else{
			return new RESTrans();
		}
	}
}
