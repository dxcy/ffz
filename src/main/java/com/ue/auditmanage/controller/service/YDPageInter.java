package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;



import entity.YDPage;

public interface YDPageInter {
	public void save(YDPage ydPage);
	public boolean deletById(int uid,String hql);
	public void update(YDPage ydPage);
	public YDPage getObject(Class<YDPage> cl,Serializable id);
	public List<YDPage> findAllData(String hql);
	public YDPage findServiceByUrl(String url);
	public List<YDPage> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<YDPage> findData( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public boolean deletById(int did);
}
