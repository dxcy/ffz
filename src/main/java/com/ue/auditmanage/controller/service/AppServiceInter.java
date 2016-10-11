package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.App;


public interface AppServiceInter {
	public void save(App app);
	public boolean deletById(int id);
	public void update(App app);
	public App getObject(Class<App> cl,Serializable id);
	public List<App> findAllData(String hql);
	public List<App> findDataByFenye(String hql, List paras,int page,int rows);
	public List<App> findData( String hql,String[] paras);
	public List<App> findApp( String hql,List paras);
	public Long getTotal(String hql,List<Object>paras);
	public App findByName(String name);
	public App findByAid(String aid);
	public App  findByUrl(String url);
}
