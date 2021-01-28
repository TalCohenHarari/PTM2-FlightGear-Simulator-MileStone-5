package model.interpreter.Command;

import model.interpreter.Utilities;
import model.interpreter.Expression.Number;

public class VarDeclarationCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception {
		
		if(Utilities.isVariableExist(args[1]))
			throw new Exception("This variable is invalid ");
		else
			Utilities.VariableTable.put(args[1], new Number(0));
		return 0;
	}

}
