package model.interpreter.Command;


import model.interpreter.Utilities;

public class BindCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception 
	{	
		Utilities.setValue(args[0],Utilities.getValue(args[1].replace("\"", "")));
		Utilities.BindTable.put(args[0], args[1]);
		return 0;
	}
}
