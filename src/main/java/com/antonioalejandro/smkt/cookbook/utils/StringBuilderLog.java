package com.antonioalejandro.smkt.cookbook.utils;

import java.util.List;

public class StringBuilderLog {
	private static final String NAME_TEMPLATE = "-----%s-----";
	private static final String PARAM_TEMPLATE = "---%s: {}---";
	private static final String DIVIDER = "#";

	private String className;
	private String methodName;
	private String params;
	private String text;

	public StringBuilderLog() {
		this.params = "";
		this.text = "";
	}

	public StringBuilderLog addClassName(String name) {
		className = String.format(NAME_TEMPLATE, name);
		return this;
	}

	public StringBuilderLog addMethodName(String name) {
		methodName = String.format(NAME_TEMPLATE, name);
		return this;
	}

	public StringBuilderLog addParams(List<String> params) {
		StringBuilder builder = new StringBuilder();
		params.stream().map(param -> String.format(PARAM_TEMPLATE, param)).forEach(builder::append);
		this.params = builder.toString();
		return this;
	}

	public StringBuilderLog addText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(className).append(DIVIDER).append(methodName).append(DIVIDER)
				.append(params.isBlank() ? text : params).toString();
	}
}
