package com.ue.auditmanage.controller.annotation;

import java.io.IOException;
import java.io.PrintWriter;
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
import vo.vAtlasType;
import vo.vappType;

import com.ue.auditmanage.controller.service.AppTypeServiceInter;

import entity.AppType;
import entity.atlasType;

//类的注解
@Controller
@RequestMapping("/AppType")
public class AppTypeController {
	@Resource(name = "AppTypeServiceImpl")
	private AppTypeServiceInter appTypeServiceInter;
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addAppType")
	@ResponseBody
	public void addAppType(HttpServletRequest request,
			HttpServletResponse response,AppType appType) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (appTypeServiceInter.findByName(appType.getName()) != null) {
			Message ms = new Message();
			ms.setText("该类型名称已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			
			try {
				
				appType.setRegisterDate(new java.util.Date());
				appType.setUpdateDate(new java.util.Date());

				appTypeServiceInter.saveAppType(appType);
				flag = true;

				vappType aType = new vappType();
				aType.setId(appType.getId());
				aType.setName(appType.getName());
				aType.setRegisterDate(sdf.format(appType.getRegisterDate()));
				aType.setUpdateDate(sdf.format(appType.getRegisterDate()));
				JSONObject object = JSONObject.fromObject(aType, jsonConfig);
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
	 * 删除图集类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delAppType")
	public void delAppType(HttpServletRequest request, HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<AppType> vatlasTypes = new ArrayList<AppType>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				AppType appType = appTypeServiceInter.getAppTypeById(AppType.class,
						Integer.parseInt(id));
				if (appTypeServiceInter.deletById(Integer.parseInt(id)))
					flag = true;
				vatlasTypes.add(appType);
			}
		}
		util.writeJson(JSONArray.fromObject(vatlasTypes), flag, request,
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
	@RequestMapping(value = "/updateAppType")
	public void updateAppType(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			AppType appType = appTypeServiceInter.getAppTypeById(AppType.class,
					Integer.parseInt(id));
			appType.setName(request.getParameter("name"));
			appType.setUpdateDate(new java.util.Date());

			appTypeServiceInter.updateAppType(appType);
			flag = true;

			vappType vType = new vappType();
			vType.setName(appType.getName());
			vType.setRegisterDate(sdf.format(appType.getRegisterDate()));
			vType.setUpdateDate(sdf.format(appType.getUpdateDate()));
			JSONObject object = JSONObject.fromObject(vType, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 分页查询所有应用类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllAppTypeByFenye")
	public void getAllAppTypeByFenye(HttpServletRequest request,
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
		String hql = " from  AppType bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from AppType where 1=1 ";
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
		List<AppType> appTypes = appTypeServiceInter.findByFenyeAppType(hql, paras,
				page, rows);

		Long total = appTypeServiceInter.getAppTypeTotal(hql_total, paras);
		if (appTypes != null && appTypes.size() > 0) {
			List<vappType> vTypes = new ArrayList<vappType>();
			for (AppType bm : appTypes) {
				vappType mo = new vappType();
				mo.setId(bm.getId());
				mo.setName(bm.getName());
				mo.setRegisterDate(sdf_fmg.format(bm.getRegisterDate()));
				mo.setUpdateDate(sdf_fmg.format(bm.getUpdateDate()));
				vTypes.add(mo);
			}
			flag = true;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", total);
			map.put("rows", vTypes);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {
			List<vappType> vTypes = new ArrayList<vappType>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", vTypes);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object , jsonConfig);
			util.writeJson(object, request, response);

		}
	}
	
	/**
	 * 查询所有图集类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllAppType")
	public void getAllAppType(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		
		boolean flag = false;
		String hql = " from  AppType bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from AppType where 1=1 ";
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
		List<AppType> appTypes = appTypeServiceInter.findAllappType(hql,paras);

		Long total = appTypeServiceInter.getAppTypeTotal(hql_total, paras);
		if (appTypes != null && appTypes.size() > 0) {
			List<vappType> vTypes = new ArrayList<vappType>();
			for (AppType bm : appTypes) {
				vappType mo = new vappType();
				mo.setId(bm.getId());
				mo.setName(bm.getName());
				mo.setRegisterDate(sdf_fmg.format(bm.getRegisterDate()));
				mo.setUpdateDate(sdf_fmg.format(bm.getUpdateDate()));
				vTypes.add(mo);
			}
			flag = true;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", total);
			map.put("rows", vTypes);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {
			List<vAtlasType> vTypes = new ArrayList<vAtlasType>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", vTypes);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object , jsonConfig);
			util.writeJson(object, request, response);

		}
	}
}
