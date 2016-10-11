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
import net.sf.json.util.CycleDetectionStrategy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.JsonDateValueProcessor;
import util.util;
import vo.Message;
import vo.vPage;

import com.ue.auditmanage.controller.service.BModuleInter;
import com.ue.auditmanage.controller.service.YDPageInter;


import entity.BModule;
import entity.YDPage;
import entity.YDService;


//类的注解
@Controller
@RequestMapping("/ydPage")
public class ydPageController {
	@Resource(name = "BModuleImpl")
	private BModuleInter bModuleInter;
	@Resource(name = "YDPageImpl")
	private YDPageInter ydPageInter;
	
	/**
	 * 得到栏目列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getModulelist")
	public void getModulelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from BModule  where 1=1";
			List<BModule> bModules = bModuleInter.findAllData(hql);
			boolean flag = false;
			String json = "[";
if(bModules!=null && bModules.size()>0 ){
	for (int i = 0; i < bModules.size(); i++) {
		if (i == 0) {
			BModule bm = bModules.get(i);
			//flag = true;
			if(bModules.size()==1){
				json += "{\"id\":" + "\"" + bm.getId() + "\"" + ","
						+ "\"text\":" + "\"" + bm.getName() + "\""
						+ "," + "\"selected\":" + flag + "}";
			}else{
				json += "{\"id\":" + "\"" + bm.getId() + "\"" + ","
						+ "\"text\":" + "\"" + bm.getName() + "\""
						+ "," + "\"selected\":" + flag + "},";
			}
		} else if (i == bModules.size() - 1) {
			BModule bm = bModules.get(i);
			json += "{\"id\":" + "\"" + bm.getId() + "\"" + ","
					+ "\"text\":" + "\"" + bm.getName()
					+ "\"}";

		} else {
			BModule bm = bModules.get(i);
			json += "{\"id\":" + "\"" +  bm.getId() + "\"" + ","
					+ "\"text\":" + "\"" +bm.getName()
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
	
	
	
	/**
	 * 无分页查询所有页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllPages4chart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllPages4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		PrintWriter writer = response.getWriter();
		String hql = "from YDPage page where 1=1 ";
		try {
			response.setContentType("text/json;charset=utf-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDPage> YDPages = ydPageInter.findAllData(hql);
		List<vPage> pages  = new ArrayList<vPage>();
		if (YDPages!=null && YDPages.size()>0) {
			
			for(YDPage s : YDPages){
				
				vPage ser = new vPage();
				ser.setId(s.getId());
				ser.setVisitedTimes(s.getVisitedTimes());
				ser.setPid(s.getPid());
				pages.add(ser);
			}
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", YDPages.size());
		map.put("rows",pages );
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
	 * 查询所有页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllPages")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllPages(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		boolean flag = false;
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;
		 int rows = 10;int page = 1;PrintWriter writer = response.getWriter();
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		String hql = "from YDPage page where 1=1 ";
		String hql_total = " select count(*) from YDPage  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("BID") != null
					&& !request.getParameter("BID").trim().equals("")) {
				hql = hql + "" + " and page.bmodule.id like '%"
						+ request.getParameter("BID").trim() + "%' ";
				hql_total = hql_total + "" + " and bmodule.id like '%"
						+ request.getParameter("BID").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and page.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and page.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and page.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and page.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDPage> YDPages = ydPageInter.findDataByFenye(hql, paras, page, rows);
		List<vPage> pages  = new ArrayList<vPage>();
		if (YDPages!=null && YDPages.size()>0) {
			
			for(YDPage s : YDPages){
				
				vPage ser = new vPage();
				ser.setId(s.getId());
				ser.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				ser.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				ser.setVisitedTimes(s.getVisitedTimes());
				ser.setBmName(s.getBmodule().getName());
				ser.setUrl(s.getUrl());
				ser.setPid(s.getPid());
				pages.add(ser);
			}
			
		long total = ydPageInter.getTotal(hql_total, null);
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows",pages );
		JSONObject objects = JSONObject.fromObject(map, jsonConfig);
		util.writeJson(objects, request, response);
		} else {
			String json = "[]";
			writer.write(json);
			writer.flush();
			writer.close();

		}

	}
	
	// 添加页面
		@SuppressWarnings("static-access")
		@RequestMapping(value = "/addYdPage")
		@ResponseBody
		public void addYdPage(HttpServletRequest request,
				HttpServletResponse response) {
			util util = new util();
			boolean flag = false;
			String BID = request.getParameter("BID");
			String Url = request.getParameter("Url");
			String pid = request.getParameter("pid");
			response.setContentType("text/json;charset=utf-8");
			PrintWriter writer;
			JsonConfig jsonConfig = util.getjsonConfig();
			
			if (ydPageInter.findServiceByUrl(Url) != null) {
				Message ms = new Message();
				ms.setText("该页面已存在");
				JSONObject object = JSONObject.fromObject(ms, jsonConfig);
				util.writeJson(object, flag, request, response);
			} else {
				YDPage ydPage = new YDPage();
				try {
					ydPage.setPid(pid);
					ydPage.setUrl(Url);
					ydPage.setRegisterDate(new java.util.Date());
					ydPage.setUpdateDate(new java.util.Date());
					ydPage.setVisitedTimes(0);
					ydPage.setBmodule(bModuleInter.getObject(BModule.class, Integer.parseInt(BID)));
				
					ydPageInter.save(ydPage);
					flag = true;

					vPage page = new vPage();
					page.setId(ydPage.getId());
					page.setPid(ydPage.getPid());
					page.setUrl(ydPage.getUrl());
					JSONObject object = JSONObject.fromObject(page, jsonConfig);
					
					util.writeJson(object, flag, request, response);
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

		}
		
		/**
		 * 删除页面
		 * 
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/delPage")
		public void delPage(HttpServletRequest request,
				HttpServletResponse response) {
			boolean flag = false;
			String[] ids = request.getParameter("id").split(",");
			ArrayList<vPage> vPages = new ArrayList<vPage>();
			if (ids != null && ids.length > 0) {
				for (String id : ids) {
					YDPage ydPage = ydPageInter.getObject(YDPage.class,Integer.parseInt(id));
					String hql = "from YDVisiterPage ys where 1=1 and ys.ydPage.id= ? ";
					List<Object> param = new ArrayList<Object>();
					param.add(Integer.parseInt(id));
				
					if (ydPageInter.deletById(Integer.parseInt(id))){
						flag = true;
						vPage page = new vPage();
						page.setId(ydPage.getId());
						page.setUrl(ydPage.getUrl());
						vPages.add(page);
					}
				}
			}			
			util.writeJson(JSONArray.fromObject(vPages), flag, request,
					response);
		}
		
		/**
		 * 跟新页面
		 * 
		 * @param request
		 * @param response
		 * @return
		 */
		@SuppressWarnings("static-access")
		@RequestMapping(value = "/updatePage")
		public void updatePage(HttpServletRequest request,
				HttpServletResponse response) {
			JsonConfig jsonConfig = new JsonConfig();
	        String Url =  request.getParameter("Url");
	        String BID = request.getParameter("BID");
	        String pid = request.getParameter("pid");
	      //  BModule bm = bModuleInter
			boolean flag = false;
			util util = new util();
			try {
				YDPage ydPage = ydPageInter.getObject(YDPage.class, Integer.parseInt(request.getParameter("id")));
				ydPage.setPid(pid);
				ydPage.setUrl(Url);
				ydPage.setBmodule(bModuleInter.getObject(BModule.class, Integer.parseInt(BID)));
				ydPage.setUpdateDate(new java.util.Date());
				ydPageInter.update(ydPage);
				flag = true;
				YDPage page = new YDPage();
				page.setId(ydPage.getId());
				page.setUrl(ydPage.getUrl());
				JSONObject object = JSONObject.fromObject(page, jsonConfig);
				util.writeJson(object, flag, request, response);
			
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				util.writeJson("修改失败", flag, request, response);
			}

		}
}
