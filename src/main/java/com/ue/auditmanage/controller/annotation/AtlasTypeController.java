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

import com.ue.auditmanage.controller.service.AtlasTypeServiceInter;
import entity.atlasType;

//类的注解
@Controller
@RequestMapping("/AtlasType")
public class AtlasTypeController {
	@Resource(name = "AtlasTypeServiceImpl")
	private AtlasTypeServiceInter atlasTypeServiceInter;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addAtlasType")
	@ResponseBody
	public void addAtlasType(HttpServletRequest request,
			HttpServletResponse response,atlasType atlasType) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (atlasTypeServiceInter.findByName(atlasType.getName()) != null) {
			Message ms = new Message();
			ms.setText("该类型名称已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			
			try {
				
				atlasType.setRegisterDate(new java.util.Date());
				atlasType.setUpdateDate(new java.util.Date());

				atlasTypeServiceInter.saveAtlasType(atlasType);
				flag = true;

				vAtlasType aType = new vAtlasType();
				aType.setId(atlasType.getId());
				aType.setName(atlasType.getName());
				aType.setRegisterDate(sdf.format(atlasType.getRegisterDate()));
				aType.setUpdateDate(sdf.format(atlasType.getRegisterDate()));
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
	@RequestMapping(value = "/delAtlasType")
	public void delAtlasType(HttpServletRequest request, HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<atlasType> vatlasTypes = new ArrayList<atlasType>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				atlasType atlasType = atlasTypeServiceInter.getAtlasTypeById(atlasType.class,
						Integer.parseInt(id));
				if (atlasTypeServiceInter.deletById(Integer.parseInt(id)))
					flag = true;
				vatlasTypes.add(atlasType);
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
	@RequestMapping(value = "/updateAtlasType")
	public void updateAtlasType(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			atlasType atlasType = atlasTypeServiceInter.getAtlasTypeById(atlasType.class,
					Integer.parseInt(id));
			atlasType.setName(request.getParameter("name"));
			atlasType.setUpdateDate(new java.util.Date());

			atlasTypeServiceInter.updateAtlasType(atlasType);
			flag = true;

			vAtlasType vType = new vAtlasType();
			vType.setName(atlasType.getName());
			vType.setRegisterDate(sdf.format(atlasType.getRegisterDate()));
			vType.setUpdateDate(sdf.format(atlasType.getUpdateDate()));
			JSONObject object = JSONObject.fromObject(vType, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 分页查询所有图集类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllAtlasTypeByFenye")
	public void getAllAtlasTypeByFenye(HttpServletRequest request,
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
		String hql = " from  atlasType bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from atlasType where 1=1 ";
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
		List<atlasType> atlasTypes = atlasTypeServiceInter.findByFenyeatlasType(hql, paras,
				page, rows);

		Long total = atlasTypeServiceInter.getatlasTypeTotal(hql_total, paras);
		if (atlasTypes != null && atlasTypes.size() > 0) {
			List<vAtlasType> vTypes = new ArrayList<vAtlasType>();
			for (atlasType bm : atlasTypes) {
				vAtlasType mo = new vAtlasType();
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
			Map<String, Object> map = new HashMap<String, Object>();
			List<vAtlasType> vTypes = new ArrayList<vAtlasType>();
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
	@RequestMapping(value = "/getAllAtlasType")
	public void getAllAtlasType(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		
		boolean flag = false;
		String hql = " from  atlasType bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from atlasType where 1=1 ";
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
		List<atlasType> atlasTypes = atlasTypeServiceInter.findAllatlasType(hql,paras);

		Long total = atlasTypeServiceInter.getatlasTypeTotal(hql_total, paras);
		if (atlasTypes != null && atlasTypes.size() > 0) {
			List<vAtlasType> vTypes = new ArrayList<vAtlasType>();
			for (atlasType bm : atlasTypes) {
				vAtlasType mo = new vAtlasType();
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
