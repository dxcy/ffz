package com.ue.auditmanage.controller.service;
import java.io.Serializable;
import java.util.List;

import entity.Atlas;
public interface AtlasServiceInter {
	public void saveAtlas( Atlas atlas);
	public void updateAtlas(Atlas atlas);
	public Atlas getAtlasById(Class<Atlas> cl,Serializable id);
	public List<Atlas> findAllatlas(String hql);
	public List<Atlas> findByFenyeatlas(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<Atlas> findatlas( String hql,String[] paras);
	public Long getAtlasTotal(String hql,List<Object>paras);
	public Atlas  findByName(String name);
	public Atlas  findByUrl(String url);
	public boolean deletById(int id);
	public List<Atlas> findAllAtlasByList( String hql,List paras);
}
