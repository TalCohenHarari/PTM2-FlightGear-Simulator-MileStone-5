package model.interpreter;

import java.io.IOException;
import model.interpreter.Command.BindCommand;
import model.interpreter.Command.ConnectCommand;
import model.interpreter.Command.DisconnectServerCommand;
import model.interpreter.Command.EqualCommand;
import model.interpreter.Command.LoopCommand;
import model.interpreter.Command.OpenServerCommand;
import model.interpreter.Command.PrintCommand;
import model.interpreter.Command.ReturnCommand;
import model.interpreter.Command.SleepCommand;
import model.interpreter.Command.VarDeclarationCommand;

public class Interpreter {

	public static int interpret(String[] lines) {
		Lexer lexer = new Lexer();
		Parser parser = new Parser();

		Utilities.setCommand("return", new ReturnCommand());
		Utilities.setCommand("var", new VarDeclarationCommand());
		Utilities.setCommand("equal", new EqualCommand());
		Utilities.setCommand("openDataServer", new OpenServerCommand());
		Utilities.setCommand("connect", new ConnectCommand());
		Utilities.setCommand("disconnect", new DisconnectServerCommand());
		Utilities.setCommand("Loop", new LoopCommand());
		Utilities.setCommand("bind", new BindCommand());
		Utilities.setCommand("Print", new PrintCommand());
		Utilities.setCommand("Sleep", new SleepCommand());

		try 
		{
			parser.parser(lines, lexer);
			//Reset the ValuesTable
			Utilities.resetVariablesTable();
		} catch (IOException e) {e.printStackTrace();}
		return 0;
	}
}
