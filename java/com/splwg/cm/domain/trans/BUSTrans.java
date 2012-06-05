package com.splwg.cm.domain.print.trans;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.splwg.cm.domain.print.DataHandlerException;
import com.splwg.cm.domain.print.base.BaseTransformer;

/**
 * 公服数据转换器
 * 需要处理分页，首页和其它页的模板不同
 *  
 * @author YANDLI
 *
 */
public class BUSTrans extends BaseTransformer{
	public BUSTrans(){
		super("BUS");
	}
	
	public String transform(Source data) throws DataHandlerException { 
		StringBuilder str = new StringBuilder();
		SAXReader reader = new SAXReader();
		StreamSource src = (StreamSource)data;
		
		StringWriter wrt = new StringWriter();
		StreamResult out = new StreamResult(wrt);
		try { 
			src.getReader().mark(1000);
			String tmp = doDataRead(src.getReader());
			src.getReader().reset();
  			Document doc = reader.read(new StringReader(tmp));
			
			XPath pathOfSp = doc.createXPath("/bill/billSegmentList/spList");
 			List spList = pathOfSp.selectNodes(doc.getRootElement());
			int sizeOfSp = spList.size();
			
			//每页只显示2个 BillSegment 信息
			//不需要分页
			if(sizeOfSp <= 2) {
				trans.setParameter("showAlgo", 1); //打印用气金额算法
			}
			//需要分页
			else{
				trans.setParameter("showAlgo", -1); //不打印用气金额算法
			} 
			trans.setParameter("showAdj", -1);
			trans.transform(data, out);
			str.append(wrt.toString());
			//前三条在第一页打印
			//第 2  to sizeOfSp
//			int pages = sizeOfSp / 2 - 1;
			
		}  catch (TransformerException e) { 
			e.printStackTrace();
		} 
		return str.toString();
	}

	private String doDataRead(Reader reader) throws IOException {
		char[] buf = new char[512];
		StringBuilder str = new StringBuilder();
		int n = 0;
		while ( (n = reader.read()) != -1)
			str.append((char)n);
		
		return str.toString();
	}

}
