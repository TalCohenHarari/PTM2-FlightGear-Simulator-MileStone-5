package model.interpreter.Command;

import model.interpreter.Utilities;
import model.interpreter.Expression.CalcExpression;

public class EqualCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception {


		StringBuilder sb = new StringBuilder();
		for (String string : args)
			sb.append(string + " ");
		
		
		// [var ,breaks, =, 0 ] / [var ,breaks, =, bind, "/..../..../..." ] : 
		if (sb.toString().contains("var"))
		{
			// Declared the variable:
			Utilities.CommandTable.get("var").doCommand(new String[] {args[0],args[1]});
			
			//Sending the new variable to bindCommand for pointing on the same object:
			if(sb.toString().contains("bind"))
			{
				Utilities.getCommand("bind").doCommand(new String[] {args[1],args[4]});
				return 0;
			}
			// Set the variable:
			StringBuilder temp = new StringBuilder();
			for (int i = 3; i < args.length; i++)
				temp.append(args[i]);
			
			Utilities.fixSameTime.set(true);
			Utilities.getValue(args[1]).setValue(CalcExpression.calc(temp.toString()));
			Utilities.fixSameTime.set(false);

			// If the left side of equal is variable from bing table
			if (Utilities.getBind(args[1]) != null)
				ConnectCommand.SendAutoPilot(Utilities.getBind(args[1]) + " " + Utilities.getValue(args[1]).calculate());
			// If the left side of equal is a regular variable that isn't from BindTable
			else
				return 0;
		}
		// [breaks, =, 0 ]:
		else {
			
			// [ h0, =, rudder] (rudder is binding variable) :
			if(sb.toString().contains("bind"))
			{
				Utilities.getCommand("bind").doCommand(new String[] {args[0],args[2]});
				return 0;
			}
			
			StringBuilder temp = new StringBuilder();
			for (int i = 2; i < args.length; i++)
				temp.append(args[i]);

			Utilities.fixSameTime.set(true);
			Utilities.getValue(args[0]).setValue(CalcExpression.calc(temp.toString()));
			Utilities.fixSameTime.set(false);

			// If the left side of equal is variable from bing table
			if (Utilities.getBind(args[0]) != null)
				ConnectCommand.SendAutoPilot(Utilities.getBind(args[0]) + " " + Utilities.getValue(args[0]).calculate());
			// If the left side of equal is a regular variable that isn't from BindTable
			else
				return 0;
		}
		return 0;
	}

}
