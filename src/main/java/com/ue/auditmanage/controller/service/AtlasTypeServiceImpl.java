package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.AppType;
import entity.BModule;
import entity.atlasType;

public class AtlasTypeServiceImpl implements AtlasTypeServiceInter {
	@Resource
	private BaseDaoInter<atlasType> baseDao;
	
	public BaseDaoInter<atlasType> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<atlasType> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void saveAtlasType(atlasType atlasType) {
		baseDao.save(atlasType);
	}

	@Override
	public void updateAtlasType(atlasType atlasType) {
		baseDao.update(atlasType);
	}

	@Override
	public atlasType getAtlasTypeById(Class<atlasType> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<atlasType> findAllatlasType(String hql) {
		// TODO Auto-generated method stub
		return baseDao.find(hql);
	}

	@Override
	public List<atlasType> findByFenyeatlasType(String hql, List paras,
			int page, int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<atlasType> findatlasType(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getatlasTypeTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public atlasType findByName(String name) {
		String hql = " from atlasType where name =?";
		String[] para = {name};
		atlasType bm = new atlasType();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from atlasType u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public List<atlasType> findAllatlasType(String hql, List paras) {
		// TODO Auto-generated method stub
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
