package fetchino.condition;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import fetchino.action.*;
import lightdom.Element;

import java.util.ArrayList;
import java.util.List;

public class ConditionParser
{
	public static Condition parse(Element conditionElement)
	{
		switch(conditionElement.getName())
		{
			case "and":
				return parseAnd(conditionElement);
			case "or":
				return parseOr(conditionElement);
			case "xor":
				return parseXor(conditionElement);
			case "not":
				return parseNot(conditionElement);
			case "matches":
				return parseMatches(conditionElement);
			default:
				throw new RuntimeException("Unknown condition: " + conditionElement.getName());
		}
	}

	private static And parseAnd(Element andElement)
	{
		List<Condition> conditions = new ArrayList<>();
		andElement.getElements().forEach(conditionElement -> conditions.add(parse(conditionElement)));
		return new And(conditions);
	}

	private static Or parseOr(Element orElement)
	{
		List<Condition> conditions = new ArrayList<>();
		orElement.getElements().forEach(conditionElement -> conditions.add(parse(conditionElement)));
		return new Or(conditions);
	}

	private static Xor parseXor(Element xorElement)
	{
		List<Condition> conditions = new ArrayList<>();
		xorElement.getElements().forEach(conditionElement -> conditions.add(parse(conditionElement)));
		return new Xor(conditions);
	}

	private static Not parseNot(Element notElement)
	{
		if(notElement.getElements().size() == 0)
			throw new RuntimeException("not cannot be empty");
		else if(notElement.getElements().size() > 1)
			throw new RuntimeException("only one condition is allowed");
		else
			return new Not(parse(notElement.getElements().get(0)));
	}

	private static Matches parseMatches(Element matchesElement)
	{
		String path = null;
		String var = null;
		String regex;

		if(!matchesElement.hasAttributeWithName("path") && !matchesElement.hasAttributeWithName("var"))
			throw new RuntimeException("matches must have either a path or a var attribute");
		else if(matchesElement.hasAttributeWithName("path") && matchesElement.hasAttributeWithName("var"))
			throw new RuntimeException("matches cannot have  a path and a var attribute");

		if(matchesElement.hasAttributeWithName("path"))
			path = matchesElement.getAttribute("path");
		else
			var = matchesElement.getAttribute("var");

		if(!matchesElement.hasAttributeWithName("regex"))
			throw new RuntimeException("matches has no regex attribute");
		else
			regex = matchesElement.getAttribute("regex");

		if(path != null)
			return new MatchesPath(path, regex);
		else
			return new MatchesVariable(var, regex);
	}
}
