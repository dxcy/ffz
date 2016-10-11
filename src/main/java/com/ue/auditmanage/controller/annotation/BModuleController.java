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
import vo.Module;








import com.ue.auditmanage.controller.service.BModuleInter;

import entity.BModule;

//类的注解
@Controller
@RequestMapping("/bmodule")
public class BModuleController {
	@Resource(name = "BModuleImpl")
	private BModuleInter bmoduleInter;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addModule")
	@ResponseBody
	public void addModule(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String name = request.getParameter("name");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (bmoduleInter.findByName(name) != null) {
			Message ms = new Message();
			ms.setText("该模块名称已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			BModule module = new BModule();
			try {
				module.setName(name);
				module.setRegisterDate(new java.util.Date());
				module.setUpdateDate(new java.util.Date());

				bmoduleInter.save(module);
				flag = true;

				Module mb = new Module();
				mb.setId(module.getId());
				mb.setName(module.getName());
				mb.setRegisterDate(sdf.format(module.getRegisterDate()));
				mb.setUpdateDate(sdf.format(module.getRegisterDate()));
				JSONObject object = JSONObject.fromObject(mb, jsonConfig);
				System.out.println("添加用户成功");
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
	@RequestMapping(value = "/delBmodule")
	public void delUser(HttpServletRequest request, HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<BModule> bmodules = new ArrayList<BModule>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				BModule bmodule = bmoduleInter.getObject(BModule.class,
						Integer.parseInt(id));
				if (bmoduleInter.deletUserById(Integer.parseInt(id)))
					flag = true;
				bmodules.add(bmodule);
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
	@RequestMapping(value = "/updateBmodule")
	public void updateBmodule(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			BModule bmodule = bmoduleInter.getObject(BModule.class,
					Integer.parseInt(id));
			bmodule.setName(request.getParameter("name"));
			bmodule.setUpdateDate(new java.util.Date());

			bmoduleInter.update(bmodule);
			flag = true;

			Module mo = new Module();
			mo.setName(bmodule.getName());
			mo.setRegisterDate(sdf.format(bmodule.getRegisterDate()));
			mo.setUpdateDate(sdf.format(bmodule.getUpdateDate()));
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
	@RequestMapping(value = "/getAllBmodule")
	public void findAllUsers(HttpServletRequest request,
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
		String hql = " from  BModule bm where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from BModule where 1=1 ";
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
		List<BModule> bmmodules = bmoduleInter.findDataByFenye(hql, paras,
				page, rows);

		Long total = bmoduleInter.getTotal(hql_total, paras);
		if (bmmodules != null && bmmodules.size() > 0) {
			List<Module> modules = new ArrayList<Module>();
			for (BModule bm : bmmodules) {
				Module mo = new Module();
				mo.setId(bm.getId());
				mo.setName(bm.getName());
				mo.setRegisterDate(sdf_fmg.format(bm.getRegisterDate()));
				mo.setUpdateDate(sdf_fmg.format(bm.getUpdateDate()));
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
	/*
	/**
	 * 查询所有业务模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
/*	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/PullBookparser")
	public  void bookParser(HttpServletRequest request,
			HttpServletResponse response) {
		String roots = util.getRootPath();
		  String path = roots+"ziliao4.xml";
		JsonConfig jsonConfig = new JsonConfig();
		Document doc = dom4jdao.getParse(path);
		Element root = doc.getRootElement();
		util util = new util();
		quhua quhua = new vo.quhua();
		Element eprovince =  root.element("province");
			province province = new province();
			List<city> cs = new ArrayList<city>();
			List<Element> citys = eprovince.elements("city");
			for(Element ecity :citys ){
				city city = new city();
				List<county> ctys = new ArrayList<county>();
				List<Element> countys = ecity.elements("county");
				for(Element ecounty : countys){
					county county =new county();
					List<country> ctrys = new ArrayList<country>();
					List<Element> countrys = ecounty.elements("country");
					for(Element ecountry: countrys){
						country country = new country();
						List<v> vs = new ArrayList<v>();
						List<Element> vss = ecountry.elements("v");
						for(Element ev:vss){
							v v= new v();
							v.setVn(ev.element("vn").getTextTrim().toString());
							v.setCode(ev.element("code").getTextTrim().toString());
							vs.add(v);
						}
						country.setCn(ecountry.element("cn").getTextTrim().toString());
						country.setCode(ecountry.element("code").getTextTrim().toString());
						country.setVs(vs);
						ctrys.add(country);
					}
					county.setCtn(ecounty.element("ctn").getTextTrim().toString());
					county.setCode(ecounty.element("code").getTextTrim().toString());
					county.setCountrys(ctrys);
					ctys.add(county);
				}//county
			
				city.setC(ecity.element("c").getTextTrim().toString());
				city.setCode(ecity.element("code").getTextTrim().toString());
				city.setCountys(ctys);
				cs.add(city);
			}//city
			province.setName(eprovince.element("name").getTextTrim().toString());
			province.setCode(eprovince.element("code").getTextTrim().toString());
			province.setCitys(cs);
			quhua.setId(root.attributeValue("id"));
			quhua.setProvicnes(province);
			JSONObject objects = JSONObject.fromObject(quhua, jsonConfig);
			util.writeJson(objects, request, response);
	}
*/
}
