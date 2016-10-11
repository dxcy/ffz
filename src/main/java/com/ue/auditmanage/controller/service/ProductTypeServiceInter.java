package com.ue.auditmanage.controller.service;
import java.io.Serializable;
import java.util.List;

import entity.AppType;
import entity.ProductionType;
public interface ProductTypeServiceInter {
	public void save(ProductionType productionType);
	public boolean deletById(int id);
	public void update(ProductionType productionType);
	public ProductionType getObject(Class<ProductionType> cl,Serializable id);
	public List<ProductionType> findAllData(String hql);
	public List<ProductionType> findDataByFenye(String hql, List paras,int page,int rows);
	public List<ProductionType> findData( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public ProductionType findByName(String name);
	public List<ProductionType> findAllProType( String hql,List paras);
}
