package com.ue.auditmanage.filter;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.ue.auditmanage.controller.service.YDQuartzInter;

import entity.YDQuartz;

public class ShutdownQuartz implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		 try
	        {
			 
	           System.out.println("系统开始关闭...");
	            WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	            YDQuartzInter ydQuartzInter =(YDQuartzInter) context.getBean("ydQuartzImpl");
	            @SuppressWarnings("unchecked")
				List<YDQuartz> ydQuartzs =  ydQuartzInter.getQuartzOpen();
	            if(ydQuartzs!=null){
	            	YDQuartz ydQuartz = ydQuartzs.get(0);
	 	            ydQuartz.setState(0);
	 				ydQuartzInter.update(ydQuartz);
	 	            Scheduler scheduler = (Scheduler) context.getBean("schedulerFactoryBean");
	 	           JobKey jobKey = JobKey.jobKey(ydQuartz.getName(), ydQuartz.getGroup());
	 	          scheduler.pauseJob(jobKey);
	 	         scheduler.deleteJob(jobKey);
	 	        System.out.println(scheduler.checkExists(jobKey)?"":"scheduler已经清空");
	 	            scheduler.shutdown(true);
	 	            Thread.sleep(1000);
	            }else{
	            	System.out.println("无启动状态的计数器");
	            }
	           
	            System.out.println("系统关闭");
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("系统开始启动");
		 WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
         YDQuartzInter ydQuartzInter =(YDQuartzInter) context.getBean("ydQuartzImpl");
         @SuppressWarnings("unchecked")
		List<YDQuartz> ydQuartzs =  ydQuartzInter.getQuartzOpen();
         if(ydQuartzs!=null){
        	 YDQuartz ydQuartz = ydQuartzs.get(0);
        	 JobKey jobKey = JobKey.jobKey(ydQuartz.getName(), ydQuartz.getGroup());
        	 
             Scheduler scheduler = (Scheduler) context.getBean("schedulerFactoryBean"); 
             try {
            	 
     			scheduler.resumeJob(jobKey);
     			 System.out.println(scheduler.checkExists(jobKey)?"scheduler已经加载"+jobKey.getName():"scheduler为空");
     		} catch (SchedulerException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
         }else{
        	 System.out.println("无启动状态的计时器");
         }
        
        
        	 
       
      
	}
	
	
	

}
