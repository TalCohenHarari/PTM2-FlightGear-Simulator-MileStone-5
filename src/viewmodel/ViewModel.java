package viewmodel;

import java.util.Observable;
import java.util.Observer;
import model.SimulatorModel;
import javafx.beans.property.*;

public class ViewModel extends Observable implements Observer
{
	//Model:
	public SimulatorModel sm;
	//Joystick
	public DoubleProperty throttle;
	public DoubleProperty rudder;
	public DoubleProperty aileronV;
	public DoubleProperty elevatorV;
	public StringProperty ipJoystick, portJoystick;
	public IntegerProperty isConnect;
	//Airplane:
	public DoubleProperty heading;
	public DoubleProperty airplaneX;
	public DoubleProperty airplaneY;
	//Map:
	public StringProperty ipPath;
	public StringProperty portPath;
	public DoubleProperty startX;
	public DoubleProperty startY;
	public DoubleProperty offset;
	public DoubleProperty markSceneX, markSceneY;
	public BooleanProperty path;
	private int data[][];

	//------------------------------------------------Constructor------------------------------------------------------
	public ViewModel(SimulatorModel sm)
	{
		//Model:
		this.sm = sm;
		
    	//Joystick:
        throttle=new SimpleDoubleProperty();
        rudder=new SimpleDoubleProperty();
        aileronV = new SimpleDoubleProperty();
    	elevatorV = new SimpleDoubleProperty();
    	isConnect=new SimpleIntegerProperty();
    	ipJoystick=new SimpleStringProperty();
    	portJoystick=new SimpleStringProperty();
    	
        //AirPlane:
        airplaneX=new SimpleDoubleProperty();
        airplaneY=new SimpleDoubleProperty();
        heading=new SimpleDoubleProperty();
        
        //Map & calcPath:
    	ipPath=new SimpleStringProperty();
        portPath=new SimpleStringProperty();
        startX=new SimpleDoubleProperty();
        startY=new SimpleDoubleProperty();
        offset=new SimpleDoubleProperty();
        markSceneX=new SimpleDoubleProperty();
        markSceneY=new SimpleDoubleProperty();
        path=new SimpleBooleanProperty();
        
        //Bind:
    	isConnect.bind(sm.isConnect);
	}

	//------------------------------------------------Functions------------------------------------------------------
	public void setData(int[][] data)
	{
		this.data = data;
		sm.airPlanePosition(startX.getValue(), startY.doubleValue(), offset.getValue());
	}

	public void findPath(double h, double w)
	{
		if (!path.getValue())
			sm.connectCalaPath(ipPath.getValue(), Integer.parseInt(portPath.getValue()));

		sm.findPath((int) (airplaneY.getValue() / -1), (int) (airplaneX.getValue() + 15),
				Math.abs((int) (markSceneY.getValue() / h)), Math.abs((int) (markSceneX.getValue() / w)), data);
	}

	//----------------------------open/disconnect/Connect to AutoPilot & Joystick-------------------------------------------
    public void connect()
    {
    	sm.connect(ipJoystick.get(), portJoystick.get());
    }

    public void openServer()
    {
		this.sm.openServer();
	}

	public void closeServer()
	{
		this.sm.closeServer();
	}
	//--------------------------------------------------joystick-------------------------------------------------------------
    public void setThrottle()
    {
    	((SimulatorModel) sm).setThrottle(this.throttle.getValue().toString());
    }

    public void setRudder()
    {
    	((SimulatorModel) sm).setRudder(this.rudder.getValue().toString());
    }

    public void setJoystick()
    {
    	((SimulatorModel) sm).setJoystick(this.aileronV.getValue().toString(),this.elevatorV.getValue().toString());
    }
    //------------------------------------------------AutoPilot-------------------------------------------------------------
    public void AutoPilot(String[] lines)
    {
    	this.sm.autoPilot(lines);
    }

    public void offAutoPilot()
    {
    	this.sm.offAutoPilot();
    }

	@Override
	public void update(Observable o, Object args) {

		if(o==sm)
		{
			 String[] arr=(String[])args;
	            if(arr[0].equals("plane"))
	            {
	                double x = Double.parseDouble(arr[1]);
	                double y = Double.parseDouble(arr[2]);
	                x = (x - startX.getValue() + offset.getValue());
	                x /= offset.getValue();
	                y = (y - startY.getValue() + offset.getValue()) / offset.getValue();
	                airplaneX.setValue(x);
	                airplaneY.setValue(y);
	                heading.setValue(Double.parseDouble(arr[3]));
	                setChanged();
	                notifyObservers();
	            }
	            else if(arr[0].equals("path"))
	            {
	                setChanged();
	                notifyObservers(arr);
	            };
		}
	}
}