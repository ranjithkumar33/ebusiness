package com.microapps.ebusiness.mystore.application.view;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerView {

	private IntegerProperty id;
	private DoubleProperty amount;
    private StringProperty name;
    private ObjectProperty<LocalDate> date;
    
    public CustomerView(int id, String name, double amount, LocalDateTime date) {
    	this.id= new SimpleIntegerProperty(id);
    	this.name = new SimpleStringProperty(name);
    	this.amount = new SimpleDoubleProperty(amount);
    	this.date = new SimpleObjectProperty<LocalDate>(date.toLocalDate());
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

	public IntegerProperty getId() {
		return id;
	}

	public void setId(IntegerProperty id) {
		this.id = id;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public ObjectProperty<LocalDate> getDate() {
		return date;
	}

	public void setDate(ObjectProperty<LocalDate> date) {
		this.date = date;
	}


}
