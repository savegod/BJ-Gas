package com.splwg.cm.domain.print.conf;

import java.io.InputStream;
import java.util.Properties;
 
/**
 * put on classpath of settings.properties 1. DB_URL: db connection string 2.
 * DB_USER/DB_PWD: db credential 3. QUERY: db query str 4. TEMPLATE: format the
 * data using the template(currently using velocity) 5. JOB_INTERVAL: interval
 * pause for job scheduling 6.
 * 
 * 
 */

public class Settings {
	// margin for pring setting
	public static int LEFT_MARGIN; // default 10mm

	public static int TOP_MARGIN;

	public static String DB_URL;

	public static String DB_USR;

	public static String DB_PWD; // 359752

	// sql for db query
	public static String QUERY_SQL;

	// template name for result formatting
	// may use Velocity
	public static String RES_TEMPLATE;

	// interval for job schedule
	public static int JOB_INTERVAL;

	public static String DATA_PROVIDER;
	
	public static String PRINTER_NAME;
	
	public static double PAPER_SIZE_WIDTH;
	public static double PAPER_SIZE_HEIGHT;
	
	public static int PRINT_COPIES;
	
	public static String STATION_PHONE;
	
	public static int QRCODE_WIDTH;
	
	//保存临时QRBar图片的目录
	public static String QRBAR_IMG_FOLDER;
	
	//所属站
	public static String ACC_GRP_CD;
	
	static Properties pro = new Properties();
	static {
		try {
			InputStream in = Settings.class.getResourceAsStream("settings.properties");
			pro.load(in);
			DB_URL = pro.getProperty("DB_URL"); 
			DB_USR = pro.getProperty("DB_USER");
			DB_PWD = pro.getProperty("DB_PWD"); 
			 

			RES_TEMPLATE = pro.getProperty("TEMPLATE");
 
			LEFT_MARGIN = getIntValue("LEFT_MARGIN", 10);
			TOP_MARGIN = getIntValue("TO_MARGIN", 10);

			DATA_PROVIDER = pro.getProperty("DATA_PROVIDER");
			
			PRINTER_NAME = pro.getProperty("PRINTER_NAME");
			
			double scaleW = 2.83;
			double scaleH = 2.83; 
			PAPER_SIZE_WIDTH = getFloatValue("PAPER_SIZE_WIDTH",80) /180*72;//* scaleW;
			PAPER_SIZE_HEIGHT = getFloatValue("PAPER_SIZE_HEIGHT",80) * scaleH;
			
			PRINT_COPIES = getIntValue("PRINT_COPIES",1);
			
			STATION_PHONE = pro.getProperty("PHONE_NUMBER", "88888888");
			
			QRCODE_WIDTH = getIntValue("QR_CODE_WIDTH", 90);
			
			QRBAR_IMG_FOLDER = pro.getProperty("QR_BAR_IMG_FOLDER","c:\\temp");
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	private static int getIntValue(String key, int defaultVal) {
		return Integer.parseInt(pro.getProperty(key,String.valueOf(defaultVal)));
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	private static double getFloatValue(String key, float defaultVal) {
		return Double.parseDouble(pro.getProperty(key,String.valueOf(defaultVal)));
	}
}
