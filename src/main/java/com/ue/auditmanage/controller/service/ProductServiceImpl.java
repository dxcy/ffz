package com.ue.auditmanage.controller.service;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.App;
import entity.Atlas;
import entity.Production;
public class ProductServiceImpl implements ProductServiceInter {
	@Resource
	private BaseDaoInter<Production> baseDao;
	
	public BaseDaoInter<Production> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<Production> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(Production production) {
		baseDao.save(production);
	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from Production u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(Production production) {
		baseDao.update(production);
	}

	@Override
	public Production getObject(Class<Production> cl, Serializable id) {
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<Production> findAllData(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<Production> findDataByFenye(String hql, List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<Production> findData(String hql, String[] paras) {
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
	
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public Production findByName(String name) {
		String hql = " from Production where name =?";
		String[] para = {name};
		Production bm = new Production();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public Production findByProid(String pid) {
		String hql = " from Production where proid =?";
		String[] para = {pid};
		Production bm = new Production();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public List<Production> findProduction(String hql, List paras) {
		
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

	@Override
	public Production findByUrl(String url) {
		String hql = " from Production where url =?";
		String[] para = {url};
		Production bm = new Production();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

}
