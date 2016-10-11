package com.ue.auditmanage.filter;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
public class JobBeanJobFactory extends SpringBeanJobFactory implements
		ApplicationContextAware {

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
	}

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		quartzJob jobInstance = (quartzJob)super.createJobInstance(bundle);
        return jobInstance;
	
	}

}
