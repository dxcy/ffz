package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.AppType;
import entity.atlasType;
public interface AppTypeServiceInter {
	public void saveAppType( AppType appType);
	public void updateAppType(AppType appType);
	public AppType getAppTypeById(Class<AppType> cl,Serializable id);
	public List<AppType> findAllAppType(String hql);
	public List<AppType> findByFenyeAppType(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<AppType> findAppType( String hql,String[] paras);
	public Long getAppTypeTotal(String hql,List<Object>paras);
	public AppType  findByName(String name);
	public List<AppType> findAllappType( String hql,List paras);
	public boolean deletById(int id);
}
