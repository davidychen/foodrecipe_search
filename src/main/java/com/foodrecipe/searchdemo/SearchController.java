package com.foodrecipe.searchdemo;

import com.foodrecipe.searchdemo.interaction.UserInteraction;
import com.foodrecipe.searchdemo.recipe.RatedRecipe;
import com.foodrecipe.searchdemo.recipe.ScoredRecipe;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
	private UserInteraction userInteraction = new UserInteraction("foodrecipe", "");
	
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public List<ScoredRecipe> search(@RequestParam(value = "query") String query,
	                                   @RequestParam(value = "n", defaultValue = "100") String strN) {
		try {
			int n = Integer.valueOf(strN);
			return userInteraction.searchRank(query, n);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/change_name")
	public void changeName(@RequestParam(value = "user_name", defaultValue = "") String userName) {
		userInteraction.changeName(userName);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rate")
	public void rate(@RequestParam(value = "recipe_id") String recipeIdStr,
	                       @RequestParam(value = "rating") String ratingStr) {
		try {
			int recipeId = Integer.valueOf(recipeIdStr);
			int rating = Integer.valueOf(ratingStr);
			userInteraction.addRating(recipeId, rating);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rated_recipes")
	public List<RatedRecipe> getRated() {
		return userInteraction.ratedRecipes();
	}
	
}
