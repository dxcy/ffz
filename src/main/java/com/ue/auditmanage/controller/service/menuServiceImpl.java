package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.ViewMenu;



public class menuServiceImpl implements menuServiceInter {
	@Resource
	private BaseDaoInter<ViewMenu> baseDao;

	public BaseDaoInter<ViewMenu> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<ViewMenu> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<ViewMenu> findAllfmmenu() {
		String hql = "from ViewMenu ";
		return baseDao.find(hql);
	}

	@Override
	public List<ViewMenu> findElements(Integer pid) {
		String hql = "from ViewMenu menu where menu.pid =? ";
		Object[] paras = { pid };

		return (List<ViewMenu>) baseDao.findObjects(hql, paras);

	}

	@Override
	public List<ViewMenu> findroots() {
		String hql = " from ViewMenu menu where menu.pid is null ";
		List<ViewMenu> f = baseDao.find(hql);
		if(f.size()>0){
			return   f;	
		}else{
			return null;
		}
		
	}

	@Override
	public boolean hasSubmit(Integer pid) {//判断是否是叶子节点
		String hql = "from ViewMenu menu where menu.pid =? ";
		Object[] paras = { pid };
		List<ViewMenu> f = baseDao.findObjects(hql, paras);
		if(f!=null && f.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public void save(ViewMenu ydData) {
		// TODO Auto-generated method stub
		baseDao.save(ydData);
	}

	@Override
	public boolean deletById(int uid, String hql) {
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
	}
	@Override
	public boolean deletNodeById(int uid) {
		
		String hql = "delete from ViewMenu p where p.id = ? or p.pid=?";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		query.setInteger(1, uid);
		return(query.executeUpdate()>0);
	}
	@Override
	public void update(ViewMenu ydData) {
		baseDao.update(ydData);
		
	}

	@Override
	public ViewMenu getObject(Class<ViewMenu> cl, Serializable id) {
		return baseDao.getObject(cl, id);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		return baseDao.getTotal(hql,paras);
	}

	@Override
	public boolean checkName(String name, Integer pid) {
		String hql = "from ViewMenu menu where menu.pid =? and menu.text=? ";
		Object[] paras = { pid,name};
		List<ViewMenu> f = baseDao.findObjects(hql, paras);
		if(f!=null && f.size()>0){
			return true;
		}
		return false;
	}

	

}
