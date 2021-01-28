package model.interpreter.Expression;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import model.interpreter.Utilities;

public class CalcExpression {
	public static double calc(String exp) {
		Queue<String> queue = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
		Stack<Expression> stackExp = new Stack<Expression>();

		String[] split = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
		
		//Changes the names of the variables (x,y,z,...) to their true values
		for (int i=0; i<split.length;i++)
			if(Utilities.isVariableExist(split[i]))
				split[i]=Utilities.getValue(split[i]).getCalculateString();
		
		
		for (String s : split) {
			if (isDouble(s)) {
				queue.add(s);
			} else {
				switch (s) {
				case "/":
				case "*":
				case "(":
					stack.push(s);
					break;
				case "+":
				case "-":
					while (!stack.empty() && (!stack.peek().equals("("))) {
						queue.add(stack.pop());
					}
					stack.push(s);
					break;
				case ")":
					while (!stack.peek().equals("(")) {
						queue.add(stack.pop());
					}
					stack.pop();
					break;
				}
			}
		}
		while (!stack.isEmpty()) {
			queue.add(stack.pop());
		}

		for (String str : queue) {
			if (isDouble(str)) {
				stackExp.push(new Number(Double.parseDouble(str)));
			} else {
				Expression right = stackExp.pop();
				//cover left is null (-x)
				Expression left;
				if(stackExp.isEmpty() && str.equals("-"))
					left = new Number(0);
				else
					left=stackExp.pop();

				switch (str) {
				case "/":
					stackExp.push(new Div(left, right));
					break;
				case "*":
					stackExp.push(new Mul(left, right));
					break;
				case "+":
					stackExp.push(new Plus(left, right));
					break;
				case "-":
					stackExp.push(new Minus(left, right));
					break;
				}
			}
		}

		return Math.floor((stackExp.pop().calculate() * 1000)) / 1000;
	}

	private static boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}

