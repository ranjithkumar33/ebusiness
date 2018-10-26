package com.microapps.ebusiness.mystore.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.microapps.ebusiness.mystore.application.dao.ItemGroupDao;
import com.microapps.ebusiness.mystore.application.dao.ItemGroupDaoImpl;
import com.microapps.ebusiness.mystore.application.entity.ItemGroup;

public class ItemGroupService {
	
	 private static final Logger LOGGER = Logger.getLogger(ItemGroupService.class.getName());
	
	private ItemGroupDao dao;
	
	public ItemGroupService() {
		dao = new ItemGroupDaoImpl();
	}
	
	public List<String> getAllItemGroups(){
		List<ItemGroup> ig = dao.findAllItemGroups();
		List<String> l = new ArrayList<>();
		if(ig != null) {
			ig.forEach(i -> {
				l.add(i.getName());
			});
		}
		return l;
	}
	
	public void itemMasterDataImport(List<String> cl) {
		   dao.saveAll(ItemGroupAssembler.toEntityList(cl));
   }

}
