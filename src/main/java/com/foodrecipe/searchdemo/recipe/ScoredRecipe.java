package com.foodrecipe.searchdemo.recipe;

import org.apache.solr.client.solrj.beans.Field;

public class ScoredRecipe extends Recipe {
	@Field("score")
	private float score;
	private double predict_rating;
	private int index;
	private float avg_rating = -1;
	private int user_rating = -1;
	
	public ScoredRecipe() {
		super();
	}
	
	public float getAvg_rating() {
		return avg_rating;
	}
	
	public void setAvg_rating(float avg_rating) {
		this.avg_rating = avg_rating;
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
	
	public float getScore() {
		return score;
	}
	
	@Field("score")
	public void setScore(float score) {
		this.score = score;
	}
	
	public double getPredict_rating() {
		return predict_rating;
	}
	
	public void setPredict_rating(double predict_rating) {
		this.predict_rating = predict_rating;
	}
	
	public double getTotal() {
		return score + predict_rating * 2;
	}
	
	@Override
	public String toString() {
		return "{" + "score=" + score + ", predict_rating=" + predict_rating + ", index=" + index + ", avg_rating=" +
				avg_rating + ", user_rating=" + user_rating + "} " + super.toString();
	}
}
