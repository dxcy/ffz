package com.ue.auditmanage.controller.service;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.AppType;
import entity.Atlas;

public class AtlasServiceImpl implements AtlasServiceInter {
	@Resource
	private BaseDaoInter<Atlas> baseDao;
	
	public BaseDaoInter<Atlas> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<Atlas> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void saveAtlas(Atlas atlas) {
		baseDao.save(atlas);
	}
	@Override
	public void updateAtlas(Atlas atlas) {
		baseDao.update(atlas);
	}
	@Override
	public Atlas getAtlasById(Class<Atlas> cl, Serializable id) {
		return baseDao.getObject(cl, id);
	}
	@Override
	public List<Atlas> findAllatlas(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<Atlas> findByFenyeatlas(String hql, List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<Atlas> findatlas(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getAtlasTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public Atlas findByName(String name) {
		String hql = " from Atlas where name =?";
		String[] para = {name};
		Atlas bm = new Atlas();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from Atlas u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public Atlas findByUrl(String url) {
		String hql = " from Atlas where aUrl =?";
		String[] para = {url};
		Atlas bm = new Atlas();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public List<Atlas> findAllAtlasByList(String hql, List paras) {
		// TODO Auto-generated method stub
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
