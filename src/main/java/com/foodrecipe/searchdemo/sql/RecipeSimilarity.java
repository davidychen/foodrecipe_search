package com.foodrecipe.searchdemo.sql;

public class RecipeSimilarity {
	private int recipeId1;
	private int recipeId2;
	private float avgRatingAll;
	private float avgRating1;
	private float avgRating2;
	private double similarity;
	
	@Override
	public String toString() {
		return "RecipeSimilarity{" + "recipeId1=" + recipeId1 + ", recipeId2=" + recipeId2 + ", avgRatingAll=" +
				avgRatingAll + ", avgRating1=" + avgRating1 + ", avgRating2=" + avgRating2 + ", similarity=" +
				similarity + '}';
	}
	
	public RecipeSimilarity(int recipeId1, int recipeId2, float avgRatingAll, float avgRating1, float avgRating2, double similarity) {
		this.recipeId1 = recipeId1;
		this.recipeId2 = recipeId2;
		this.avgRatingAll = avgRatingAll;
		this.avgRating1 = avgRating1;
		this.avgRating2 = avgRating2;
		this.similarity = similarity;
	}
	
	public float getAvgRatingAll() {
		return avgRatingAll;
	}
	
	public void setAvgRatingAll(float avgRatingAll) {
		this.avgRatingAll = avgRatingAll;
	}
	
	public int getRecipeId1() {
		return recipeId1;
	}
	
	public void setRecipeId1(int recipeId1) {
		this.recipeId1 = recipeId1;
	}
	
	public int getRecipeId2() {
		return recipeId2;
	}
	
	public void setRecipeId2(int recipeId2) {
		this.recipeId2 = recipeId2;
	}
	
	public float getAvgRating1() {
		return avgRating1;
	}
	
	public void setAvgRating1(float avgRating1) {
		this.avgRating1 = avgRating1;
	}
	
	public float getAvgRating2() {
		return avgRating2;
	}
	
	public void setAvgRating2(float avgRating2) {
		this.avgRating2 = avgRating2;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
}
