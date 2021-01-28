package model.interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public int parser(String[] lines, Lexer tokens) throws IOException {
		try {
			
			for (int i = 0; i < lines.length; i++) {

				String[] arrLine = tokens.lexer(lines[i]);
				StringBuilder sb = new StringBuilder(); // This "sb" helps me to do in the "if else" .toString() method.
				for (String string : arrLine)
					sb.append(string + " ");
					
				// WhileCommand
				if (arrLine[0].equals("while")) {
					List<String> list=new ArrayList<>();
					while(!(lines[i].contains("}"))){
						list.add(lines[i]);
						i++;
					}
					list.add(lines[i]);
					String[] arrLoop = new String[list.size()];
					for (int j = 0; j < list.size(); j++)
						arrLoop[j]=list.get(j);
					Utilities.getCommand("Loop").doCommand(arrLoop);
					continue;
				}

				// varDeclaration command
				if (arrLine[0].equals("var") && arrLine.length == 2) {
					Utilities.CommandTable.get("var").doCommand(arrLine);
					continue;
				}

				// Equal command
				if (sb.toString().contains("=") || arrLine[0].contains("=")) {
					Utilities.CommandTable.get("equal").doCommand(arrLine);
					continue;
				}

				// print Command:
				if (sb.toString().contains("print")) {
					Utilities.CommandTable.get("Print").doCommand(arrLine);
					continue;
				}
				
				// openDataServer Command:
//				if (arrLine[0].equals("openDataServer")) {
//					Utilities.CommandTable.get("openDataServer").doCommand(arrLine);
//					continue;
//				}
				// connect Command:
//				if (arrLine[0].equals("connect")) {
//					Utilities.CommandTable.get("connect").doCommand(arrLine);
//					continue;
//				}
				
				// disconnect Command:
//				if (sb.toString().contains("done")) {
//					Utilities.CommandTable.get("disconnect").doCommand(new String[] {});
//					continue;
//				}
				
				// return command:
//				if (arrLine[0].equals("return")) {
//					Utilities.CommandTable.get("return").doCommand(arrLine);
//					continue;
//				}
			}
		} catch (Exception e) {e.printStackTrace();}
		return 0;
	}

}
