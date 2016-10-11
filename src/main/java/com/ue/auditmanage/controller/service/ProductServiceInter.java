package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.Atlas;
import entity.Production;


public interface ProductServiceInter {
	public void save(Production production);
	public boolean deletById(int id);
	public void update(Production production);
	public Production getObject(Class<Production> cl,Serializable id);
	public List<Production> findAllData(String hql);
	public List<Production> findDataByFenye(String hql, List paras,int page,int rows);
	public List<Production> findData( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public Production findByName(String name);
	public Production  findByProid(String pid);
	
	public Production  findByUrl(String url);
	public List<Production> findProduction( String hql,List paras);
	
}
