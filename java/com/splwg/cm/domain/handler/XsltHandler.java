package com.splwg.cm.domain.print.handler;

import java.io.File;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.splwg.cm.domain.print.CmXmlTrans;
import com.splwg.cm.domain.print.DataHandlerException;
import com.splwg.cm.domain.print.base.AbstractDataHandler;
import com.splwg.cm.domain.print.base.CMTransformer;

public class XsltHandler extends AbstractDataHandler {
	Map<String, Source> xslCache = new ConcurrentHashMap<String, Source>();

	public static final Log log = LogFactory.getLog(XsltHandler.class);

	public String invoke(String dataXml) throws DataHandlerException {
		String template = (String) attrs.get("template");
		int idx = (Integer) attrs.get("index");
		//计算dueDate = 传入的dueDays + 当前日期
		dataXml = CmXmlTrans.replaeDueDate(dataXml);
		Source xmlSource = new StreamSource(new StringReader(dataXml));
		CMTransformer trans = getTransformer(template);
		return trans.transform(xmlSource);
	} 
	
	
	public static void main(String[] args) {
		// EnvironmentCheck ec = new EnvironmentCheck();
		// PrintWriter sendOutputTo = new PrintWriter(System.out, true);
		// ec.checkEnvironment(sendOutputTo);
		XsltHandler handler = new XsltHandler();
		handler.setAttribute("template", "RES");
		Document doc;
		try {
			SAXReader sr = new SAXReader();
			doc = sr.read(new File("d:\\data.xml"));
			handler.invoke(doc);
		} catch (DocumentException e) { 
		} catch (DataHandlerException e) {
			e.printStackTrace();
		}
	}
}
