package com.foodrecipe.searchdemo.solr;

import com.foodrecipe.searchdemo.csv.CsvUtils;
import com.foodrecipe.searchdemo.recipe.RatedRecipe;
import com.foodrecipe.searchdemo.recipe.Recipe;
import com.foodrecipe.searchdemo.recipe.ScoredRecipe;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SolrCollection {
	
	private final SolrClient client;
	
	public SolrCollection(String collectionName) {
		client = new HttpSolrClient.Builder("http://localhost:8983/solr/" + collectionName).build();
		if (!hasDocuments()) {
			addAll();
		}
	}
	
	//	public static void  main(String[] args) {
	//		SolrCollection col = new SolrCollection("foodrecipe");
	//		List<ScoredRecipe> results = col.search("curry chicken", 100);
	//
	//		System.out.println(results);
	//	}
	
	private boolean hasDocuments() {
		final SolrQuery query = new SolrQuery("*:*");
		query.setRows(0);
		
		final QueryResponse response;
		try {
			response = client.query(query);
			return response.getResults().getNumFound() > 0;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void addAll() {
		Iterable<Recipe> recipes = CsvUtils.parseRecipes();
		for (Recipe rec : recipes) {
			try {
				client.addBean(rec);
			} catch (IOException | SolrServerException e) {
				e.printStackTrace();
			}
		}
		try {
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	static void deleteAll() {
		//Preparing the Solr client
		String urlString = "http://localhost:8983/solr/foodrecipe";
		SolrClient Solr = new HttpSolrClient.Builder(urlString).build();
		
		//Preparing the Solr document
		SolrInputDocument doc = new SolrInputDocument();
		
		try {
			//Deleting the documents from Solr
			Solr.deleteByQuery("*");
			//Saving the document
			Solr.commit();
			System.out.println("Documents deleted");
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Map<Integer, RatedRecipe> searchRated(List<Integer> recipeIds) {
		if (recipeIds.size() == 0) return new HashMap<>();
		String query = "id:(" + recipeIds.stream().map(Object::toString).collect(Collectors.joining(" ")) +")";
		final SolrQuery solrQuery = new SolrQuery(query);
		solrQuery.setRows(recipeIds.size());
		final QueryResponse response;
		try {
			Map<Integer, RatedRecipe> result = new HashMap<>();
			response = client.query(solrQuery);
			List<RatedRecipe> res = response.getBeans(RatedRecipe.class);
			for (RatedRecipe r : res) {
				result.put(Integer.valueOf(r.getId()), r);
			}
			return result;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
	
	public List<ScoredRecipe> search(String query, int n) {
		final SolrQuery solrQuery = new SolrQuery(query);
//		 solrQuery.setRows(n);
//		 solrQuery.setIncludeScore(true);
		 solrQuery.setRequestHandler("/search");
		final QueryResponse response;
		try {
			response = client.query(solrQuery);
			return response.getBeans(ScoredRecipe.class);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
