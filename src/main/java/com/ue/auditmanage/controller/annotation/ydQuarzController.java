package com.ue.auditmanage.controller.annotation;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.util;
import vo.vQuartz;

import com.ue.auditmanage.controller.service.YDQuartzInter;
import com.ue.auditmanage.filter.quartzJob;

import entity.YDQuartz;

//类的注解
@Controller
@RequestMapping("/quartz")
public class ydQuarzController {
	@Resource(name = "ydQuartzImpl")
	private YDQuartzInter ydQuartzInter;
	@Resource(name = "schedulerFactoryBean")
	private Scheduler scheduler;

	@PostConstruct
	public void autoStart() {
		// runQuartz(request, response);
		@SuppressWarnings("unchecked")
		List<YDQuartz> ydQuartzs = ydQuartzInter.getQuartzOpen();
		if (ydQuartzs != null) {
			YDQuartz ydQuartz = ydQuartzs.get(0);

			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(ydQuartz
						.getName());
				scheduler.getTrigger(triggerKey);
				// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
				CronTrigger trigger = (CronTrigger) scheduler
						.getTrigger(triggerKey);

				JobDetail jobDetail = JobBuilder.newJob(quartzJob.class)
						.withIdentity(ydQuartz.getName(), "计时器任务").build();
				jobDetail.getJobDataMap().put("scheduleJob", ydQuartz);
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
						.cronSchedule(ydQuartz.getCronExpression());
				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger()
						.withIdentity(ydQuartz.getName(), null)
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.start();
				System.out
						.println(scheduler.checkExists(triggerKey) ? "scheduler已经加载"
								+ triggerKey.getName()
								: "scheduler为空");
			} catch (SchedulerException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("无启动状态的计时器");
		}
	}

//	/**
//	 * 跟新
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SuppressWarnings("static-access")
//	@RequestMapping(value = "/updateQuartz")
//	public void updateQuartz(HttpServletRequest request,
//			HttpServletResponse response) {
//		JsonConfig jsonConfig = new JsonConfig();
//
//		boolean flag = false;
//		util util = new util();
//
//		String id = request.getParameter("id");
//		String jobName = request.getParameter("name");
//		String CronExpression = request.getParameter("cronExpression");
//		String discription = request.getParameter("discription");
//		try {
//
//			
//			// Scheduler scheduler = schedulerFactoryBean.getScheduler();
//			TriggerKey triggerKey = TriggerKey.triggerKey(ydQuartz.getName(),
//					ydQuartz.getGroup());
//
//			scheduler.getTrigger(triggerKey);
//			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
//			CronTrigger trigger = (CronTrigger) scheduler
//					.getTrigger(triggerKey);
//			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
//					.cronSchedule(ydQuartz.getCronExpression());
//			// 按新的cronExpression表达式重新构建trigger
//			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
//					.withSchedule(scheduleBuilder).build();
//			// 按新的trigger重新设置job执行
//			scheduler.rescheduleJob(triggerKey, trigger);
//			flag = true;
//			YDQuartz ydQuartz = ydQuartzInter.getObject(YDQuartz.class,
//					Integer.parseInt(id));
//			ydQuartz.setName(jobName);
//			ydQuartz.setCronExpression(CronExpression);
//			ydQuartz.setDiscription(discription);
//			ydQuartz.setState(0);
//			ydQuartzInter.update(ydQuartz);
//			vQuartz mo = new vQuartz();
//			mo.setName(ydQuartz.getName());
//			mo.setState("关闭");
//			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
//			util.writeJson(object, flag, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			util.writeJson("修改失败", flag, request, response);
//		}
//
//	}

	/**
	 * 启动
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/runQuartz")
	public void runQuartz(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		util util = new util();
		String id = request.getParameter("id");
		try {
			YDQuartz ydQuartz = ydQuartzInter.getObject(YDQuartz.class,
					Integer.parseInt(id));
			@SuppressWarnings("unchecked")
			List<YDQuartz> ydQuartzs = ydQuartzInter.getQuartzOpen();
			if (ydQuartzs != null && ydQuartzs.size() > 0) {

				for (YDQuartz quartz : ydQuartzs) {
					quartz.setState(0);
					ydQuartzInter.update(quartz);
					JobKey jobKey = JobKey.jobKey(quartz.getName(),
							quartz.getGroup());
					scheduler.pauseJob(jobKey);
				}
			}

			
			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(ydQuartz
						.getName());
				if(scheduler.checkExists(triggerKey)){
					JobKey jobKey = JobKey.jobKey(ydQuartz.getName(),
							ydQuartz.getGroup());
					scheduler.resumeJob(jobKey);
				}else{
					scheduler.getTrigger(triggerKey);
					// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
					CronTrigger trigger = (CronTrigger) scheduler
							.getTrigger(triggerKey);

					JobDetail jobDetail = JobBuilder.newJob(quartzJob.class)
							.withIdentity(ydQuartz.getName(), "计时器任务").build();
					jobDetail.getJobDataMap().put("scheduleJob", ydQuartz);
					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
							.cronSchedule(ydQuartz.getCronExpression());
					// 按新的cronExpression表达式构建一个新的trigger
					trigger = TriggerBuilder.newTrigger()
							.withIdentity(ydQuartz.getName(), null)
							.withSchedule(scheduleBuilder).build();
					scheduler.scheduleJob(jobDetail, trigger);
					scheduler.start();
				}
				ydQuartz.setState(1);
				ydQuartzInter.update(ydQuartz);
				flag = true;
				vQuartz mo = new vQuartz();
				mo.setName(ydQuartz.getName());
				mo.setState("启动");

				JSONObject object = JSONObject.fromObject(mo, jsonConfig);
				util.writeJson(object, flag, request, response);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 关闭
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/stopQuartz")
	public void stopQuartz(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		util util = new util();
		String id = request.getParameter("id");
		try {

			YDQuartz ydQuartz = ydQuartzInter.getObject(YDQuartz.class,
					Integer.parseInt(id));
			
			JobKey jobKey = JobKey.jobKey(ydQuartz.getName(),
					ydQuartz.getGroup());
			scheduler.pauseJob(jobKey);

			flag = true;
			ydQuartz.setState(0);
			ydQuartzInter.update(ydQuartz);
			vQuartz mo = new vQuartz();
			mo.setName(ydQuartz.getName());
			mo.setState("关闭");
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 添加
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addQuartz")
	public void addQuartz(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();
		String jobName = request.getParameter("name");
		String CronExpression = request.getParameter("cronExpression");
		String discription = request.getParameter("discription");
		YDQuartz job = new YDQuartz();
		job.setName(jobName);
		job.setCronExpression(CronExpression);
		job.setDiscription(discription);
		job.setState(0);
		job.setGroup("计时器任务");
		try {
			flag = true;
			// Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
			scheduler.getTrigger(triggerKey);
			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler
					.getTrigger(triggerKey);

			JobDetail jobDetail = JobBuilder.newJob(quartzJob.class)
					.withIdentity(jobName, "计时器任务").build();
			jobDetail.getJobDataMap().put("scheduleJob", job);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(CronExpression);
			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(jobName, null)
					.withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
			ydQuartzInter.save(job);
			vQuartz mo = new vQuartz();
			mo.setName(job.getName());
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws SchedulerException
	 */
	@RequestMapping(value = "/deluartz")
	public void deluartz(HttpServletRequest request,
			HttpServletResponse response) throws SchedulerException {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<YDQuartz> bmodules = new ArrayList<YDQuartz>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				YDQuartz bmodule = ydQuartzInter.getObject(YDQuartz.class,
						Integer.parseInt(id));
				JobKey jobKey = JobKey.jobKey(bmodule.getName(),
						bmodule.getGroup());
				try {
					
					if(scheduler.checkExists(jobKey) ){
						if(scheduler.deleteJob(jobKey))ydQuartzInter.deletUserById(Integer.parseInt(id));						
					}else{
						ydQuartzInter.deletUserById(Integer.parseInt(id));
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
				flag = true;
				bmodules.add(bmodule);
			}
		}
		util.writeJson(JSONArray.fromObject(bmodules), flag, request,
				response);
	}

	/**
	 * 查询所有
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getAllQuartz")
	public void getAllQuartz(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		PrintWriter writer = null;
		int rows = 10;
		int page = 1;
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("rows")) > 0)
			rows = Integer.parseInt(request.getParameter("rows"));
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("page")) > 0)
			page = Integer.parseInt(request.getParameter("page"));

		boolean flag = false;
		String hql = " from  YDQuartz bm where 1=1 ";
		String hql_total = "select count(*) from YDQuartz where 1=1 ";
		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");
			writer = response.getWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDQuartz> bmmodules = ydQuartzInter.findDataByFenye(hql, null,
				page, rows);

		Long total = ydQuartzInter.getTotal(hql_total, null);
		if (bmmodules != null && bmmodules.size() > 0) {
			List<vQuartz> modules = new ArrayList<vQuartz>();
			for (YDQuartz bm : bmmodules) {
				vQuartz mo = new vQuartz();
				mo.setId(bm.getId());
				mo.setName(bm.getName());
				mo.setCronExpression(bm.getCronExpression());
				mo.setDiscription(bm.getDiscription());
				if (bm.getState() == 0) {
					mo.setState("关闭");
				} else {
					mo.setState("启动");
				}
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

			String json = "[]";
			writer.write(json);
			writer.flush();
			writer.close();

		}
	}
}
