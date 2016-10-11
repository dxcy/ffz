package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.App;
import entity.ProductionType;
import entity.ViewMenu;

public class ProductTypeServiceImpl implements ProductTypeServiceInter {
	@Resource
	private BaseDaoInter<ProductionType> baseDao;
	
	public BaseDaoInter<ProductionType> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<ProductionType> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(ProductionType productionType) {
		baseDao.save(productionType);
	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from ProductionType u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(ProductionType productionType) {
		baseDao.update(productionType);
	}

	@Override
	public ProductionType getObject(Class<ProductionType> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<ProductionType> findAllData(String hql) {
		// TODO Auto-generated method stub
		return baseDao.find(hql);
	}

	@Override
	public List<ProductionType> findDataByFenye(String hql, List paras,
			int page, int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<ProductionType> findData(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public ProductionType findByName(String name) {
		String hql = " from ProductionType where name =?";
		String[] para = {name};
		ProductionType bm = new ProductionType();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public List<ProductionType> findAllProType(String hql, List paras) {
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
