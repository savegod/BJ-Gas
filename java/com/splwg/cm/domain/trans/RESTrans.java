package com.splwg.cm.domain.print.trans;

import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import com.splwg.cm.domain.print.DataHandlerException;
import com.splwg.cm.domain.print.base.BaseTransformer;

/**
 * 民用及催费(暂定)转换器
 * 直接处理转换
 * @author YANDLI
 *
 */
public class RESTrans extends BaseTransformer {
	public RESTrans(){
		super("RES");
	}
	
 	public String transform(Source data) throws DataHandlerException { 
		StringWriter wrt = new StringWriter();
		StreamResult out = new StreamResult(wrt);
 		try {
			trans.transform(data, out);
			System.out.println(wrt.toString());
			return (wrt.toString());			
		} catch (TransformerConfigurationException e) {
			log.error("构造转换器异常！", e);
			throw new DataHandlerException(e.getMessage());
		} catch (TransformerException e) {
			log.error("转换xml到HTML异常", e);
			throw new DataHandlerException(e.getMessage());
		} catch (Exception e) { 
			log.error("转换异常",e); 
			throw new DataHandlerException(e.getMessage());
		} 
	}

}
