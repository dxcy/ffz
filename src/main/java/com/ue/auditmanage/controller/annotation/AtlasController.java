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
import vo.vAtlas;
import vo.vUser;

import com.ue.auditmanage.controller.service.AtlasServiceInter;
import com.ue.auditmanage.controller.service.AtlasTypeServiceInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;

import entity.Atlas;
import entity.YDUser;
import entity.atlasType;

/**
 * 引入Controller注解
 * 
 * @author riverplant
 * 
 */
// 类的注解
@Controller
@RequestMapping("/atlas")
/**
 * 配置了@RequestMapping("/user")之后，里面所有的new ModelAndView("/annotation","result",result);
 * 都不需要再写/user了
 * @author riverplant
 *
 */
public class AtlasController {
	@Resource(name = "AtlasServiceImpl")
	private AtlasServiceInter atlasServiceInter;
	@Resource(name = "AtlasTypeServiceImpl")
	private AtlasTypeServiceInter atlasTypeServiceInter;

	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	@Resource(name = "ydUserImpl")
	private YDUserInter ydUserInter;
	
	/**
	 * 各分中心发布图集统计chart
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllatlasByOrgchart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllatlasByOrgchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = "from Atlas yd where 1=1 ";
		String hql_total = " select count(*) from Atlas  where 1=1 ";
      try {
    	  List<Atlas> atlas = atlasServiceInter.findAllatlas(hql);
  		if(atlas!=null && atlas.size()>0){
  			Map<String, Integer> atlasmap = util.getatlasDownloadTimesByOrg(atlas);
  			int id = 1;
  			Iterator<?> iter = atlasmap.entrySet().iterator();
  			List<vAtlas> ss  = new ArrayList<vAtlas>();
  			while (iter.hasNext()) {
  			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			vAtlas va = new vAtlas();
  			va.setId(id++);
  			va.setProvider(entry.getKey());
  			va.setDownloadTime(entry.getValue());
  			ss.add(va);
  			}
  			long total = atlasServiceInter.getAtlasTotal(hql_total, null);
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
	
	/**
	 * 各分中心发布图集统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllatlasByOrg")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllatlasByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		String hql = "from Atlas yd where 1=1 ";
		String hql_total = " select count(*) from Atlas  where 1=1 ";
      try {
    	  List<Atlas> atlas = atlasServiceInter.findAllatlas(hql);
  		if(atlas!=null && atlas.size()>0){
  			Map<String, Integer> atlasmap = util.getatlasDownloadTimesByOrg(atlas);
  			int id = 1;
  			Iterator<?> iter = atlasmap.entrySet().iterator();
  			List<vAtlas> ss  = new ArrayList<vAtlas>();
  			while (iter.hasNext()) {
  			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			vAtlas va = new vAtlas();
  			va.setId(id++);
  			va.setProvider(entry.getKey());
  			va.setDownloadTime(entry.getValue());
  			ss.add(va);
  			}
  			long total = atlasServiceInter.getAtlasTotal(hql_total, null);
  			flag = true;
  			Map<String, Object> map = new HashMap<String, Object>();
  			map.put("success", flag);
  			map.put("total", total);
  			map.put("rows",util.getFenyeFiles(ss, rows, page) );
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
//	
	/**
	 * 图集分页查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllAtlas")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllAtlas(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
		String hql = "from Atlas yd where 1=1 ";
		String hql_total = " select count(*) from Atlas  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("aurl") != null
					&& !request.getParameter("aurl").trim().equals("")) {
				hql = hql + "" + " and yd.aUrl like '%"
						+ request.getParameter("aurl").trim() + "%' ";
				hql_total = hql_total + "" + " and aUrl like '%"
						+ request.getParameter("aurl").trim() + "%' ";
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
		List<Atlas> atlass = atlasServiceInter.findByFenyeatlas(hql, paras, page, rows);
		List<vAtlas> va  = new ArrayList<vAtlas>();
		if (atlass!=null && atlass.size()>0) {
			
			for(Atlas s : atlass){
				
				vAtlas ser = new vAtlas();
				ser.setId(s.getId());
				ser.setName(s.getName());
				ser.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				ser.setAid(s.getAid());
				ser.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				ser.setVisiteTotalTime(s.getVisitedTimes());
				ser.setDownloadTime(s.getDownloadTime());
				ser.setProvider(s.getProvider().getUname());
				ser.setAtlasType(s.getaType().getName());
				ser.setAurl(s.getaUrl());
				va.add(ser);
			}
			
		long total = atlasServiceInter.getAtlasTotal(hql_total, null);
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
	 * 得到图集类型列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAtlasTypelist")
	public void getAtlasTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from atlasType  where 1=1";
			List<atlasType> atlasTypes = atlasTypeServiceInter.findAllatlasType(hql);
			boolean flag = false;
			String json = "[";
           if(atlasTypes!=null && atlasTypes.size()>0){
        	   for (int i = 0; i < atlasTypes.size(); i++) {
   				
   				if (i == 0) {
   					atlasType ut = atlasTypes.get(i);
   					flag = true;
   					if(atlasTypes.size()==1){
         					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
         							+ "\"text\":" + "\"" + ut.getName() + "\""
         							+ "," + "\"selected\":" + flag + "}";
             		  }else{
             			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" + ut.getName() + "\""
   							+ "," + "\"selected\":" + flag + "},";  
             		  }
   				} else if (i == atlasTypes.size() - 1) {
   					atlasType ut = atlasTypes.get(i);
   					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" + ut.getName()
   							+ "\"}";

   				} else {
   					atlasType ut = atlasTypes.get(i);
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
	
	
	
	// 添加图集
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addAtlas")
	@ResponseBody
	public void addAtlas(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		String aid = request.getParameter("aid");
		String typeId = request.getParameter("typeId");
		String url =request.getParameter("aurl");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		try {
			YDUser provider = ydUserInter.getObject(YDUser.class, Integer.parseInt(request.getParameter("providerId")));
			atlasType aType = atlasTypeServiceInter.getAtlasTypeById(atlasType.class, Integer.parseInt(typeId));
		if (atlasServiceInter.findByUrl(url)!= null ) {
			Message ms = new Message();
			ms.setText("该图集url已存在,或服务地址已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			Atlas atlas = new Atlas();
			
			atlas.setAid(aid);
			atlas.setName(request.getParameter("name"));
			atlas.setaUrl(url);
			atlas.setRegisterDate(new java.util.Date());
			atlas.setUpdateDate(new java.util.Date());
			atlas.setVisitedTimes(0);atlas.setDownloadTime(0);
			
			atlas.setaType(aType);
			atlas.setProvider(provider);
			atlasServiceInter.saveAtlas(atlas);
				flag = true;

				vAtlas  va = new vAtlas();
				va.setAid(atlas.getAid());
				
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
	 * 删除图集
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delAtlas")
	public void delAtlas(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<vAtlas> vatlas = new ArrayList<vAtlas>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				String hql = "from Atlas ys where 1=1 and ys.id= ? ";
				List<Object> param = new ArrayList<Object>();
				param.add(Integer.parseInt(id));
		
			
				Atlas atlas = atlasServiceInter.getAtlasById(Atlas.class,Integer.parseInt(id));
				
				if (atlasServiceInter.deletById(Integer.parseInt(id))){
					vAtlas va = new vAtlas(); va.setAid(atlas.getAid());
					flag = true;vatlas.add(va);
				}
			}
		}
		util.writeJson(JSONArray.fromObject(vatlas), flag, request,
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
	@RequestMapping(value = "/updateAtlas")
	public void updateAtlas(HttpServletRequest request,
			HttpServletResponse response) {
		boolean flag = false;
		util util = new util();
		String oldid = request.getParameter("oldid");
		String typeId = request.getParameter("typeId");
		String providerId = request.getParameter("providerId");
		response.setContentType("text/json;charset=utf-8");
		JsonConfig jsonConfig = util.getjsonConfig();
	
			try {
				Atlas atlas = atlasServiceInter.findByUrl(oldid);
				atlas.setAid(request.getParameter("aid"));
				atlas.setaUrl(request.getParameter("aurl"));
				atlas.setName(request.getParameter("name"));
				atlasType aType = atlasTypeServiceInter.getAtlasTypeById(atlasType.class, Integer.parseInt(typeId));
				atlas.setaType(aType);
				YDUser provider = ydUserInter.getObject(YDUser.class, Integer.parseInt(providerId));
				atlas.setProvider(provider);
				atlas.setUpdateDate(new java.util.Date());
				atlasServiceInter.updateAtlas(atlas);
				flag = true;
				vAtlas mo = new vAtlas();
				mo.setAid(atlas.getAid());
				JSONObject object = JSONObject.fromObject(mo, jsonConfig);
				util.writeJson(object, flag, request, response);
			
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				util.writeJson("修改失败", flag, request, response);
			}
	
		
	}
}
