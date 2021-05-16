package com.antonioalejandro.smkt.cookbook.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

	private String id;

	private String title;

	private List<Ingredient> ingredients;

	private List<String> steps;

	private double time;

	@JsonIgnore
	private String userId;
}
