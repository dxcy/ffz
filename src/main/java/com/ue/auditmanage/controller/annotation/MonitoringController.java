package com.ue.auditmanage.controller.annotation;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vo.Module;
import vo.vMonitoring;

import com.ue.auditmanage.controller.service.YDMonitoringInter;

import entity.Monitoring;

//类的注解
@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
	@Resource(name = "YDMonitoringImpl")
	private YDMonitoringInter ydMonitoringInter;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addMonitoring")
	@ResponseBody
	public void addMonitoring(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		String name = request.getParameter("name");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (ydMonitoringInter.findByName(name) != null) {
			Message ms = new Message();
			ms.setText("该模块名称已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			Monitoring monitoring = new Monitoring();
			try {
				monitoring.setName(name);
				monitoring.setRegisterDate(new java.util.Date());
				monitoring.setUpdateDate(new java.util.Date());
				monitoring.setUrl(request.getParameter("url"));
				ydMonitoringInter.save(monitoring);
				flag = true;
				vMonitoring vm = new vMonitoring();
				vm.setName(monitoring.getName());
				JSONObject object = JSONObject.fromObject(vm, jsonConfig);
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加业务模块失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}

	/**
	 * 删除模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delMonitoring")
	public void delMonitoring(HttpServletRequest request, HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<Monitoring> bmodules = new ArrayList<Monitoring>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				Monitoring monitoring = ydMonitoringInter.getObject(Monitoring.class,
						Integer.parseInt(id));
				if (ydMonitoringInter.deletUserById(Integer.parseInt(id)))
					flag = true;
				bmodules.add(monitoring);
			}
		}
		util.writeJson(JSONArray.fromObject(bmodules), flag, request,
				response);
	}

	/**
	 * 跟新
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateMonitoring")
	public void updateMonitoring(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			Monitoring monitoring = ydMonitoringInter.getObject(Monitoring.class,
					Integer.parseInt(id));
			monitoring.setName(request.getParameter("name"));
			monitoring.setUpdateDate(new java.util.Date());
			monitoring.setUrl(request.getParameter("url"));

			ydMonitoringInter.update(monitoring);
			flag = true;

			vMonitoring mo = new vMonitoring();
			mo.setName(monitoring.getName());
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 查询所有业务模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllMonitoring")
	public void getAllMonitoring(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		int rows = 10;
		int page = 1;
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("rows")) > 0)
			rows = Integer.parseInt(request.getParameter("rows"));
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("page")) > 0)
			page = Integer.parseInt(request.getParameter("page"));

		boolean flag = false;
		String hql = " from  Monitoring bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from Monitoring where 1=1 ";
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;

		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("name") != null
					&& !request.getParameter("name").trim().equals("")) {
				hql = hql + "" + " and bm.name like '%"
						+ request.getParameter("name").trim() + "%' ";
				hql_total = hql_total + "" + " and name like '%"
						+ request.getParameter("name").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and bm.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and bm.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and bm.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and bm.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Monitoring> monitorings = ydMonitoringInter.findDataByFenye(hql, paras,
				page, rows);

		Long total = ydMonitoringInter.getTotal(hql_total, paras);
		if (monitorings != null && monitorings.size() > 0) {
			List<vMonitoring> modules = new ArrayList<vMonitoring>();
			for (Monitoring bm : monitorings) {
				vMonitoring mo = new vMonitoring();
				mo.setId(bm.getId());
				mo.setName(bm.getName());
				mo.setRegisterDate(sdf_fmg.format(bm.getRegisterDate()));
				mo.setUpdateDate(sdf_fmg.format(bm.getUpdateDate()));
				mo.setUrl(bm.getUrl());
				modules.add(mo);
			}
			flag = true;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", total);
			map.put("rows", modules);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {
			
			List<Module> modules = new ArrayList<Module>();
			Module u = new Module();
			modules.add(u);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", modules);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object , jsonConfig);
			util.writeJson(object, request, response);

		}
	}
	static final Map<Integer,String> map = util.getdicMonoriting();
	/**
	 * 获取服务监控结果
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ })
	@RequestMapping(value = "/getServiceMonitoring")
	public void getServiceMonitoring(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();boolean flag = false;
		int rows = 10;
		int page = 1;
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("rows")) > 0)
			rows = Integer.parseInt(request.getParameter("rows"));
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("page")) > 0)
			page = Integer.parseInt(request.getParameter("page"));
		String hql = " from Monitoring where 1=1";
		String hql_total = "select count(*) from Monitoring where 1=1 ";
		ArrayList<Monitoring>monitorings = (ArrayList<Monitoring>) ydMonitoringInter.findDataByFenye(hql, null, page, rows);
		ArrayList<vMonitoring> vMonitorings = new ArrayList<vMonitoring>();
		if(monitorings!=null && monitorings.size()>0){
			flag = true;
			for(Monitoring monitoring :monitorings ){
				vMonitoring vm = new vMonitoring();
				vm.setId(monitoring.getId());
				vm.setName(monitoring.getName());
				vm.setUrl(monitoring.getUrl());
				vm.setCode(util.getCodefromWeb(monitoring.getUrl()));
				if(util.getStatutfromWeb(vm.getCode(), map)!=null){
					vm.setStatut(util.getStatutfromWeb(vm.getCode(), map));
				}else{
					vm.setStatut("网络问题或者其它未知错误");
				}
				
				vm.setImgSrc("./monitoring/getServiceMonitoringImgs.do?id=" + monitoring.getId());
				vMonitorings.add(vm);
			}
		}
		Long total = ydMonitoringInter.getTotal(hql_total, null);
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows", vMonitorings);
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
	}

	/**
	 * 获取服务监控结果
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ })
	@RequestMapping(value = "/getServiceMonitoringImgs")
	public void getServiceMonitoringImgs(HttpServletRequest request,
			HttpServletResponse response) {
		
		String id = request.getParameter("id");
		Monitoring monitoring = ydMonitoringInter.getObject(Monitoring.class, Integer.parseInt(id));
		try {
			if(util.getMonitoringfromWeb(monitoring.getUrl())){
			    util.getMonitoringIcon4pass(request, response, monitoring.getName());	
			}else{
				util.getMonitoringIcon4error(request, response, monitoring.getName());		
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
