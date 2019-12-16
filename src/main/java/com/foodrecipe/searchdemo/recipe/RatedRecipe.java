package com.foodrecipe.searchdemo.recipe;

public class RatedRecipe extends Recipe {
	private int index;
	private int user_rating = -1;
	public RatedRecipe() {
		super();
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getUser_rating() {
		return user_rating;
	}
	
	public void setUser_rating(int userRating) {
		this.user_rating = userRating;
	}
}
