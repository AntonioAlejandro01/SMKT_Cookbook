package com.antonioalejandro.smkt.cookbook.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The class RecipeDTO
 * 
 * @author <a href="http://www.antonioalejandro.com">AntonioAlejandro01</a>
 * @version 1.0.0
 *
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

	
	private String title;
	
	private List<IngredientDTO> ingredients;
	
	private List<String> steps;
	
	private double time;

}
