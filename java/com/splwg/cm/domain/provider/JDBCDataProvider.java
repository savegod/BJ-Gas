package com.splwg.cm.domain.print.provider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.splwg.cm.domain.print.DataHandlerException;
import com.splwg.cm.domain.print.base.AbstractDataProvider;
import com.splwg.cm.domain.print.base.DataHandler;
import com.splwg.cm.domain.print.conf.Settings;
import com.splwg.cm.domain.print.handler.XsltHandler;

public class JDBCDataProvider extends AbstractDataProvider {
	public static final Log log = LogFactory.getLog(JDBCDataProvider.class);

	static String PRINT_DATA_RM = "delete from cm_bill_print where bill_id = ?";

	static String INSERT_LOG = "insert into cm_bill_print_his  "
			+ "select seq_bill_print_his.nextval,bill_id,BILL_RTE_TYPE_CD,MR_RTE_CD,ACCESS_GRP_CD,cm_prt_date,sysdate "
			+ "from cm_bill_print  where bill_id = ?";

	ThreadLocal<Connection> local = new ThreadLocal<Connection>();

	public void processData() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			String sql = Settings.QUERY_SQL;
			if (Settings.ACC_GRP_CD == null || Settings.ACC_GRP_CD.length() < 1) {
				log.error("can't load setting properties"); 
				return;
			}
			conn = getConn(); // ThreadLocal
			pst = conn.prepareStatement(sql);
			//pst.setString(1, Settings.ACC_GRP_CD.trim());
			rs = pst.executeQuery();

			while (rs.next()) {
				Clob itemClob = rs.getClob("cm_bill_item");
				String billType = rs.getString("bill_rte_type_cd").trim();
				String billId = rs.getString("bill_id");
				
				//gen QR Image
				generateQRImg(rs.getBlob("barcode"));
				//put billId into 在ThreadLocal中
				localBill.set(billId);
				String xml = "";
				try {
					xml = readClob(itemClob);
				} catch (IOException e) {
					log.error("fail to retrieve Blob: ", e); 
					return;
				}
				handler.setAttribute("template", billType);
				try {
					handler.invoke(xml);
					
					/** move the delete operation to CMPrintJobListener **/

				} catch (DataHandlerException e) {
					log.error("Process BillItem error: ", e); 
					return;
 				}finally{
 					localBill.set(null);
 				}
 			}
		} catch (SQLException e) {
			log.error("error query failed: ", e); 
			return;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Generate QR image
	 * @param blob blob , output to c:\temp\qrbar.png
	 */
	private void generateQRImg(Blob blob) {
		String _folder = Settings.QRBAR_IMG_FOLDER;
		File temp = new File(_folder);
		if(!temp.exists()) temp.mkdir();
		
		try {
			InputStream in = blob.getBinaryStream(); 
			
			File img = new File(temp,"qrbar.png");
			BufferedImage bi = ImageIO.read(in);
			
			ImageIO.write(bi, "png", img);
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}

	/**
	 * delete printed cm_bill_print
	 * 
	 * @param conn   do not close it
	 * @param billId
	 * @throws SQLException
	 */
	public boolean handleCallback(String billId) {
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		Connection conn = getConn();
		try {
			// do print log insertion
			pst1 = conn.prepareStatement(INSERT_LOG);
			pst1.setString(1, billId); 
			pst1.executeUpdate();

			// do print data removal
			pst2 = conn.prepareStatement(PRINT_DATA_RM);
			pst2.setString(1, billId); 
			pst2.executeUpdate();

			conn.commit();
			return true;
		} catch (Exception e) {
			log.error("handleCallback data error: billId=" + billId, e);
			return false;
		} finally {
			try {
				if (pst1 != null)
					pst1.close();
				if (pst2 != null)
					pst2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	protected Connection getConn() {
		Connection conn = local.get();
		if (conn == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(Settings.DB_URL, Settings.DB_USR, Settings.DB_PWD);
				local.set(conn);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return conn;
	}

	public void releaseConn() {
		try {
			Connection conn = local.get();
			if (conn != null)
				conn.close();

			local.set(null);
		} catch (Exception e) {
			log.error("failt to release connection!");
		}

	}
 
}