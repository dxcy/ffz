package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.YDPage;

public class YDPageImpl implements YDPageInter {
	@Resource
	private BaseDaoInter<YDPage> baseDao;
	
	public BaseDaoInter<YDPage> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<YDPage> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(YDPage ydPage) {
		baseDao.save(ydPage);

	}

	@Override
	public boolean deletById(int uid, String hql) {
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(YDPage ydPage) {
		baseDao.update(ydPage);

	}

	@Override
	public YDPage getObject(Class<YDPage> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<YDPage> findAllData(String hql) {
		// TODO Auto-generated method stub
		return baseDao.find(hql);
	}

	@Override
	public YDPage findServiceByUrl(String url) {
		String hql = "from YDPage  where 1=1  and Url =?";
		String[] paras = {url};
		YDPage data = baseDao.findObject(hql, paras);
		return data!=null ? data : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDPage> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDPage> findData(String hql, String[] paras) {
		return (List<YDPage>) baseDao.findObject(hql,paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras) ;
	}

	@Override
	public boolean deletById(int did) {
		String hql = "delete from YDPage p where p.id = ? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, did);
		
		return(query.executeUpdate()>0);
	}

}
