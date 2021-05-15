package com.antonioalejandro.smkt.cookbook.utils;

import java.util.List;

public class Utils {

	private Utils() {

	}

	public static String formatLog(Class<?> clazz, String method, String... params) {
		return new StringBuilderLog().addClassName(clazz.getSimpleName()).addMethodName(method).addParams(List.of(params))
				.toString();
	}

	public static String formatLogText(Class<?> clazz, String method, String text) {
		return new StringBuilderLog().addClassName(clazz.getSimpleName()).addMethodName(method).addText(text).toString();
	}

}
