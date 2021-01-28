package model.interpreter.Command;

public class SleepCommand implements Command {

	@Override
	public int doCommand(String[] args) throws Exception {
		Thread.sleep(Integer.parseInt(args[1].replace("  ", "")));
		return 0;
	}

}
