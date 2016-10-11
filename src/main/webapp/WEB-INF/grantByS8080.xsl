<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dp="http://www.datapower.com/extensions"
	xmlns:dpquery="http://www.datapower.com/param/query" xmlns:math="http://exslt.org/math"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:dpfunc="http://www.datapower.com/extensions/functions"
	xmlns:func="http://exslt.org/functions" xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp func dpfunc" exclude-result-prefixes="dp dpfunc func">


	<xsl:variable name="vRequestUrl" select="dp:variable('var://service/URL-out')" />
	<xsl:variable name="uIp"
		select="dp:variable('var://service/transaction-client')" />
	<xsl:output method="text" version="1.0" encoding="UTF-8"
		indent="yes" />
	<xsl:variable name="vPostURL"
		select="concat('/',substring-after(substring-after($vRequestUrl,'//'),'/'))" />
	<xsl:variable name="vDesURI" select="concat(string($vPostURL),'?uid=')" />

	<xsl:variable name="vContext">
		<xsl:choose>
			<xsl:when test="contains(substring-after($vPostURL,'/'),'/')">
				<xsl:value-of
					select="substring-before(substring-after(string($vPostURL),'/'),'/')" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="substring-after($vPostURL,'/')" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="vDesURINoPara">
		<xsl:choose>
			<xsl:when test="contains($vPostURL,'?')">
				<xsl:value-of select="substring-before($vPostURL,'?')" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$vPostURL" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="/">
		<!-- 获得输入参数 -->
		<xsl:variable name="uid">
			<xsl:value-of select="/request/args/arg[@name='uid']" />
		</xsl:variable>
		<xsl:variable name="sid">
			<xsl:value-of select="/request/args/arg[@name='sid']" />
		</xsl:variable>


		<dp:set-request-header name="'uid'" value="string($uid)" />
		<dp:set-request-header name="'sid'" value="string($sid)" />
		<!-- <dp:set-request-header name="'Cookie'" value="string($Cookie)" /> -->

		<dp:set-request-header name="'vDesURI'" value="string($vDesURI)" />

		<xsl:variable name="SearchStatement">
			select * from FINEWAY_USER_RES where FUID=? and RES_ID=?
		</xsl:variable>

		<xsl:variable name="relativeStatement">
			select c.srv_id from res c where c.srv_group_kind in(select
			a.res_group_id from role_res_group a inner join sys_fineway_user b on
			a.role_id=b.role_id)
		</xsl:variable>

		<xsl:variable name="SESSIONIDStatement">
			select SESSIONID from SYS_FINEWAY_VERIFI where FUID=?
		</xsl:variable>
		<xsl:variable name="resultSearch">
			<dp:sql-execute source="'orcl8080'" statement="$SearchStatement">
				<arguments>
					<argument>
						<xsl:value-of select="$uid" />
					</argument>
					<argument>
						<xsl:value-of select="$sid" />
					</argument>
				</arguments>
			</dp:sql-execute>
		</xsl:variable>
		
		<xsl:choose>
		<!-- 第一层匹配 -->
			<xsl:when test=" $resultSearch=''">
			<dp:set-request-header name="'flag'" value="'userid与resid匹配为空'" />
				<!-- 第二层匹配(option) -->
				<xsl:variable name="resultrelativeSearch"
					select="dp:sql-execute('orcl8080',$relativeStatement)" />
			
			<xsl:choose>
					<xsl:when test=" $resultrelativeSearch=''">
						<dp:xreject reason="'查询结果SRV_ID为空!'"></dp:xreject>
					</xsl:when>
					<xsl:otherwise>
					<!-- 第三层匹配sessionid -->
						<xsl:variable name="sess">
							<dp:sql-execute source="'orcl8080'" statement="$SESSIONIDStatement">
								<arguments>
									<argument>
										<xsl:value-of select="$uid" />
									</argument>
								</arguments>
							</dp:sql-execute>
						</xsl:variable>
						<xsl:choose>
					<xsl:when test=" $sess=''">
						<dp:xreject reason="'查询sessionid为空!'"></dp:xreject>
					</xsl:when>
					<xsl:otherwise>
					<!-- 查询srv_level -->
					<xsl:variable name="srv_leveltatement">select srv_level from res where srv_id=? </xsl:variable>
						<xsl:variable name="srvlevel">
					<dp:sql-execute source="'orcl8080'" statement="$srv_leveltatement">
				<arguments>
					<argument>
						<xsl:value-of select="$sid" />
					</argument>
				</arguments>
			</dp:sql-execute>
		</xsl:variable>
		<xsl:choose>
				
					<!-- 第三层验证 -->
					<xsl:when test=" $srvlevel='0'">
						<dp:xreject reason="'srvlevel为0!'"></dp:xreject>
					</xsl:when>
					<xsl:otherwise>
					
					 <xsl:variable name="zhlxdmStatement">
			select zhlxdm from jfxt_account where yhwybz=?  and zhlxdm !='100' and zhlxdm like '10%';
		</xsl:variable>
				
					<xsl:variable name="vzhlxdm">
					<dp:sql-execute source="'orcl8080'" statement="$zhlxdmStatement">
				<arguments>
					<argument>
						<xsl:value-of select="$fuid" />
					</argument>
				</arguments>
			</dp:sql-execute>
		</xsl:variable>
		<xsl:variable name="zhlxdm" select="substring-after($vzhlxdm,'10')"/>
					<xsl:choose>
					
					<!-- 第三层验证 -->
					<xsl:when test=" number($srvlevel) > number($zhlxdm) ">
						<dp:xreject reason="'srvlevel大于zhlxdm'"></dp:xreject>
					</xsl:when>
					<xsl:otherwise>	
					<!-- 路游 -->
						<xsl:variable name="vDesIPPort">
							<xsl:choose>
								<xsl:when test="contains($vContext , 'tokenService')">

									192.168.10.21:8282
								</xsl:when>
								<xsl:otherwise>
									192.168.0.54:8080
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
					
						<dp:set-request-header name="'sess'" value="string($sess)" />
						<xsl:variable name="vsess">
							<xsl:value-of select="substring-after(string($sess),'SESSIONID')" />
						</xsl:variable>
						<dp:set-request-header name="'sessionid'"
							value="string($vsess)" />
						<xsl:variable name="vDesURI"
							select="concat(concat(concat(concat(concat(string($vDesURI),string($uid)),'&amp;sid='),string($sid)),'&amp;sessionid='),string($vsess))" />
						<xsl:variable name="vDesURL"
							select="concat('http://',string($vDesIPPort))" />
						<dp:set-variable name="'var://service/routing-url'"
							value="string($vDesURL)" />
						<dp:set-variable name="'var://service/URI'" value="string($vDesURI)" />
						<dp:set-request-header name="'vDesURL'"
							value="string($vDesURL)" />
						<dp:set-request-header name="'vDesURI'"
							value="string($vDesURI)" />
					</xsl:otherwise></xsl:choose>	
					</xsl:otherwise></xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>

				
			</xsl:when>

			<xsl:otherwise>
			<dp:set-request-header name="'flag'" value="'userid与resid匹配不为空'" />
	<!-- 查询结果不为空-->
		<!-- 查询sessionid -->
						<xsl:variable name="sess">
							<dp:sql-execute source="'orcl8080'" statement="$SESSIONIDStatement">
								<arguments>
									<argument>
										<xsl:value-of select="$uid" />
									</argument>
								</arguments>
							</dp:sql-execute>
						</xsl:variable>
	<xsl:choose> <xsl:when test=" $sess=''">
	<dp:xreject reason="'权限验证失败：用户尚未登录!'"></dp:xreject>
	</xsl:when><xsl:otherwise>
	<!-- 路游 -->
						<xsl:variable name="vDesIPPort">
							<xsl:choose>
								<xsl:when test="contains($vContext , 'tokenService')">
									192.168.10.229:8282
								</xsl:when>
								<xsl:otherwise>
									192.168.0.54:8080
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						
				<dp:set-request-header name="'sess'" value="string($sess)" />		
	<xsl:variable name="vsess">
							<xsl:value-of select="substring-after(string($sess),'SESSIONID')" />
						</xsl:variable>
						<dp:set-request-header name="'sessionid2'"
							value="string($vsess)" />
						<xsl:variable name="vDesURI"
							select="concat(concat(concat(concat(concat(string($vDesURI),string($uid)),'&amp;sid='),string($sid)),'&amp;sessionid='),string($vsess))" />
						<xsl:variable name="vDesURL"
							select="concat('http://',string($vDesIPPort))" />
					
						
					<!-- <dp:set-variable name="'var://service/routing-url'" value="string($vDesURL)" /> -->
                                 <dp:set-variable name="'var://service/routing-url'" value="'http://192.168.10.21:8282'" />
 		             	<dp:set-variable name="'var://service/URI'" value="string($vDesURI)"/>
						
						
						<dp:set-request-header name="'vDesURL2'"
							value="string($vDesURL)" />
						<dp:set-request-header name="'vDesURI2'"
							value="string($vDesURI)" />
							<!-- 添加日志信息 -->
							<xsl:variable name="vvvurl" select="dp:variable('var://service/URL-in')" />
	
	<xsl:variable name="endix" select="substring-after(substring-after(substring-after($vvvurl,'//'),'/'),'.')"/>
	<dp:set-request-header name="'end'" value="string($endix)" />
	<xsl:choose>
               <xsl:when test="contains($endix , 'gif')"></xsl:when>
	        <xsl:when test=" $endix='gif'"></xsl:when>
                          <xsl:when test="contains($endix , 'js')"></xsl:when>
			  <xsl:when test=" $endix='8.3.min.js'"></xsl:when>
                        <xsl:when test="contains($endix , 'jpg')"></xsl:when>
			<xsl:when test=" $endix='jpg'">
			
			</xsl:when>
                       <xsl:when test="contains($endix , 'png')"></xsl:when>
			<xsl:when test=" $endix='png'">
			</xsl:when>
                          <xsl:when test=" $endix='min.js'">
			</xsl:when>
                         <xsl:when test="contains($endix , 'ico')"></xsl:when>
                         <xsl:when test=" $endix='ico'">
			</xsl:when>
                        <xsl:when test="contains($endix , 'css')"></xsl:when>
			<xsl:when test="$endix='js'"></xsl:when>
			<xsl:when test="$endix='css'"></xsl:when>
			<xsl:otherwise>
			<xsl:message terminate="no" dp:type="youedataAudit"
			dp:priority="notice">
			tokengrantBys | <xsl:value-of select="string($uid)"/> | <xsl:value-of select="string($sid)"/> |
			<xsl:value-of select="dp:variable('var://service/URL-in')"/> |
                      <xsl:value-of select="dp:variable('var://service/URL-out')"/>
		</xsl:message>
			</xsl:otherwise></xsl:choose>
			
			</xsl:otherwise></xsl:choose>	
			</xsl:otherwise></xsl:choose>			

	</xsl:template>
</xsl:stylesheet>























