package com.foodrecipe.searchdemo.recipe;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;
import java.util.List;

public class Recipe {
	@CsvBindByName
	@Field("name")
	private String name;
	@CsvBindByName
	@Field("id")
	private String id;
	@CsvBindByName
	@Field("minutes")
	private int minutes;
	@CsvBindByName
	@Field("contributor_id")
	private String contributor_id;
	@CsvBindByName
	@CsvDate("yyyy-MM-dd")
	@Field("submitted")
	private Date submitted;
	@CsvBindAndSplitByName(elementType = String.class)
	@Field("tags")
	private List<String> tags;
	@CsvBindAndSplitByName(elementType = Float.class)
	@Field("nutrition")
	private List<Float> nutrition;
	@CsvBindByName
	@Field("n_steps")
	private int n_steps;
	@CsvBindAndSplitByName(elementType = String.class)
	@Field("steps")
	private List<String> steps;
	@CsvBindByName
	@Field("description")
	private String description;
	@CsvBindAndSplitByName(elementType = String.class)
	@Field("ingredients")
	private List<String> ingredients;
	@CsvBindByName
	@Field("n_ingredients")
	private int n_ingredients;
	
	public Recipe(){
	
	}
	
	public Recipe(String name, String id, int minutes, String contributor_id, Date submitted, List<String> tags,
	       List<Float> nutrition, int n_steps, List<String> steps, String description, List<String> ingredients,
	       int n_ingredients) {
		this.name = name;
		this.id = id;
		this.minutes = minutes;
		this.contributor_id = contributor_id;
		this.submitted = submitted;
		this.tags = tags;
		this.nutrition = nutrition;
		this.n_steps = n_steps;
		this.steps = steps;
		this.description = description;
		this.ingredients = ingredients;
		this.n_ingredients = n_ingredients;
	}
	
	public String getName() {
		return name;
	}
	
	@Field("name")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	@Field("id")
	public void setId(String id) {
		this.id = id;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	@Field("minutes")
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	public String getContributor_id() {
		return contributor_id;
	}
	
	@Field("contributor_id")
	public void setContributor_id(String contributor_id) {
		this.contributor_id = contributor_id;
	}
	
	public Date getSubmitted() {
		return submitted;
	}
	
	@Field("submitted")
	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	@Field("tags")
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public List<Float> getNutrition() {
		return nutrition;
	}
	
	@Field("nutrition")
	public void setNutrition(List<Float> nutrition) {
		this.nutrition = nutrition;
	}
	
	public int getN_steps() {
		return n_steps;
	}
	
	@Field("n_steps")
	public void setN_steps(int n_steps) {
		this.n_steps = n_steps;
	}
	
	public List<String> getSteps() {
		return steps;
	}
	
	@Field("steps")
	public void setSteps(List<String> steps) {
		this.steps = steps;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Field("description")
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<String> getIngredients() {
		return ingredients;
	}
	
	@Field("ingredients")
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	
	@Override
	public String toString() {
		return "Recipe{" + "name='" + name + '\'' + ", id='" + id + '\'' + ", minutes=" + minutes +
				", contributor_id='" + contributor_id + '\'' + ", submitted=" + submitted + ", tags=" + tags +
				", nutrition=" + nutrition + ", n_steps=" + n_steps + ", steps=" + steps + ", description='" +
				description + '\'' + ", ingredients=" + ingredients + ", n_ingredients=" + n_ingredients + '}';
	}
	
	public int getN_ingredients() {
		return n_ingredients;
	}
	
	@Field("n_ingredients")
	public void setN_ingredients(int n_ingredients) {
		this.n_ingredients = n_ingredients;
	}
	
}
