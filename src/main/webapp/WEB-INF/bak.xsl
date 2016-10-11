<?xml version="1.0" encoding="UTF-8"?>  
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dp="http://www.datapower.com/extensions" 

xmlns:dpquery="http://www.datapower.com/param/query" extension-element-prefixes="dp" exclude-result-prefixes="dp">
	<xsl:variable name="vRequestUrl" select="dp:variable('var://service/URL-out')"/>
	
	<xsl:variable name="vPostURL" select="concat('/',substring-after(substring-after($vRequestUrl,'//'),'/'))"/>
	<xsl:variable name="vDesURI" select="string($vPostURL)"/>
	<xsl:variable name="vContext">
		<xsl:choose>
			<xsl:when test="contains(substring-after($vPostURL,'/'),'/')"><xsl:value-of select="substring-before(substring-after(string

($vPostURL),'/'),'/')"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="substring-after($vPostURL,'/')"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="vDesURINoPara">
		<xsl:choose>
			<xsl:when test="contains($vPostURL,'?')"><xsl:value-of select="substring-before($vPostURL,'?')"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$vPostURL"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="vDesIPPort">
		<xsl:choose>
			<!-- ws and rest route case -->
			<!-- redirect to loader balance -->
			<xsl:when test="$vDesURINoPara = '/arcgis/rest/css'">172.168.0.11:8399</xsl:when>

<!-- ws and rest route case 20150511-->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/AsiaEN/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/Asia/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/jjj/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/js/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/zj/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/6201/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/6201/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/6202/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/6202/MapServer')">172.168.0.10:6080</xsl:when>


<!-- jie20150507 -->
              <xsl:when test="contains($vDesURINoPara , 'arcgis/rest/directories/arcgisoutput')">172.168.0.10:6080</xsl:when>




   <xsl:when test="contains($vDesURINoPara , '/arcgis/server/arcgisoutput')">172.168.0.11:8399</xsl:when>        
 <xsl:when test="$vContext = '/'">172.168.0.1:8081</xsl:when>  

<!-- 20150407 -->
<!--   <xsl:when test="contains($vContext , 'web')">172.168.0.10</xsl:when> -->

<!-- <xsl:when test="contains($vContext , 'webservice')">172.168.0.10</xsl:when> -->



<xsl:when test="contains($vDesURINoPara , '/ServicePictures')">172.168.0.10:9002</xsl:when> 

<!-- 20160614 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2429/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2429/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2430/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2430/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2433/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2433/MapServer')">172.168.0.10:6080</xsl:when>



<!--20150202  hitTimer-->

 <xsl:when test="contains($vContext , 'hitTimer')">172.168.0.1:8081</xsl:when>

<xsl:when test="contains($vContext , 'flexmapview')">172.168.0.1:8081</xsl:when>

<!-- <xsl:when test="contains($vContext , 'hitTimer')">192.168.0.54:8383</xsl:when>  -->

<xsl:when test="contains($vContext , '/')">172.168.0.1:8081</xsl:when>
<xsl:when test="contains($vContext , 'qgqhbjtzfb')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgqhqhfb')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgjdqhfx')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qhbhdzdgcyxysyfx')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgfsnyscxffx')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgfsnyhjfx')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'fzxnyzc')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgstbj')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'sjydqstbj')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qghsmdjzyswgjstxtlxkjfb')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgfsjzdqyslfglfb')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgtrqs')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'sjydqcdthtrqsfb')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgghzhls')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qghszhls')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qglsdzzh')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgdzizhls')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgzrzhfxqhzh')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgqxzhfq')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qghszhfq')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgdzizhfq')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgdznzhfq')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgslzhfq')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qghszhzhpg')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgdzizhzhpg')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgdznzhzhpg')">172.168.0.1:8080</xsl:when>
<xsl:when test="contains($vContext , 'qgnzwzhzhpg')">172.168.0.1:8080</xsl:when>  
<xsl:when test="contains($vContext , 'qgslzhzhpg')">172.168.0.1:8080</xsl:when>         
<xsl:when test="contains($vContext , '100wshiliang')">172.168.0.1:8080</xsl:when>     
<!-- jie20150509 -->
<xsl:when test="contains($vContext , 'show')">172.168.0.14:8081</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/Scripts/swfobject_modified.js')">172.168.0.14:8081</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/jjj/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/jjj/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/jjj/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/js/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/js/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/js/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/zj/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/zj/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/zj/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/Asia/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/Asia/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/Asia/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/AsiaEN/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/AsiaEN/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/AsiaEN/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/africa/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/africa/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/africa/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/Africa/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/Africa/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/Africa/MapServer')">172.168.0.10:6080</xsl:when>

<!-- jie20150428 -->


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/anhui/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/anhui/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/anhui/MapServer')">172.168.0.10:6080</xsl:when>



<!-- jie20150604 -->

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2302/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2301/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>



<!-- jie20150506 -->

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2401/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2401/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2401/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-0101/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-0101/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-0101/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1202/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1202/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1202/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1205/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1205/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1205/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1301/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1301/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1301/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1302/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1302/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1302/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1502/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1502/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1502/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1503/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1503/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1503/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1705/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1705/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1705/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1706/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1706/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1706/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1817/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1817/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1817/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1818/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1818/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1818/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2001/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2001/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2001/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2002/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2002/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2002/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2003/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2003/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2003/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2101/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2101/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2101/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2102/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2102/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2102/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2103/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2103/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2103/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2105/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2105/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2105/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2106/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2106/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2106/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2405/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2405/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2405/wms130')">172.168.0.10:8090</xsl:when>

<!-- jie20150427  -->

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2013/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2013/rest')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2103/wms130')">172.168.0.10:8090</xsl:when>



<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-12022/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-12022/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-12022/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1205/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1205/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1205/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-0101/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-0101/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-0101/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1301/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1301/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1301/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1302/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1302/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1302/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1502/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1502/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1502/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1503/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1503/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1503/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1705/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1705/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1705/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1706/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1706/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1706/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1817/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1817/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1817/wms130')">172.168.0.10:8090</xsl:when>
 
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1818/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-1818/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-1818/wms130')">172.168.0.10:8090</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2001/rest/maps')">172.168.0.10:8090</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2001/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2001/wms130')">172.168.0.10:8090</xsl:when>  

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2002/rest/maps')">172.168.0.10:8090</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2002/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2002/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2003/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2003/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2003/wms130')">172.168.0.10:8090</xsl:when>     

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2101/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2101/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2101/wms130')">172.168.0.10:8090</xsl:when>     

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2102/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2102/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2102/wms130')">172.168.0.10:8090</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-21032/rest/maps')">172.168.0.10:8090</xsl:when>  
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-21032/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-21032/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2105/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2105/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2105/wms130')">172.168.0.10:8090</xsl:when>   

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2106/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2106/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2106/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2405/rest/maps')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/data-2405/rest/data')">172.168.0.10:8090</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2405/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2404/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2404/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2404/MapServer')">172.168.0.10:6080</xsl:when>

<!-- jie20150424 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/0101/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/0101/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/0101/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2401/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2401/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2401/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2403/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2403/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2403/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2405/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2405/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2405/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2406/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2406/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2406/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2501/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2501/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2501/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2502/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2502/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2502/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2503/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2503/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2503/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2504/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2504/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2504/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2505/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2505/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2505/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2506/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2506/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2506/MapServer')">172.168.0.10:6080</xsl:when>

<!-- jie20150205 -->
<!-- 2010年分县人口总数（人）  CP0803020300001 -->

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0803020300001WFS/MapServer')">172.168.0.11:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0803020300001WFS/MapServer')">172.168.0.11:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0803020300001/MapServer')">172.168.0.11:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0803020300001/MapServer')">172.168.0.11:8399</xsl:when>


<!-- 20150330 -->
 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/2002/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2002/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2002/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2001/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2001/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2001/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2003/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2003/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2003/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/login')">172.168.0.10:6080</xsl:when> 


<!-- 20150417 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1817/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1817/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1817/MapServer')">172.168.0.10:6080</xsl:when> 


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1818/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1818/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1818/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2105/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2105/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2105/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2101/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2101/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2101/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2102/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2102/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2102/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2013/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2013/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2013/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1102/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1102/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1102/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1201/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1201/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1201/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1202/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1202/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1202/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1205/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1205/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1205/MapServer')">172.168.0.10:6080</xsl:when>




<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1301/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1301/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1301/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1302/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1302/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1302/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1401/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1401/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1401/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1404/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1404/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1404/MapServer')">172.168.0.10:6080</xsl:when>



<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1502/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1502/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1502/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1503/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1503/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1503/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1705/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1705/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1705/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1706/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1706/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1706/MapServer')">172.168.0.10:6080</xsl:when>



<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2101/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2101/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2101/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2106/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2106/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2106/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2402/MapServer/WMSServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2402/MapServer')">172.168.0.10:6080</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2402/MapServer')">172.168.0.10:6080</xsl:when>

<!-- 2010年分县人口性别比（女=100）CP0803020300003 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0803020300003/MapServer')">172.168.0.11:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0803020300003/MapServer')">172.168.0.11:8399</xsl:when>


<!--   2010年出生率（%）CP0803020300013 -->

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0803020300013/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0803020300013/MapServer')">172.168.0.11:8399</xsl:when>


<!-- 2010年自然增长率（%）CP0803020300015 -->

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0803020300015/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0803020300015/MapServer')">172.168.0.11:8399</xsl:when>



<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0803020300001WFS/MapServer/WFSServer')">172.168.0.11:8399</xsl:when>


<!-- 长三角地区核心区城市化水平分布2005  CP08010101001 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP08010101001/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP08010101001/MapServer')">172.168.0.11:8399</xsl:when>


<!-- 长三角地区核心区各市进出口总额分布2003  CP08010101002 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP08010101002/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP08010101002/MapServer')">172.168.0.11:8399</xsl:when>


<!-- 长三角地区核心区水资源三级分区     CP08010101003 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP08010101003/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP08010101003/MapServer')">172.168.0.11:8399</xsl:when>


<!-- 长三角地区核心区各城市人口密度分布2005  CP08010102001 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP08010102001/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP08010102001/MapServer')">172.168.0.11:8399</xsl:when>


<!-- 京津冀地区各城市社会消费品零售总额   CP080201020021 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP08020102002/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP08020102002/MapServer')">172.168.0.11:8399</xsl:when>

<!-- 京津冀地区各城市出口总额分布    CP08020102001 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP08020102001/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP08020102001/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0302020200_863/MapServer')">172.168.0.11:8399</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0302020200_863/MapServer')">172.168.0.11:8399</xsl:when>

<!-- arcgis-11 -->
<xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702010100/MapServer')">172.168.0.11:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702010100/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702010200/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702010200/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702010300/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702010300/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702020100/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702020100/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702030200/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702030200/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702030400/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702030400/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0702030500/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0702030500/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0501010100/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0501010100/MapServer')">172.168.0.11:8399</xsl:when>
  
 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0501010200/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0501010200/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0501010300/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0501010300/MapServer')">172.168.0.11:8399</xsl:when> 
 
 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0501010400/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0501010400/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502010100/MapServer')">172.168.0.11:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502010100/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502010200/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502010200/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502010300/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502010300/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP01020201/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP01020201/MapServer')">172.168.0.11:8399</xsl:when> 
 
 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP01020202/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP01020202/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP01020203/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP01020203/MapServer')">172.168.0.11:8399</xsl:when> 
 
 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP01020204/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP01020204/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP01020205/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP01020205/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP01020206/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP01020206/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0301010100/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0301010100/MapServer')">172.168.0.11:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0303010100/MapServer')">172.168.0.11:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0303010100/MapServer')">172.168.0.11:8399</xsl:when>

<!-- arcgis-12 -->
 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0703010100/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0703010100/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0703010200/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0703010200/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0703020100/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0703020100/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0703020200/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0703020200/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0703020300/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0703020300/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0703020400/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0703020400/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502010400/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502010400/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0303010200/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0303010200/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0303020200/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0303020200/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0303030100/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0303030100/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502010500/MapServer')">172.168.0.12:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502010500/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502010600/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502010600/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502020100/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502020100/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502020200/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502020200/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502020300/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502020300/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502020400/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502020400/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0502020500/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0502020500/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0601020100/MapServer')">172.168.0.12:8399</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0601020100/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP0601020200/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP0601020200/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP09010102/MapServer')">172.168.0.12:8399</xsl:when> 
<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP09010102/MapServer')">172.168.0.12:8399</xsl:when>

 <xsl:when test="contains($vDesURINoPara , '/arcgis/services/CP090301/MapServer')">172.168.0.12:8399</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/CP090301/MapServer')">172.168.0.12:8399</xsl:when>
<!-- 20151029-->


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2411/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2411/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2412/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2412/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2413/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2413/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2417/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2417/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2414/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2414/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2416/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2416/MapServer')">172.168.0.10:6080</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2418/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2418/MapServer')">172.168.0.10:6080</xsl:when>



<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2415/rest/maps')">172.168.0.10:8090</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-JingJinJiDiTu/rest/maps')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-QuanGuoDianXingJingJiQuYuDiTu/rest/maps')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-PinKunXianDiTu/rest/maps')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-QuanGuoJingJiQuDiTu/rest/maps')">172.168.0.10:8090</xsl:when>


<xsl:when test="$vContext = 'gfcloud'">172.168.5.55:80</xsl:when>



<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-2415/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-JingJinJiDiTu/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-QuanGuoDianXingJingJiQuYuDiTu/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-PinKunXianDiTu/wms130')">172.168.0.10:8090</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-QuanGuoJingJiQuDiTu/wms130')">172.168.0.10:8090</xsl:when>


<!-- 20151102 -->
<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-QuanGuoFenFuDiTu/rest/maps')">172.168.0.10:8090</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/iserver/services/map-QuanGuoFenFuDiTu/wms130')">172.168.0.10:8090</xsl:when>


<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2419/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2419/MapServer')">172.168.0.10:6080</xsl:when>
                                   

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2420/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2420/MapServer')">172.168.0.10:6080</xsl:when>


<!-- 20151104 -->

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/1819/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/1819/MapServer')">172.168.0.10:6080</xsl:when>
                                   

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2423/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2423/MapServer')">172.168.0.10:6080</xsl:when>

<xsl:when test="contains($vDesURINoPara , '/arcgis/services/2424/MapServer')">172.168.0.10:6080</xsl:when> 

<xsl:when test="contains($vDesURINoPara , '/arcgis/rest/services/2424/MapServer')">172.168.0.10:6080</xsl:when>

<!-- gridmap-->

<xsl:when test="contains($vDesURINoPara , '/mapserver/tms/services')">172.168.0.13:80</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/mapserver/wms/services')">172.168.0.13:80</xsl:when>
<xsl:when test="contains($vDesURINoPara , '/flexmapview')">172.168.0.13:80</xsl:when>		  

			

                        
			<xsl:when test="$vContext = 'catalog'">172.168.0.1:8081</xsl:when>
			<xsl:when test="$vContext = 'gateway'">172.168.0.1:8081</xsl:when>
                        <xsl:when test="$vContext = 'appimg'">172.168.0.1:8081</xsl:when>
			<xsl:when test="$vContext = 'proimg'">172.168.0.1:8081</xsl:when>
                        <xsl:when test="$vContext = 'prozip'">172.168.0.1:8081</xsl:when>
			<xsl:when test="$vContext = 'srvimg'">172.168.0.1:8081</xsl:when>
			
                             <!-- redirect to loader balance -->

			<!-- application route case -->
			    <xsl:otherwise>172.168.0.1:8081</xsl:otherwise>
			<!--<xsl:otherwise>172.168.1.100:2048</xsl:otherwise>-->

		</xsl:choose>
	</xsl:variable>

	
	<xsl:variable name="vDesURL" select="concat(concat('http://',string($vDesIPPort)),$vDesURI)"/>
	

	<xsl:template match="/">
		<xsl:message>
			RUL-out:<xsl:value-of select="dp:variable('var://service/URL-out')"/>
		</xsl:message>
		<xsl:message >
			PostURL:<xsl:value-of select="$vPostURL"/>
		</xsl:message>
		<xsl:message >
			vDesURI:<xsl:value-of select="$vDesURI"/>
		</xsl:message>
		<xsl:message >
			vContext:<xsl:value-of select="$vContext"/>
		</xsl:message>
		<xsl:message >
			vDesURINoPara:<xsl:value-of select="$vDesURINoPara"/>
		</xsl:message>
		
		<dp:set-variable name="'var://service/routing-url'" value="string($vDesURL)"/>
		<dp:set-variable name="'var://service/URI'" value="string($vDesURI)"/>
	</xsl:template>
</xsl:stylesheet>