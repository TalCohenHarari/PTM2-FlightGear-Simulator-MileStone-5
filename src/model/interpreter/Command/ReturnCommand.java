package model.interpreter.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.interpreter.Expression.CalcExpression;

public class ReturnCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception {

		List<String> arr = new ArrayList<String>();

		for (String string : args)
			arr.add(string);
		arr.remove(0);

		String list = Arrays.toString(arr.toArray()).replace("[", "").replace("]", "").replace(",", "").replace(" ",
				"");

		return (int) CalcExpression.calc(list);

	}
}
