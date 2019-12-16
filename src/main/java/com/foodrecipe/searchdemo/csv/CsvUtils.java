package com.foodrecipe.searchdemo.csv;

import com.foodrecipe.searchdemo.interaction.Interaction;
import com.foodrecipe.searchdemo.recipe.Recipe;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvUtils {
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Iterable<Recipe> parseRecipes() {
		String RECIPE_CSV_PATH = "src/main/resources/food-com-recipes-and-user-interactions/RAW_recipes.csv";
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(RECIPE_CSV_PATH)).withSkipLines(1).build();
			Iterator<String[]> iter = reader.iterator();
			return () -> new Iterator<Recipe>() {
				@Override
				public boolean hasNext() {
					return iter.hasNext();
				}
				
				@Override
				public Recipe next() {
					String[] nextLine = iter.next();
					String name = nextLine[0];
					String id = nextLine[1];
					int minutes = Integer.valueOf(nextLine[2]);
					String contributor_id = nextLine[3];
					Date submitted = new Date(0);
					try {
						submitted = DATE_FORMAT.parse(nextLine[4]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					List<String> tags = parseStrings(nextLine[5]);
					List<Float> nutrition = parseFloats(nextLine[6]);
					int n_steps = Integer.valueOf(nextLine[7]);
					List<String> steps = parseStrings(nextLine[8]);
					String description = nextLine[9];
					List<String> ingredients = parseStrings(nextLine[10]);
					int n_ingredients = Integer.valueOf(nextLine[11]);
					
					return new Recipe(name, id, minutes, contributor_id, submitted, tags, nutrition, n_steps, steps,
							description, ingredients, n_ingredients);
				}
				
				@Override
				public void remove() {
					// TODO code to remove item or throw exception
				}
				
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return () -> new Iterator<Recipe>() {
			@Override
			public boolean hasNext() {
				return false;
			}
			
			@Override
			public Recipe next() {
				return null;
			}
		};
		
	}
	
	private static List<String> parseStrings(String line) {
		line = line.replaceAll("^\\[|]$", "");
		return Stream.of(line.split("\\s*,\\s*")).map(l -> l.replaceAll("^'|'$", "")).collect(Collectors.toList());
	}
	
	private static List<Float> parseFloats(String line) {
		line = line.replaceAll("^\\[|]$", "");
		return Stream.of(line.split(",")).map(Float::valueOf).collect(Collectors.toList());
	}
	
	public static Iterable<Interaction> parseInteractions() {
		String RECIPE_CSV_PATH = "src/main/resources/food-com-recipes-and-user-interactions/RAW_interactions.csv";
		try {
			CsvToBean<Interaction> csvToBean =
					new CsvToBeanBuilder<Interaction>(new FileReader(RECIPE_CSV_PATH)).withType(Interaction.class).build();
			Iterator<Interaction> iter = csvToBean.iterator();
			return () -> new Iterator<Interaction>() {
				@Override
				public boolean hasNext() {
					return iter.hasNext();
				}
				
				@Override
				public Interaction next() {
					return iter.next();
				}
			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return () -> new Iterator<Interaction>() {
			@Override
			public boolean hasNext() {
				return false;
			}
			
			@Override
			public Interaction next() {
				return null;
			}
		};
		
	}
}
