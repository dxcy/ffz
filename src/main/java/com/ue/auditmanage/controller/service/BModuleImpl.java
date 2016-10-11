package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.BModule;
public class BModuleImpl implements BModuleInter {
	@Resource
	private BaseDaoInter<BModule> baseDao;

	public BaseDaoInter<BModule> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<BModule> baseDao) {
		this.baseDao = baseDao;
	}
	@Override
	public void save(BModule ydData) {
		baseDao.save(ydData);

	}

	@Override
	public boolean deletUserById(int uid) {
		String hql = "delete from BModule u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
		
	}

	@Override
	public void update(BModule ydData) {
		baseDao.update(ydData);
	}

	@Override
	public BModule getObject(Class<BModule> cl, Serializable id) {
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<BModule> findAllData(String hql) {
		return baseDao.find(hql);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<BModule> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BModule> findData(String hql, String[] paras) {
		return (List<BModule>) baseDao.findObject(hql,paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
				return baseDao.getTotal(hql,paras);
	}
	
	@Override
	public BModule findByName(String name) {
		String hql = " from BModule where name =?";
		String[] para = {name};
		BModule bm = new BModule();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

}
