<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <xsl:output method="xml"  indent="yes" encoding="UTF-8"/>
    <xsl:param name="phone" />    <!-- 所属站联系电话 -->
    <xsl:param name="showAdj" /> <!-- 是否显示金额调整 -->
    <xsl:template match="/">
<html>
   <body>
    <div style="position:absolute;top:5px;right:80px;">
      <img src="file:///c:/temp/qrbar.png" width="60" height="60"/>
    </div>
    <div style="position:absolute;width:240px;height:10px;top:45pt;left:70pt;">
		<xsl:value-of select="/bill/acctId"/><!-- 用户编号 -->
	</div>
    <div style="position:absolute;width:600px;height:10px;top:65pt;left:70pt;">
		<xsl:value-of select="/bill/userName"/><!-- 用户名 -->
	</div>
    <div style="position:absolute;width:600px;height:10px;top:85pt;left:70pt;">
		<xsl:value-of select="/bill/address"/> <!-- 地址 -->
	</div>
    <div style="position:absolute;width:240px;height:10px;top:105pt;left:70pt;">
		<xsl:value-of select="$phone"/> <!-- 电话 -->
	</div>
    <div style="position:absolute;width:240px;height:10px;top:105pt;left:300pt;padding-left:20px;">
		<xsl:value-of select="/bill/billDate"/> <!-- 计费日期 -->
	</div>
    <div style="position:absolute;width:240px;height:10px;top:125pt;left:70pt;">
		<xsl:value-of select="/bill/station"/> <!-- 所属站 -->
	</div>
    <div style="position:absolute;width:240px;height:10px;top:125pt;left:300pt;padding-left:20px;">
		<xsl:value-of select="/bill/mrReader"/> <!-- 查表员 -->
	</div>
    <div style="position:absolute;width:80px;height:10px;top:145pt;left:40pt;text-align:right;">
		<xsl:value-of select="/bill/lastAmt"/><!-- 上期余额 -->
	</div>
    <div style="position:absolute;width:100px;height:10px;top:145pt;left:145pt;text-align:right;">
		<xsl:value-of select="/bill/curAmt"/><!-- 本期余额 -->
	</div>
    <div style="position:absolute;width:80px;height:10px;top:145pt;left:310pt;text-align:right;">
		<xsl:value-of select="/bill/billAmt"/> <!-- 应缴金额 -->
	</div>
    
	<div style="position:absolute;width:140px;height:10px;top:165pt;left:140pt;padding-left:10px;">
		<xsl:value-of select="/bill/dueDate" /> <!-- 最迟缴费日期 -->
	</div>
    <div style="position:absolute;width:80px;height:10px;top:165pt;left:310pt;text-align:right;">
		<xsl:value-of select="/bill/usage" /> <!-- 合计用量 -->
	</div>
     
  
    <div style="position:absolute;width:500px;top:185pt;left:10pt;">
      <table border="0" cellpadding="0" cellspacing="0">	   
		<!-- 账单段 -->		   
		 <xsl:for-each select="/bill/billSegmentList">
         <tr>
            <td colspan="3" style="border-style:none;padding-top:5px;font-size:12px;">用气地址</td>
            <td colspan="7" style="border-style:none;padding-top:5px;font-size:12px;"><xsl:value-of select="address"/></td>
         </tr>
	
		 <xsl:for-each select="./spList"><!-- 计量表 开始 -->	 
		 	<xsl:if test="position() &lt; 3">
         <tr>
            <td width="11%" style="border-style:none;font-size:12px;">表位置</td>
            <td colspan="5" style="border-style:none;font-size:12px;"><xsl:value-of select="sp"/></td>
         </tr>
         <tr>
            <td width="11%" height="22" style="border-style:none;font-size:12px;">上次抄表数</td>
            <td width="12%" height="22" style="border-style:none;font-size:12px;"><xsl:value-of select="lastRead"/></td>
            <td width="20%" height="22" style="border-style:none;font-size:12px;">本次抄表数</td>
            <td width="8%" height="22" style="border-style:none;font-size:12px;"><xsl:value-of select="presentRead"/></td>
            <td width="22%" height="22" style="border-style:none;font-size:12px;">用量</td>
            <td width="15%" height="22" style="border-style:none;font-size:12px;"><xsl:value-of select="dosage"/></td>
         </tr>
		 
         <xsl:for-each select="./algorithmList"><!-- 用气金额算法循环 -->
         <tr>
            <td width="25%" colspan="2" align="center" style="border-style:none;font-size:12px;">用气金额算法</td>
            <td colspan="2" style="border-style:none;font-size:12px;"><xsl:value-of select="algorithm"/></td>
            <td width="22%" style="border-style:none;font-size:12px;">金额</td>
            <td width="15%" style="border-style:none;font-size:12px;"><xsl:value-of select="amount"/></td>
         </tr>
	 		<xsl:if test="(position()=last())" >
		 <tr> 
			<td colspan="8">------------------------------------------------------------------------</td>
		 </tr>
			</xsl:if>
         <tr>
	    	<td colspan="6" height="2" style="border-style:none;margin-top: 0px;"></td>
		 </tr>
		 	</xsl:for-each><!-- 用气金额算法循环 结束 -->
		 	</xsl:if>
    	 </xsl:for-each><!-- 计量表 结束-->	 
	
		<xsl:if test="true($showAdj)" >    
		 <xsl:for-each select="/bill/adjustmentList">	
			 <tr>
				<td align="center" width="752" colspan="4" style="border-style:none;font-size:12px;">
					<xsl:value-of select="name"/>
				</td>	
				<td style="border-style:none;font-size:12px;">金额</td>
				<td width="295" style="border-style:none;font-size:12px;"><xsl:value-of select="amount"/></td>
			 </tr> 
		</xsl:for-each>        
		</xsl:if>
	
		<xsl:if test="not(position()=last())" >
		 <tr> 
			<td colspan="8">------------------------------------------------------------------------</td>
		 </tr>
		</xsl:if>	    
    </xsl:for-each>
	 <xsl:if test="not($showAdj)">
	   <tr><td colspan="6" style="padding-left:400px;">&#8594;</td></tr>
	 </xsl:if>
	  </table>
	</div>
   </body>
 </html> 
       
    </xsl:template>
</xsl:stylesheet>