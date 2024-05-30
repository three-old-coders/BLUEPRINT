package com.github.three_old_coders.blueprint.spring.app;

public class SimpleJavaClassB_using_SimpleJavaClassA
{
	private final String prefix;

	public SimpleJavaClassB_using_SimpleJavaClassA(String prefix) {
		this.prefix = prefix;
	}

	public void letsGo(String input) {
		System.out.println(prefix + ": " + input);
	}
}
