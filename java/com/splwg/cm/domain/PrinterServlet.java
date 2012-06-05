package com.splwg.cm.domain.print;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.splwg.base.api.service.RequestContext;
import com.splwg.base.support.context.ApplicationContext;
import com.splwg.base.support.context.ContextHolder;
import com.splwg.base.support.context.FrameworkSession;
import com.splwg.base.support.context.SessionHolder;
import com.splwg.cm.domain.print.base.DataHandler;
import com.splwg.cm.domain.print.base.DataWriter;
import com.splwg.cm.domain.print.conf.Settings;
import com.splwg.cm.domain.print.handler.ServletResponseWriter;
import com.splwg.cm.domain.print.handler.XsltHandler;

 
/**
 * 打印处理 Servlet
 * @author YANDLI
 *
 */
public class PrinterServlet extends HttpServlet{
	public static final Log log = LogFactory.getLog(PrinterServlet.class);
	
	String query = "select a.bill_id, cm_bill_item, a.bill_rte_type_cd,b.cm_qrcode barcode " + 
				 "from cm_bill_print a,  cm_bill_item b,   cm_mr_rte rte, cm_mr_rte_reader rteRdr, " + 
			         "ci_bill bill,  ci_acct_per ap, ci_per per " + 
			         "where a.bill_id = b.bill_id  and a.access_grp_cd = rte.access_grp_cd " +
			         "and a.mr_rte_cd = rte.mr_rte_cd and rte.mr_rte_cd = rteRdr.Mr_Rte_Cd " + 
			         "and rteRdr.cm_status = 'Y' and bill.bill_id = a.bill_id and bill.acct_id = ap.acct_id " + 
			         "and ap.per_id = per.per_id and a.bill_id in (@ID-LIST@) order by rte.mr_rte_cd,per.address1";
	
	DataHandler handler;
 
	
	public void init() throws ServletException {
 		super.init();
 		handler = new XsltHandler();
 		DataWriter writer = new ServletResponseWriter();
 		handler.setDataWriter(writer); 
	}
	  
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { 
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print("<html><body>");
 		String billIds = req.getParameter("billIds");
		if(billIds == null || billIds.length() < 1){
			res.getWriter().print("请选择要打印的数据!");
			res.flushBuffer();
			return;
		}
		
		query = query.replace("@ID-LIST@", billIds);
		
		RequestContext rCtx = new RequestContext();
		ApplicationContext aCtx = ContextHolder.getContext();
		FrameworkSession ss = new FrameworkSession(aCtx, rCtx);
		org.hibernate.Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = ss.getHibernateSession();
			//ugly code for getting the connection
			conn = session.connection();
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			int idx = 1;
			while(rs.next()){
				Clob itemClob = rs.getClob("cm_bill_item");
				String billType = rs.getString("bill_rte_type_cd").trim();
				String billId = rs.getString("bill_id");
				
				//从Blob读出QR图
				generateQRImg(rs.getBlob("barcode"), billId);
				 
				String xml = "";
				try {
					xml = readClob(itemClob);
				} catch (IOException e) {
					log.error("获取Clob对象异常：", e);  
				}
				handler.setAttribute("template", billType);
				handler.setAttribute("index", idx ++);
				try {
					String html  = handler.invoke(xml);
					res.getWriter().write(html);
					res.flushBuffer();
					/** 移除已打印数据 移动到CMPrintJobListener 中 **/
				} catch (DataHandlerException e) {
					log.error("处理BillItem数据异常：", e);  
 				}finally{ 
 				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally { 
			try {
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
			
			res.getWriter().print("</body></html>");
		}
	}
	
	/**
	 * 生成QR扫描图
	 * @param blob blob字段，保存QR图片
	 *  从blob字段读入字节流，输出到 c:\temp\qrbar.png
	 */
	private void generateQRImg(Blob blob, String billId) {
		String _folder = "c:\\temp";
		File temp = new File(_folder);
		if(!temp.exists()) temp.mkdir();
		
		try {
			InputStream in = blob.getBinaryStream(); 
			
			File img = new File(temp, billId + "qrbar.png");
			BufferedImage bi = ImageIO.read(in);
			
			ImageIO.write(bi, "png", img);
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Read Clob and return the string
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private String readClob(Clob clob) throws SQLException, IOException {
		StringBuilder str = new StringBuilder();
		Reader r = clob.getCharacterStream();
		char[] buf = new char[128];
		int len = 0;
		while ((len = r.read(buf)) != -1) {
			str.append(buf, 0, len);
		}
		return str.toString();
	}
}
