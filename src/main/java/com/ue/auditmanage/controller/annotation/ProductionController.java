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
import vo.vProduction;
import vo.vUser;

import com.ue.auditmanage.controller.service.AppServiceInter;
import com.ue.auditmanage.controller.service.AppTypeServiceInter;
import com.ue.auditmanage.controller.service.ProductServiceInter;
import com.ue.auditmanage.controller.service.ProductTypeServiceInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;

import entity.App;
import entity.AppType;
import entity.Atlas;
import entity.Production;
import entity.ProductionType;
import entity.YDUser;

/**
 * 引入Controller注解
 * 
 * @author riverplant
 * 
 */
// 类的注解
@Controller
@RequestMapping("/production")
/**
 * 配置了@RequestMapping("/user")之后，里面所有的new ModelAndView("/annotation","result",result);
 * 都不需要再写/user了
 * @author riverplant
 *
 */
public class ProductionController {
	@Resource(name = "ProductServiceImpl")
	private ProductServiceInter productServiceInter;
	@Resource(name = "ProductTypeServiceImpl")
	private ProductTypeServiceInter productTypeServiceInter;

	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	@Resource(name = "ydUserImpl")
	private YDUserInter ydUserInter;
	
	
	/**
	 * 各分中心发布产品统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllProductionByOrg")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllProductionByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = "from Production yd where 1=1 ";
		String hql_total = " select count(*) from Production  where 1=1 ";
      try {
    	  List<Production> productions = productServiceInter.findAllData(hql);
  		if(productions!=null && productions.size()>0){
  			Map<String, Integer> productionmap = util.geProductionByOrg(productions);
  			int id = 1;
  			Iterator<?> iter = productionmap.entrySet().iterator();
  			List<vUser> ss  = new ArrayList<vUser>();
  			while (iter.hasNext()) {
  			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			vUser user = new vUser();
  			user.setId(id++);
  			user.setUname(entry.getKey());
  			user.setNbrProduction(entry.getValue());
  			ss.add(user);
  			}
  			long total = productServiceInter.getTotal(hql_total, null);
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
	 * 产品分页查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllProductionsByFenye")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllProductionsByFenye(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
		String hql = "from Production yd where 1=1 ";
		String hql_total = " select count(*) from Production  where 1=1 ";
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
		List<Production> productions = productServiceInter.findDataByFenye(hql, paras, page, rows);
		List<vProduction> va  = new ArrayList<vProduction>();
		if (productions!=null && productions.size()>0) {
			
			for(Production s : productions){
				
				vProduction ser = new vProduction();
				ser.setId(s.getId());
				ser.setName(s.getName());
				ser.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				ser.setProid(s.getProid());
				ser.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				ser.setVisitedTimes(s.getVisitedTimes());
				ser.setProvider(s.getProvider().getUname());
				ser.setpType(s.getpType().getName());
				ser.setUrl(s.getUrl());
				va.add(ser);
			}
			
		long total = productServiceInter.getTotal(hql_total, null);
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
	 * 得到产品类型列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getProductionTypelist")
	public void getProductionTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from ProductionType where 1=1";
			List<ProductionType> productionTypes = productTypeServiceInter.findAllData(hql);
			boolean flag = false;
			String json = "[";
           if(productionTypes!=null && productionTypes.size()>0){
        	   for (int i = 0; i < productionTypes.size(); i++) {
   				
   				if (i == 0) {
   					ProductionType ut = productionTypes.get(i);
   					flag = true;
   					if(productionTypes.size()==1){
         					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
         							+ "\"text\":" + "\"" + ut.getName() + "\""
         							+ "," + "\"selected\":" + flag + "}";
             		  }else{
             			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" + ut.getName() + "\""
   							+ "," + "\"selected\":" + flag + "},";  
             		  }
   				} else if (i == productionTypes.size() - 1) {
   					ProductionType ut = productionTypes.get(i);
   					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
   							+ "\"text\":" + "\"" + ut.getName()
   							+ "\"}";

   				} else {
   					ProductionType ut = productionTypes.get(i);
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
	
	
	
	// 添加产品
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addProduction")
	@ResponseBody
	public void addProduction(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		String proid = request.getParameter("proid");
		String typeId = request.getParameter("typeId");
		String url = request.getParameter("url");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		try {
			YDUser provider = ydUserInter.getObject(YDUser.class, Integer.parseInt(request.getParameter("providerId")));
			ProductionType productionType = productTypeServiceInter.getObject(ProductionType.class, Integer.parseInt(typeId));
		if (productServiceInter.findByProid(proid) != null || productServiceInter.findByUrl(url)!=null) {
			Message ms = new Message();
			ms.setText("该产品id已存在,或服务地址已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			Production production = new Production();
			production.setProid(proid);
			production.setName(request.getParameter("name"));
			production.setUrl(url);
			production.setRegisterDate(new java.util.Date());
			production.setUpdateDate(new java.util.Date());
			production.setVisitedTimes(0);
			production.setpType(productionType);
			production.setProvider(provider);
			productServiceInter.save(production);
				flag = true;

				vProduction  va = new vProduction();
				va.setProid(production.getProid());
				
				JSONObject object = JSONObject.fromObject(va, jsonConfig);
				
				util.writeJson(object, flag, request, response);
			}
		} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加失败}");
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
	@RequestMapping(value = "/delProduction")
	public void delProduction(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<vProduction> vapps = new ArrayList<vProduction>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				Production production = productServiceInter.getObject(Production.class,Integer.parseInt(id));
				
				if (productServiceInter.deletById(Integer.parseInt(id))){
					vProduction va = new vProduction(); va.setProid(production.getProid());
					flag = true;vapps.add(va);
				}
			}
		}
		util.writeJson(JSONArray.fromObject(vapps), flag, request,
				response);
	}
	
	
	/**
	 * 跟新产品
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	/**
	 * @param request
	 * @param response
	 */
	/**
	 * @param request
	 * @param response
	 */
	/**
	 * @param request
	 * @param response
	 */
	/**
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateProduction")
	public void updateProduction(HttpServletRequest request,
			HttpServletResponse response) {
		boolean flag = false;
		util util = new util();
		String oldid = request.getParameter("oldid");
		String typeId = request.getParameter("typeId");
		String providerId = request.getParameter("providerId");
		response.setContentType("text/json;charset=utf-8");
		JsonConfig jsonConfig = util.getjsonConfig();
	
			try {
				Production production = productServiceInter.findByProid(oldid);
				production.setProid(request.getParameter("proid"));
				production.setUrl(request.getParameter("url"));
				production.setName(request.getParameter("name"));
				ProductionType productionType = productTypeServiceInter.getObject(ProductionType.class, Integer.parseInt(typeId));
				production.setpType(productionType);
				YDUser provider = ydUserInter.getObject(YDUser.class, Integer.parseInt(providerId));
				production.setProvider(provider);
				production.setUpdateDate(new java.util.Date());
				productServiceInter.update(production);
				flag = true;
				vProduction mo = new vProduction();
				mo.setProid(production.getProid());
				JSONObject object = JSONObject.fromObject(mo, jsonConfig);
				util.writeJson(object, flag, request, response);
			
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				util.writeJson("修改失败", flag, request, response);
			}
	
		
	}
}
