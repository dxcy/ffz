package com.ue.auditmanage.controller.annotation;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.util;
import vo.Message;
import vo.vApp;
import vo.vAtlas;
import vo.vUser;

import com.ue.auditmanage.controller.service.AppServiceInter;
import com.ue.auditmanage.controller.service.AppTypeServiceInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;

import entity.App;
import entity.AppType;
import entity.YDUser;

/**
 * 引入Controller注解
 * 
 * @author riverplant
 * 
 */
// 类的注解
@Controller
@RequestMapping("/app")
/**
 * 配置了@RequestMapping("/user")之后，里面所有的new ModelAndView("/annotation","result",result);
 * 都不需要再写/user了
 * @author riverplant
 *
 */
public class AppController {
	@Resource(name = "AppServiceImpl")
	private AppServiceInter appServiceInter;
	@Resource(name = "AppTypeServiceImpl")
	private AppTypeServiceInter appTypeServiceInter;

	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	@Resource(name = "ydUserImpl")
	private YDUserInter ydUserInter;
	
//	/**
//	 * 服务被访问Top10
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getTop3Service")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getTop10Service(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		PrintWriter writer = response.getWriter();
//		
//		String hql = "from YDService u order by u.visitedTime desc ";
//		try {
//			response.setContentType("text/json;charset=utf-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		List<YDService> services = ydServiceInter.findAllService(hql);
//		List<Service> ss  = new ArrayList<Service>();
//		if (services!=null && services.size()>0) {
//			int temp =services.size();
//			if(services.size()>3){
//				temp = 3;
//			}
//			for(int i=0;i<temp;i++){		
//				Service ser = new Service();
//				ser.setsId(services.get(i).getSid());
//				ser.setVisiteTotalTime(services.get(i).getVisitedTime());
//				ss.add(ser);
//			}
//		flag = true;
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("success", flag);
//		map.put("total", 3);
//		map.put("rows",ss );
//		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//		util.writeJson(objects, request, response);
//		} else {
//			String json = "[]";
//			writer.write(json);
//			writer.flush();
//			writer.close();
//
//		}
//
//	}
	
	/**
	 * 各分中心发布应用统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllappByOrg")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllAppByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = "from App yd where 1=1 ";
		String hql_total = " select count(*) from App  where 1=1 ";
      try {
    	  List<App> apps = appServiceInter.findAllData(hql);
  		if(apps!=null && apps.size()>0){
  			Map<String, Integer> appmap = util.getappByOrg(apps);
  			int id = 1;
  			Iterator<?> iter = appmap.entrySet().iterator();
  			List<vUser> ss  = new ArrayList<vUser>();
  			while (iter.hasNext()) {
  			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			vUser user = new vUser();
  			user.setId(id++);
  			user.setUname(entry.getKey());
  			user.setNbrApp(entry.getValue());
  			ss.add(user);
  			}
  			long total = appServiceInter.getTotal(hql_total, null);
  			flag = true;
  			Map<String, Object> map = new HashMap<String, Object>();
  			map.put("success", flag);
  			map.put("total", total);
  			map.put("rows",ss );
  			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
  			util.writeJson(objects, request, response);
  			
  		}else {
  			flag = true;List<vUser> ss  = new ArrayList<vUser>();
  			Map<String, Object> map = new HashMap<String, Object>();
  			map.put("success", flag);
  			map.put("total", 0);
  			map.put("rows",ss );
  			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
  			util.writeJson(objects, request, response);

  		}
	} catch (Exception e) {
		e.printStackTrace();
		flag = true;List<vUser> ss  = new ArrayList<vUser>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 0);
			map.put("rows",ss );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
	}	
	}
	
//	/**
//	 * 服务实时访问
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getAllService4chart")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getAllService4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;PrintWriter writer = response.getWriter();
//		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
//		Date registerDateEnd;
//		Date updateDateStart;
//		Date registerDateStart;
//		Date updateDateEnd;
//		String hql = "from YDService yd where 1=1 ";
//		String hql_total = " select count(*) from YDService  where 1=1 ";
//		List<Date> paras = new ArrayList<Date>();
//		try {
//			response.setContentType("text/json;charset=utf-8");
//
//			if (request.getParameter("surl") != null
//					&& !request.getParameter("surl").trim().equals("")) {
//				hql = hql + "" + " and yd.surl like '%"
//						+ request.getParameter("surl").trim() + "%' ";
//				hql_total = hql_total + "" + " and surl like '%"
//						+ request.getParameter("surl").trim() + "%' ";
//			}
//			if (request.getParameter("registerDateStart") != null
//					&& !request.getParameter("registerDateStart").trim()
//							.equals("")) {
//				hql = hql + "" + " and yd.registerDate > ? ";
//				hql_total = hql_total + "" + " and registerDate > ? ";
//				String registerDateStart_temp = request
//						.getParameter("registerDateStart");
//				registerDateStart = sdf_fmg.parse(registerDateStart_temp);
//
//				paras.add(registerDateStart);
//			}
//			if (request.getParameter("registerDateEnd") != null
//					&& !request.getParameter("registerDateEnd").trim()
//							.equals("")) {
//				hql = hql + " and yd.registerDate < ? ";
//				hql_total = hql_total + " and registerDate < ? ";
//				registerDateEnd = sdf_fmg.parse(request
//						.getParameter("registerDateEnd"));
//				paras.add(registerDateEnd);
//			}
//			if (request.getParameter("updateDateStart") != null
//					&& !request.getParameter("updateDateStart").trim()
//							.equals("")) {
//				hql = hql + "" + " and yd.updateDate > ? ";
//				hql_total = hql_total + "" + " and updateDate > ? ";
//				updateDateStart = sdf_fmg.parse(request
//						.getParameter("updateDateStart"));
//				paras.add(updateDateStart);
//			}
//			if (request.getParameter("updateDateEnd") != null
//					&& !request.getParameter("updateDateEnd").trim().equals("")) {
//				hql = hql + "" + " and yd.updateDate < ? ";
//				hql_total = hql_total + "" + " and updateDate < ? ";
//
//				updateDateEnd = sdf_fmg.parse(request
//						.getParameter("updateDateEnd"));
//				paras.add(updateDateEnd);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		List<YDService> services = ydServiceInter.findAllService(hql);
//		List<Service> ss  = new ArrayList<Service>();
//		if (services!=null && services.size()>0) {
//			
//			for(YDService s : services){
//				
//				Service ser = new Service();
//				ser.setId(s.getId());
//				ser.setsId(s.getSid());
//				ser.setVisiteTotalTime(s.getVisitedTime());
//				ss.add(ser);
//			}
//			
//		long total = ydServiceInter.getTotal(hql_total, null);
//		flag = true;
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("success", flag);
//		map.put("total", total);
//		map.put("rows",ss );
//		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//		util.writeJson(objects, request, response);
//		} else {
//			String json = "[]";
//			writer.write(json);
//			writer.flush();
//			writer.close();
//
//		}
//
//	}
	
	/**
	 * 应用分页查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllAppsByFenye")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllAppsByFenye(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;
		 int rows = 10;int page = 1;PrintWriter writer = response.getWriter();
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		String hql = "from App yd where 1=1 ";
		String hql_total = " select count(*) from App  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("url") != null
					&& !request.getParameter("url").trim().equals("")) {
				hql = hql + "" + " and yd.url like '%"
						+ request.getParameter("url").trim() + "%' ";
				hql_total = hql_total + "" + " and url like '%"
						+ request.getParameter("url").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and yd.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and yd.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and yd.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and yd.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<App> apps = appServiceInter.findDataByFenye(hql, paras, page, rows);
		List<vApp> va  = new ArrayList<vApp>();
		if (apps!=null && apps.size()>0) {
			
			for(App s : apps){
				
				vApp ser = new vApp();
				ser.setId(s.getId());
				ser.setName(s.getName());
				ser.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				ser.setAid(s.getAid());
				ser.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				ser.setVisitedTimes(s.getVisitedTimes());
				ser.setProvider(s.getProvider().getUname());
				ser.setaType(s.getaType().getName());
				ser.setUrl(s.getUrl());
				va.add(ser);
			}
			
		long total = appServiceInter.getTotal(hql_total, null);
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows",va );
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		} else {
			String json = "[]";
			writer.write(json);
			writer.flush();
			writer.close();

		}

	}

	/**
	 * 得到应用类型列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAppTypelist")
	public void getAtlasTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from AppType where 1=1";
			List<AppType> appTypes = appTypeServiceInter.findAllAppType(hql);
			boolean flag = false;
			String json = "[";
           if(appTypes!=null && appTypes.size()>0){
        	   for (int i = 0; i < appTypes.size(); i++) {
   				
   				if (i == 0) {
   					AppType ut = appTypes.get(i);
   					flag = true;
   					if(appTypes.size()==1){
         					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
         							+ "\"text\":" + "\"" + ut.getName() + "\""
         							+ "," + "\"selected\":" + flag + "}";
             		  }else{
             			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" + ut.getName() + "\""
   							+ "," + "\"selected\":" + flag + "},";  
             		  }
   				} else if (i == appTypes.size() - 1) {
   					AppType ut = appTypes.get(i);
   					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" + ut.getName()
   							+ "\"}";

   				} else {
   					AppType ut = appTypes.get(i);
   					json += "{\"id\":" + "\"" +  ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" +ut.getName()
   							+ "\"},";
   				}

   			} 
           }
			
			json += "]";
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	// 添加应用
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addApp")
	@ResponseBody
	public void addApp(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		String aid = request.getParameter("aid");
		String typeId = request.getParameter("typeId");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		try {
			YDUser provider = ydUserInter.getObject(YDUser.class, Integer.parseInt(request.getParameter("providerId")));
			AppType aType = appTypeServiceInter.getAppTypeById(AppType.class, Integer.parseInt(typeId));
		if (appServiceInter.findByAid(aid) != null ) {
			Message ms = new Message();
			ms.setText("该图集id已存在,或服务地址已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			App app = new App();
			
			app.setAid(aid);
			app.setName(request.getParameter("name"));
			app.setUrl(request.getParameter("url"));
			app.setRegisterDate(new java.util.Date());
			app.setUpdateDate(new java.util.Date());
			app.setVisitedTimes(0);
			
			app.setaType(aType);
			app.setProvider(provider);
			appServiceInter.save(app);
				flag = true;

				vApp  va = new vApp();
				va.setAid(app.getAid());
				
				JSONObject object = JSONObject.fromObject(va, jsonConfig);
				
				util.writeJson(object, flag, request, response);
			}
		} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加服务失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}
	
	
	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delApp")
	public void delApp(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<vApp> vapps = new ArrayList<vApp>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				App app = appServiceInter.getObject(App.class,Integer.parseInt(id));
				
				if (appServiceInter.deletById(Integer.parseInt(id))){
					vApp va = new vApp(); va.setAid(app.getAid());
					flag = true;vapps.add(va);
				}
			}
		}
		util.writeJson(JSONArray.fromObject(vapps), flag, request,
				response);
	}
	
	
	/**
	 * 跟新图集
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateApp")
	public void updateApp(HttpServletRequest request,
			HttpServletResponse response) {
		boolean flag = false;
		util util = new util();
		String oldid = request.getParameter("oldid");
		String typeId = request.getParameter("typeId");
		String providerId = request.getParameter("providerId");
		response.setContentType("text/json;charset=utf-8");
		JsonConfig jsonConfig = util.getjsonConfig();
	
			try {
				App app = appServiceInter.findByAid(oldid);
				app.setAid(request.getParameter("aid"));
				app.setUrl(request.getParameter("url"));
				app.setName(request.getParameter("name"));
				AppType aType = appTypeServiceInter.getAppTypeById(AppType.class, Integer.parseInt(typeId));
				app.setaType(aType);
				YDUser provider = ydUserInter.getObject(YDUser.class, Integer.parseInt(providerId));
				app.setProvider(provider);
				app.setUpdateDate(new java.util.Date());
				appServiceInter.update(app);
				flag = true;
				vAtlas mo = new vAtlas();
				mo.setAid(app.getAid());
				JSONObject object = JSONObject.fromObject(mo, jsonConfig);
				util.writeJson(object, flag, request, response);
			
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				util.writeJson("修改失败", flag, request, response);
			}
	
		
	}
}
