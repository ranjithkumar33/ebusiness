package com.microapps.ebusiness.mystore.application.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.microapps.ebusiness.mystore.application.dao.ActivityDao;
import com.microapps.ebusiness.mystore.application.dao.ActivityDaoImpl;
import com.microapps.ebusiness.mystore.application.dao.CustomerDao;
import com.microapps.ebusiness.mystore.application.dao.CustomerDaoImpl;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.entity.Customer;
import com.microapps.ebusiness.mystore.application.exception.DuplicateEntryException;

public class CustomerService {
	
	private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
	   
	
	private CustomerDao cdao;
	
	private ActivityDao adao;
	
	private LoyalyPointsService lps;
	
	public CustomerService() {
		cdao = new CustomerDaoImpl();
		adao = new ActivityDaoImpl();
		lps = new LoyalyPointsService();
	}
	
	public List<CustomerDto> getAllCustomers() {
		 List<CustomerDto> dList = new ArrayList<>();
		List<Customer> cList = cdao.findAllCustomers();
		if(cList != null) {
			for(Customer c : cList) {
				dList.add(CustomerAssembler.toDto(c));
			}
		}
	   return dList;
    }
	
	/*public List<CustomerDto> getAllCustomersForReport() {
		   return cdao.findAllCustomersForCSV();
	}*/
	
	public CustomerDto getCustomerById(int id)  {
		Customer c = cdao.findCustomerById(id);
		//c.setActivities(adao.findActivitiesByCustomer(c.getId()));
		Session.getSession().addCustomerToSession(CustomerAssembler.toDto(c));
		return Session.getSession().getCustomerFromSession();
	}
	
   public CustomerDto saveCustomer(CustomerDto c)  {
	    Customer _c = cdao.saveCustomer(CustomerAssembler.toEntity(c));
	    Session.getSession().addCustomerToSession(CustomerAssembler.toDto(_c));
		return Session.getSession().getCustomerFromSession();
		
	}
   
   public void bulkCustomerDataImport(List<CustomerDto> cl) {
		cdao.saveAllCustomers(CustomerAssembler.toEntityList(cl));
   }
   
   public CustomerDto updateCustomer(CustomerDto c) {
	    Customer _c = cdao.saveCustomer(CustomerAssembler.toEntity(c));
	    Session.getSession().addCustomerToSession(CustomerAssembler.toDto(_c));
		return Session.getSession().getCustomerFromSession();
		
	}
   
	public CustomerDto searchCustomer(String query) throws Exception {
		CustomerDto c = null;
		if(query != null) {
			
			if(query.length() == 16 ) {
				Customer _c = cdao.searchCustomerByCard(query);
				//c.setActivities(adao.findActivitiesByCustomer(c.getId()));
				c = CustomerAssembler.toDto(_c);
			}else {
				Customer _c  = cdao.searchCustomerByMobile(query);
				//c.setActivities(adao.findActivitiesByCustomer(c.getId()));
				c = CustomerAssembler.toDto(_c);
			}
		}
		Session.getSession().addCustomerToSession(c);
		return Session.getSession().getCustomerFromSession();
		
	}

}
