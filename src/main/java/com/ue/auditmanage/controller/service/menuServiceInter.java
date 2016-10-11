package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.ViewMenu;


public interface menuServiceInter  {
	public List<ViewMenu> findAllfmmenu();
	public List<ViewMenu> findElements( Integer pid  );
	public List<ViewMenu> findroots( );
	
	public boolean hasSubmit( Integer pid );
	public boolean checkName( String name ,Integer pid);
	public boolean deletNodeById(int uid);
	public void save(ViewMenu ydData);
	public boolean deletById(int uid,String hql);
	public void update(ViewMenu ydData);
	public ViewMenu getObject(Class<ViewMenu> cl,Serializable id);

	public Long getTotal(String hql,List<Object>paras);
}
