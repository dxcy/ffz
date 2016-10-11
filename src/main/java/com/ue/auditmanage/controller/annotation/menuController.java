package com.ue.auditmanage.controller.annotation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.util;
import vo.TreeVo;
import entity.ViewMenu;

//类的注解
@Controller
@RequestMapping("/viewMenu")
public class menuController {
	@Resource(name = "menuserviceImpl")
	private com.ue.auditmanage.controller.service.menuServiceInter menuServiceInter;
	/**
	 * 跳到业务模块管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/gobmodule")
	public String gobmodule() {

		return "/Bmodule";
	}
	
	/**
	 * 跳到业务模块管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goMonitoring")
	public String goMonitoring() {

		return "/Monitoring";
	}
	
	/**
	 * 跳到业务模块管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goYDFtp")
	public String goYDFtp() {

		return "/YDFtp";
	}
	
	/**
	 * 跳到业务模块管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goYDQuartz")
	public String goYDQuartz() {

		return "/YDQuartz";
	}
	/**
	 * 跳到用户类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/gouserType")
	public String gouserType() {

		return "/UserType";
	}
	
	/**
	 * 跳到图集类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goatlasType")
	public String goatlasType() {

		return "/AtlasType";
	}
	
	/**
	 * 跳到图集管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goatlas")
	public String goatlas() {

		return "/Atlas";
	}
	
	/**
	 * 跳到应用管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goapp")
	public String goapp() {

		return "/App";
	}
	
	/**
	 * 跳到产品管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goproduction")
	public String goproduction() {

		return "/Production";
	}
	
	/**
	 * 跳到产品类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goproductionType")
	public String goproductionType() {

		return "/ProductionType";
	}
	
	/**
	 * 跳到应用类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goappType")
	public String goappType() {

		return "/AppType";
	}
	
	/**
	 * 跳到数据类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goDataType")
	public String goDataType() {

		return "/DataType";
	}

	/**
	 * 跳到数据管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goydData")
	public String goydData() {

		return "/ydData";
	}
	
	/**
	 * 跳到页面管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goydPage")
	public String goydPage() {

		return "/ydPage";
	}
	/**
	 * 跳到服务管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goadminService")
	public String goadminPlayer() {

		return "/UserService";
	}

	/**
	 * 跳到日志管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goLogsService")
	public String goLogsService() {

		return "/logs";
	}
	
	/**
	 * 跳到用户类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goServices")
	public String goServices() {

		return "/Services";
	}
	/**
	 * 跳到服务类型管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/goserviceType")
	public String goserviceType() {

		return "/ServiceType";
	}
	/**
	 * 得到父节点列表
	 * 
	 * @return
	 */
	@RequestMapping("/getroots")
	public void getroots(HttpServletRequest request,
			HttpServletResponse response) {
		List<ViewMenu> us = menuServiceInter.findroots();
		util.creeTree(us, request, response);
	}
	
	/**
	 * 跟新节点
	 * 
	 * @return
	 */
	@RequestMapping("/updateTree")
	public void updateTree(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String text = request.getParameter("text");
		String pid = request.getParameter("pid");
		String seq = request.getParameter("seq");
		boolean flag = false;
		ArrayList<TreeVo> treeVos = new ArrayList<TreeVo>();
		ViewMenu vm = menuServiceInter.getObject(ViewMenu.class, Integer.parseInt(id));
		JsonConfig jsonConfig = util.getjsonConfig();
		if(pid==null || "".equals(pid)){
				
			try {
				vm.setText(text);
				vm.setState("open");
				vm.setIconCls("icon-save");
				vm.setSeq(seq);
				vm.setPid(null);
				flag = true;
				menuServiceInter.update(vm);
				ArrayList<ViewMenu> vms = (ArrayList<ViewMenu>) menuServiceInter
						.findAllfmmenu();
				for (ViewMenu v : vms) {
					TreeVo vo = new TreeVo();
					vo.setId(v.getId());
					vo.setState("open");
					vo.setText(v.getText());
					vo.setIconCls(v.getIconCls());
					Map<String, Object> Attributes = new HashMap<String, Object>();
					Attributes.put("url", v.getUrl());
					vo.setAttributes(Attributes);
					treeVos.add(vo);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			JSONArray object = JSONArray.fromObject(treeVos, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			
			try {
				vm.setText(text);
				vm.setState("open");
				vm.setPid(Integer.parseInt(pid));
				vm.setIconCls("icon-save");
				vm.setSeq(seq);
				flag = true;
				menuServiceInter.update(vm);
				ArrayList<ViewMenu> vms = (ArrayList<ViewMenu>) menuServiceInter
						.findAllfmmenu();
				for (ViewMenu v : vms) {
					TreeVo vo = new TreeVo();
					vo.setId(v.getId());
					vo.setState("closed");
					vo.setText(v.getText());
					vo.setPid(vm.getPid());
					vo.setIconCls(v.getIconCls());
					Map<String, Object> Attributes = new HashMap<String, Object>();
					Attributes.put("url", v.getUrl());
					vo.setAttributes(Attributes);
					treeVos.add(vo);
				}
				JSONArray object = JSONArray.fromObject(treeVos, jsonConfig);
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	
		
	}
	/**
	 * 删除节点
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteNode")
	public void deleteNode(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = util.getjsonConfig();
		boolean flag = false;
		String ids = request.getParameter("id");
		ArrayList<ViewMenu> vms = new ArrayList<ViewMenu>();
		ArrayList<TreeVo> to = new ArrayList<TreeVo>();
		
		if (ids != null && !"".equals(ids)) {
			
				ViewMenu vm = menuServiceInter.getObject(ViewMenu.class,
						Integer.parseInt(ids));
				if (menuServiceInter.deletNodeById(Integer.parseInt(ids)))
					flag = true;
				vms.add(vm);
			
		}
		
		for(ViewMenu vm : vms){
			TreeVo vo = new TreeVo();
			vo.setText(vm.getText());
			to.add(vo);
		}
		
		JSONArray object = JSONArray.fromObject(to, jsonConfig);
		util.writeJson(object, flag, request, response);
	}

	/**
	 * 得到父节点列表
	 * 
	 * @return
	 */
	@RequestMapping("/addTree")
	public void addTree(HttpServletRequest request, HttpServletResponse response) {
		String text = request.getParameter("text");
		String pid = request.getParameter("pid");
		String seq = request.getParameter("seq");
		boolean flag = false;
		ArrayList<TreeVo> treeVos = new ArrayList<TreeVo>();
		
		JsonConfig jsonConfig = util.getjsonConfig();
		if(pid==null || "".equals(pid)){
			ViewMenu vm = new ViewMenu();	
			try {
				vm.setText(text);
				vm.setState("open");
				vm.setIconCls("icon-save");
				vm.setSeq(seq);
		
				flag = true;
				menuServiceInter.save(vm);
				ArrayList<ViewMenu> vms = (ArrayList<ViewMenu>) menuServiceInter
						.findAllfmmenu();
				for (ViewMenu v : vms) {
					TreeVo vo = new TreeVo();
					vo.setId(v.getId());
					vo.setState("open");
					vo.setText(v.getText());
					vo.setIconCls(v.getIconCls());
					Map<String, Object> Attributes = new HashMap<String, Object>();
					Attributes.put("url", v.getUrl());
					vo.setAttributes(Attributes);
					treeVos.add(vo);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			JSONArray object = JSONArray.fromObject(treeVos, jsonConfig);
			util.writeJson(object, flag, request, response);
		}else if (menuServiceInter.checkName(text, Integer.parseInt(pid))) {// 通过 名称来判断

			response.setContentType("text/json;ch球员arset=utf-8");
			PrintWriter writer;
			try {
				writer = response.getWriter();
				writer.write("{\"success\":false,\"result\": 该子节点已存在}");
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		} else {
			System.out.println("进入添加球员方法");
			ViewMenu vm = new ViewMenu();

			try {
				vm.setText(text);
				vm.setState("open");
				vm.setPid(Integer.parseInt(pid));
				vm.setIconCls("icon-save");
				vm.setSeq(seq);
				flag = true;
				menuServiceInter.save(vm);
				ArrayList<ViewMenu> vms = (ArrayList<ViewMenu>) menuServiceInter
						.findAllfmmenu();
				for (ViewMenu v : vms) {
					TreeVo vo = new TreeVo();
					vo.setId(v.getId());
					vo.setState("closed");
					vo.setText(v.getText());
					vo.setPid(vm.getPid());
					vo.setIconCls(v.getIconCls());
					Map<String, Object> Attributes = new HashMap<String, Object>();
					Attributes.put("url", v.getUrl());
					vo.setAttributes(Attributes);
					treeVos.add(vo);
				}
				JSONArray object = JSONArray.fromObject(treeVos, jsonConfig);
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	
		
	}

	/**
	 * 获取树
	 * 
	 * @return
	 */
	@RequestMapping("/loadTree")
	public void loadTree(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");// 获取当前展开节点的id
		ArrayList<TreeVo> treeVos = new ArrayList<TreeVo>();
		if (id == null || "".equals(id)) {
			List<ViewMenu> us = menuServiceInter.findroots();

			if (us.size() > 0) {
				for (ViewMenu v : us) {
					TreeVo treeVo = new TreeVo();
					treeVo.setId(v.getId());
					treeVo.setState("closed");
					treeVo.setText(v.getText());
					treeVo.setIconCls(v.getIconCls());
					Map<String, Object> Attributes = new HashMap<String, Object>();
					Attributes.put("url", v.getUrl());
					treeVo.setAttributes(Attributes);
					treeVos.add(treeVo);
				}

			} else {// 不存在根节点，需要初始化添加
				ViewMenu vm = new ViewMenu();
				vm.setText("视图管理");
				vm.setIconCls("icon-redo");
				try {
					menuServiceInter.save(vm);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {

			ArrayList<ViewMenu> element = (ArrayList<ViewMenu>) menuServiceInter
					.findElements(Integer.parseInt(id));

			if (element != null && element.size() > 0) {
				for (ViewMenu f : element) {
					TreeVo treeVo = new TreeVo();
					treeVo.setId(f.getId());

					treeVo.setText(f.getText());
					treeVo.setIconCls(f.getIconCls());
					Map<String, Object> Attributes = new HashMap<String, Object>();
					Attributes.put("url", f.getUrl());
					treeVo.setAttributes(Attributes);
					if (menuServiceInter.hasSubmit(f.getId())) {
						treeVo.setState("closed");
					} else {
						treeVo.setState("open");
					}
					treeVos.add(treeVo);
				}

			}

		}
		util.creeTree(treeVos, request, response);
	}

}
