package model.interpreter.Command;

import model.interpreter.DataReaderServer;

public class OpenServerCommand implements Command {
	DataReaderServer drs;
	@Override
	public int doCommand(String[] args) throws Exception {
		if(args.length<2)
			throw new Exception("The values are incorrect, expect two values and there is less than that");
		else {
			drs=new DataReaderServer(args);
		}

		return 0;
	}

}
