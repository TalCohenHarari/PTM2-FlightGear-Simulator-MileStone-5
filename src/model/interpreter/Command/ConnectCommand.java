package model.interpreter.Command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ConnectCommand implements Command {
	private static Socket client;
	private static PrintWriter out;
	static boolean stop = false;
	public static Thread threadOut;
	public IntegerProperty isConnect;
	
	public ConnectCommand() {
		isConnect = new SimpleIntegerProperty();
	}
	@Override
	public int doCommand(String[] args) throws Exception 
	{
		threadOut = new Thread(() -> client(args));
		threadOut.start();
		return 0;
	}

	private void client(String[] args) 
	{
		while(!stop) 
		{
				try 
				{
					client = new Socket(args[1], Integer.parseInt(args[2]));
					isConnect.setValue(1);
					out = new PrintWriter(client.getOutputStream());
					stop=true;
					System.out.println("We have successfully connected to the server");
				} catch (IOException e) {/*System.out.println(e);*/}
			}
	}

	public static void Send(String[] cmd) {
		for (String s : cmd) 
		{
			out.println(s);
			out.flush();
			System.out.println(s);
		}

	}
	public static void SendAutoPilot(String c)
	{
		String command = "set " + c.replace("\"", "");
		out.println(command);
		out.flush();
		System.out.println(command);			
	}

	public static void close() 
	{
		stop = true;
		if (out != null) 
		{
			out.close();
			try {client.close();} catch (IOException e) {e.printStackTrace();}
		}
	}
}
