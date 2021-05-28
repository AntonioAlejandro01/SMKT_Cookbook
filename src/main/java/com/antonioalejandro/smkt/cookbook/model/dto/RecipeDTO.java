package com.antonioalejandro.smkt.cookbook.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Recipe for Request", value = "Recipe Request")
public class RecipeDTO {

	@ApiModelProperty(dataType = "string", example = "Fries Eggs", position = 0, value = "The title for the recipe")
	private String title;

	@ApiModelProperty(position = 1, value = "The List of ingredients")
	private List<IngredientDTO> ingredients;

	@ApiModelProperty(position = 2, value = "The List of steps", example = "[\"Step 1\", \"Step 2\"]")
	private List<String> steps;

	@ApiModelProperty(dataType = "double", example = "1.23", position = 3, value = "The time for recipe in hours")
	private double time;

}
