package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.Atlas;
import entity.atlasType;

public interface AtlasTypeServiceInter {
	public void saveAtlasType( atlasType atlasType);
	public void updateAtlasType(atlasType atlasType);
	public atlasType getAtlasTypeById(Class<atlasType> cl,Serializable id);
	public List<atlasType> findAllatlasType(String hql);
	public List<atlasType> findByFenyeatlasType(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<atlasType> findatlasType( String hql,String[] paras);
	public List<atlasType> findAllatlasType( String hql,List paras);
	public Long getatlasTypeTotal(String hql,List<Object>paras);
	public atlasType  findByName(String name);
	public boolean deletById(int id);
}
