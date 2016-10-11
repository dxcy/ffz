package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.YDQuartz;
public interface YDQuartzInter {
	public void save(YDQuartz ydData);
	public boolean deletUserById(int uid);
	public void update(YDQuartz ydData);
	public YDQuartz getObject(Class<YDQuartz> cl,Serializable id);
	public List<YDQuartz> findAllData();
	public List<YDQuartz> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<YDQuartz> findData( String hql,@SuppressWarnings("rawtypes") List[] paras);
	public Long getTotal(String hql,List<Object>paras);
	@SuppressWarnings("rawtypes")
	public List getQuartzOpen();

}
