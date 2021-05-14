package com.antonioalejandro.smkt.cookbook.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Recipe {

	private String id;

	private String title;

	private List<Ingredient> ingredients;

	private List<String> steps;
	
	private double time;
	
	@JsonIgnore
	private String userId;
}
