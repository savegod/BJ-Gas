package com.splwg.cm.domain.print;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CmXmlTrans {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static String replaeDueDate(String xml) {
		String startTag = "dueDate";
		int start = xml.indexOf(startTag);
		if (start != -1) {
			int end = xml.indexOf(startTag, start + startTag.length());
			
			String val = xml.substring(start + startTag.length() + 1, end - 2);
			int days = 0;
			try{
				days = Integer.parseInt(val); 
			}catch(Exception ex){
				//如果传入的值异常，直接返回原值
				return xml;
			}
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DAY_OF_MONTH, days);
			String dueDate = sdf.format(ca.getTime());
			return xml.substring(0, start + startTag.length() + 1) + dueDate + xml.substring(end - 2);
		} else {
			return xml;
		}
	}
	
	public static void main(String[] args) {
		String xml = "<root><a>测试</a><dueDate>10</dueDate><b>asldfas</b></root>";
		xml = replaeDueDate(xml);
		System.out.println(xml);
	}
}
