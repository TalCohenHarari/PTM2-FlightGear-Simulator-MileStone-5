package model.interpreter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import model.interpreter.Expression.Number;

public class DataReaderServer {

	private int port;
	static volatile boolean stop = false;
	public static Thread threadIn;
	public static volatile boolean excute = false;

	public DataReaderServer(String[] args) throws Exception
	{
		setCommands();
		this.port = Integer.parseInt(args[1]);
		threadIn = new Thread(() -> runServer());
		threadIn.start();
	}

	private void runServer()
	{
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while (!stop)
			{
				try {
				Socket client = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				System.out.println("The client successfully connected to our server");
				String line = null;
				String[] arrSim;
				Double[] arrDub = new Double[25];
				while (!stop)
				{
					try
					{
						line = in.readLine();
						arrSim = line.split("\t*,\t*");
						for (int i = 0; i < arrSim.length; i++)
							arrDub[i] = Double.parseDouble(arrSim[i].replace(" ", ""));

						if (!Utilities.fixSameTime.get()) {
							Utilities.getValue("/instrumentation/airspeed-indicator/indicated-speed-kt").setValue(arrDub[0]);
							Utilities.getValue("/instrumentation/altimeter/indicated-altitude-ft").setValue(arrDub[1]);
							Utilities.getValue("/instrumentation/altimeter/pressure-alt-ft").setValue(arrDub[2]);
							Utilities.getValue("/instrumentation/attitude-indicator/indicated-pitch-deg").setValue(arrDub[3]);
							Utilities.getValue("/instrumentation/attitude-indicator/indicated-roll-deg").setValue(arrDub[4]);
							Utilities.getValue("/instrumentation/attitude-indicator/internal-pitch-deg").setValue(arrDub[5]);
							Utilities.getValue("/instrumentation/attitude-indicator/internal-roll-deg").setValue(arrDub[6]);
							Utilities.getValue("/instrumentation/encoder/indicated-altitude-ft").setValue(arrDub[7]);
							Utilities.getValue("/instrumentation/encoder/pressure-alt-ft").setValue(arrDub[8]);
							Utilities.getValue("/instrumentation/gps/indicated-altitude-ft").setValue(arrDub[9]);
							Utilities.getValue("/instrumentation/gps/indicated-ground-speed-kt").setValue(arrDub[10]);
							Utilities.getValue("/instrumentation/gps/indicated-vertical-speed").setValue(arrDub[11]);
							Utilities.getValue("/instrumentation/heading-indicator/indicated-heading-deg").setValue(arrDub[12]);
							Utilities.getValue("/instrumentation/magnetic-compass/indicated-heading-deg").setValue(arrDub[13]);
							Utilities.getValue("/instrumentation/slip-skid-ball/indicated-slip-skid").setValue(arrDub[14]);
							Utilities.getValue("/instrumentation/turn-indicator/indicated-turn-rate").setValue(arrDub[15]);
							Utilities.getValue("/instrumentation/vertical-speed-indicator/indicated-speed-fpm").setValue(arrDub[16]);
							Utilities.getValue("/controls/flight/aileron").setValue(arrDub[17]);
							Utilities.getValue("/controls/flight/elevator").setValue(arrDub[18]);
							Utilities.getValue("/controls/flight/rudder").setValue(arrDub[19]);
							Utilities.getValue("/controls/flight/flaps").setValue(arrDub[20]);
							Utilities.getValue("/controls/engines/current-engine/throttle").setValue(arrDub[21]);
							Utilities.getValue("/engines/engine/rpm").setValue(arrDub[22]);
							Utilities.getValue("xOnMap").setValue(arrDub[23]);
							Utilities.getValue("yOnMap").setValue(arrDub[24]);
						}
					} catch (Exception e) {}
				}
				in.close();
				client.close();
				}catch(SocketTimeoutException e){}
			}
			server.close();
		} catch (Exception e) {}
	}

	public static void close() {
		stop = true;
	}

	public void setCommands()
	{
		Utilities.setValue("/controls/flight/speedbrake", new Number(0));
		Utilities.setValue("/instrumentation/airspeed-indicator/indicated-speed-kt", new Number(0));
		Utilities.setValue("/instrumentation/altimeter/indicated-altitude-ft", new Number(0));
		Utilities.setValue("/instrumentation/altimeter/pressure-alt-ft", new Number(0));
		Utilities.setValue("/instrumentation/attitude-indicator/indicated-pitch-deg", new Number(0));
		Utilities.setValue("/instrumentation/attitude-indicator/indicated-roll-deg", new Number(0));
		Utilities.setValue("/instrumentation/attitude-indicator/internal-pitch-deg", new Number(0));
		Utilities.setValue("/instrumentation/attitude-indicator/internal-roll-deg", new Number(0));
		Utilities.setValue("/instrumentation/encoder/indicated-altitude-ft", new Number(0));
		Utilities.setValue("/instrumentation/encoder/pressure-alt-ft", new Number(0));
		Utilities.setValue("/instrumentation/gps/indicated-altitude-ft", new Number(0));
		Utilities.setValue("/instrumentation/gps/indicated-ground-speed-kt", new Number(0));
		Utilities.setValue("/instrumentation/gps/indicated-vertical-speed", new Number(0));
		Utilities.setValue("/instrumentation/heading-indicator/indicated-heading-deg", new Number(0));
		Utilities.setValue("/instrumentation/magnetic-compass/indicated-heading-deg", new Number(0));
		Utilities.setValue("/instrumentation/slip-skid-ball/indicated-slip-skid", new Number(0));
		Utilities.setValue("/instrumentation/turn-indicator/indicated-turn-rate", new Number(0));
		Utilities.setValue("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", new Number(0));
		Utilities.setValue("/controls/flight/aileron", new Number(0));
		Utilities.setValue("/controls/flight/elevator", new Number(0));
		Utilities.setValue("/controls/flight/rudder", new Number(0));
		Utilities.setValue("/controls/flight/flaps", new Number(0));
		Utilities.setValue("/controls/engines/current-engine/throttle", new Number(0));
		Utilities.setValue("/engines/engine/rpm", new Number(0));
		Utilities.setValue("xOnMap", new Number(0));
		Utilities.setValue("yOnMap", new Number(0));
	}
}
