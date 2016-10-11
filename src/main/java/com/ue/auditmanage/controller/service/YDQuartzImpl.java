package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.YDQuartz;

public class YDQuartzImpl implements YDQuartzInter {
	@Resource
	private BaseDaoInter<YDQuartz> baseDao;
	public BaseDaoInter<YDQuartz> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<YDQuartz> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(YDQuartz ydData) {
		baseDao.save(ydData);

	}

	@Override
	public boolean deletUserById(int uid) {
		String hql = "delete from YDQuartz u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(YDQuartz ydData) {
		baseDao.update(ydData);

	}

	@Override
	public YDQuartz getObject(Class<YDQuartz> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<YDQuartz> findAllData() {
		String hql = "from YDQuartz where 1=1";
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDQuartz> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<YDQuartz> findData(String hql, @SuppressWarnings("rawtypes") List[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getQuartzOpen() {
		String hql = "from YDQuartz ftp where 1=1 and ftp.state =?";
		List<Serializable> paras = new ArrayList<Serializable>();
		paras.add(1);
		return (baseDao.findBydate(hql, paras)!=null &&baseDao.findBydate(hql, paras).size()>0 )? baseDao.findBydate(hql, paras):null;
	}

}
