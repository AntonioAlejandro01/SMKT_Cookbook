package com.antonioalejandro.smkt.cookbook.model.dto;

import java.util.List;

import lombok.Data;

/**
 * The class RecipeDTO
 * 
 * @author <a href="http://www.antonioalejandro.com">AntonioAlejandro01</a>
 * @version 1.0.0
 *
 */
@Data
public class RecipeDTO {

	
	private String title;
	
	private List<IngredientDTO> ingredients;
	
	private List<String> steps;
	
	private double time;

}
