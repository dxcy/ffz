package com.ue.auditmanage.controller.service;

import java.util.List;

import javax.annotation.Resource;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.AuditInfos;
import entity.YDUser;

public class AuditInfosServiceImpl implements AuditInfoServiceInter {
	@Resource
	private BaseDaoInter<AuditInfos> baseDao;
	public BaseDaoInter<AuditInfos> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<AuditInfos> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(AuditInfos auditInfos) {
		baseDao.save(auditInfos);

	}

	@Override
	public List<AuditInfos> findAllData(String hql) {
		
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditInfos> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,
			int page, int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras) ;
	}

	@Override
	public List<AuditInfos> find(String hql, String[] paras) {
		return (List<AuditInfos>) baseDao.findObjects(hql,paras);
	}

	@Override
	public List<AuditInfos> findByList(String hql, List paras) {
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
