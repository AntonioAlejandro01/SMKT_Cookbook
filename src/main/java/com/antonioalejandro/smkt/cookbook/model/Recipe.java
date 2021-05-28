package com.antonioalejandro.smkt.cookbook.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Recipe Class
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Recipe for response , include de ID", value = "Recipe Response")
public class Recipe {

	@ApiModelProperty(dataType = "string", example = "123e4567-e89b-42d3-a456-556642440000", position = 0, value = "The ID for Product, it's a UUID")
	private String id;

	@ApiModelProperty(dataType = "string", example = "Fries Eggs", position = 1, value = "The title for the recipe")
	private String title;

	@ApiModelProperty(position = 2, value = "The List of ingredients")
	private List<Ingredient> ingredients;

	@ApiModelProperty(position = 3, value = "The List of steps", example = "[\"Step 1\", \"Step 2\"]")
	private List<String> steps;

	@ApiModelProperty(dataType = "double", example = "1.23", position = 4, value = "The time for recipe in hours")
	private double time;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private String userId;
}
