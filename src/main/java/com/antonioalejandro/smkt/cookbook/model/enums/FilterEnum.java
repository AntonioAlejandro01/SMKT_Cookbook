package com.antonioalejandro.smkt.cookbook.model.enums;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.antonioalejandro.smkt.cookbook.utils.SearchFunction;

import lombok.Getter;

public enum FilterEnum {

	TIME("time"), TITLE("title");

	@Getter
	private String name;

	private static List<Map<String, String>> listToSend;

	private FilterEnum(String name) {
		this.name = name;
	}

	public static Optional<FilterEnum> fromName(String name) {
		return Stream.of(FilterEnum.values()).filter(e -> name.equalsIgnoreCase(e.name)).findFirst();
	}

	public static List<Map<String, String>> toBeSended() {

		if (FilterEnum.listToSend == null) {
			FilterEnum.listToSend = Stream.of(FilterEnum.values()).map(e -> Map.of("value", e.getName().toUpperCase()))
					.collect(Collectors.toList());
		}
		return FilterEnum.listToSend;

	}

	public SearchFunction getFunctionSearch() {
		switch (this) {
		case TIME:
			return (uId, val, d) -> d.byTime(uId, Double.parseDouble(val));
		default: // TITLE
			return (uId, val, d) -> d.byTitle(uId, val);
		}
	}

}
