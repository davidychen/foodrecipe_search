package com.foodrecipe.searchdemo.interaction;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class Interaction {
	@CsvBindByName
	private String user_id;
	@CsvBindByName
	private String recipe_id;
	@CsvBindByName
	@CsvDate("yyyy-MM-dd")
	private Date date;
	@CsvBindByName
	private int rating;
	
	public Interaction() {
	
	}
	
	@Override
	public String toString() {
		return "User_id: " + getUser_id() + "\n" + "Recipe_id: " + getRecipe_id() + "\n" + "Date: " + getDate() + "\n" +
				"Rating: " + getRating();
	}
	
	public String getUser_id() {
		return user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getRecipe_id() {
		return recipe_id;
	}
	
	public void setRecipe_id(String recipe_id) {
		this.recipe_id = recipe_id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
}
