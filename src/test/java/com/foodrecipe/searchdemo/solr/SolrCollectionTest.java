package com.foodrecipe.searchdemo.solr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolrCollectionTest {
	
	@Test
	void search() {
		SolrCollection sol = new SolrCollection("foodrecipe");
		System.out.println(sol.search("heat canola oil and cook garlic and ginger over medium heat until fragrant", 2000).subList(0,10));
		//System.out.println(sol.search("curry chicken", 200).size());
	}
}