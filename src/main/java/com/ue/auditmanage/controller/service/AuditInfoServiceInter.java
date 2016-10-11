package com.ue.auditmanage.controller.service;

import java.util.List;

import entity.AuditInfos;

public interface AuditInfoServiceInter  {
	public void save(AuditInfos auditInfos);
	public List<AuditInfos> findAllData(String hql);
	public List<AuditInfos> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public Long getTotal(String hql,List<Object>paras);
	public List<AuditInfos> find( String hql,String[] paras);
	public List<AuditInfos> findByList( String hql,List paras);
}
