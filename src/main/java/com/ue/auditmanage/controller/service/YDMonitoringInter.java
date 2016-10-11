package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.Monitoring;


public interface YDMonitoringInter {

		public void save(Monitoring monitoring);
		public boolean deletUserById(int uid);
		public void update(Monitoring monitoring);
		public Monitoring getObject(Class<Monitoring> cl,Serializable id);
		public List<Monitoring> findAllData(String hql);
		public List<Monitoring> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
		public List<Monitoring> findData( String hql,String[] paras);
		public Long getTotal(String hql,List<Object>paras);
		public Monitoring findByName(String name);
	}


