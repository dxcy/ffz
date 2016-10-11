package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class test {
public static void main(String[] args) throws UnknownHostException {
	String url ="http://59.255.40.22/arcgis/rest/services/1301/MapServer?f=pjson";
	 String hostName=url.substring(url.indexOf(":")+3);
	 System.out.println(hostName);
     System.out.println(hostName.substring(0, hostName.indexOf("/")));
}
}
