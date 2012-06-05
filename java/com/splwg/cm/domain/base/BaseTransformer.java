package com.splwg.cm.domain.print.base;

import java.io.InputStream;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.splwg.cm.domain.print.DataHandlerException;
import com.splwg.cm.domain.print.handler.XsltHandler;

public abstract class BaseTransformer implements CMTransformer {
	public static final Log log = LogFactory.getLog(BaseTransformer.class);
	
	protected Source xslSource = null;// 每次都必须构造一下stream
	protected Transformer trans;  
	public BaseTransformer() {
	}

	public BaseTransformer(String template) {
		InputStream xslStream = XsltHandler.class.getResourceAsStream(template + ".xsl");
		xslSource = new StreamSource(xslStream);
		try {
			trans = transFac.newTransformer(xslSource);
			/**
			 * 通用的参数
			 */
			trans.setParameter("curDate", new Date()); //打印日期
			trans.setParameter("phone", "88888888");//Settings.STATION_PHONE); //所属站电话
			trans.setOutputProperty(OutputKeys.INDENT, "true");
 		} catch (TransformerConfigurationException e) {
 			e.printStackTrace();
		}
 	}

	public void addParameter(String key, String val) {
		this.trans.setParameter(key, val); 
	}
	public abstract String transform(Source data) throws DataHandlerException;

}
