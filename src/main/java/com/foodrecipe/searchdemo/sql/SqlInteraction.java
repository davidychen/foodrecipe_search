package com.foodrecipe.searchdemo.sql;

import com.foodrecipe.searchdemo.csv.CsvUtils;
import com.foodrecipe.searchdemo.interaction.Interaction;
import com.foodrecipe.searchdemo.interaction.RatedRecipeId;
import com.foodrecipe.searchdemo.recipe.Recipe;
import com.foodrecipe.searchdemo.recipe.ScoredRecipe;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SqlInteraction { // Save as "JdbcSelectTest.java"
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private Statement stmt;
	
	public SqlInteraction(String collection) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + collection +
							"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", "myuser",
					"9ol.0p;/");// for MySQL only)
			stmt = conn.createStatement();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	//	public static void main(String[] args) {
	//		SqlInteraction sqlInteraction = new SqlInteraction("foodrecipe");
	//
	//		if (!sqlInteraction.hasRecords("interactions")) {
	//			sqlInteraction.addAll("interactions");
	//		}
	//
	//		// sqlInteraction.deleteAll("similarity");
	//		//		if (!sqlInteraction.hasRecords("similarity")) {
	//		//			sqlInteraction.calculateSimilarity();
	//		//		}
	//	}
	
	private boolean hasRecords(String table) {
		try {
			// Step 3: Execute a SQL SELECT query. The query result is returned in a 'ResultSet' object.
			String strSelect = "select * from " + table;
			System.out.println("The SQL statement is: " + strSelect + "\n"); // Echo For debugging
			
			ResultSet rset = stmt.executeQuery(strSelect);
			
			// Step 4: Process the ResultSet by scrolling the cursor forward via next().
			//  For each row, retrieve the contents of the cells with getXxx(columnName).
			System.out.println("The records selected are:");
			return rset.next();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
		return false;
	}
	
	public void addAll(String table) {
		try {
			for (Interaction interaction : CsvUtils.parseInteractions()) {
				// INSERT a record
				String sqlInsert = "insert into " + table + " values (" + interaction.getUser_id() + ", " +
						interaction.getRecipe_id() + ", '" + DATE_FORMAT.format(interaction.getDate()) + "', " +
						interaction.getRating() + ")";
				System.out.println("The SQL statement is: " + sqlInsert + "\n");  // Echo for debugging
				int countInserted = stmt.executeUpdate(sqlInsert);
				System.out.println(countInserted + " records inserted.\n");
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}  // Step 5: Close conn and stmt - Done automatically by try-with-resources
	}
	
	public List<ScoredRecipe> rankRecipes(List<ScoredRecipe> foundRecipes, List<RatedRecipeId> ratings) {
		if (foundRecipes.size() < 1) {
			return foundRecipes;
		}
		Map<Integer, Float> avgMap = new HashMap<>();
		try {
			String strSelectAvg = "select distinct recipe_id, avg_rating from all_recipes where recipe_id in (" +
					foundRecipes.stream().map(Recipe::getId).collect(Collectors.joining(", ")) + ")";
			System.out.println("The SQL statement is: " + strSelectAvg + "\n");  // Echo For debugging
			ResultSet rsetAvg = stmt.executeQuery(strSelectAvg);
			
			while (rsetAvg.next()) {   // Move the cursor to the next row
				avgMap.put(rsetAvg.getInt("recipe_id"), rsetAvg.getFloat("avg_rating"));
			}
			for (ScoredRecipe recipe : foundRecipes) {
				int id = Integer.valueOf(recipe.getId());
				
				if (avgMap.containsKey(id)) {
					recipe.setAvg_rating(avgMap.get(id));
					recipe.setPredict_rating(avgMap.get(id));
				}
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}  // Step 5: Close conn and stmt - Done automatically by try-with-resources
		
		if (ratings.size() < 1) {
			foundRecipes.sort(Comparator.comparingDouble(ScoredRecipe::getTotal).reversed());
			IntStream.range(0, foundRecipes.size()).forEach(i -> foundRecipes.get(i).setIndex(i));
			return foundRecipes;
		}
		Map<Integer, Integer> ratingMap = new HashMap<>();
		ratings.forEach(r -> ratingMap.put(r.getRecipe_id(), r.getRating()));
		int takeN = (ratings.size() + 1) / 2;
		double b_x = ratings.stream().mapToDouble(RatedRecipeId::getRating).average().orElse(0.0);
		Map<Integer, List<RecipeSimilarity>> map = new HashMap<>();
		
		try {
			
			String strSelect = "with temp as (select * from similarity where recipe_id1 in (" +
					foundRecipes.stream().map(Recipe::getId).collect(Collectors.joining(", ")) +
					")) select * from temp where recipe_id2 in (" +
					ratings.stream().map(r -> ((Integer) r.getRecipe_id()).toString())
							.collect(Collectors.joining(", ")) + ")";
			System.out.println("The SQL statement is: " + strSelect + "\n");  // Echo For debugging
			ResultSet rset = stmt.executeQuery(strSelect);
			
			while (rset.next()) {   // Move the cursor to the next row
				RecipeSimilarity row = new RecipeSimilarity(rset.getInt("recipe_id1"), rset.getInt("recipe_id2"),
						rset.getFloat("avg_rating_all"), rset.getFloat("avg_rating1"), rset.getFloat("avg_rating2"),
						rset.getDouble("similarity"));
				map.computeIfAbsent(row.getRecipeId1(), k -> new ArrayList<>()).add(row);
			}
			
			for (ScoredRecipe recipe : foundRecipes) {
				int id = Integer.valueOf(recipe.getId());
				if (map.containsKey(id)) {
					List<RecipeSimilarity> lst = map.get(id);
					lst.sort(Comparator.comparingDouble(RecipeSimilarity::getSimilarity).reversed());
					double b_xi = 0.0;
					double sum = 0.0;
					double weight = 0.0;
					int curTake = Math.min(takeN, lst.size());
					for (int i = 0; i < curTake; i++) {
						RecipeSimilarity rSim = lst.get(i);
						if (i == 0) {
							b_xi += b_x + rSim.getAvgRating1() - rSim.getAvgRatingAll();
						}
						if (i < curTake - 1 || weight + rSim.getSimilarity() != 0) {
							sum += rSim.getSimilarity() * (ratingMap.get(rSim.getRecipeId2()) -
									(b_x + rSim.getAvgRating2() - rSim.getAvgRatingAll()));
							weight += rSim.getSimilarity();
						}
					}
					if (weight != 0.0) {
						double predictScore = b_xi + sum / weight;
						recipe.setPredict_rating(predictScore);
					}
				}
				if (ratingMap.containsKey(id)) {
					recipe.setUser_rating(ratingMap.get(id));
					recipe.setPredict_rating(ratingMap.get(id));
				}
			}
			
			foundRecipes.sort(Comparator.comparingDouble(ScoredRecipe::getTotal).reversed());
			IntStream.range(0, foundRecipes.size()).forEach(i -> foundRecipes.get(i).setIndex(i));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}  // Step 5: Close conn and stmt - Done automatically by try-with-resources
		return foundRecipes;
	}
	
	public void deleteAll(String table) {
		try {
			String sqlDelete = "delete from " + table;
			System.out.println("The SQL statement is: " + sqlDelete + "\n");  // Echo for debugging
			int countDeleted = stmt.executeUpdate(sqlDelete);
			System.out.println(countDeleted + " records deleted.\n");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void printAll() {
		try {
			
			String strSelect = "select * from interactions";
			System.out.println("The SQL statement is: " + strSelect + "\n");  // Echo For debugging
			ResultSet rset = stmt.executeQuery(strSelect);
			while (rset.next()) {   // Move the cursor to the next row
				System.out.println(
						rset.getString("user_id") + ", " + rset.getString("recipe_id") + ", " + rset.getString("date") +
								", " + rset.getInt("rating"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}  // Step 5: Close conn and stmt - Done automatically by try-with-resources
	}
	
}
