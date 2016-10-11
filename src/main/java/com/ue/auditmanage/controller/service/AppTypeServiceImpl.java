package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.App;
import entity.AppType;

public class AppTypeServiceImpl implements AppTypeServiceInter {
	@Resource
	private BaseDaoInter<AppType> baseDao;
	
	public BaseDaoInter<AppType> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<AppType> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void saveAppType(AppType appType) {
		baseDao.save(appType);
	}

	@Override
	public void updateAppType(AppType appType) {
		baseDao.update(appType);
	}

	@Override
	public AppType getAppTypeById(Class<AppType> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<AppType> findAllAppType(String hql) {
		// TODO Auto-generated method stub
		return baseDao.find(hql);
	}

	@Override
	public List<AppType> findByFenyeAppType(String hql, List paras, int page,
			int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<AppType> findAppType(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getAppTypeTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public AppType findByName(String name) {
		String hql = " from AppType where name =?";
		String[] para = {name};
		AppType bm = new AppType();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from AppType u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public List<AppType> findAllappType(String hql, List paras) {
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
