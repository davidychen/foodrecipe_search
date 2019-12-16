package com.foodrecipe.searchdemo.interaction;

import com.foodrecipe.searchdemo.recipe.RatedRecipe;
import com.foodrecipe.searchdemo.recipe.ScoredRecipe;
import com.foodrecipe.searchdemo.solr.SolrCollection;
import com.foodrecipe.searchdemo.sql.SqlInteraction;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserInteraction {
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	SolrCollection col;
	private String userName;
	private List<RatedRecipeId> cache = new ArrayList<>();
	private Statement stmt;
	
	public UserInteraction(String collection, String userName) {
		this.userName = userName;
		col = new SolrCollection("foodrecipe");
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + collection +
							"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", "myuser",
					"9ol.0p;/");// for MySQL only)
			stmt = conn.createStatement();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		updateCache();
	}
	
	
	private void updateCache() {
		if (userName.length() == 0) {
			cache.clear();
		} else {
			try {
				String strSelect =
						"select * from user_interaction where user_name = '" + userName + "' order by rated_at desc";
				System.out.println("The SQL statement is: " + strSelect + "\n");  // Echo For debugging
				ResultSet rset = stmt.executeQuery(strSelect);
				cache.clear();
				while (rset.next()) {   // Move the cursor to the next row
					cache.add(new RatedRecipeId(rset.getInt("recipe_id"), rset.getInt("rating"), rset.getDate("rated_at")));
				}
				System.out.println("Cached ratings: " + cache);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}  // Step 5: Close conn and stmt - Done automatically by try-with-resources
		}
	}
	
	//	public static void main(String[] args) {
	//		UserInteraction userInteraction = new UserInteraction("foodrecipe", "test");
	//		userInteraction.searchRank("curry", 2000);
	//	}
	
	public List<RatedRecipe> ratedRecipes(){
		Map<Integer, RatedRecipe> foundMap = col.searchRated(cache.stream().map(RatedRecipeId::getRecipe_id).collect(Collectors.toList()));
		return IntStream.range(0,cache.size()).mapToObj(i -> {
			RatedRecipeId r = cache.get(i);
			RatedRecipe recipe =  foundMap.get(r.getRecipe_id());
			recipe.setUser_rating(r.getRating());
			recipe.setIndex(i);
			return recipe;
		}).collect(Collectors.toList());
	}
	
	public List<ScoredRecipe> searchRank(String query, int n) {
		SqlInteraction sqlInteraction = new SqlInteraction("foodrecipe");
		List<ScoredRecipe> results = col.search(query, n * 20);
		results = sqlInteraction.rankRecipes(results, cache);
		return results.subList(0, Math.min(n, results.size()));
	}
	
	public void addRating(int recipeId, int rating) {
		if (userName.length() > 0) {
			try {
				
				// INSERT a record
				String sqlInsert = "replace into user_interaction values ('" + userName + "', " + recipeId + ", '" +
						DATE_FORMAT.format(new Date()) + "', " + rating + ")";
				System.out.println("The SQL statement is: " + sqlInsert + "\n");  // Echo for debugging
				int countInserted = stmt.executeUpdate(sqlInsert);
				System.out.println(countInserted + " records inserted/updated.\n");
				
				
			} catch (SQLException ex) {
				ex.printStackTrace();
			}  // Step 5: Close conn and stmt - Done automatically by try-with-resources
			updateCache();
		}
	}
	
	public void changeName(String userName) {
		if (!this.userName.equals(userName)) {
			this.userName = userName;
			System.out.println("Name changed to: " + userName);
			updateCache();
		}
	}
}
