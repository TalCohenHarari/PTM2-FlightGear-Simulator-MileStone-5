package model.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lexer {

	public String[] lexer(String line) {

		if(line.contains("="))
		{
			int equalIndex = line.indexOf("=");
			StringBuilder leftSide=new StringBuilder();
			StringBuilder rigthSide=new StringBuilder();
			
			for (int i = 0; i < equalIndex; i++)
				leftSide.append(line.charAt(i));
			
			for (int i = equalIndex+1; i <line.length() ; i++) 
			{
				if(line.charAt(i)=='"')
					continue;
				rigthSide.append(line.charAt(i));
			}
			line=leftSide.toString()+" "+"="+" "+rigthSide.toString();
		}
		
		
		List<String> arr = new ArrayList<String>();
		Scanner scan = new Scanner(line);

		while (scan.hasNext())
			arr.add(scan.next());
		
		String[] arrLine=new String[arr.size()];
		
		for (int i = 0; i < arr.size(); i++)
			arrLine[i]=arr.get(i);
		
		scan.close();
		return arrLine;
	}
}
