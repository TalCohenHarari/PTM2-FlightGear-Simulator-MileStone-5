package model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.interpreter.Interpreter;
import model.interpreter.Utilities;
import model.interpreter.Command.ConnectCommand;
import model.interpreter.Command.DisconnectServerCommand;
import model.interpreter.Command.OpenServerCommand;
import viewmodel.ViewModel;

public class SimulatorModel extends Observable implements Observer 
{
	//View Model:
    public ViewModel vm;
    //AutoPilot:
	public Thread threadAutoPilot;
	
	//AutoPilot and joystick server:
	private OpenServerCommand server;
	private DisconnectServerCommand disconnect;
	private ConnectCommand connect;
	public IntegerProperty isConnect;

	//CalcPath server:
    private static Socket socketPath;
    private  static PrintWriter outPath;
    private  static BufferedReader inPath;
    public static volatile boolean stop=false;

	//Plane:
    double startX;
    double startY;
    double planeX;
    double planeY;
    double currentPositionX;
    double currentPositionY;
    double currentHeading;

	//Map:
    double markX;
    double markY;
    int[][] data;
    double offset;


	//------------------------------------------------Constructor------------------------------------------------------
    public  SimulatorModel()
    {
        server=new OpenServerCommand();
		disconnect=new DisconnectServerCommand();
		connect =new ConnectCommand();
		isConnect= new SimpleIntegerProperty();
		isConnect.bind(connect.isConnect);
    }
    //-------------------------------------------------Functions-------------------------------------------------------
    //set the model:
   	public void setVm(ViewModel vm) 
   	{
   		this.vm=vm;
   	}

   	@Override
   	public void update(Observable arg0, Object arg1) {}
   	
    //------------------------------------------------airPlane------------------------------------------------------------
    public void airPlanePosition(double startX,double startY, double offset)
    {
        this.offset=offset;
        this.startX=startX;
        this.startY=startY;
		new Thread(() ->
		{
			Socket socket = null;
			try
			{
				socket = new Socket("127.0.0.1", 5402);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (!stop)
				{
					out.println("dump /position");
					out.flush();
					String line;
					List<String> lines = new ArrayList<>();
					while (!(line = br.readLine()).equals("</PropertyList>"))
						if (!line.equals(""))
							lines.add(line);

					String longtitude = lines.get(2);
					String latitude = lines.get(3);
					String[] x = longtitude.split("[<>]");
					String[] y = latitude.split("[<>]");
					br.readLine();
					out.println("get /instrumentation/heading-indicator/indicated-heading-deg");
					out.flush();
					String[] h = br.readLine().split(" ");
					int temp = h[3].length();
					String[] data = { "plane", x[2], y[2], h[3].substring(1, temp - 1) };
					this.setChanged();
					this.notifyObservers(data);
					try
					{Thread.sleep(250);}
					catch (InterruptedException e) {e.printStackTrace();}
				}
				socket.close();
			}
			catch (IOException e)
			{
				try{socket.close();}
				catch (IOException ex) {}
			}
		}).start();
	}
	//-------------------------------------------------calcPath-------------------------------------------------------
    //Connect to calcPath server:
	public void connectCalaPath(String ip, int port)
	{
		try
		{
			socketPath = new Socket(ip, port);
			outPath = new PrintWriter(socketPath.getOutputStream());
			inPath = new BufferedReader(new InputStreamReader(socketPath.getInputStream()));
		} catch (IOException e) {e.printStackTrace();}
	}
	//send (and get) the start and end points to the calaPath server:
	public void findPath(int planeX, int planeY, int markX, int markY, int[][] data)
	{
		this.planeX = planeX;
		this.planeY = planeY;
		this.markX = markX;
		this.markY = markY;
		this.data = data;

		new Thread(() ->
		{

			int j, i;

			for (i = 0; i < data.length; i++)
			{
				System.out.print("");
				for (j = 0; j < data[i].length - 1; j++)
					outPath.print(data[i][j] + ",");

				outPath.println(data[i][j]);
			}

			outPath.println("end");
			outPath.println(planeX + "," + planeY);
			outPath.println(markX + "," + markY);
			outPath.flush();
			String solution = null;

			try {solution = inPath.readLine();} catch (IOException e) {e.printStackTrace();}

			System.out.println("The path is:");
			System.out.println(solution);
			String[] temp = solution.split(",");

			String[] notify = new String[temp.length + 1];
			notify[0] = "path";
			for (i = 0; i < temp.length; i++)
				notify[i + 1] = temp[i];

			this.setChanged();
			this.notifyObservers(notify);
		}).start();
	}

    //-------------------------------------------------Joystick and autoPilot Connect---------------------------------------------------------------
	 public void connect(String ip, String port)
	 {
			 try {  connect.doCommand(new String[]{"connect",ip,port}); }
			 catch (Exception e) {e.printStackTrace();}
	 }
	 //-------------------------------------open our listener server to get the values from simulator------------------------
	public void openServer()
	{
		try { server.doCommand(new String[] {"opedDataServer","5400","10"}); }
		catch (Exception e) {e.printStackTrace();}
	}
	 //-------------------------------------close our listener server-------------------------------------------------------
	public void closeServer()
	{
		try { disconnect.doCommand(new String[]{""}); }
		catch (Exception e) {e.printStackTrace();}
	}
	//-------------------------------------------------joystick------------------------------------------------------------
	public void setThrottle(String throttle)
	{
		String[] t = { "set /controls/engines/current-engine/throttle " + throttle };
		ConnectCommand.Send(t);
	}

	public void setRudder(String rudder)
	{
		String[] r = { "set /controls/flight/rudder " + rudder };
		ConnectCommand.Send(r);
	}

	public void setJoystick(String aileron, String elevator)
	{
		String[] data = { "set /controls/flight/aileron " + aileron, "set /controls/flight/elevator " + elevator, };
		ConnectCommand.Send(data);
	}
	//--------------------------------------------------Autopilot------------------------------------------------------------
	//sent the commands to the interpreter:
	public void autoPilot(String[] lines)
	{
		threadAutoPilot= new Thread(() -> Interpreter.interpret(lines));
		threadAutoPilot.start();
	}
	//end autoPilot (when select manual):
    public void offAutoPilot()
    {
    	if(threadAutoPilot!=null && threadAutoPilot.isAlive())
    		threadAutoPilot.stop();
		Utilities.resetVariablesTable();
	}
}
