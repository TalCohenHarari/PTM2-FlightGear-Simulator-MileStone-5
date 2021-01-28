package model.interpreter.Command;


import model.interpreter.Lexer;
import model.interpreter.Utilities;


public class LoopCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception {

		StringBuilder left = new StringBuilder();
		int indexOfOperetor= args[0].indexOf("<");
		for (int i = 6; i < indexOfOperetor-1; i++) 
			left.append(args[0].charAt(i));
		String[] insideTheLoop=new String[args.length-2];
		for (int i=1; !(args[i].equals("}"));i++) {
			insideTheLoop[i-1]=args[i];
		}
		StringBuilder temp=new StringBuilder();
		int indexOfBracket = args[0].indexOf("{");
		for (int i = indexOfOperetor+2; i < indexOfBracket-1; i++) 
			temp.append(args[0].charAt(i));
		int ifNumber=Integer.parseInt(temp.toString());
		Lexer tokens=new Lexer();
	
		//עדיף את צד ימין לשלוח למחלקה לחישוב ביטוי מתמטי  במידה ויהיה באמת חישוב מתמטי
		while (Utilities.getValue(left.toString()).calculate() < ifNumber) 
		{
			for (int i = 0; i < insideTheLoop.length; i++) 
			{
				String[] arrLine = tokens.lexer(insideTheLoop[i]);
				StringBuilder s=new StringBuilder();
				for (String string : arrLine) 
					s.append(string + " ");
				
				if(s.toString().contains("="))
					Utilities.getCommand("equal").doCommand(arrLine);
				else if(s.toString().contains("print"))
					Utilities.getCommand("Print").doCommand(arrLine);
				else if(s.toString().contains("sleep")) 
				{
					Utilities.getCommand("Sleep").doCommand(arrLine);
				}
			}
		}
		return 0;
	}

}
