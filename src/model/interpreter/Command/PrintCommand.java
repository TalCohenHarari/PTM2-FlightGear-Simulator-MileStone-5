package model.interpreter.Command;

//import model.interpreter.Parser;
import model.interpreter.Utilities;

public class PrintCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		for (int i = 1; i < args.length; i++)
			sb.append(args[i]);

		
		if(Utilities.getValue(sb.toString())==null)
		{
			if(args[0].toString().contains("done")) {
				System.out.println(args[0].replace("print ", "").replace("\"", ""));
//				Utilities.CommandTable.get("disconnect").doCommand(new String[] {});
			}
			else
				System.out.println(sb.toString()); //sb.toString().replace("print ", "")
		}
		else
			System.out.println(sb.toString()+ " = " + Utilities.getValue(sb.toString()).calculate());
//		if(sb.toString().contains("done"))
//		{
//			Utilities.CommandTable.get("disconnect").doCommand(args);
//		}
		return 0;
	}

}
