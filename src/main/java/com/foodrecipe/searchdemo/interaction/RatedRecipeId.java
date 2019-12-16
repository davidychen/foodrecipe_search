package com.foodrecipe.searchdemo.interaction;

import java.util.Date;

public class RatedRecipeId {
	private int recipe_id;
	private int rating;
	private Date rated_at;
	public RatedRecipeId (int recipe_id, int rating,Date rated_at) {
		this.recipe_id = recipe_id;
		this.rating = rating;
		this.rated_at = rated_at;
	}
	
	public int getRecipe_id() {
		return recipe_id;
	}
	
	public void setRecipe_id(int recipe_id) {
		this.recipe_id = recipe_id;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public Date getRated_at() {
		return rated_at;
	}
	
	public void setRated_at(Date rated_at) {
		this.rated_at = rated_at;
	}
	
	@Override
	public String toString() {
		return "RatedRecipeId{" + "recipe_id=" + recipe_id + ", rating=" + rating + ", rated_at=" + rated_at + '}';
	}
}
