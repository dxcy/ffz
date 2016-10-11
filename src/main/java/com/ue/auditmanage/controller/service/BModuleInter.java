package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.BModule;


public interface BModuleInter {
	public void save(BModule ydData);
	public boolean deletUserById(int uid);
	public void update(BModule ydData);
	public BModule getObject(Class<BModule> cl,Serializable id);
	public List<BModule> findAllData(String hql);
	public List<BModule> findDataByFenye(String hql, List<?> paras,int page,int rows);
	public List<BModule> findData( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public BModule findByName(String name);
}
