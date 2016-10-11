package com.ue.auditmanage.controller.annotation;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.cataUtil;
import util.util;
import vo.Service;
import vo.item;
import vo.vApp;
import vo.vAtlas;
import vo.vAuditInfos;
import vo.vData;
import vo.vUser;
import vo.vuserType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ue.auditmanage.controller.service.AppServiceInter;
import com.ue.auditmanage.controller.service.AppTypeServiceInter;
import com.ue.auditmanage.controller.service.AtlasTypeServiceInter;
import com.ue.auditmanage.controller.service.AuditInfoServiceInter;
import com.ue.auditmanage.controller.service.DataTypeInter;
import com.ue.auditmanage.controller.service.ProductServiceInter;
import com.ue.auditmanage.controller.service.ProductTypeServiceInter;
import com.ue.auditmanage.controller.service.ServiceTypeInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDDataInter;
import com.ue.auditmanage.controller.service.YDServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;
import com.ue.auditmanage.controller.service.AtlasServiceInter;

import entity.App;
import entity.AppType;
import entity.Atlas;
import entity.AuditInfos;
import entity.DataType;
import entity.Production;
import entity.ProductionType;
import entity.ServiceType;
import entity.UserType;
import entity.YDData;
import entity.YDService;
import entity.YDUser;
import entity.atlasType;
//类的注解
@Controller
@RequestMapping("/auditInfos")
public class AuditInfosController {
	@Resource(name = "AuditInfosServiceImpl")
	private AuditInfoServiceInter auditInfoServiceInter;
	@Resource(name = "ydUserImpl")
	private YDUserInter ydVisiterInter;
	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	@Resource(name = "ydDataImpl")
	private  YDDataInter ydDataInter;
	@Resource(name = "dataTypeImpl")
	private  DataTypeInter dataTypeInter;
	@Resource(name = "ServiceTypeImpl")
	private  ServiceTypeInter serviceTypeInter;
	@Resource(name = "ydServiceImpl")
	private  YDServiceInter ydServiceInter;
	@Resource(name = "AtlasTypeServiceImpl")
	private AtlasTypeServiceInter atlasTypeServiceInter;
	@Resource(name = "AtlasServiceImpl")
	private AtlasServiceInter  atlasServiceInter;
	@Resource(name = "AppTypeServiceImpl")
	private AppTypeServiceInter appTypeServiceInter;
	@Resource(name = "AppServiceImpl")
	private AppServiceInter appServiceInter;
	@Resource(name = "ProductTypeServiceImpl")
	private ProductTypeServiceInter productTypeServiceInter;
	@Resource(name = "ProductServiceImpl")
	private ProductServiceInter productServiceInter;
	/**
	 * 添加审计记录
	 * WriteLog_1("USERID","USERNAME","USERTYPE","BEHAVETYPE","KEYWORD","BEHAVEID","BEHAVENAME","BUSSTYPE","IP","urlstr");
	 * @param request
	 * @param response
	 * @return
	 */
	/**
	 * @param request
	 * @param response
	 * @param i
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addAuditInfos")
	public void addAuditInfos(HttpServletRequest request,
			HttpServletResponse response,String i) {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		util util = new util();
		String client_ip = util.getIpAddr(request);
		try {
			response.setContentType("text/json;charset=utf-8");
			AuditInfos behave = new AuditInfos();
			behave = new ObjectMapper().readValue(i, AuditInfos.class);
			if(behave!=null){
				behave.setAuditDate(new  Date());
				behave.setUSERIP(client_ip);
				String pro_ip =util.getIpFromUrl(behave.getURL());
				String pro = cataUtil.getcenterType(pro_ip);
				if(pro!=null && !"".equalsIgnoreCase(pro)){
					behave.setProvider(pro);
				}
				if(behave.getUSERID()==null && "".equalsIgnoreCase(behave.getUSERID())){
					behave.setUSERID(UUID.randomUUID().toString());
				}
				auditInfoServiceInter.save(behave);
				//注册未知类型
				if(userTypeServiceInter.findByName("其它")==null){//注册未知用户类型
					UserType uType = new UserType();
					uType.setName("其它");
					uType.setRegisterDate(new Date() );
					uType.setUpdateDate(new Date());
					userTypeServiceInter.saveUserType(uType);
				}
				if(dataTypeInter.findServiceByName("异常数据类型")==null){//注册未知数据类型
					DataType dataType = new DataType();
					dataType.setName("异常数据类型");
					dataType.setRegisterDate(new Date() );
					dataType.setUpdateDate(new Date());
					dataTypeInter.save(dataType);
				}
				if(serviceTypeInter.findByName("异常服务类型")==null){//注册未知服务类型
					ServiceType serviceType = new ServiceType();
					serviceType.setName("异常服务类型");
					serviceType.setRegisterDate(new Date() );
					serviceType.setUpdateDate(new Date());
					serviceTypeInter.saveServiceType(serviceType);
				}
				if(atlasTypeServiceInter.findByName("异常图集类型")==null){//注册未知图集类型
					atlasType atlastype = new atlasType();
					atlastype.setName("异常图集类型");
					atlastype.setRegisterDate(new Date() );
					atlastype.setUpdateDate(new Date());
					atlasTypeServiceInter.saveAtlasType(atlastype);
				}
				if(appTypeServiceInter.findByName("异常应用类型")==null){//注册未知应用类型
					AppType appType = new AppType();
					appType.setName("异常应用类型");
					appType.setRegisterDate(new Date() );
					appType.setUpdateDate(new Date());
					appTypeServiceInter.saveAppType(appType);
				}
				if(productTypeServiceInter.findByName("异常产品类型")==null){//注册未知产品类型
					ProductionType productionType = new ProductionType();
					productionType.setName("异常产品类型");
					productionType.setRegisterDate(new Date() );
					productionType.setUpdateDate(new Date());
					productTypeServiceInter.save(productionType);
				}
				YDUser visitor = null;
				if(behave.getUSERNAME()!=null && !"".equalsIgnoreCase(behave.getUSERNAME())){
					visitor = ydVisiterInter.findVisitorByUname(behave.getUSERNAME());
				}else{
					 visitor = ydVisiterInter.findVisitorByIp(client_ip);
				}
				
				if(behave.getBEHAVETYPE().equalsIgnoreCase("03")){//执行查询操作,只需要记录用户的c
					try {
						if(visitor==null){//未知用户
							visitor = new YDUser();
							visitor.setDownloadAtlasTime(0);
							visitor.setUname(behave.getUSERNAME());
							visitor.setVisitAppTimes(0);
							visitor.setVisitPageTime(0);
							visitor.setVisitAtlasTimes(0);
							visitor.setVisitDataTimes(0);
							visitor.setVisitProductionTimes(0);
							visitor.setVisitServiceTimes(0);
							visitor.setSearchTime(1);
							visitor.setRegisterDate(new Date());
							visitor.setUpdateDate(new Date());
							visitor.setUid(behave.getUSERID());
							visitor.setUserIp(client_ip);
							visitor.setUserType(userTypeServiceInter.findByName("其它"));
							ydVisiterInter.save(visitor);
							
						}else{
							visitor.setSearchTime(visitor.getSearchTime()+1);
							visitor.setUpdateDate(new Date());
							ydVisiterInter.update(visitor);
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//解析前台参数
				if(behave.getBUSSTYPE().equalsIgnoreCase("01")){//图集,
					Atlas atlas = atlasServiceInter.findByUrl(behave.getURL());
					if(behave.getBEHAVETYPE().equalsIgnoreCase("01")){//下载
						try {	
							if(visitor==null){//未知用户
								visitor = new YDUser();
								visitor.setDownloadAtlasTime(1);
								visitor.setUname(behave.getUSERNAME());
								visitor.setVisitAppTimes(0);
								visitor.setVisitPageTime(0);
								visitor.setVisitAtlasTimes(0);
								visitor.setVisitDataTimes(0);
								visitor.setVisitProductionTimes(0);
								visitor.setVisitServiceTimes(0);
								visitor.setSearchTime(0);
								visitor.setRegisterDate(new Date());
								visitor.setUpdateDate(new Date());
								visitor.setUid(behave.getUSERID());
								visitor.setUserIp(client_ip);
								visitor.setUserType(userTypeServiceInter.findByName("其它"));
								ydVisiterInter.save(visitor);
								
							}else{
								visitor.setDownloadAtlasTime(visitor.getDownloadAtlasTime()+1);
								visitor.setUpdateDate(new Date());
								ydVisiterInter.update(visitor);
								
							}
							if(atlas==null){//访问了未知图集
								atlas = new Atlas();
								atlas.setAid(behave.getBEHAVEID());
								atlas.setaUrl(behave.getURL());
								atlas.setaType(atlasTypeServiceInter.findByName("异常图集类型"));
								atlas.setName(behave.getBEHAVENAME());
								atlas.setRegisterDate(new Date());
								atlas.setUpdateDate(new Date());
								String ip = util.getIpFromUrl(behave.getURL());
								YDUser provider = ydVisiterInter.findVisitorByIp(ip);
								if(provider==null){
									provider = new YDUser();
									provider.setDownloadAtlasTime(0);
									provider.setUname(behave.getUSERNAME());
									provider.setVisitAppTimes(0);
									provider.setVisitPageTime(0);
									provider.setVisitAtlasTimes(0);
									provider.setVisitDataTimes(0);
									provider.setVisitProductionTimes(0);
									provider.setVisitServiceTimes(0);
									provider.setSearchTime(0);
									provider.setRegisterDate(new Date());
									provider.setUpdateDate(new Date());
									provider.setUid(UUID.randomUUID().toString());
									provider.setUserIp(ip);
									provider.setUserType(userTypeServiceInter.findByName("其它"));
									ydVisiterInter.save(provider);
								}
								atlas.setProvider(provider);
								atlas.setDownloadTime(1);
								atlas.setVisitedTimes(0);
								atlasServiceInter.saveAtlas(atlas);
							}else{
								atlas.setDownloadTime(atlas.getDownloadTime()+1);
								atlas.setUpdateDate(new Date());
								atlasServiceInter.updateAtlas(atlas);
							}
								
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(behave.getBEHAVETYPE().equalsIgnoreCase("02")){//浏览图集
						try {	
							if(visitor==null){//未知用户
								visitor = new YDUser();
								visitor.setDownloadAtlasTime(0);
								visitor.setUname(behave.getUSERNAME());
								visitor.setVisitAppTimes(0);
								visitor.setVisitPageTime(0);
								visitor.setVisitAtlasTimes(1);
								visitor.setVisitDataTimes(0);
								visitor.setVisitProductionTimes(0);
								visitor.setVisitServiceTimes(0);
								visitor.setSearchTime(0);
								visitor.setRegisterDate(new Date());
								visitor.setUpdateDate(new Date());
								visitor.setUid(behave.getUSERID());
								visitor.setUserIp(client_ip);
								visitor.setUserType(userTypeServiceInter.findByName("其它"));
								ydVisiterInter.save(visitor);
								
							}else{
								visitor.setVisitAtlasTimes(visitor.getVisitAtlasTimes()+1);
								visitor.setUpdateDate(new Date());
								ydVisiterInter.update(visitor);
								
							}
							if(atlas==null){//访问了未知图集
								atlas = new Atlas();
								atlas.setAid(behave.getBEHAVEID());
								atlas.setaUrl(behave.getURL());
								atlas.setaType(atlasTypeServiceInter.findByName("未知图集类型"));
								atlas.setName(behave.getBEHAVENAME());
								atlas.setRegisterDate(new Date());
								atlas.setUpdateDate(new Date());
								String ip = util.getIpFromUrl(behave.getURL());
								YDUser provider = ydVisiterInter.findVisitorByIp(ip);
								if(provider==null){
									provider = new YDUser();
									provider.setDownloadAtlasTime(0);
									provider.setUname(behave.getUSERNAME());
									provider.setVisitAppTimes(0);
									provider.setVisitPageTime(0);
									provider.setVisitAtlasTimes(0);
									provider.setVisitDataTimes(0);
									provider.setVisitProductionTimes(0);
									provider.setVisitServiceTimes(0);
									provider.setSearchTime(0);
									provider.setRegisterDate(new Date());
									provider.setUpdateDate(new Date());
									provider.setUid(UUID.randomUUID().toString());
									provider.setUserIp(ip);
									provider.setUserType(userTypeServiceInter.findByName("其它"));
									ydVisiterInter.save(provider);
									
								}
								
								atlas.setProvider(provider);
								atlas.setDownloadTime(0);
								atlas.setVisitedTimes(1);
								atlasServiceInter.saveAtlas(atlas);
							}else{
								atlas.setVisitedTimes(atlas.getVisitedTimes()+1);
								atlas.setUpdateDate(new Date());
								atlasServiceInter.updateAtlas(atlas);
							}
								
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				}else if(behave.getBUSSTYPE().equalsIgnoreCase("02")){//应用
					App app = appServiceInter.findByAid(behave.getBEHAVEID());
					try {	
						if(visitor==null){//未知用户
							visitor = new YDUser();
							visitor.setDownloadAtlasTime(0);
							visitor.setUname(behave.getUSERNAME());
							visitor.setVisitAppTimes(1);
							visitor.setVisitPageTime(0);
							visitor.setVisitAtlasTimes(0);
							visitor.setVisitDataTimes(0);
							visitor.setVisitProductionTimes(0);
							visitor.setVisitServiceTimes(0);
							visitor.setSearchTime(0);
							visitor.setRegisterDate(new Date());
							visitor.setUpdateDate(new Date());
							visitor.setUid(behave.getUSERID());
							visitor.setUserIp(client_ip);
							visitor.setUserType(userTypeServiceInter.findByName("其它"));
							ydVisiterInter.save(visitor);
							
						}else{
							visitor.setVisitAppTimes(visitor.getVisitAppTimes()+1);
							visitor.setUpdateDate(new Date());
							ydVisiterInter.update(visitor);
							
						}
						if(app==null){//访问了未知图集
							app = new App();
							app.setAid(behave.getBEHAVEID());
							app.setUrl(behave.getURL());
							app.setaType(appTypeServiceInter.findByName("异常应用类型"));
							app.setName(behave.getBEHAVENAME());
							app.setRegisterDate(new Date());
							app.setUpdateDate(new Date());
							String ip = util.getIpFromUrl(behave.getURL());
							YDUser provider = ydVisiterInter.findVisitorByIp(ip);
							if(provider==null){
								provider = new YDUser();
								provider.setDownloadAtlasTime(0);
								provider.setUname(behave.getUSERNAME());
								provider.setVisitAppTimes(0);
								provider.setVisitPageTime(0);
								provider.setVisitAtlasTimes(0);
								provider.setVisitDataTimes(0);
								provider.setVisitProductionTimes(0);
								provider.setVisitServiceTimes(0);
								provider.setSearchTime(0);
								provider.setRegisterDate(new Date());
								provider.setUpdateDate(new Date());
								provider.setUid(UUID.randomUUID().toString());
								provider.setUserIp(ip);
								provider.setUserType(userTypeServiceInter.findByName("其它"));
								ydVisiterInter.save(provider);
								
							}
							
							app.setProvider(provider);
							app.setVisitedTimes(1);
							appServiceInter.save(app);
						}else{
							app.setVisitedTimes(app.getVisitedTimes()+1);
							app.setUpdateDate(new Date());
							appServiceInter.update(app);
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(behave.getBUSSTYPE().equalsIgnoreCase("03")){//数据
					YDData data = ydDataInter.findServiceByDid(behave.getBEHAVEID());
					try {	
						if(visitor==null){//未知用户
							visitor = new YDUser();
							visitor.setDownloadAtlasTime(0);
							visitor.setUname(behave.getUSERNAME());
							visitor.setVisitAppTimes(0);
							visitor.setVisitPageTime(0);
							visitor.setVisitAtlasTimes(0);
							visitor.setVisitDataTimes(1);
							visitor.setVisitProductionTimes(0);
							visitor.setVisitServiceTimes(0);
							visitor.setSearchTime(0);
							visitor.setRegisterDate(new Date());
							visitor.setUpdateDate(new Date());
							visitor.setUid(behave.getUSERID());
							visitor.setUserIp(client_ip);
							visitor.setUserType(userTypeServiceInter.findByName("其它"));
							ydVisiterInter.save(visitor);
							
						}else{
							visitor.setVisitDataTimes(visitor.getVisitDataTimes()+1);
							visitor.setUpdateDate(new Date());
							ydVisiterInter.update(visitor);
							
						}
						if(data==null){//访问了未知图集
							data = new YDData();
							data.setDid(behave.getBEHAVEID());
							data.setdUrl(behave.getURL());
							data.setDataType(dataTypeInter.findServiceByName("异常数据类型"));
							data.setName(behave.getBEHAVENAME());
							data.setRegisterDate(new Date());
							data.setUpdateDate(new Date());
							String ip = util.getIpFromUrl(behave.getURL());
							YDUser provider = ydVisiterInter.findVisitorByIp(ip);
							if(provider==null){
								provider = new YDUser();
								provider.setDownloadAtlasTime(0);
								provider.setUname(behave.getUSERNAME());
								provider.setVisitAppTimes(0);
								provider.setVisitPageTime(0);
								provider.setVisitAtlasTimes(0);
								provider.setVisitDataTimes(0);
								provider.setVisitProductionTimes(0);
								provider.setVisitServiceTimes(0);
								provider.setSearchTime(0);
								provider.setRegisterDate(new Date());
								provider.setUpdateDate(new Date());
								provider.setUid(UUID.randomUUID().toString());
								provider.setUserIp(ip);
								provider.setUserType(userTypeServiceInter.findByName("其它"));
								ydVisiterInter.save(provider);
								
							}
							
							data.setProvider(provider);
							data.setVisitedTimes(1);
							ydDataInter.save(data);
						}else{
							data.setVisitedTimes(data.getVisitedTimes()+1);
							data.setUpdateDate(new Date());
							ydDataInter.update(data);
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(behave.getBUSSTYPE().equalsIgnoreCase("04")){//产品
					Production production = productServiceInter.findByProid(behave.getBEHAVEID());
					try {	
						if(visitor==null){//未知用户
							visitor = new YDUser();
							visitor.setDownloadAtlasTime(0);
							visitor.setUname(behave.getUSERNAME());
							visitor.setVisitAppTimes(0);
							visitor.setVisitPageTime(0);
							visitor.setVisitAtlasTimes(0);
							visitor.setVisitDataTimes(0);
							visitor.setVisitProductionTimes(1);
							visitor.setVisitServiceTimes(0);
							visitor.setSearchTime(0);
							visitor.setRegisterDate(new Date());
							visitor.setUpdateDate(new Date());
							visitor.setUid(behave.getUSERID());
							visitor.setUserIp(client_ip);
							visitor.setUserType(userTypeServiceInter.findByName("其它"));
							ydVisiterInter.save(visitor);
							
						}else{
							visitor.setVisitProductionTimes(visitor.getVisitProductionTimes()+1);
							visitor.setUpdateDate(new Date());
							ydVisiterInter.update(visitor);
							
						}
						if(production==null){//访问了未知产品
							production = new Production();
							production.setProid(behave.getBEHAVEID());
							production.setUrl(behave.getURL());
							production.setpType(productTypeServiceInter.findByName("异常产品类型"));
							production.setName(behave.getBEHAVENAME());
							production.setRegisterDate(new Date());
							production.setUpdateDate(new Date());
							String ip = util.getIpFromUrl(behave.getURL());
							YDUser provider = ydVisiterInter.findVisitorByIp(ip);
							if(provider==null){
								provider = new YDUser();
								provider.setDownloadAtlasTime(0);
								provider.setUname(behave.getUSERNAME());
								provider.setVisitAppTimes(0);
								provider.setVisitPageTime(0);
								provider.setVisitAtlasTimes(0);
								provider.setVisitDataTimes(0);
								provider.setVisitProductionTimes(0);
								provider.setVisitServiceTimes(0);
								provider.setSearchTime(0);
								provider.setRegisterDate(new Date());
								provider.setUpdateDate(new Date());
								provider.setUid(UUID.randomUUID().toString());
								provider.setUserIp(ip);
								provider.setUserType(userTypeServiceInter.findByName("其它"));
								ydVisiterInter.save(provider);
								
							}
							
							production.setProvider(provider);
							production.setVisitedTimes(1);
							productServiceInter.save(production);
						}else{
							production.setVisitedTimes(production.getVisitedTimes()+1);
							production.setUpdateDate(new Date());
							productServiceInter.update(production);
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(behave.getBUSSTYPE().equalsIgnoreCase("05")){//服务
					YDService service = ydServiceInter.findServiceBySid(behave.getBEHAVEID());
					try {	
						if(visitor==null){//未知用户
							visitor = new YDUser();
							visitor.setDownloadAtlasTime(0);
							if(behave.getUSERNAME()!=null && !"".equalsIgnoreCase(behave.getUSERNAME())){
								visitor.setUname(behave.getUSERNAME());
							}else{
								visitor.setUname(client_ip);
							}
							visitor.setVisitAppTimes(0);
							visitor.setVisitPageTime(0);
							visitor.setVisitAtlasTimes(0);
							visitor.setVisitDataTimes(0);
							visitor.setVisitProductionTimes(0);
							visitor.setVisitServiceTimes(1);
							visitor.setSearchTime(0);
							visitor.setRegisterDate(new Date());
							visitor.setUpdateDate(new Date());
						    visitor.setUid(behave.getUSERID());
							visitor.setUserIp(client_ip);
							visitor.setUserType(userTypeServiceInter.findByName("其它"));
							ydVisiterInter.save(visitor);
							
						}else{
							visitor.setVisitServiceTimes(visitor.getVisitServiceTimes()+1);
							visitor.setUpdateDate(new Date());
							ydVisiterInter.update(visitor);
							
						}
						if(service==null){//访问了未知产品
							service = new YDService();
							service.setSid(behave.getBEHAVEID());
							service.setSurl(behave.getURL());
							service.setServiceType(serviceTypeInter.findByName("异常服务类型"));
							service.setsName(behave.getBEHAVENAME());
							service.setRegisterDate(new Date());
							service.setUpdateDate(new Date());
							String ip = util.getIpFromUrl(behave.getURL());
							YDUser provider = ydVisiterInter.findVisitorByIp(ip);
							if(provider==null){
								provider = new YDUser();
								provider.setDownloadAtlasTime(0);
								if(behave.getUSERNAME()!=null && !"".equalsIgnoreCase(behave.getUSERNAME())){
									provider.setUname(behave.getUSERNAME());
								}else{
									provider.setUname(ip);
								}
								
								provider.setVisitAppTimes(0);
								provider.setVisitPageTime(0);
								provider.setVisitAtlasTimes(0);
								provider.setVisitDataTimes(0);
								provider.setVisitProductionTimes(0);
								provider.setVisitServiceTimes(0);
								provider.setSearchTime(0);
								provider.setRegisterDate(new Date());
								provider.setUpdateDate(new Date());
								provider.setUid(UUID.randomUUID().toString());
								provider.setUserIp(ip);
								provider.setUserType(userTypeServiceInter.findByName("其它"));
								ydVisiterInter.save(provider);
								
							}
							
							service.setProvider(provider);
							service.setVisitedTime(1);
							ydServiceInter.save(service);
						}else{
							service.setVisitedTime(service.getVisitedTime()+1);
							service.setUpdateDate(new Date());
							ydServiceInter.update(service);
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			flag= true;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);	
	}
	
	/**
	 * 各分中心访问统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/visitedStaticByOrg")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void visitedStaticByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql_service = "from YDService  where 1=1";
		String hql_app = "from App  where 1=1";
		String hql_atlas = "from Atlas  where 1=1";
		String hql_data = "from YDData  where 1=1";
		String hql_production = "from Production  where 1=1";
		Map<String ,Integer > servicemap = new HashMap<String ,Integer >();
		Map<String ,Integer > appmap = new HashMap<String ,Integer >();
		Map<String ,Integer > atlasmap = new HashMap<String ,Integer >();
		Map<String ,Integer > datamap = new HashMap<String ,Integer >();
		Map<String ,Integer > productionmap = new HashMap<String ,Integer >();
		List<item> appList = new ArrayList<item>();
    	List<item> serviceList = new ArrayList<item>();
    	List<item> dataList = new ArrayList<item>();
    	List<item> productionList = new ArrayList<item>();
    	List<item> atlasList = new ArrayList<item>();
      try {
    	List<App> apps = appServiceInter.findAllData(hql_app);
    	List<Atlas> atlas = atlasServiceInter.findAllatlas(hql_atlas);
    	List<YDData> datas = ydDataInter.findAllData(hql_data);
    	List<YDService> services = ydServiceInter.findAllService(hql_service);
    	List<Production> productions = productServiceInter.findAllData(hql_production);
    	
    	if(apps!=null && apps.size()>0){
    		
    		for(App app : apps){
    			if(appmap.containsKey(app.getProvider().getUname())){
    				
    				appmap.put(app.getProvider().getUname(), appmap.get(app.getProvider().getUname())+app.getVisitedTimes());
    			}else{
    				appmap.put(app.getProvider().getUname(), app.getVisitedTimes());
    			}
    		}
    		Iterator<?> iter = appmap.entrySet().iterator();
    		int id=1;
  			while (iter.hasNext()) {
  			@SuppressWarnings("unchecked")
			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			item it= new item();
  			it.setId(id++);
  			it.setName(entry.getKey());
  			it.setVal(entry.getValue());
  			appList.add(it);
  			}
    		
    	}//获取所有供应商app的访问数量
    	if(atlas!=null && atlas.size()>0){
    		for(Atlas atla : atlas){
    			if(atlasmap.containsKey(atla.getProvider().getUname())){
    				atlasmap.put(atla.getProvider().getUname(), atlasmap.get(atla.getProvider().getUname())+atla.getVisitedTimes());
    			}else{
    				atlasmap.put(atla.getProvider().getUname(), atla.getVisitedTimes());
    			}
    		}
    		Iterator<?> iter = atlasmap.entrySet().iterator();
    		int id=1;
  			while (iter.hasNext()) {
  			@SuppressWarnings("unchecked")
			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			item it= new item();
  			it.setId(id++);
  			it.setName(entry.getKey());
  			it.setVal(entry.getValue());
  			atlasList.add(it);
    		
    	}//获取所有供应商图集的访问数量
    	}
    	if(datas!=null && datas.size()>0){
    		for(YDData data : datas){
    			if(datamap.containsKey(data.getProvider().getUname())){
    				datamap.put(data.getProvider().getUname(), datamap.get(data.getProvider().getUname())+data.getVisitedTimes());
    			}else{
    				datamap.put(data.getProvider().getUname(), data.getVisitedTimes());
    			}
    		}
    		Iterator<?> iter = datamap.entrySet().iterator();
    		int id=1;
  			while (iter.hasNext()) {
  			@SuppressWarnings("unchecked")
			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			item it= new item();
  			it.setId(id++);
  			it.setName(entry.getKey());
  			it.setVal(entry.getValue());
  			dataList.add(it);
    		
    	}//获取所有供应商数据的访问数量
    	}
    	if(services!=null && services.size()>0){
    		for(YDService service : services){
    			if(servicemap.containsKey(service.getProvider().getUname())){
    				servicemap.put(service.getProvider().getUname(), servicemap.get(service.getProvider().getUname())+service.getVisitedTime());
    			}else{
    				servicemap.put(service.getProvider().getUname(), service.getVisitedTime());
    			}
    		}
    		Iterator<?> iter = servicemap.entrySet().iterator();
    		int id=1;
  			while (iter.hasNext()) {
  			@SuppressWarnings("unchecked")
			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			item it= new item();
  			it.setId(id++);
  			it.setName(entry.getKey());
  			it.setVal(entry.getValue());
  			serviceList.add(it);
    	}//获取所有供应商服务的访问数量
    	}
    	if(productions!=null && productions.size()>0){
    		for(Production production : productions){
    			if(productionmap.containsKey(production.getProvider().getUname())){
    				productionmap.put(production.getProvider().getUname(), productionmap.get(production.getProvider().getUname())+production.getVisitedTimes());
    			}else{
    				productionmap.put(production.getProvider().getUname(), production.getVisitedTimes());
    			}
    		}
    		Iterator<?> iter = productionmap.entrySet().iterator();
    		int id=1;
  			while (iter.hasNext()) {
  			@SuppressWarnings("unchecked")
			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			item it= new item();
  			it.setId(id++);
  			it.setName(entry.getKey());
  			it.setVal(entry.getValue());
  			productionList.add(it);
    		
    	}//获取所有供应商产品的访问数量
    	}
    	flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("apps",appList );
		map.put("services",serviceList );
		map.put("datas",dataList );
		map.put("productions",productionList );
		map.put("atlas",atlasList );
		
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	} catch (Exception e) {
		e.printStackTrace();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("apps",appList );
		map.put("services",serviceList );
		map.put("datas",dataList );
		map.put("productions",productionList );
		map.put("atlas",atlasList );
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}	
	}
	
	
	/**
	 * 按分中心名称分中心访问统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/visitedStaticByProvider")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void visitedStaticByProvider(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
		String hql_service = "from YDService  where 1=1 and provider.uname = ? ";
		String hql_app = "from App  where 1=1 and provider.uname = ? ";
		String hql_atlas = "from Atlas  where 1=1 and provider.uname = ? ";
		String hql_data = "from YDData  where 1=1 and provider.uname = ? ";
		String hql_production = "from Production  where 1=1 and provider.uname = ? ";
		Map<String ,Integer > servicemap = new HashMap<String ,Integer >();
		Map<String ,Integer > appmap = new HashMap<String ,Integer >();
		Map<String ,Integer > atlasmap = new HashMap<String ,Integer >();
		Map<String ,Integer > datamap = new HashMap<String ,Integer >();
		Map<String ,Integer > productionmap = new HashMap<String ,Integer >();
		List<item> appList = new ArrayList<item>();
    	List<item> serviceList = new ArrayList<item>();
    	List<item> dataList = new ArrayList<item>();
    	List<item> productionList = new ArrayList<item>();
    	List<item> atlasList = new ArrayList<item>();
    	List<String> para=new ArrayList<String>();
    	if(provider!=null){
    		para.add(provider);
    		try {
    	    	List<App> apps = appServiceInter.findApp(hql_app,para);
    	    	List<Atlas> atlas = atlasServiceInter.findAllAtlasByList(hql_atlas,para);
    	    	List<YDData> datas = ydDataInter.findDataByList(hql_data,para);
    	    	List<YDService> services = ydServiceInter.findServiceByList(hql_service,para);
    	    	List<Production> productions = productServiceInter.findProduction(hql_production,para);
    	    	if(apps!=null && apps.size()>0){
    	    		for(App app : apps){
    	    			if(appmap.containsKey(app.getProvider().getUname())){
    	    				
    	    				appmap.put(app.getProvider().getUname(), appmap.get(app.getProvider().getUname())+app.getVisitedTimes());
    	    			}else{
    	    				appmap.put(app.getProvider().getUname(), app.getVisitedTimes());
    	    			}
    	    		}
    	    		Iterator<?> iter = appmap.entrySet().iterator();
    	    		int id=1;
    	  			while (iter.hasNext()) {
    	  			@SuppressWarnings("unchecked")
    				Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
    	  			item it= new item();
    	  			it.setId(id++);
    	  			it.setName(entry.getKey());
    	  			it.setVal(entry.getValue());
    	  			appList.add(it);
    	  			}
    	    		
    	    	}//获取所有供应商app的访问数量
    	    	if(atlas!=null && atlas.size()>0){
    	    		for(Atlas atla : atlas){
    	    			if(atlasmap.containsKey(atla.getProvider().getUname())){
    	    				atlasmap.put(atla.getProvider().getUname(), atlasmap.get(atla.getProvider().getUname())+atla.getVisitedTimes());
    	    			}else{
    	    				atlasmap.put(atla.getProvider().getUname(), atla.getVisitedTimes());
    	    			}
    	    		}
    	    		Iterator<?> iter = atlasmap.entrySet().iterator();
    	    		int id=1;
    	  			while (iter.hasNext()) {
    	  			@SuppressWarnings("unchecked")
    				Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
    	  			item it= new item();
    	  			it.setId(id++);
    	  			it.setName(entry.getKey());
    	  			it.setVal(entry.getValue());
    	  			atlasList.add(it);
    	    		
    	    	}//获取所有供应商图集的访问数量
    	    	}
    	    	if(datas!=null && datas.size()>0){
    	    		for(YDData data : datas){
    	    			if(datamap.containsKey(data.getProvider().getUname())){
    	    				datamap.put(data.getProvider().getUname(), datamap.get(data.getProvider().getUname())+data.getVisitedTimes());
    	    			}else{
    	    				datamap.put(data.getProvider().getUname(), data.getVisitedTimes());
    	    			}
    	    		}
    	    		Iterator<?> iter = datamap.entrySet().iterator();
    	    		int id=1;
    	  			while (iter.hasNext()) {
    	  			@SuppressWarnings("unchecked")
    				Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
    	  			item it= new item();
    	  			it.setId(id++);
    	  			it.setName(entry.getKey());
    	  			it.setVal(entry.getValue());
    	  			dataList.add(it);
    	    		
    	    	}//获取所有供应商数据的访问数量
    	    	}
    	    	if(services!=null && services.size()>0){
    	    		for(YDService service : services){
    	    			if(servicemap.containsKey(service.getProvider().getUname())){
    	    				servicemap.put(service.getProvider().getUname(), servicemap.get(service.getProvider().getUname())+service.getVisitedTime());
    	    			}else{
    	    				servicemap.put(service.getProvider().getUname(), service.getVisitedTime());
    	    			}
    	    		}
    	    		Iterator<?> iter = servicemap.entrySet().iterator();
    	    		int id=1;
    	  			while (iter.hasNext()) {
    	  			@SuppressWarnings("unchecked")
    				Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
    	  			item it= new item();
    	  			it.setId(id++);
    	  			it.setName(entry.getKey());
    	  			it.setVal(entry.getValue());
    	  			serviceList.add(it);
    	    	}//获取所有供应商服务的访问数量
    	    	}
    	    	if(productions!=null && productions.size()>0){
    	    		for(Production production : productions){
    	    			if(productionmap.containsKey(production.getProvider().getUname())){
    	    				productionmap.put(production.getProvider().getUname(), productionmap.get(production.getProvider().getUname())+production.getVisitedTimes());
    	    			}else{
    	    				productionmap.put(production.getProvider().getUname(), production.getVisitedTimes());
    	    			}
    	    		}
    	    		Iterator<?> iter = productionmap.entrySet().iterator();
    	    		int id=1;
    	  			while (iter.hasNext()) {
    	  			@SuppressWarnings("unchecked")
    				Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
    	  			item it= new item();
    	  			it.setId(id++);
    	  			it.setName(entry.getKey());
    	  			it.setVal(entry.getValue());
    	  			productionList.add(it);
    	    		
    	    	}//获取所有供应商产品的访问数量
    	    	}
    	    	flag = true;
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.put("success", flag);
    			map.put("apps",appList );
    			map.put("services",serviceList );
    			map.put("datas",dataList );
    			map.put("productions",productionList );
    			map.put("atlas",atlasList );
    			
    			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
    			util.writeJson(objects, request, response);
    		} catch (Exception e) {
    			e.printStackTrace();
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.put("success", flag);
    			map.put("apps",appList );
    			map.put("services",serviceList );
    			map.put("datas",dataList );
    			map.put("productions",productionList );
    			map.put("atlas",atlasList );
    			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
    			util.writeJson(objects, request, response);
    		}	
    	}
      
	}
	
	/**
	 * 
                     本年度服务查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServicesByprovider")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServicesByprovider(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		List<Object> paras =new ArrayList<Object>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and provider= ?  ";
		try {
			paras.add(provider);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				flag= true;int id=1;
				
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				
				for(AuditInfos ys :auditInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setVisitId(ys.getUSERID());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auditInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
////////////////////////////////////////////////本年度////////////////////////////////////////////////////////////////	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllServiceByvisitTypeTodychart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByvisitTypeTodychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				
				Map<String,Integer> userTypes = util.getServiceByuType(auditInfos,ydVisiterInter);
				ArrayList<vUser>  json = new ArrayList<vUser>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vUser vp = new vUser();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitedServiceTimes(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 今天服务按服务类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByvisitTypeToday")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByvisitTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceByuType(auditInfos,ydVisiterInter);
				ArrayList<vUser>  json = new ArrayList<vUser>();
				int id =(page-1)*rows+1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vUser vp = new vUser();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitedServiceTimes(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 
                     本日服务按服务类型查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServicesTodyByuType")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServicesTodyByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>(); String typeName = request.getParameter("typeName");
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'  and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
				if(auInfos!=null &&auInfos.size()>0 ){
					flag= true;int id=1;
					List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
					for(AuditInfos ys :auditInfos ){
						vAuditInfos v = new vAuditInfos();
						v.setId(id++);
						v.setVisitId(ys.getUSERID());
						v.setVisitIp(ys.getUSERIP());
						v.setAuditTime(sdf.format(ys.getAuditDate()));
						vauditInfos.add(v);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", flag);
					map.put("total", auditInfos.size());
					map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}else{
					flag = true;
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("success", flag);
					map.put("total", 0);
					String json = "[]";
					map.put("rows", json);
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}	
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}

	}
	
	/**
	 * 本日服务按类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByStypeTodaychart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByStypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				
				Map<String,Integer> services = util.getServiceBysType(auditInfos,ydServiceInter);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setServiceType(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	/**
	 * 本日服务按服务类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByStypeToday")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByStypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> services = util.getServiceBysType(auditInfos,ydServiceInter);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id =(page-1)*rows+1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setServiceType(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 
                     本日服务按服务类型查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServicesTodayBysType")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServicesTodayBysType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;String serviceType = URLDecoder.decode(request.getParameter("serviceType"), "UTF-8");
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>(); 
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'  and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				List<AuditInfos> auInfos = util.getServiceDetailBysType(auditInfos, ydServiceInter, serviceType);
				if(auInfos!=null &&auInfos.size()>0 ){
					flag= true;int id=1;
					List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
					for(AuditInfos ys :auInfos ){
						vAuditInfos v = new vAuditInfos();
						v.setId(id++);
						v.setVisitId(ys.getUSERID());
						v.setVisitIp(ys.getUSERIP());
						v.setAuditTime(sdf.format(ys.getAuditDate()));
						vauditInfos.add(v);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", flag);
					map.put("total", auInfos.size());
					map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}else{
					flag = true;
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("success", flag);
					map.put("total", 0);
					String json = "[]";
					map.put("rows", json);
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}	
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}

	}
	
	/**
	 * 本日服务按分中心实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByCenterTodaychart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByCenterTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				
				Map<String,Integer> services = util.getServiceByCenter(auditInfos);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setProvider(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	/**
	 * 本日服务按分中心实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByCenterToday")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByCenterToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> services = util.getServiceByCenter(auditInfos);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id =(page-1)*rows+1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setProvider(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 
                     本日度服务查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServicesTodayByprovider")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServicesTodayByprovider(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and provider=? and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(provider);paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				flag= true;int id=1;
				
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				
				for(AuditInfos ys :auditInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setVisitId(ys.getUSERID());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auditInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/////////////////////////////////////////////本日///////////////////////////////////////////////////////////////
	/**
	 * 本年服务按用户类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllServiceByvisitTypeThisYearchart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByvisitTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				
				Map<String,Integer> userTypes = util.getServiceByuType(auditInfos,ydVisiterInter);
				ArrayList<vUser>  json = new ArrayList<vUser>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vUser vp = new vUser();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitedServiceTimes(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	/**
	 * 本年服务按服务类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByvisitTypeThisYear")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByvisitTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceByuType(auditInfos,ydVisiterInter);
				ArrayList<vUser>  json = new ArrayList<vUser>();
				int id =(page-1)*rows+1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vUser vp = new vUser();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitedServiceTimes(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 
                     本年度服务按服务类型查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServices4yearByuType")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServices4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'  and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
				if(auInfos!=null &&auInfos.size()>0 ){
					flag= true;int id=1;
					List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
					for(AuditInfos ys :auditInfos ){
						vAuditInfos v = new vAuditInfos();
						v.setId(id++);
						v.setVisitId(ys.getUSERID());
						v.setVisitIp(ys.getUSERIP());
						v.setAuditTime(sdf.format(ys.getAuditDate()));
						vauditInfos.add(v);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", flag);
					map.put("total", auditInfos.size());
					map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}else{
					flag = true;
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("success", flag);
					map.put("total", 0);
					String json = "[]";
					map.put("rows", json);
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}	
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}

	}
	
	/**
	 * 本年服务按类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllServiceByStypeThisYearchart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByStypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				
				Map<String,Integer> services = util.getServiceBysType(auditInfos,ydServiceInter);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setServiceType(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	/**
	 * 本年服务按服务类型实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByStypeThisYear")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByStypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> services = util.getServiceBysType(auditInfos,ydServiceInter);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id =(page-1)*rows+1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setServiceType(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 
                     本年度服务按服务类型查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServices4yearBysType")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServices4yearBysType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 int rows = 10;int page = 1;String sType = URLDecoder.decode(request.getParameter("serviceType"), "UTF-8");
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>(); 
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				List<AuditInfos> auInfos = util.getServiceDetailBysType(auditInfos, ydServiceInter, sType);
				if(auInfos!=null &&auInfos.size()>0 ){
					flag= true;int id=1;
					List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
					for(AuditInfos ys :auditInfos ){
						vAuditInfos v = new vAuditInfos();
						v.setId(id++);
						v.setVisitId(ys.getUSERID());
						v.setVisitIp(ys.getUSERIP());
						v.setAuditTime(sdf.format(ys.getAuditDate()));
						vauditInfos.add(v);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", flag);
					map.put("total", auditInfos.size());
					map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}else{
					flag = true;
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("success", flag);
					map.put("total", 0);
					String json = "[]";
					map.put("rows", json);
					JSONObject objects = JSONObject.fromObject(map, jsonConfig);
					util.writeJson(objects, request, response);
				}	
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}

	}
	
	/**
	 * 本年服务按分中心实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllServiceByCenterThisYearchart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByCenterThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				
				Map<String,Integer> services = util.getServiceByCenter(auditInfos);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setProvider(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	/**
	 * 本年服务按分中心实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllServiceByCenterThisYear")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceByCenterThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Date> paras =new ArrayList<Date>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> services = util.getServiceByCenter(auditInfos);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id =(page-1)*rows+1;
				Iterator<?> iter = services.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setProvider(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", services.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 
                     本年度服务查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServices4yearByprovider")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServices4yearBySid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and provider=? and AuditDate > ? and AuditDate < ? ";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(provider);paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				flag= true;int id=1;
				
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				
				for(AuditInfos ys :auditInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setVisitId(ys.getUSERID());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auditInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 
                     本日服务查询细节
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getDetailServicesTodayBySid")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getDetailServicesTodayBySid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		Date d = new Date();
		List<Object> paras =new ArrayList<Object>();
		String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				flag= true;int id=1;
				List<AuditInfos> ainfos =util.getAuditDetailByCenter(auditInfos,provider);
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				
				for(AuditInfos ys :ainfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setVisitId(ys.getUSERID());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auditInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
		} catch (Exception e) {
			String json = "[]";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 0);
			map.put("rows",json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}

	}
/////////////////////////////////////////////本月///////////////////////////////////////////////////////////////
/**
* 本月服务按用户类型实时访问
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
/**
* @param request
* @param response
* @throws IOException
*/
@RequestMapping(value = "/getAllServiceByvisitTypeThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAllServiceByvisitTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> userTypes = util.getServiceByuType(auditInfos,ydVisiterInter);
ArrayList<vUser>  json = new ArrayList<vUser>();
int id = 1;
Iterator<?> iter = userTypes.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vUser vp = new vUser();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedServiceTimes(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", userTypes.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
/**
* 本月服务按服务类型实时访问
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getAllServiceByvisitTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getAllServiceByvisitTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
Map<String,Integer> userTypes = util.getServiceByuType(auditInfos,ydVisiterInter);
ArrayList<vUser>  json = new ArrayList<vUser>();
int id =(page-1)*rows+1;
Iterator<?> iter = userTypes.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vUser vp = new vUser();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedServiceTimes(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", userTypes.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
/**
* 
本月服务按服务类型查询细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailServices4MonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailServices4MonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'  and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
if(auInfos!=null &&auInfos.size()>0 ){
flag= true;int id=1;
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
for(AuditInfos ys :auditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setVisitId(ys.getUSERID());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}else{
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}	
} else {
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 本月服务按类型实时访问
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
/**
* @param request
* @param response
* @throws IOException
*/
@RequestMapping(value = "/getAllServiceByStypeThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAllServiceByStypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> services = util.getServiceBysType(auditInfos,ydServiceInter);
ArrayList<Service>  json = new ArrayList<Service>();
int id = 1;
Iterator<?> iter = services.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
Service vp = new Service();
vp.setId(id++);
vp.setServiceType(entry.getKey());
vp.setVisiteTotalTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", services.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}

/**
* 本年服务按服务类型实时访问
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getAllServiceByStypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getAllServiceByStypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
Map<String,Integer> services = util.getServiceBysType(auditInfos,ydServiceInter);
ArrayList<Service>  json = new ArrayList<Service>();
int id =(page-1)*rows+1;
Iterator<?> iter = services.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
Service vp = new Service();
vp.setId(id++);
vp.setServiceType(entry.getKey());
vp.setVisiteTotalTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", services.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}


/**
* 
本年度服务按服务类型查询细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailServicesThisMonthBysType")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailServicesThisMonthBysType(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;String serviceType = URLDecoder.decode(request.getParameter("serviceType"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Object> paras =new ArrayList<Object>(); 
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'  and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);;
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
List<AuditInfos> auInfos = util.getServiceDetailBysType(auditInfos, ydServiceInter, serviceType);
if(auInfos!=null &&auInfos.size()>0 ){
flag= true;int id=1;
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
for(AuditInfos ys :auInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setVisitId(ys.getUSERID());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}else{
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}	
} else {
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}

}

/**
* 本月服务按分中心实时访问
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
/**
* @param request
* @param response
* @throws IOException
*/
@RequestMapping(value = "/getAllServiceByCenterThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAllServiceByCenterThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);;
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> services = util.getServiceByCenter(auditInfos);
ArrayList<Service>  json = new ArrayList<Service>();
int id = 1;
Iterator<?> iter = services.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
Service vp = new Service();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisiteTotalTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", services.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}



}

/**
* 本年服务按分中心实时访问
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getAllServiceByCenterThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getAllServiceByCenterThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);;
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
Map<String,Integer> services = util.getServiceByCenter(auditInfos);
ArrayList<Service>  json = new ArrayList<Service>();
int id =(page-1)*rows+1;
Iterator<?> iter = services.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
Service vp = new Service();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisiteTotalTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", services.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}


/**
* 
本月服务查询细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailServicesThisMonthByprovider")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailServicesThisMonthByprovider(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and provider=? and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
	paras.add(provider);paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
	
	flag= true;int id=1;
	List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
	
		for(AuditInfos ys :auditInfos ){
			vAuditInfos v = new vAuditInfos();
			v.setId(id++);
			v.setVisitId(ys.getUSERID());
			v.setVisitIp(ys.getUSERIP());
			v.setAuditTime(sdf.format(ys.getAuditDate()));
			vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
	
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}
/**
* 
本月服务查询细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailServicesThisMonthBySid")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailServicesThisMonthBySid(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ? ";
try {
	Calendar cal = Calendar.getInstance();   cal.setTime(d);
	String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
	Date d_start = sdf.parse(start);
	String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
	Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
flag= true;int id=1;
List<AuditInfos> ainfos =util.getAuditDetailByCenter(auditInfos,provider);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :ainfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setVisitId(ys.getUSERID());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}
} catch (Exception e) {
String json = "[]";
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", 0);
map.put("rows",json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}}

/**
 * 各分中心历史访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedHistoryStaticByOrgchart")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedHistoryStaticByOrgchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceByCenter(auditInfos);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setProvider(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}

/**
 * 各分中心历史访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedHistoryStaticByOrg")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedHistoryStaticByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceByCenter(auditInfos);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setProvider(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}

/**
 * 服务类型历史访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedHistoryStaticBysTypechart")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedHistoryStaticBysTypechart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceBysType(auditInfos, ydServiceInter);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setServiceType(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}

/**
 * 服务类型历史访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedHistoryStaticBysType")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedHistoryStaticBysType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceBysType(auditInfos, ydServiceInter);
				ArrayList<Service>  json = new ArrayList<Service>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				Service vp = new Service();
				vp.setId(id++);
				vp.setServiceType(entry.getKey());
				vp.setVisiteTotalTime(entry.getValue());
				json.add(vp);}
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}

/**
 * 用户类型历史访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedHistoryStaticByuTypechart")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedHistoryStaticByuTypechart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceByuType(auditInfos, ydVisiterInter);
				ArrayList<vUser>  json = new ArrayList<vUser>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vUser vp = new vUser();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitedServiceTimes(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}

/**
 * 用户类型历史访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedHistoryStaticByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedHistoryStaticByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and AuditDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
			List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
			if (auditInfos != null && auditInfos.size() > 0) {
				Map<String,Integer> userTypes = util.getServiceByuType(auditInfos, ydVisiterInter);
				ArrayList<vUser>  json = new ArrayList<vUser>();
				int id = 1;
				Iterator<?> iter = userTypes.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vUser vp = new vUser();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitedServiceTimes(entry.getValue());
				json.add(vp);}
     			flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", userTypes.size());
				map.put("rows", util.getFenyeFiles(json, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				
			} else {
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
	
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}

/**
 * 
                 本年度各分中心服务查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailHistoryServicesByOrg")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailHistoryServicesByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05' and provider= ?  ";
	Date DateStart;
	Date DateEnd;paras.add(provider);
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and vs.visisteDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and vs.visisteDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
		
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setVisitId(ys.getUSERID());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

/**
 * 
                 本年度各服务类型查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailHistoryServicesBysType")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailHistoryServicesBysType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 int rows = 10;int page = 1;String serviceType = URLDecoder.decode(request.getParameter("serviceType"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'";
	Date DateStart;
	Date DateEnd;
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and vs.visisteDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and vs.visisteDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
		
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> auInfos = util.getServiceDetailBysType(auditInfos, ydServiceInter, serviceType);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setVisitId(ys.getUSERID());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

/**
 * 
                 本年度用户类型查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailHistoryServicesByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailHistoryServicesByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 int rows = 10;int page = 1;String userType = URLDecoder.decode(request.getParameter("userType"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='05'";
	Date DateStart;
	Date DateEnd;
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			hql = hql + "" + " and vs.visisteDate > ? ";
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
		
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			hql = hql + "" + " and vs.visisteDate < ? ";
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
		
			paras.add(DateEnd);
		}
		
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, userType);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setVisitId(ys.getUSERID());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

////////////////////////////////////////////////////用户///////////////////////////////////////////////////////////////




///**
// * 查询所有用户
// * 
// * @param request
// * @param response
// * @return
// */
//@SuppressWarnings({ "unchecked", "static-access" })
//@RequestMapping(value = "/findAllUserType")
//public void findAllUserType(HttpServletRequest request,
//		HttpServletResponse response) {
//	JsonConfig jsonConfig = new JsonConfig();
//	int rows = 10;
//	int page = 1;
//	if (request.getParameter("rows") != null
//			&& Integer.parseInt(request.getParameter("rows")) > 0)
//		rows = Integer.parseInt(request.getParameter("rows"));
//	if (request.getParameter("rows") != null
//			&& Integer.parseInt(request.getParameter("page")) > 0)
//		page = Integer.parseInt(request.getParameter("page"));
//	boolean flag = false;
//	String hql = " from  YDUser visiter where 1=1 ";
//	util util = new util();
//	try {
//		response.setContentType("text/json;charset=utf-8");	
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	List<YDUser> ydVisiters = ydVisiterInter.findAll(hql);
//	if (ydVisiters != null && ydVisiters.size() > 0) {
//		Map<String , vuserType> mapType = util.getAllUserTypes(ydVisiters);
//		
//		List<vuserType> json = new ArrayList<vuserType>();
//		Iterator<?> iter = mapType.entrySet().iterator();
//		while (iter.hasNext()) {
//		Map.Entry<String , vuserType> entry =  (Entry<String, vuserType>) iter.next();
//		json.add(entry.getValue());}
//		flag = true;			
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("success", flag);
//		map.put("total", json.size());
//		map.put("rows", util.getFenyeFiles(json, rows, page));
//		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//		util.writeJson(objects, request, response);
//	} else {
//        System.out.println("没有查询到用户");
//		List<vUser> visitors = new ArrayList<vUser>();
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("total", 0);
//		map.put("rows", visitors);
//		JSONObject object = JSONObject.fromObject(map);
//		object.fromObject(object, jsonConfig);
//		util.writeJson(object, request, response);
//
//	}
//}

/**
 * 本年度用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedStaticThisyearByuTypechart")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedStaticThisyearByuTypechart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 List<Date> paras =new ArrayList<Date>();Date d = new Date();
		String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
		String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
		String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
		String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
		String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
		String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
		String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
		try {
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
			List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
			List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
			List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
			List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
			List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
			List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
			Map<String,Integer> map_service = new HashMap<String, Integer>() ;
			Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
			Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_app =new HashMap<String, Integer>() ;
			int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
			if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
				map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
				Iterator<?> iter = map_service.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vuserType vp = new vuserType();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitServiceTimes(entry.getValue());
				vp.setVisitAppTimes(0);
				vp.setVisitAtlasTimes(0);
				vp.setVisitDataTimes(0);
				vp.setVisitProductionTimes(0);
				vp.setDownloadAtlasTime(0);
				vp.setSeartchTimes(0);
				json.add(vp);}
			} 

			if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
				
				map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
				Iterator<?> iter = map_production.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitProductionTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(entry.getValue());
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_production
			} 
			if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
				map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
				Iterator<?> iter = map_data.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitDataTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(entry.getValue());
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_data		
			     		
							
						} 
			if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
				map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
				Iterator<?> iter = map_atlas_visited.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAtlasTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(entry.getValue());
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_visited	
	     		
				
			} 
			if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
							
				map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
				Iterator<?> iter = map_atlas_download.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setDownloadAtlasTime(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(entry.getValue());
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download		
							
						} 
			if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
				
				map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
				Iterator<?> iter = map_search.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setSeartchTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(entry.getValue());
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
				
                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
				Iterator<?> iter = map_app.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAppTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(entry.getValue());
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 flag = true;
 				Map<String,Object> map = new HashMap<String,Object>();
 				map.put("success", flag);
 				map.put("total", json.size());
 				map.put("rows", json);
 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
 				util.writeJson(objects, request, response);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}


/**
 * 本年度用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedStaticThisyearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedStaticThisyearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 List<Date> paras =new ArrayList<Date>();Date d = new Date();
			String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
			String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
			String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
			String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
			String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
			String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
			String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
			try {
				String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
				Date d_start = sdf.parse(start);
				String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
				Date d_end = sdf.parse(end);
				paras.add(d_start);paras.add(d_end);
				List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
				List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
				List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
				List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
				List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
				List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
				List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
				Map<String,Integer> map_service = new HashMap<String, Integer>() ;
				Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
				Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_app =new HashMap<String, Integer>() ;
				int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
				if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
					map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
					Iterator<?> iter = map_service.entrySet().iterator();
					while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					vuserType vp = new vuserType();
					vp.setId(id++);
					vp.setTypeName(entry.getKey());
					vp.setVisitServiceTimes(entry.getValue());
					vp.setVisitAppTimes(0);
					vp.setVisitAtlasTimes(0);
					vp.setVisitDataTimes(0);
					vp.setVisitProductionTimes(0);
					vp.setDownloadAtlasTime(0);
					vp.setSeartchTimes(0);
					json.add(vp);}
				} 

				if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
					
					map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
					Iterator<?> iter = map_production.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitProductionTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(entry.getValue());
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_production
				} 
				if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
					map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
					Iterator<?> iter = map_data.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitDataTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(entry.getValue());
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_data		
				     		
								
							} 
				if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
					map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
					Iterator<?> iter = map_atlas_visited.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAtlasTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(entry.getValue());
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_visited	
		     		
					
				} 
				if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
								
					map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
					Iterator<?> iter = map_atlas_download.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setDownloadAtlasTime(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(entry.getValue());
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download		
								
							} 
				if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
					
					map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
					Iterator<?> iter = map_search.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setSeartchTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(entry.getValue());
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
					
	                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
					Iterator<?> iter = map_app.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAppTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(entry.getValue());
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 flag = true;
	 				Map<String,Object> map = new HashMap<String,Object>();
	 				map.put("success", flag);
	 				map.put("total", json.size());
	 				map.put("rows", util.getFenyeFiles(json, rows, page));
	 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
	 				util.writeJson(objects, request, response);
			} catch (Exception e) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				e.printStackTrace();
			}
		
	
}

/**
 * 
                 本年度用户访问服务查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitServiceDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitServiceDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName =URLDecoder.decode(request.getParameter("typeName"), "UTF-8"); 
	String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
		if (auditInfos_service != null && auditInfos_service.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_service, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本年度用户访问产品查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitProductionDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitProductionDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
		if (auditInfos_production != null && auditInfos_production.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_production, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本年度用户访问数据细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitDataDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitDataDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
		if (auditInfos_data != null && auditInfos_data.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_data, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本年度用户访问应用细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAppDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAppDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
		if (auditInfos_app != null && auditInfos_app.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_app, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本年度用户访问图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAtlasDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAtlasDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
		if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_visited, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本年度用户下载图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getdownloaddAtlasDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getdownloaddAtlasDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
		if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_download, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}
/**
 * 
                 本年度用户查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getSearchDetailstatic4yearByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getSearchDetailstatic4yearByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
		if (auditInfos_search != null && auditInfos_search.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_search, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/////////////////////////////////////////////////用户访问本年度//////////////////////////////////////////////////////////////////
////////////////////////////////////////////////用户访问本月/////////////////////////////////////////////////////////////////////
/**
 * 本月用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedStaticThisMonthByuTypechart")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedStaticThisMonthByuTypechart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 List<Date> paras =new ArrayList<Date>();Date d = new Date();
		String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
		String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
		String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
		String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
		String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
		String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
		String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
		try {
			Calendar cal = Calendar.getInstance();   cal.setTime(d);
			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
			Date d_start = sdf.parse(start);
			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
			Date d_end = sdf.parse(end);
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
			List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
			List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
			List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
			List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
			List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
			List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
			Map<String,Integer> map_service = new HashMap<String, Integer>() ;
			Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
			Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_app =new HashMap<String, Integer>() ;
			int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
			if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
				map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
				Iterator<?> iter = map_service.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vuserType vp = new vuserType();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitServiceTimes(entry.getValue());
				vp.setVisitAppTimes(0);
				vp.setVisitAtlasTimes(0);
				vp.setVisitDataTimes(0);
				vp.setVisitProductionTimes(0);
				vp.setDownloadAtlasTime(0);
				vp.setSeartchTimes(0);
				json.add(vp);}
			} 

			if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
				
				map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
				Iterator<?> iter = map_production.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitProductionTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(entry.getValue());
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_production
			} 
			if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
				map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
				Iterator<?> iter = map_data.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitDataTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(entry.getValue());
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_data		
			     		
							
						} 
			if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
				map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
				Iterator<?> iter = map_atlas_visited.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAtlasTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(entry.getValue());
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_visited	
	     		
				
			} 
			if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
							
				map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
				Iterator<?> iter = map_atlas_download.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setDownloadAtlasTime(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(entry.getValue());
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download		
							
						} 
			if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
				
				map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
				Iterator<?> iter = map_search.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setSeartchTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(entry.getValue());
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
				
                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
				Iterator<?> iter = map_app.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAppTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(entry.getValue());
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 flag = true;
 				Map<String,Object> map = new HashMap<String,Object>();
 				map.put("success", flag);
 				map.put("total", json.size());
 				map.put("rows", json);
 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
 				util.writeJson(objects, request, response);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}


/**
 * 本月用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedStaticThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedStaticThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		 List<Date> paras =new ArrayList<Date>();Date d = new Date();
			String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
			String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
			String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
			String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
			String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
			String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
			String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
			try {
				Calendar cal = Calendar.getInstance();   cal.setTime(d);
				String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
				Date d_start = sdf.parse(start);
				String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
				Date d_end = sdf.parse(end);
				paras.add(d_start);paras.add(d_end);
				List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
				List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
				List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
				List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
				List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
				List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
				List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
				Map<String,Integer> map_service = new HashMap<String, Integer>() ;
				Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
				Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_app =new HashMap<String, Integer>() ;
				int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
				if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
					map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
					Iterator<?> iter = map_service.entrySet().iterator();
					while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					vuserType vp = new vuserType();
					vp.setId(id++);
					vp.setTypeName(entry.getKey());
					vp.setVisitServiceTimes(entry.getValue());
					vp.setVisitAppTimes(0);
					vp.setVisitAtlasTimes(0);
					vp.setVisitDataTimes(0);
					vp.setVisitProductionTimes(0);
					vp.setDownloadAtlasTime(0);
					vp.setSeartchTimes(0);
					json.add(vp);}
				} 

				if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
					
					map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
					Iterator<?> iter = map_production.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitProductionTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(entry.getValue());
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_production
				} 
				if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
					map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
					Iterator<?> iter = map_data.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitDataTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(entry.getValue());
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_data		
				     		
								
							} 
				if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
					map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
					Iterator<?> iter = map_atlas_visited.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAtlasTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(entry.getValue());
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_visited	
		     		
					
				} 
				if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
								
					map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
					Iterator<?> iter = map_atlas_download.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setDownloadAtlasTime(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(entry.getValue());
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download		
								
							} 
				if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
					
					map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
					Iterator<?> iter = map_search.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setSeartchTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(entry.getValue());
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
					
	                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
					Iterator<?> iter = map_app.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAppTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(entry.getValue());
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 flag = true;
	 				Map<String,Object> map = new HashMap<String,Object>();
	 				map.put("success", flag);
	 				map.put("total", json.size());
	 				map.put("rows", util.getFenyeFiles(json, rows, page));
	 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
	 				util.writeJson(objects, request, response);
			} catch (Exception e) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				e.printStackTrace();
			}
		
	
}

/**
 * 
                 本月用户访问服务查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitServiceDetailThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitServiceDetailThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
		if (auditInfos_service != null && auditInfos_service.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_service, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本月用户访问产品查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitProductionDetailThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitProductionDetailThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
		if (auditInfos_production != null && auditInfos_production.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_production, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本月用户访问数据细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitDataDetailThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitDataDetailThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
		if (auditInfos_data != null && auditInfos_data.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_data, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本月用户访问应用细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAppDetaiThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAppDetaiThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
		if (auditInfos_app != null && auditInfos_app.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_app, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本月用户访问图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAtlasDetailThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAtlasDetailThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
		if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_visited, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本月用户下载图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getdownloaddAtlasDetailThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getdownloaddAtlasDetailThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
		if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_download, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}
/**
 * 
                 本月用户查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getSearchDetailThisMonthByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getSearchDetailThisMonthByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
		if (auditInfos_search != null && auditInfos_search.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_search, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}
////////////////////////////////////////////////用户访问本月////////////////////////////////////////////////////////////
////////////////////////////////////////////////用户访问本日////////////////////////////////////////////////////////////
/**
 * 本日用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedStaticTodayByuTypechart")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedStaticTodayByuTypechart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 List<Date> paras =new ArrayList<Date>();Date d = new Date();
		String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
		String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
		String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
		String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
		String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
		String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
		String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
		try {
			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
			paras.add(d_start);paras.add(d_end);
			List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
			List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
			List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
			List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
			List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
			List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
			List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
			Map<String,Integer> map_service = new HashMap<String, Integer>() ;
			Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
			Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_app =new HashMap<String, Integer>() ;
			int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
			if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
				map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
				Iterator<?> iter = map_service.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vuserType vp = new vuserType();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitServiceTimes(entry.getValue());
				vp.setVisitAppTimes(0);
				vp.setVisitAtlasTimes(0);
				vp.setVisitDataTimes(0);
				vp.setVisitProductionTimes(0);
				vp.setDownloadAtlasTime(0);
				vp.setSeartchTimes(0);
				json.add(vp);}
			} 

			if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
				
				map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
				Iterator<?> iter = map_production.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitProductionTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(entry.getValue());
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_production
			} 
			if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
				map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
				Iterator<?> iter = map_data.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitDataTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(entry.getValue());
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_data		
			     		
							
						} 
			if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
				map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
				Iterator<?> iter = map_atlas_visited.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAtlasTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(entry.getValue());
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_visited	
	     		
				
			} 
			if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
							
				map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
				Iterator<?> iter = map_atlas_download.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setDownloadAtlasTime(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(entry.getValue());
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download		
							
						} 
			if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
				
				map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
				Iterator<?> iter = map_search.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setSeartchTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(entry.getValue());
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
				
                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
				Iterator<?> iter = map_app.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAppTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(entry.getValue());
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 flag = true;
 				Map<String,Object> map = new HashMap<String,Object>();
 				map.put("success", flag);
 				map.put("total", json.size());
 				map.put("rows", json);
 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
 				util.writeJson(objects, request, response);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}


/**
 * 本日用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/visitedStaticTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void visitedStaticTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 List<Date> paras =new ArrayList<Date>();Date d = new Date();
			String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
			String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
			String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
			String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
			String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
			String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
			String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
			try {
				Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
				Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
				paras.add(d_start);paras.add(d_end);
				List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
				List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
				List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
				List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
				List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
				List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
				List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
				Map<String,Integer> map_service = new HashMap<String, Integer>() ;
				Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
				Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_app =new HashMap<String, Integer>() ;
				int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
				if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
					map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
					Iterator<?> iter = map_service.entrySet().iterator();
					while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					vuserType vp = new vuserType();
					vp.setId(id++);
					vp.setTypeName(entry.getKey());
					vp.setVisitServiceTimes(entry.getValue());
					vp.setVisitAppTimes(0);
					vp.setVisitAtlasTimes(0);
					vp.setVisitDataTimes(0);
					vp.setVisitProductionTimes(0);
					vp.setDownloadAtlasTime(0);
					vp.setSeartchTimes(0);
					json.add(vp);}
				} 

				if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
					
					map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
					Iterator<?> iter = map_production.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitProductionTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(entry.getValue());
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_production
				} 
				if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
					map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
					Iterator<?> iter = map_data.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitDataTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(entry.getValue());
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_data		
				     		
								
							} 
				if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
					map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
					Iterator<?> iter = map_atlas_visited.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAtlasTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(entry.getValue());
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_visited	
		     		
					
				} 
				if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
								
					map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
					Iterator<?> iter = map_atlas_download.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setDownloadAtlasTime(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(entry.getValue());
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download		
								
							} 
				if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
					
					map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
					Iterator<?> iter = map_search.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setSeartchTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(entry.getValue());
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
					
	                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
					Iterator<?> iter = map_app.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAppTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(entry.getValue());
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 flag = true;
	 				Map<String,Object> map = new HashMap<String,Object>();
	 				map.put("success", flag);
	 				map.put("total", json.size());
	 				map.put("rows", util.getFenyeFiles(json, rows, page));
	 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
	 				util.writeJson(objects, request, response);
			} catch (Exception e) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				e.printStackTrace();
			}
		
	
}

/**
 * 
                 本日用户访问服务查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitServiceDetailTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitServiceDetailTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
		if (auditInfos_service != null && auditInfos_service.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_service, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本日用户访问产品查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitProductionDetailTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitProductionDetailTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
		if (auditInfos_production != null && auditInfos_production.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_production, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本日用户访问数据细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitDataDetailTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitDataDetailTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
		if (auditInfos_data != null && auditInfos_data.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_data, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本日用户访问应用细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAppDetaiTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAppDetaiTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
		if (auditInfos_app != null && auditInfos_app.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_app, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本日用户访问图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAtlasDetailTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAtlasDetailTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
		if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_visited, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 本日用户下载图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getdownloaddAtlasDetailTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getdownloaddAtlasDetailTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
		if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_download, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}
/**
 * 
                 本日用户查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getSearchDetailTodayByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getSearchDetailTodayByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' and AuditDate > ? and AuditDate < ?";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
		if (auditInfos_search != null && auditInfos_search.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_search, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

///////////////////////用户访问本日//////////////////////////////////////////////////////////////////////////////
///////////////////////用户访问历史记录///////////////////////////////////////////////////////////////////////////


/**
 * 历史用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/UservisitedHistoryByuTypechart")
@ResponseBody
// 将json对象直接转换成字符串
public void UservisitedHistoryByuTypechart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date DateStart;
	Date DateEnd;
	 List<Date> paras =new ArrayList<Date>();
		String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
		String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' ";
		String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' ";
		String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' ";
		String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' ";
		String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
		String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' ";
		try {
			if (request.getParameter("DateStart") != null
					&& !request.getParameter("DateStart").trim()
							.equals("")) {

				String registerDateStart_temp = request
						.getParameter("DateStart");
				DateStart = sdf.parse(registerDateStart_temp);
				hql_service = hql_service + "" + " and AuditDate > ? ";
				hql_production = hql_production + "" + " and AuditDate > ? ";
				hql_data = hql_data + "" + " and AuditDate > ? ";
				hql_app = hql_app + "" + " and AuditDate > ? ";
				hql_production = hql_production + "" + " and AuditDate > ? ";
				hql_atlas_visited = hql_atlas_visited + "" + " and AuditDate > ? ";
				hql_atlas_downlaod = hql_atlas_downlaod + "" + " and AuditDate > ? ";
				hql_search = hql_search + "" + " and AuditDate > ? ";
				paras.add(DateStart);
			}
			if (request.getParameter("DateEnd") != null
					&& !request.getParameter("DateEnd").trim()
							.equals("")) {
				String registerDateEnd_temp = request
						.getParameter("DateEnd");
				DateEnd = sdf.parse(registerDateEnd_temp);
				hql_service = hql_service + "" + " and AuditDate < ? ";
				hql_production = hql_production + "" + " and AuditDate < ? ";
				hql_data = hql_data + "" + " and AuditDate < ? ";
				hql_app = hql_app + "" + " and AuditDate < ? ";
				hql_production = hql_production + "" + " and AuditDate < ? ";
				hql_atlas_visited = hql_atlas_visited + "" + " and AuditDate < ? ";
				hql_atlas_downlaod = hql_atlas_downlaod + "" + " and AuditDate < ? ";
				hql_search = hql_search + "" + " and AuditDate > ? ";
				paras.add(DateEnd);
			}
			List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
			List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
			List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
			List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
			List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
			List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
			List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
			Map<String,Integer> map_service = new HashMap<String, Integer>() ;
			Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
			Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
			Map<String,Integer> map_app =new HashMap<String, Integer>() ;
			int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
			if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
				map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
				Iterator<?> iter = map_service.entrySet().iterator();
				while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
				vuserType vp = new vuserType();
				vp.setId(id++);
				vp.setTypeName(entry.getKey());
				vp.setVisitServiceTimes(entry.getValue());
				vp.setVisitAppTimes(0);
				vp.setVisitAtlasTimes(0);
				vp.setVisitDataTimes(0);
				vp.setVisitProductionTimes(0);
				vp.setDownloadAtlasTime(0);
				vp.setSeartchTimes(0);
				json.add(vp);}
			} 

			if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
				
				map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
				Iterator<?> iter = map_production.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitProductionTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(entry.getValue());
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_production
			} 
			if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
				map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
				Iterator<?> iter = map_data.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitDataTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(entry.getValue());
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_data		
			     		
							
						} 
			if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
				map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
				Iterator<?> iter = map_atlas_visited.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAtlasTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(entry.getValue());
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_visited	
	     		
				
			} 
			if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
							
				map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
				Iterator<?> iter = map_atlas_download.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setDownloadAtlasTime(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(entry.getValue());
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download		
							
						} 
			if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
				
				map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
				Iterator<?> iter = map_search.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setSeartchTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(0);
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(entry.getValue());
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
				
                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
				Iterator<?> iter = map_app.entrySet().iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					if(json!=null && json.size()>0){
						boolean fag = true;
						for(vuserType v : json){
							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
								v.setVisitAppTimes(entry.getValue());
								fag = false;
							}	
							}//for
						if(fag){
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
					}else{
						vuserType v2 = new vuserType();
						v2.setId(id++);
						v2.setTypeName(entry.getKey());
						v2.setVisitServiceTimes(0);
						v2.setVisitAppTimes(entry.getValue());
						v2.setVisitAtlasTimes(0);
						v2.setVisitDataTimes(0);
						v2.setVisitProductionTimes(0);
						v2.setDownloadAtlasTime(0);
						v2.setSeartchTimes(0);
						json.add(v2);
					}
					}//	map_atlas_download	
				
			} 
                 flag = true;
 				Map<String,Object> map = new HashMap<String,Object>();
 				map.put("success", flag);
 				map.put("total", json.size());
 				map.put("rows", json);
 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
 				util.writeJson(objects, request, response);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			e.printStackTrace();
		}
	
}


/**
 * 历史用户类型访问统计
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/UservisitedHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void UservisitedHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rows = 10;int page = 1;
		if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Date> paras =new ArrayList<Date>();
		Date DateStart;
		Date DateEnd;
			String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
			String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' ";
			String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' ";
			String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' ";
			String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' ";
			String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
			String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' ";
			try {
				if (request.getParameter("DateStart") != null
						&& !request.getParameter("DateStart").trim()
								.equals("")) {

					String registerDateStart_temp = request
							.getParameter("DateStart");
					DateStart = sdf.parse(registerDateStart_temp);
					hql_service = hql_service + "" + " and AuditDate > ? ";
					hql_production = hql_production + "" + " and AuditDate > ? ";
					hql_data = hql_data + "" + " and AuditDate > ? ";
					hql_app = hql_app + "" + " and AuditDate > ? ";
					hql_production = hql_production + "" + " and AuditDate > ? ";
					hql_atlas_visited = hql_atlas_visited + "" + " and AuditDate > ? ";
					hql_atlas_downlaod = hql_atlas_downlaod + "" + " and AuditDate > ? ";
					hql_search = hql_search + "" + " and AuditDate > ? ";
					paras.add(DateStart);
				}
				if (request.getParameter("DateEnd") != null
						&& !request.getParameter("DateEnd").trim()
								.equals("")) {
					String registerDateEnd_temp = request
							.getParameter("DateEnd");
					DateEnd = sdf.parse(registerDateEnd_temp);
					hql_service = hql_service + "" + " and AuditDate < ? ";
					hql_production = hql_production + "" + " and AuditDate < ? ";
					hql_data = hql_data + "" + " and AuditDate < ? ";
					hql_app = hql_app + "" + " and AuditDate < ? ";
					hql_production = hql_production + "" + " and AuditDate < ? ";
					hql_atlas_visited = hql_atlas_visited + "" + " and AuditDate < ? ";
					hql_atlas_downlaod = hql_atlas_downlaod + "" + " and AuditDate < ? ";
					hql_search = hql_search + "" + " and AuditDate > ? ";
					paras.add(DateEnd);
				}
				List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
				List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
				List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
				List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
				List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
				List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
				List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
				Map<String,Integer> map_service = new HashMap<String, Integer>() ;
				Map<String,Integer> map_production =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_data =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_atlas_visited = new HashMap<String, Integer>() ;
				Map<String,Integer> map_atlas_download =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_search =new HashMap<String, Integer>() ; 
				Map<String,Integer> map_app =new HashMap<String, Integer>() ;
				int id = 1;ArrayList<vuserType>  json = new ArrayList<vuserType>();
				if (auditInfos_service != null && auditInfos_service.size() > 0) {//服务访问量
					map_service = util.getServiceByuType(auditInfos_service, ydVisiterInter);
					Iterator<?> iter = map_service.entrySet().iterator();
					while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
					vuserType vp = new vuserType();
					vp.setId(id++);
					vp.setTypeName(entry.getKey());
					vp.setVisitServiceTimes(entry.getValue());
					vp.setVisitAppTimes(0);
					vp.setVisitAtlasTimes(0);
					vp.setVisitDataTimes(0);
					vp.setVisitProductionTimes(0);
					vp.setDownloadAtlasTime(0);
					vp.setSeartchTimes(0);
					json.add(vp);}
				} 

				if (auditInfos_production != null && auditInfos_production.size() > 0) {//产品访问量
					
					map_production = util.getvisiteTImesByuType(auditInfos_production, ydVisiterInter);
					Iterator<?> iter = map_production.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitProductionTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(entry.getValue());
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(entry.getValue());
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_production
				} 
				if (auditInfos_data != null && auditInfos_data.size() > 0) {//数据访问量
					map_data = util.getvisiteTImesByuType(auditInfos_data, ydVisiterInter);
					Iterator<?> iter = map_data.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitDataTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(entry.getValue());
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(entry.getValue());
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_data		
				     		
								
							} 
				if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {//图集访问量
					map_atlas_visited = util.getvisiteTImesByuType(auditInfos_atlas_visited, ydVisiterInter);
					Iterator<?> iter = map_atlas_visited.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAtlasTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(entry.getValue());
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(entry.getValue());
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_visited	
		     		
					
				} 
				if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {//图集下载量
								
					map_atlas_download = util.getvisiteTImesByuType(auditInfos_atlas_download, ydVisiterInter);
					Iterator<?> iter = map_atlas_download.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setDownloadAtlasTime(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(entry.getValue());
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(entry.getValue());
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download		
								
							} 
				if (auditInfos_search != null && auditInfos_search.size() > 0) {//用户搜索量
					
					map_search = util.getvisiteTImesByuType(auditInfos_search, ydVisiterInter);
					Iterator<?> iter = map_search.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setSeartchTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(0);
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(entry.getValue());
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(0);
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(entry.getValue());
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 if (auditInfos_app != null && auditInfos_app.size() > 0) {//应用访问量
					
	                	 map_app = util.getvisiteTImesByuType(auditInfos_app, ydVisiterInter);
					Iterator<?> iter = map_app.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
						if(json!=null && json.size()>0){
							boolean fag = true;
							for(vuserType v : json){
								if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
									v.setVisitAppTimes(entry.getValue());
									fag = false;
								}	
								}//for
							if(fag){
								vuserType v2 = new vuserType();
								v2.setId(id++);
								v2.setTypeName(entry.getKey());
								v2.setVisitServiceTimes(0);
								v2.setVisitAppTimes(entry.getValue());
								v2.setVisitAtlasTimes(0);
								v2.setVisitDataTimes(0);
								v2.setVisitProductionTimes(0);
								v2.setDownloadAtlasTime(0);
								v2.setSeartchTimes(0);
								json.add(v2);
							}
						}else{
							vuserType v2 = new vuserType();
							v2.setId(id++);
							v2.setTypeName(entry.getKey());
							v2.setVisitServiceTimes(0);
							v2.setVisitAppTimes(entry.getValue());
							v2.setVisitAtlasTimes(0);
							v2.setVisitDataTimes(0);
							v2.setVisitProductionTimes(0);
							v2.setDownloadAtlasTime(0);
							v2.setSeartchTimes(0);
							json.add(v2);
						}
						}//	map_atlas_download	
					
				} 
	                 flag = true;
	 				Map<String,Object> map = new HashMap<String,Object>();
	 				map.put("success", flag);
	 				map.put("total", json.size());
	 				map.put("rows", util.getFenyeFiles(json, rows, page));
	 				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
	 				util.writeJson(objects, request, response);
			} catch (Exception e) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
				e.printStackTrace();
			}
		
	
}

/**
 * 
                 历史用户访问服务查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/UservisitServiceDetailHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void UservisitServiceDetailHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	 String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_service = "from AuditInfos  where 1=1 and BUSSTYPE ='05' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_service = hql_service + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
			if (request.getParameter("DateEnd") != null
					&& !request.getParameter("DateEnd").trim()
							.equals("")) {
				String registerDateEnd_temp = request
						.getParameter("DateEnd");
				DateEnd = sdf.parse(registerDateEnd_temp);
				hql_service = hql_service + "" + " and AuditDate < ? ";
				paras.add(DateEnd);
			}
		List<AuditInfos> auditInfos_service = auditInfoServiceInter.findByList(hql_service, paras);
		if (auditInfos_service != null && auditInfos_service.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_service, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 历史用户访问产品查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitProductionDetailHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitProductionDetailHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_production = "from AuditInfos  where 1=1 and BUSSTYPE ='04' ";
	try {
		
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_production = hql_production + "" + " and AuditDate > ? ";
			paras.add(DateStart);	
		}
			if (request.getParameter("DateEnd") != null
					&& !request.getParameter("DateEnd").trim()
							.equals("")) {
				String registerDateEnd_temp = request
						.getParameter("DateEnd");
				DateEnd = sdf.parse(registerDateEnd_temp);
				hql_production = hql_production + "" + " and AuditDate < ? ";
				paras.add(DateEnd);
			}
		List<AuditInfos> auditInfos_production = auditInfoServiceInter.findByList(hql_production, paras);
		if (auditInfos_production != null && auditInfos_production.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_production, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 历史用户访问数据细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitDataDetailHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitDataDetailHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_data = "from AuditInfos  where 1=1 and BUSSTYPE ='03' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_data = hql_data + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql_data = hql_data + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos_data = auditInfoServiceInter.findByList(hql_data, paras);
		if (auditInfos_data != null && auditInfos_data.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_data, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 历史用户访问应用细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAppDetaiHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAppDetaiHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_app = "from AuditInfos  where 1=1 and BUSSTYPE ='02' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_app = hql_app + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql_app = hql_app + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		
		List<AuditInfos> auditInfos_app = auditInfoServiceInter.findByList(hql_app, paras);
		if (auditInfos_app != null && auditInfos_app.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_app, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 历史用户访问图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getvisitAtlasDetailHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getvisitAtlasDetailHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>(); String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_visited = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='02' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
		
			
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_atlas_visited = hql_atlas_visited + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			
			
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql_atlas_visited = hql_atlas_visited + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos_atlas_visited = auditInfoServiceInter.findByList(hql_atlas_visited, paras);
		if (auditInfos_atlas_visited != null && auditInfos_atlas_visited.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_visited, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 
                 历史用户下载图集细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getdownloaddAtlasDetailHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getdownloaddAtlasDetailHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	 String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_atlas_downlaod = "from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_atlas_downlaod = hql_atlas_downlaod + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql_atlas_downlaod = hql_atlas_downlaod + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos_atlas_download = auditInfoServiceInter.findByList(hql_atlas_downlaod, paras);
		if (auditInfos_atlas_download != null && auditInfos_atlas_download.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_atlas_download, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					System.out.println("ip="+ys.getUSERIP());
					System.out.println("url="+ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setUrl(ys.getURL());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}
/**
 * 
                 历史用户查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getSearchDetailHistoryByuType")
@ResponseBody
// 将json对象直接转换成字符串
public void getSearchDetailHistoryByuType(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	 String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	String hql_search = "from AuditInfos  where 1=1  and  BEHAVETYPE='03' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql_search = hql_search + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql_search = hql_search + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		
		List<AuditInfos> auditInfos_search = auditInfoServiceInter.findByList(hql_search, paras);
		if (auditInfos_search != null && auditInfos_search.size() > 0) {
			List<AuditInfos> auInfos = util.getVisitedDetailBysUype(auditInfos_search, ydVisiterInter, typeName);
			if(auInfos!=null &&auInfos.size()>0 ){
				flag= true;int id=1;
				List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
				for(AuditInfos ys :auInfos ){
					vAuditInfos v = new vAuditInfos();
					v.setId(id++);
					v.setUrl(ys.getURL());
					v.setVisitIp(ys.getUSERIP());
					v.setAuditTime(sdf.format(ys.getAuditDate()));
					vauditInfos.add(v);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", auInfos.size());
				map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}else{
				flag = true;
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("success", flag);
				map.put("total", 0);
				String json = "[]";
				map.put("rows", json);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			}	
		} else {
			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

//////////////////////////////////////////////////数据下载///////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////实时动态////////////////////////////////////////////////////////////////////
/**
 * 
                实时动态图集下载查询细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByprovider")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByprovider(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and provider= ?  ";
	try {
		paras.add(provider);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
/////////////////////////////////////////////////////////////当前累计//////////////////////////////////////////////////
/**
 * 本日图集下载按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeTodaychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	


}

/**
 * 本日图集下载按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
/**
 * @param request
 * @param response
 * @throws IOException
 */
@RequestMapping(value = "/getatlasDownloadByTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本日图集下载按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01'  ";
	try {
		flag= true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getAtlasDownloadDetailBysType(auditInfos, atlasServiceInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 本日图集下载按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeTodaychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本日图集下载按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本日图集下载按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByuTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByuTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01'and AuditDate > ? and AuditDate < ?  ";
	try {
		flag= true;
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/**
 * 本日图集下载按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderTodaychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本日图集下载按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本日图集下载按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByProviderToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByProviderToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");Date d = new Date();
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and provider= ? and AuditDate > ? and AuditDate < ? ";
	try {
		paras.add(provider);
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

//////////////////////////////////////////图集下载量本日/////////////////////////////////////////////////////////////////
//////////////////////////////////////////图集下载量本年/////////////////////////////////////////////////////////////////
/**
 * 本那年图集下载按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeThisYearchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	


}

/**
 * 本年图集下载按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本年图集下载按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01'and AuditDate > ? and AuditDate < ?   ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		flag= true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getAtlasDownloadDetailBysType(auditInfos, atlasServiceInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 本年图集下载按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeThisYearchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本年+图集下载按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本年图集下载按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByuTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByuTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?  ";
	try {
		flag= true;
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/**
 * 本年图集下载按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderThisYearchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本年图集下载按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本年图集下载按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByProviderThisyear")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByProviderThisyear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");Date d = new Date();
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and provider= ? and AuditDate > ? and AuditDate < ? ";
	try {
		paras.add(provider);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/////////////////////////////////////////////////////////图集下载本年/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////图集下载本月/////////////////////////////////////////////////////////////
/**
 * 本月图集下载按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本月图集下载按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本月图集下载按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01'and AuditDate > ? and AuditDate < ?   ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		flag= true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getAtlasDownloadDetailBysType(auditInfos, atlasServiceInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 本月图集下载按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本月图集下载按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本月图集下载按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByuTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByuTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ?  ";
	try {
		flag= true;
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/**
 * 本月图集下载按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本月图集下载按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setDownloadTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本月图集下载按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByProviderThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByProviderThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");Date d = new Date();
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' and provider= ? and AuditDate > ? and AuditDate < ? ";
	try {
		paras.add(provider);
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

//////////////////////////////////////////////////////////图集下载历史接口//////////////////////////////////////////////////////
/**
 * 历史图集下载按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeHistorychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 历史图集下载按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getAtlasDownloadByType(auditInfos,atlasServiceInter);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setAtlasType(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 
               历史图集下载按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date DateStart;
	 Date DateEnd;
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> afos = util.getAtlasDownloadDetailBysType(auditInfos, atlasServiceInter, typeName);
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :afos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 历史图集下载按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeHistorychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTimesByUserType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 历史图集下载按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByuTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByuTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTimesByUserType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setDownloadAtlasTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 
               历史图集下载按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByuTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByuTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date DateStart;
	 Date DateEnd;
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> afos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :afos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 历史图集下载按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderHistorychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 历史图集下载按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getatlasDownloadByProviderHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getatlasDownloadByProviderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vAtlas>  json = new ArrayList<vAtlas>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vAtlas vp = new vAtlas();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisiteTotalTime(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 
               历史图集下载按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAtlasDownloadByProviderHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAtlasDownloadByProviderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date DateStart;
	 Date DateEnd;
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='01' and  BEHAVETYPE='01' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> afos = util.getAuditDetailByCenter(auditInfos, provider);
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :afos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

////////////////////////////////////////访问应用/////////////////////////////////////////////////////////////////////
////////////////////////////////////////实时动态////////////////////////////////////////////////////////////////////
/**
 * 
                实时动态应用访问细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppByprovider")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppByprovider(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and provider= ?  ";
	try {
		paras.add(provider);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		flag= true;
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}
//////////////////////////////////////////////////////app当前累计////////////////////////////////////////////////////////
/////////////////////////////////////////////////////本日/////////////////////////////////////////////////////////////
/**
 * 本日访问app按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getappVisitedByTypeTodaychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getappVisitedByTypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos,appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	


}

/**
 * 本日app访问按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
/**
 * @param request
 * @param response
 * @throws IOException
 */
@RequestMapping(value = "/getappVisitedByTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getappVisitedByTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos,appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本日app访问按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		flag= true;
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getAppVisitedDetailBysType(auditInfos, appServiceInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 本日app访问按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisiteddByuTypeTodaychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisiteddByuTypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本日app访问按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本日app访问按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByuTypeToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByuTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?  ";
	try {
		flag= true;
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/**
 * 本日app访问按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderTodaychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本日app访问按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisiteddByProviderToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisiteddByProviderToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本日app访问按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAVisitedByProviderToday")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByProviderToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");Date d = new Date();
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and provider= ? and AuditDate > ? and AuditDate < ? ";
	try {
		paras.add(provider);
		Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
		Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

//////////////////////////////////////////app访问量本日/////////////////////////////////////////////////////////////////
//////////////////////////////////////////app访问量本年/////////////////////////////////////////////////////////////////
/**
 * 本年访问按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByTypeThisYearchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos, appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	


}

/**
 * 本年app访问按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos, appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本年app访问按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?   ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		flag= true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getAppVisitedDetailBysType(auditInfos, appServiceInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}

}

/**
 * 本年app访问按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeThisYearchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本年app访问按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本年app访问按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByuTypeThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByuTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ?  ";
	try {
		flag= true;
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> appAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :appAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/**
 * 本年app访问按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderThisYearchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本年app访问按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderThisYear")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本年app访问按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisiteddByProviderThisyear")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisiteddByProviderThisyear(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");Date d = new Date();
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and provider= ? and AuditDate > ? and AuditDate < ? ";
	try {
		paras.add(provider);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/////////////////////////////////////////////////////////app访问本年/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////app访问本月/////////////////////////////////////////////////////////////
/**
 * 本月app访问按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByTypeThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos, appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本月app访问按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos, appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本月app访问按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAVisitedByTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' and AuditDate > ? and AuditDate < ?   ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		flag= true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> appAuditInfos = util.getAppVisitedDetailBysType(auditInfos, appServiceInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :appAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 本月app访问按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本月app访问按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本月app访问按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAVisitedByuTypeThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByuTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();Date d = new Date();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ?  ";
	try {
		flag= true;
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			int id=1;
			List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :downloadAuditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
/**
 * 本月app访问按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderThisMonthchart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
	


}

/**
 * 本月app访问按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	Date d = new Date();
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Date> paras =new ArrayList<Date>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and AuditDate > ? and AuditDate < ? ";
	try {
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

/**
 * 
               本月app访问按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByProviderThisMonth")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByProviderThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");Date d = new Date();
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  and provider= ? and AuditDate > ? and AuditDate < ? ";
	try {
		paras.add(provider);
		Calendar cal = Calendar.getInstance();   cal.setTime(d);
		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
		Date d_start = sdf.parse(start);
		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
		Date d_end = sdf.parse(end);
		paras.add(d_start);paras.add(d_end);
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :auditInfos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

//////////////////////////////////////////////////////////app访问历史接口//////////////////////////////////////////////////////
/**
 * 历史app访问按类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByTypeHistorychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByTypeHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos, appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 历史app访问按类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getappVisiteddByType(auditInfos,appServiceInter);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setaType(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 
               历史app访问按类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date DateStart;
	 Date DateEnd;
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> afos = util.getAppVisitedDetailBysType(auditInfos, appServiceInter, typeName);
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :afos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 历史app访问按用户类型chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeHistorychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> apps = util.getvisiteTimesByUserType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = apps.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", apps.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 历史app访问载按用户类型
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByuTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByuTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getvisiteTimesByUserType(auditInfos,ydVisiterInter);
			ArrayList<vuserType>  json = new ArrayList<vuserType>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vuserType vp = new vuserType();
			vp.setId(id++);
			vp.setTypeName(entry.getKey());
			vp.setVisitAppTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 
               历史app访问按用户类型细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByuTypeHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByuTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date DateStart;
	 Date DateEnd;
	 int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> afos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :afos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}

/**
 * 历史app访问按供应商chart
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderHistorychart")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = atals.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", atals.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 历史app访问按供应商
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getAppVisitedByProviderHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getAppVisitedByProviderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<Date> paras =new ArrayList<Date>();
	Date DateStart;
	Date DateEnd;
	 int rows = 10;int page = 1;
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02'  ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		flag = true;
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			
			Map<String,Integer> app = util.getServiceByCenter(auditInfos);
			ArrayList<vApp>  json = new ArrayList<vApp>();
			int id = 1;
			Iterator<?> iter = app.entrySet().iterator();
			while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
			vApp vp = new vApp();
			vp.setId(id++);
			vp.setProvider(entry.getKey());
			vp.setVisitedTimes(entry.getValue());
			json.add(vp);}
 			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", app.size());
			map.put("rows", util.getFenyeFiles(json, rows, page));
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
			
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		e.printStackTrace();
	}
}

/**
 * 
               历史app访问按供应商细节
 * 
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping(value = "/getDetailAppVisitedByProviderHistory")
@ResponseBody
// 将json对象直接转换成字符串
public void getDetailAppVisitedByProviderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	JsonConfig jsonConfig = new JsonConfig();
	boolean flag = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 Date DateStart;
	 Date DateEnd;
	 int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
	 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
	 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	List<Object> paras =new ArrayList<Object>();
	String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='02' ";
	try {
		if (request.getParameter("DateStart") != null
				&& !request.getParameter("DateStart").trim()
						.equals("")) {
			String registerDateStart_temp = request
					.getParameter("DateStart");
			DateStart = sdf.parse(registerDateStart_temp);
			hql = hql + "" + " and AuditDate > ? ";
			paras.add(DateStart);
		}
		if (request.getParameter("DateEnd") != null
				&& !request.getParameter("DateEnd").trim()
						.equals("")) {
			String registerDateEnd_temp = request
					.getParameter("DateEnd");
			DateEnd = sdf.parse(registerDateEnd_temp);
			hql = hql + "" + " and AuditDate < ? ";
			paras.add(DateEnd);
		}
		List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
		if (auditInfos != null && auditInfos.size() > 0) {
			List<AuditInfos> afos = util.getAuditDetailByCenter(auditInfos, provider);
			flag= true;int id=1;
			
			List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();
			
			for(AuditInfos ys :afos ){
				vAuditInfos v = new vAuditInfos();
				v.setId(id++);
				v.setUrl(ys.getURL());
				v.setVisitIp(ys.getUSERIP());
				v.setAuditTime(sdf.format(ys.getAuditDate()));
				vauditInfos.add(v);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", auditInfos.size());
			map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", flag);
			map.put("total", 0);
			String json = "[]";
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		}

	} catch (Exception e) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("total", 0);
		String json = "[]";
		map.put("rows", json);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

}
///////////////////////////////////////////////////////////////app访问////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////元数据访问///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////实时动态////////////////////////////////////////////////////////////////////
/**
* 
实时动态元数据访问细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailmetaByprovider")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailmetaByprovider(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and provider= ?  ";
try {
paras.add(provider);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
flag= true;
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :auditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}
//////////////////////////////////////////////////////meta当前累计////////////////////////////////////////////////////////
/////////////////////////////////////////////////////本日/////////////////////////////////////////////////////////////
/**
* 本日访问meta按类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getametaVisitedByTypeTodaychart")
@ResponseBody
//将json对象直接转换成字符串
public void getametaVisitedByTypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getmetaVisiteddByType(auditInfos,ydDataInter);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}



}

/**
* 本日meta访问按类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/

@RequestMapping(value = "/getametaVisitedByTypeToday")
@ResponseBody
//将json对象直接转换成字符串
public void getametaVisitedByTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> metas = util.getmetaVisiteddByType(auditInfos,ydDataInter);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = metas.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", metas.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本日meta访问按类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByTypeToday")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
flag= true;
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;
List<AuditInfos> metaAuditInfos = util.getMetaVisitedDetailBysType(auditInfos, ydDataInter, typeName);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :metaAuditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}

/**
* 本日Meta访问按用户类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisiteddByuTypeTodaychart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisiteddByuTypeTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本日Meta访问按用户类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeToday")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本日meta访问按用户类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByuTypeToday")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByuTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?  ";
try {
flag= true;
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;
List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :downloadAuditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}
/**
* 本日Meta访问按供应商chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderTodaychart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderTodaychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ? ";
try {
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本日Meta访问按供应商
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderToday")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}


Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本日Meta访问按供应商细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByProviderToday")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByProviderToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");Date d = new Date();
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and provider= ? and AuditDate > ? and AuditDate < ? ";
try {
paras.add(provider);
Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
flag= true;int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :auditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}

//////////////////////////////////////////Meta访问量本日/////////////////////////////////////////////////////////////////
//////////////////////////////////////////Meta访问量本年/////////////////////////////////////////////////////////////////
/**
* 本年访问按类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByTypeThisYearchart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getmetaVisiteddByType(auditInfos, ydDataInter);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}



}

/**
* 本年Meta访问按类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByTypeThisYear")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

	Map<String,Integer> apps = util.getmetaVisiteddByType(auditInfos, ydDataInter);
	ArrayList<vData>  json = new ArrayList<vData>();
	int id = 1;
	Iterator<?> iter = apps.entrySet().iterator();
	while (iter.hasNext()) {
	@SuppressWarnings("unchecked")
	Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
	vData vp = new vData();
	vp.setId(id++);
	vp.setTypeName(entry.getKey());
	vp.setVisitedTime(entry.getValue());
	json.add(vp);}
	flag = true;
	Map<String,Object> map = new HashMap<String,Object>();
	map.put("success", flag);
	map.put("total", apps.size());
	map.put("rows", util.getFenyeFiles(json, rows, page));
	JSONObject objects = JSONObject.fromObject(map, jsonConfig);
	util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本年Meta访问按类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByTypeThisYear")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();Date d = new Date();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?   ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
flag= true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;
List<AuditInfos> downloadAuditInfos = util.getMetaVisitedDetailBysType(auditInfos, ydDataInter, typeName);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :downloadAuditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}

}

/**
* 本年Meta访问按用户类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeThisYearchart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本年Meta访问按用户类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeThisYear")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本年Meta访问按用户类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByuTypeThisYear")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByuTypeThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();Date d = new Date();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ?  ";
try {
flag= true;
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;
List<AuditInfos> appAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :appAuditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}
/**
* 本年Meta访问按供应商chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderThisYearchart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderThisYearchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> metas = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = metas.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", metas.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本年Meta访问按供应商
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderThisYear")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

	Map<String,Integer> metas = util.getServiceByCenter(auditInfos);
	ArrayList<vData>  json = new ArrayList<vData>();
	int id = 1;
	Iterator<?> iter = metas.entrySet().iterator();
	while (iter.hasNext()) {
	@SuppressWarnings("unchecked")
	Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
	vData vp = new vData();
	vp.setId(id++);
	vp.setProvider(entry.getKey());
	vp.setVisitedTime(entry.getValue());
	json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", metas.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本年Meta访问按供应商细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisiteddByProviderThisyear")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisiteddByProviderThisyear(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");Date d = new Date();
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and provider= ? and AuditDate > ? and AuditDate < ? ";
try {
paras.add(provider);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
flag= true;int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :auditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}
/////////////////////////////////////////////////////////Meta访问本年/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////Meta访问本月/////////////////////////////////////////////////////////////
/**
* 本月Meta访问按类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByTypeThisMonthchart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> metas = util.getmetaVisiteddByType(auditInfos, ydDataInter);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = metas.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", metas.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本月Meta访问按类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByTypeThisMonth")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

	Map<String,Integer> metas = util.getmetaVisiteddByType(auditInfos, ydDataInter);
	ArrayList<vData>  json = new ArrayList<vData>();
	int id = 1;
	Iterator<?> iter = metas.entrySet().iterator();
	while (iter.hasNext()) {
	@SuppressWarnings("unchecked")
	Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
	vData vp = new vData();
	vp.setId(id++);
	vp.setTypeName(entry.getKey());
	vp.setVisitedTime(entry.getValue());
	json.add(vp);}

flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", metas.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本月Meta访问按类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByTypeThisMonth")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();Date d = new Date();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03' and AuditDate > ? and AuditDate < ?   ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
flag= true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;
List<AuditInfos> appAuditInfos = util.getMetaVisitedDetailBysType(auditInfos, ydDataInter, typeName);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :appAuditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}

/**
* 本月meta访问按用户类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeThisMonthchart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本月meta访问按用户类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeThisMonth")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getvisiteTImesByuType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}
flag = true;
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本月meta访问按用户类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByuTypeThisMonth")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByuTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();Date d = new Date();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ?  ";
try {
flag= true;
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
int id=1;
List<AuditInfos> downloadAuditInfos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :downloadAuditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}
/**
* 本月meta访问按供应商chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderThisMonthchart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderThisMonthchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);flag = true;
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}



}

/**
* 本月meta访问按供应商
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderThisMonth")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
Date d = new Date();
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Date> paras =new ArrayList<Date>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and AuditDate > ? and AuditDate < ? ";
try {
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

}

/**
* 
本月meta访问按供应商细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByProviderThisMonth")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByProviderThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy");Date d = new Date();
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  and provider= ? and AuditDate > ? and AuditDate < ? ";
try {
paras.add(provider);
Calendar cal = Calendar.getInstance();   cal.setTime(d);
String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
Date d_start = sdf.parse(start);
String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
Date d_end = sdf.parse(end);
paras.add(d_start);paras.add(d_end);
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
flag= true;int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :auditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}

//////////////////////////////////////////////////////////meta访问历史接口//////////////////////////////////////////////////////
/**
* 历史meta访问按类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByTypeHistorychart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByTypeHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List<Date> paras =new ArrayList<Date>();
Date DateStart;
Date DateEnd;
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03' ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getmetaVisiteddByType(auditInfos, ydDataInter);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 历史meta访问按类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByTypeHistory")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List<Date> paras =new ArrayList<Date>();
Date DateStart;
Date DateEnd;
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> metas = util.getmetaVisiteddByType(auditInfos, ydDataInter);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = metas.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", metas.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 
历史meta访问按类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByTypeHistory")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date DateStart;
Date DateEnd;
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
List<AuditInfos> afos = util.getMetaVisitedDetailBysType(auditInfos, ydDataInter, typeName);
flag= true;int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :afos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}

/**
* 历史meta访问按用户类型chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeHistorychart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List<Date> paras =new ArrayList<Date>();
Date DateStart;
Date DateEnd;
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> apps = util.getvisiteTimesByUserType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = apps.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitAppTimes(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", apps.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 历史meta访问载按用户类型
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByuTypeHistory")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByuTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List<Date> paras =new ArrayList<Date>();
Date DateStart;
Date DateEnd;
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getvisiteTimesByUserType(auditInfos,ydVisiterInter);
ArrayList<vuserType>  json = new ArrayList<vuserType>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vuserType vp = new vuserType();
vp.setId(id++);
vp.setTypeName(entry.getKey());
vp.setVisitDataTimes(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 
历史meta访问按用户类型细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByuTypeHistory")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByuTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date DateStart;
Date DateEnd;
int rows = 10;int page = 1;String typeName = URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {
List<AuditInfos> afos = util.getVisitedDetailBysUype(auditInfos, ydVisiterInter, typeName);
flag= true;int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :afos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}

/**
* 历史meta访问按供应商chart
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
/**
 * @param request
 * @param response
 * @throws IOException
 */
@RequestMapping(value = "/getMetaVisitedByProviderHistorychart")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderHistorychart(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List<Date> paras =new ArrayList<Date>();
Date DateStart;
Date DateEnd;
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03' ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> atals = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = atals.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", atals.size());
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 历史meta访问按供应商
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getMetaVisitedByProviderHistory")
@ResponseBody
//将json对象直接转换成字符串
public void getMetaVisitedByProviderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
List<Date> paras =new ArrayList<Date>();
Date DateStart;
Date DateEnd;
int rows = 10;int page = 1;
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
String hql = " from AuditInfos  where 1=1 and BUSSTYPE ='03'  ";
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
flag = true;
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

Map<String,Integer> app = util.getServiceByCenter(auditInfos);
ArrayList<vData>  json = new ArrayList<vData>();
int id = 1;
Iterator<?> iter = app.entrySet().iterator();
while (iter.hasNext()) {
@SuppressWarnings("unchecked")
Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
vData vp = new vData();
vp.setId(id++);
vp.setProvider(entry.getKey());
vp.setVisitedTime(entry.getValue());
json.add(vp);}

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", app.size());
map.put("rows", util.getFenyeFiles(json, rows, page));
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);

} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
e.printStackTrace();
}
}

/**
* 
历史meta访问按供应商细节
* 
* @param request
* @param response
* @return
* @throws IOException 
*/
@RequestMapping(value = "/getDetailMetaVisitedByProviderHistory")
@ResponseBody
//将json对象直接转换成字符串
public void getDetailMetaVisitedByProviderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
JsonConfig jsonConfig = new JsonConfig();
boolean flag = false;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date DateStart;
Date DateEnd;
int rows = 10;int page = 1;String provider = URLDecoder.decode(request.getParameter("provider"), "UTF-8");
if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
List<Object> paras =new ArrayList<Object>();
String hql = " from AuditInfos  where 1=1 and provider=? and BUSSTYPE ='03' ";
paras.add(provider);
try {
if (request.getParameter("DateStart") != null
&& !request.getParameter("DateStart").trim()
.equals("")) {
String registerDateStart_temp = request
.getParameter("DateStart");
DateStart = sdf.parse(registerDateStart_temp);
hql = hql + "" + " and AuditDate > ? ";
paras.add(DateStart);
}
if (request.getParameter("DateEnd") != null
&& !request.getParameter("DateEnd").trim()
.equals("")) {
String registerDateEnd_temp = request
.getParameter("DateEnd");
DateEnd = sdf.parse(registerDateEnd_temp);
hql = hql + "" + " and AuditDate < ? ";
paras.add(DateEnd);
}
List<AuditInfos> auditInfos = auditInfoServiceInter.findByList(hql, paras);
if (auditInfos != null && auditInfos.size() > 0) {

flag= true;int id=1;

List<vAuditInfos>	vauditInfos = new ArrayList<vAuditInfos>();

for(AuditInfos ys :auditInfos ){
vAuditInfos v = new vAuditInfos();
v.setId(id++);
v.setUrl(ys.getURL());
v.setVisitIp(ys.getUSERIP());
v.setAuditTime(sdf.format(ys.getAuditDate()));
vauditInfos.add(v);
}
Map<String, Object> map = new HashMap<String, Object>();
map.put("success", flag);
map.put("total", auditInfos.size());
map.put("rows",util.getFenyeFiles(vauditInfos, rows, page) );
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
} else {

Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

} catch (Exception e) {
Map<String,Object> map = new HashMap<String,Object>();
map.put("success", flag);
map.put("total", 0);
String json = "[]";
map.put("rows", json);
JSONObject objects = JSONObject.fromObject(map, jsonConfig);
util.writeJson(objects, request, response);
}

}
}
