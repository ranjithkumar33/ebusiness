package com.microapps.ebusiness.mystore.application.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.microapps.ebusiness.mystore.application.entity.ItemGroup;

public class ItemGroupAssembler {
	
	public static List<ItemGroup> toEntityList(List<String> l){
		List<ItemGroup> ig = new ArrayList<>();
		if(l != null) {
			
			l.forEach(s -> {
				ItemGroup g = new ItemGroup();
				g.setCreatedBy(SecurityContext.getSecurityContext().getAuthenticatedUser().getUsername());
				g.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				g.setName(s);
				g.setStatus(true);
				
				ig.add(g);
			});
			
		}
		
		return ig;
		
	}
	

}
