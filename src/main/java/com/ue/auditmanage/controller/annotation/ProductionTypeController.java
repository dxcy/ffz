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
import vo.vProType;
import com.ue.auditmanage.controller.service.ProductTypeServiceInter;

import entity.ProductionType;

//类的注解
@Controller
@RequestMapping("/ProType")
public class ProductionTypeController {
	@Resource(name = "ProductTypeServiceImpl")
	private ProductTypeServiceInter productTypeServiceInter;
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addProType")
	@ResponseBody
	public void addProType(HttpServletRequest request,
			HttpServletResponse response,ProductionType productionType) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (productTypeServiceInter.findByName(productionType.getName()) != null) {
			Message ms = new Message();
			ms.setText("该类型名称已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			try {
				productionType.setRegisterDate(new java.util.Date());
				productionType.setUpdateDate(new java.util.Date());
				productTypeServiceInter.save(productionType);
				flag = true;
				vProType aType = new vProType();
				aType.setId(productionType.getId());
				aType.setName(productionType.getName());
				aType.setRegisterDate(sdf.format(productionType.getRegisterDate()));
				aType.setUpdateDate(sdf.format(productionType.getRegisterDate()));
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
	 * 删除产品类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delProType")
	public void delProType(HttpServletRequest request, HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<ProductionType> vatlasTypes = new ArrayList<ProductionType>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				ProductionType productionType = productTypeServiceInter.getObject(ProductionType.class,
						Integer.parseInt(id));
				if (productTypeServiceInter.deletById(Integer.parseInt(id)))
					flag = true;
				vatlasTypes.add(productionType);
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
	@RequestMapping(value = "/updateProType")
	public void updateProType(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			ProductionType productionType = productTypeServiceInter.getObject(ProductionType.class,
					Integer.parseInt(id));
			productionType.setName(request.getParameter("name"));
			productionType.setUpdateDate(new java.util.Date());

			productTypeServiceInter.update(productionType);
			flag = true;

			vProType vType = new vProType();
			vType.setName(productionType.getName());
			vType.setRegisterDate(sdf.format(productionType.getRegisterDate()));
			vType.setUpdateDate(sdf.format(productionType.getUpdateDate()));
			JSONObject object = JSONObject.fromObject(vType, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 分页查询所有产品类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllProTypeByFenye")
	public void getAllProTypeByFenye(HttpServletRequest request,
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
		String hql = " from  ProductionType bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from ProductionType where 1=1 ";
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
		List<ProductionType> productionTypes = productTypeServiceInter.findDataByFenye(hql, paras,
				page, rows);

		Long total = productTypeServiceInter.getTotal(hql_total, paras);
		if (productionTypes != null && productionTypes.size() > 0) {
			List<vProType> vTypes = new ArrayList<vProType>();
			for (ProductionType bm : productionTypes) {
				vProType mo = new vProType();
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
			List<vProType> vTypes = new ArrayList<vProType>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", vTypes);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object , jsonConfig);
			util.writeJson(object, request, response);

		}
	}
	
	/**
	 * 查询所有产品类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllProType")
	public void getAllProType(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		
		boolean flag = false;
		String hql = " from  ProductionType bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from ProductionType where 1=1 ";
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
		List<ProductionType> productionTypes  = productTypeServiceInter.findAllProType(hql,paras);

		Long total = productTypeServiceInter.getTotal(hql_total, paras);
		if (productionTypes != null && productionTypes.size() > 0) {
			List<vProType> vTypes = new ArrayList<vProType>();
			for (ProductionType bm : productionTypes) {
				vProType mo = new vProType();
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
