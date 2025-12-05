package com.example.emailclient.service.interpreter;

public class SearchParser {

	public static Expression parse(String input) {
		input = input.trim().toLowerCase();

		if (input.contains(" and ")) {
			String[] parts = input.split(" and ");
			return new AndExpression(parse(parts[0]), parse(parts[1]));
		}

		if (input.contains(" or ")) {
			String[] parts = input.split(" or ");
			return new OrExpression(parse(parts[0]), parse(parts[1]));
		}

		if (input.startsWith("subject:")) {
			String value = input.substring("subject:".length()).replace("\"", "").trim();
			return new SubjectContainsExpression(value);
		}

		if (input.startsWith("body:")) {
			String value = input.substring("body:".length()).replace("\"", "").trim();
			return new BodyContainsExpression(value);
		}
		return msg -> false;
	}
}

