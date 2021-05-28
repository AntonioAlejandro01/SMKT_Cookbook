package com.antonioalejandro.smkt.cookbook.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Ingredient DTO Class
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 *
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Ingredient", value = "Ingredient Request")
public class IngredientDTO {

	@ApiModelProperty(dataType = "string", example = "Meat", position = 0, value = "The ingredient name")
	private String name;

	@ApiModelProperty(dataType = "string", example = "125 gr.", position = 1, value = "Amount for the ingredient")
	private String amount;

}
