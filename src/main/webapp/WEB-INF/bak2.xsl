<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dp="http://www.datapower.com/extensions"
	xmlns:dpquery="http://www.datapower.com/param/query" xmlns:math="http://exslt.org/math"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:dpfunc="http://www.datapower.com/extensions/functions"
	xmlns:func="http://exslt.org/functions" xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp func dpfunc" exclude-result-prefixes="dp dpfunc func">
	<xsl:variable name="vRequestUrl" select="dp:variable('var://service/URL-out')" />
	<xsl:variable name="uIp" select="dp:variable('var://service/transaction-client')" />
	<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes" />

	<!-- http://203.207.196.237:8080/ambari -->

	<!-- sid =YEDAPIPUB0006 -->

	<xsl:variable name="vPostURL"
		select="concat('/',substring-after(substring-after($vRequestUrl,'//'),'/'))" />
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
		
			<xsl:variable name="vDesURL" select="concat('http://','192.168.10.221:8080')" />	
		<xsl:variable name="vDesURI" select="''" />
<dp:set-variable name="'var://service/routing-url'" value="string($vDesURL)" /> 
            <dp:set-variable name="'var://service/URI'" value="string($vDesURI)" />
 				
	<!-- 结尾 -->	
		
	</xsl:template>
</xsl:stylesheet>
