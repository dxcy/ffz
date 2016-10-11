package com.ue.auditmanage.controller.service;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import com.ue.auditmanage.controller.dao.BaseDaoInter;
import entity.App;
public class AppServiceImpl implements AppServiceInter {
	@Resource
	private BaseDaoInter<App> baseDao;
	public BaseDaoInter<App> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<App> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(App app) {
		baseDao.save(app);

	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from App u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(App app) {
		baseDao.update(app);
	}

	@Override
	public App getObject(Class<App> cl, Serializable id) {
		return baseDao.getObject(cl,id);
	}

	@Override
	public List<App> findAllData(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<App> findDataByFenye(String hql, List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<App> findData(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public App findByName(String name) {
		String hql = " from App where name =?";
		String[] para = {name};
		App bm = new App();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public App findByAid(String aid) {
		String hql = " from App where aid =?";
		String[] para = {aid};
		App bm = new App();
		bm =baseDao.findObject(hql, para);
		return bm;
	}

	@Override
	public List<App> findApp(String hql, List paras) {
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

	@Override
	public App findByUrl(String url) {
		String hql = " from App where url =?";
		String[] para = {url};
		return  baseDao.findObject(hql, para)!=null?baseDao.findObject(hql, para):null;
	}

}
