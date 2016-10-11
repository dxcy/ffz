package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.Monitoring;

public class YDMonitoringImpl implements YDMonitoringInter {
	@Resource
	private BaseDaoInter<Monitoring> baseDao;
	public BaseDaoInter<Monitoring> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<Monitoring> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(Monitoring monitoring) {
		baseDao.save(monitoring);

	}

	@Override
	public boolean deletUserById(int uid) {
		String hql = "delete from Monitoring u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(Monitoring monitoring) {
		baseDao.update(monitoring);

	}

	@Override
	public Monitoring getObject(Class<Monitoring> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<Monitoring> findAllData(String hql) {
		// TODO Auto-generated method stub
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Monitoring> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Monitoring> findData(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return (List<Monitoring>) baseDao.findObject(hql,paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql,paras);
	}

	@Override
	public Monitoring findByName(String name) {
		String hql = " from Monitoring where name =?";
		String[] para = {name};
		Monitoring bm = new Monitoring();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

}
