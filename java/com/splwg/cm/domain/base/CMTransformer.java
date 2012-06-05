package com.splwg.cm.domain.print.base;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;

import com.splwg.cm.domain.print.DataHandlerException;

public interface CMTransformer {

	public static TransformerFactory transFac = TransformerFactory.newInstance(); 
	public String transform(Source data) throws DataHandlerException;
	public void addParameter(String key, String val);
}
