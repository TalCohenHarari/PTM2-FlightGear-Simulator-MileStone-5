package model.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import model.interpreter.Expression.Number;
import model.interpreter.Command.Command;

public class Utilities 
{
	public static Map<String, String> BindTable = new HashMap<>();
	public static Map<String, Number> VariableTable = new HashMap<>();
	public static Map<String, Command> CommandTable = new HashMap<>();
	public static AtomicBoolean fixSameTime=new AtomicBoolean();

	public static void resetVariablesTable()
	{
		//delete all the variables that not some path to simulator command:
		Set<String> keys = VariableTable.keySet();
		String[] arr=new String[keys.size()];
		int i=0;
		for (String k: keys) 
		{
			arr[i]=k;
			i++;
		}
		for (int j=0;j<arr.length;j++)
		{
			if(!arr[j].contains("/")) 
			{
				VariableTable.remove(arr[j]);
				if(isVariableExist(arr[j]))
					System.out.println("yes and not good");
			}
		}
		//Reset the bind table:
		BindTable=new HashMap<>();
	}
	public static boolean isVariableExist(String key) {
		return VariableTable.containsKey(key);
	}

	public static Number getValue(String key) {
		return VariableTable.getOrDefault(key, null);
	}

	public static void setValue(String key, Number value) {
		VariableTable.put(key, value);

	}
	public static String getBind(String key) {
		return BindTable.getOrDefault(key, null);
	}

	public static void setBind(String key, String value) {
		BindTable.put(key, value);
	}

	public static boolean isCommandExist(String key) {
		return CommandTable.containsKey(key);
	}

	public static Command getCommand(String key) {
		return CommandTable.getOrDefault(key, null);
	}

	public static void setCommand(String key, Command value) {
		CommandTable.put(key, value);

	}

}
