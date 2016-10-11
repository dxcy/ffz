package util;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import com.ms.websvc.client.CatalogStub;
import com.ms.websvc.client.CatalogStub.MetadataDocument;
import com.ms.websvc.client.CatalogStub.Query;
import com.ms.websvc.client.CatalogStub.QueryRequest;
import com.ms.websvc.client.CatalogStub.QueryResponse;
import com.ue.auditmanage.controller.service.AppServiceInter;
import com.ue.auditmanage.controller.service.AppTypeServiceInter;
import com.ue.auditmanage.controller.service.DataTypeInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDDataInter;
import com.ue.auditmanage.controller.service.YDUserInter;

import entity.App;
import entity.AppType;
import entity.DataType;
import entity.UserType;
import entity.YDData;
import entity.YDUser;

public class cataUtil {
	public static String catalogUrl = null;
	
	public static String catalogUsername = null;
	public static String catalogPassword = null;
	public static String catalogProtocolVersion = null;
	public static String catalogDatabaseId = null;
	public static String DatabaseIdApp = null;
	private static String path = "./catalog.properties";
	private static Map<String, String> UserIpmap = new HashMap<String, String>();

	static {
		PropertyUtil p =new PropertyUtil();
		catalogUrl = p.getValueByKey(path, "yd.url");
		catalogUsername = p.getValueByKey(path, "yd.username");
		catalogPassword = p.getValueByKey(path, "yd.password");
		catalogProtocolVersion =  p.getValueByKey(path, "yd.ProtocolVersion");
		catalogDatabaseId = p.getValueByKey(path, "yd.DatabaseId");
		DatabaseIdApp = p.getValueByKey(path, "yd.DatabaseIdApp");
		
		UserIpmap.put("48", "测绘分中心");
		UserIpmap.put("40", "数据主中心");
		UserIpmap.put("42", "国土分中心");
		UserIpmap.put("43", "水利分中心");
		UserIpmap.put("44", "资源环境分中心");
		UserIpmap.put("45", "卫星遥感分中心");
		UserIpmap.put("46", "海洋分中心");
		UserIpmap.put("49", "林业分中心");
		UserIpmap.put("50", "气象分中心");
		UserIpmap.put("52", "军事测绘分中心");
		UserIpmap.put("53", "军事航天分中心");
		UserIpmap.put("51", "资源卫星分中心");
		
	}
	public static void main(String[] args) {
		String ip = "59.255.40.22";
		String pp = (ip.substring(ip.indexOf(".")+1)).substring((ip.substring(ip.indexOf(".")+1)).indexOf(".")+1).substring(0, (ip.substring(ip.indexOf(".")+1)).substring((ip.substring(ip.indexOf(".")+1)).indexOf(".")+1).indexOf("."));
		System.out.println(pp);
		
	}
	/**
	 * 获得分中心
	 * @param ip
	 * @return
	 */
	public static String getcenterType(String ip) {
		String pp = (ip.substring(ip.indexOf(".")+1)).substring((ip.substring(ip.indexOf(".")+1)).indexOf(".")+1).substring(0, (ip.substring(ip.indexOf(".")+1)).substring((ip.substring(ip.indexOf(".")+1)).indexOf(".")+1).indexOf("."));
		if (UserIpmap.containsKey(pp)) {
			return UserIpmap.get(pp).toString();

		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param srvId
	 * @return
	 * @throws RemoteException 
	 */
		public static void test() throws RemoteException{
			CatalogStub stub = new CatalogStub(catalogUrl);
			Query query = new Query();
			QueryRequest queryRequest = new QueryRequest();

			queryRequest.setUsername(catalogUsername);
			queryRequest.setPassword(catalogPassword);
			queryRequest.setProtocolVersion(catalogProtocolVersion);
			
			queryRequest.setRecordSetStartPoint(0);
			queryRequest.setRecordSetEndPoint(5);

			/**
			 * 设置元数据库信息
			 */
			CatalogStub.Databases databases = new CatalogStub.Databases();
			databases.addDatabaseId("newappmdb");
			queryRequest.setDatabases(databases);
			/**
			 * 设置返回信息点
			 */
			CatalogStub.ElementSet elementSet = new CatalogStub.ElementSet();
			elementSet.addElement("10.1.2");//proid
			elementSet.addElement("10.1.7");//url
			elementSet.addElement("10.1.1");//服务名称
			elementSet.addElement("10.1.5");//服务级别
			queryRequest.setElementSet(elementSet);
			query.setReq(queryRequest);
			QueryResponse response = stub.query(query);
			CatalogStub.QueryResult result = response.get_return();
			MetadataDocument[] mes = result.getRecordSet().getRecord();
			for (MetadataDocument me : mes) {
			
				System.out.println(me.getData());
			}
			
		}
		/**
		 * 同步已注册应用库
		 * @param ydDataInter
		 * @param dataTypeInter
		 * @param ydUserInter
		 * @param ydVisiterServiceInter
		 * @throws RemoteException
		 */
			public static void appcatalogSynchro(AppServiceInter appServiceInter,AppTypeServiceInter appTypeServiceInter,YDUserInter ydUserInter,UserTypeServiceInter ydVisiterServiceInter) throws RemoteException{
				CatalogStub stub = new CatalogStub(catalogUrl);
				Query query = new Query();
				QueryRequest queryRequest = new QueryRequest();

				queryRequest.setUsername(catalogUsername);
				queryRequest.setPassword(catalogPassword);
				queryRequest.setProtocolVersion(catalogProtocolVersion);
				
				queryRequest.setRecordSetStartPoint(0);
				queryRequest.setRecordSetEndPoint(5);

				/**
				 * 设置元数据库信息
				 */
				CatalogStub.Databases databases = new CatalogStub.Databases();
				databases.addDatabaseId(DatabaseIdApp);
				queryRequest.setDatabases(databases);
				/**
				 * 设置返回信息点
				 */
				CatalogStub.ElementSet elementSet = new CatalogStub.ElementSet();
				elementSet.addElement("10.1.2");//proid
				elementSet.addElement("10.1.7");//url
				elementSet.addElement("10.1.10");//服务名称
				queryRequest.setElementSet(elementSet);
				query.setReq(queryRequest);
				QueryResponse response = stub.query(query);
				CatalogStub.QueryResult result = response.get_return();
				if(result.getRecordSet()!=null && result.getRecordSet().getRecord().length>0){
					int locaoTotalNumber = result.getTotalNumberOfRecords();
					int mod = locaoTotalNumber % 100;
					int size = (mod==0)? (locaoTotalNumber/100) : (locaoTotalNumber/100)+1;
					getAppDBBycycle(stub,queryRequest,0,100,size,100,appServiceInter,appTypeServiceInter, ydUserInter, ydVisiterServiceInter);	
				}		
			}

	/**
	 * 同步已注册服务库
	 * @param ydDataInter
	 * @param dataTypeInter
	 * @param ydUserInter
	 * @param ydVisiterServiceInter
	 * @throws RemoteException
	 */
		public static void catalogSynchro(YDDataInter ydDataInter,DataTypeInter dataTypeInter,YDUserInter ydUserInter,UserTypeServiceInter ydVisiterServiceInter) throws RemoteException{
			CatalogStub stub = new CatalogStub(catalogUrl);
			Query query = new Query();
			QueryRequest queryRequest = new QueryRequest();

			queryRequest.setUsername(catalogUsername);
			queryRequest.setPassword(catalogPassword);
			queryRequest.setProtocolVersion(catalogProtocolVersion);
			
			queryRequest.setRecordSetStartPoint(0);
			queryRequest.setRecordSetEndPoint(5);

			/**
			 * 设置元数据库信息
			 */
			CatalogStub.Databases databases = new CatalogStub.Databases();
			databases.addDatabaseId(catalogDatabaseId);
			queryRequest.setDatabases(databases);
			/**
			 * 设置返回信息点
			 */
			CatalogStub.ElementSet elementSet = new CatalogStub.ElementSet();
			elementSet.addElement("10.1.23");//sid
			elementSet.addElement("10.1.9");//url
			elementSet.addElement("10.1.1");//服务名称
			elementSet.addElement("10.1.10");//服务级别
			queryRequest.setElementSet(elementSet);
			query.setReq(queryRequest);
			QueryResponse response = stub.query(query);
			CatalogStub.QueryResult result = response.get_return();
			if(result.getRecordSet()!=null && result.getRecordSet().getRecord().length>0){
				int locaoTotalNumber = result.getTotalNumberOfRecords();
				int mod = locaoTotalNumber % 100;
				int size = (mod==0)? (locaoTotalNumber/100) : (locaoTotalNumber/100)+1;
				getDBBycycle(stub,queryRequest,0,100,size,100,ydDataInter,dataTypeInter, ydUserInter, ydVisiterServiceInter);	
			}		
		}
		
		/**
		 * 
		 * @param start
		 * @param end
		 * @param DatabaseId
		 * @param diff
		 * @param out_path
		 * @throws AxisFault
		 * @throws RemoteException
		 */
			/**
			 * @param stub
			 * @param queryRequest
			 * @param start
			 * @param end
			 * @param size_
			 * @param diff
			 * @param appServiceInter
			 * @param appTypeServiceInter
			 * @param ydUserInter
			 * @param userTypeInter
			 * @throws AxisFault
			 * @throws RemoteException
			 */
			private static void getAppDBBycycle(CatalogStub stub,CatalogStub.QueryRequest queryRequest,int start, int end, int size_,
					int diff,AppServiceInter appServiceInter,AppTypeServiceInter appTypeServiceInter,YDUserInter ydUserInter,UserTypeServiceInter userTypeInter) throws AxisFault, RemoteException {

				Query query = new Query(); 
				try {
					if(userTypeInter.findByName("供应商")==null){//验证用户类型
						UserType userType = new UserType();
						userType.setName("供应商");
						userType.setRegisterDate(new Date());
						userType.setUpdateDate(new Date());
						userTypeInter.saveUserType(userType);
					}
					int time=1;
				for (int i = 0; i < size_; i++) {//循环次数
					
					queryRequest.setRecordSetEndPoint(end);
					queryRequest.setRecordSetStartPoint(start);
					query.setReq(queryRequest);
					QueryResponse response = stub.query(query);
					CatalogStub.QueryResult result = response.get_return();
					if(result.getRecordSet()!=null && result.getRecordSet().getRecord().length>0){
						int size = result.getRecordSet().getRecord().length;//查询到的篇数
						
						for(int i2=0;i2<size ;i2++){
							time++;
							if(result.getRecordSet().getRecord()[i2].getItemList().getItems()!=null && result.getRecordSet().getRecord()[i2].getItemList().getItems().length>0){
								int itemSize = result.getRecordSet().getRecord()[i2].getItemList().getItems().length;String url="";String uip="";String slevel="公开";String proid="";String appname="";
								for(int j=0;j<itemSize;j++){
									
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.10")){//url										
										appname = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0];	
									} else
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.2")){//proid
										proid = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0];
									}  else
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.7")){//url
										url = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0];
										uip = util.getIpFromUrl(url);
									}
								//入库
							
								}//itemSize
								if(appTypeServiceInter.findByName(slevel)==null){
									AppType appType  = new AppType();
									appType.setName(slevel);
									appType.setRegisterDate(new Date());
									appType.setUpdateDate(new Date());
									appTypeServiceInter.saveAppType(appType);
								}//验证服务级别
								if(ydUserInter.findVisitorByUname(getcenterType(uip))==null){//验证供应商
									UserType userType = userTypeInter.findByName("供应商");
									YDUser user = new YDUser();
									user.setUname(getcenterType(uip));
									String uid = UUID.randomUUID().toString();
									user.setUid(uid);
									user.setUserType(userType);
									user.setDownloadAtlasTime(0);
									user.setSearchTime(0);
									user.setVisitAppTimes(0);
									user.setVisitAtlasTimes(0);
									user.setVisitDataTimes(0);
									user.setVisitProductionTimes(0);
									user.setVisitServiceTimes(0);
									user.setVisitPageTime(0);
									user.setUserIp(uip);
									user.setRegisterDate(new Date());
									user.setUpdateDate(new Date());
									ydUserInter.save(user);
								}
								if(appServiceInter.findByAid(proid)==null){//注册数据
									App app = new App();
									app.setAid(proid);
									app.setUrl(url);
									app.setProvider(ydUserInter.findVisitorByUname(getcenterType(uip)));
									app.setaType(appTypeServiceInter.findByName(slevel));
									app.setName(appname);
									app.setSearchedTime(0);
									app.setVisitedTimes(0);
									app.setRegisterDate(new Date());
									app.setUpdateDate(new Date());
									appServiceInter.save(app);
									System.out.println("添加应用" +app.getName());
								}else{
									System.out.println(proid+"该应用已注册");
								}
								
							}
						}//Record
						
					}
					end += diff;
					start += diff;
				}//size_
				System.out.println("共有"+time+"篇");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		/**
		 * 
		 * @param start
		 * @param end
		 * @param DatabaseId
		 * @param diff
		 * @param out_path
		 * @throws AxisFault
		 * @throws RemoteException
		 */
			private static void getDBBycycle(CatalogStub stub,CatalogStub.QueryRequest queryRequest,int start, int end, int size_,
					int diff,YDDataInter ydDataInter,DataTypeInter dataTypeInter,YDUserInter ydUserInter,UserTypeServiceInter userTypeInter) throws AxisFault, RemoteException {

				Query query = new Query(); 
				try {
					if(userTypeInter.findByName("供应商")==null){//验证用户类型
						UserType userType = new UserType();
						userType.setName("供应商");
						userType.setRegisterDate(new Date());
						userType.setUpdateDate(new Date());
						userTypeInter.saveUserType(userType);
					}
					int time=1;
				for (int i = 0; i < size_; i++) {//循环次数
					
					queryRequest.setRecordSetEndPoint(end);
					queryRequest.setRecordSetStartPoint(start);
					query.setReq(queryRequest);
					QueryResponse response = stub.query(query);
					CatalogStub.QueryResult result = response.get_return();
					if(result.getRecordSet()!=null && result.getRecordSet().getRecord().length>0){
						int size = result.getRecordSet().getRecord().length;//查询到的篇数
						
						for(int i2=0;i2<size ;i2++){
							time++;
							if(result.getRecordSet().getRecord()[i2].getItemList().getItems()!=null && result.getRecordSet().getRecord()[i2].getItemList().getItems().length>0){
								int itemSize = result.getRecordSet().getRecord()[i2].getItemList().getItems().length;String url="";String uip="";String slevel="";String mid="";String sname="";
								for(int j=0;j<itemSize;j++){
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.10")){//服务级别
										 slevel = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0]; 
										 if(slevel.equalsIgnoreCase("开放")){
											 slevel = "公开"; 
										 }
									} else
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.1")){//供应商
										sname = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0];	
									} else
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.23")){
										mid = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0];
									}  else
									if(result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemName().equalsIgnoreCase("10.1.9")){
										url = result.getRecordSet().getRecord()[i2].getItemList().getItems()[j].getItemValues()[0];
										uip = util.getIpFromUrl(url);
									}
								//入库
							
								}//itemSize
								if(dataTypeInter.findServiceByName(slevel)==null){
									DataType dataType  = new DataType();
									dataType.setName(slevel);
									dataType.setRegisterDate(new Date());
									dataType.setUpdateDate(new Date());
									dataTypeInter.save(dataType);
								}//验证服务级别
								if(ydUserInter.findVisitorByUname(getcenterType(uip))==null){//验证供应商
									UserType userType = userTypeInter.findByName("供应商");
									YDUser user = new YDUser();
									user.setUname(getcenterType(uip));
									String uid = UUID.randomUUID().toString();
									user.setUid(uid);
									user.setUserType(userType);
									user.setDownloadAtlasTime(0);
									user.setSearchTime(0);
									user.setVisitAppTimes(0);
									user.setVisitAtlasTimes(0);
									user.setVisitDataTimes(0);
									user.setVisitProductionTimes(0);
									user.setVisitServiceTimes(0);
									user.setVisitPageTime(0);
									user.setUserIp(uip);
									user.setRegisterDate(new Date());
									user.setUpdateDate(new Date());
									ydUserInter.save(user);
								}
								if(ydDataInter.findServiceBydUrl(url)==null){//注册数据
									YDData ydData = new YDData();
									ydData.setDid(mid);
									ydData.setdUrl(url);
									ydData.setProvider(ydUserInter.findVisitorByUname(getcenterType(uip)));
									ydData.setDataType(dataTypeInter.findServiceByName(slevel));
									ydData.setName(sname);
									ydData.setSearchedTime(0);
									ydData.setVisitedTimes(0);
									ydData.setRegisterDate(new Date());
									ydData.setUpdateDate(new Date());
									ydData.setIsNew("true");
									ydDataInter.save(ydData);
									System.out.println("添加数据" +ydData.getName());
								}else{
									System.out.println(mid+"该数据已注册");
								}
								
							}
						}//Record
						
					}
					end += diff;
					start += diff;
				}//size_
				System.out.println("共有"+time+"篇");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

}
