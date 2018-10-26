package com.microapps.ebusiness.mystore.application.view;

import java.time.LocalDateTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ActivityView {
		private DoubleProperty amount;
	    private StringProperty name;
	    private StringProperty itemGroup;
	    private ObjectProperty<LocalDateTime> date;
	    
	    public ActivityView(String name, String itemGroup, double amount, LocalDateTime date) {
	    	this.name = new SimpleStringProperty(name);
	    	this.amount = new SimpleDoubleProperty(amount);
	    	this.date = new SimpleObjectProperty<LocalDateTime>(date);
	    	this.itemGroup=new SimpleStringProperty(itemGroup);
	    }

		public DoubleProperty getAmount() {
			return amount;
		}

		public void setAmount(DoubleProperty amount) {
			this.amount = amount;
		}

		public StringProperty getName() {
			return name;
		}

		public void setName(StringProperty name) {
			this.name = name;
		}

		public ObjectProperty<LocalDateTime> getDate() {
			return date;
		}

		public void setDate(ObjectProperty<LocalDateTime> date) {
			this.date = date;
		}

		public StringProperty getItemGroup() {
			return itemGroup;
		}

		public void setItemGroup(StringProperty itemGroup) {
			this.itemGroup = itemGroup;
		}
}
