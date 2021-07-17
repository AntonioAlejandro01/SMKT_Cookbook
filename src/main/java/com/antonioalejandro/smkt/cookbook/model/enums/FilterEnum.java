package com.antonioalejandro.smkt.cookbook.model.enums;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.antonioalejandro.smkt.cookbook.utils.SearchFunction;

import lombok.Getter;

/**
 * Filter Enum
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @see SearchFunction
 * 
 */
public enum FilterEnum {

	TIME("time"), TITLE("title");

	@Getter
	private String name;

	/**
	 * List with prepared JSON that contain all filters. Only created the first time
	 * that it's called
	 */
	private static List<Map<String, String>> listToSend;

	/**
	 * Create Filter Enum instance
	 * 
	 * @param name {@link String}
	 */
	private FilterEnum(String name) {
		this.name = name;
	}

	/**
	 * Return a filled {@link Optional} if the name matches with any of the enum.
	 * 
	 * @param name {@link String}
	 * @return {@link Optional}&lt;{@link FilterEnum}&gt;
	 */
	public static Optional<FilterEnum> fromName(String name) {
		return Stream.of(FilterEnum.values()).filter(e -> name.equalsIgnoreCase(e.name)).findFirst();
	}

	/**
	 * Create a list with a prepared JSON to be sent. Only create the list if is the
	 * first time that the method it's called
	 * 
	 * @return {@link List}&lt;{@link Map}&lt;{@link String}, {@link String}&gt;&gt;
	 */
	public static List<Map<String, String>> toBeSended() {

		if (FilterEnum.listToSend == null) {
			FilterEnum.listToSend = Stream.of(FilterEnum.values()).map(e -> Map.of("value", e.getName().toUpperCase()))
					.collect(Collectors.toList());
		}
		return FilterEnum.listToSend;

	}

	/**
	 * Return the function associated with each filter
	 * 
	 * @return {@link SearchFunction}
	 */
	public SearchFunction getFunctionSearch() {
		return this == TIME ? (uId, val, repo) -> Optional.ofNullable(repo.byTime(uId, Double.parseDouble(val)))
				: (uId, val, repo) -> Optional.ofNullable(repo.byTitle(uId, val));
	}

}
