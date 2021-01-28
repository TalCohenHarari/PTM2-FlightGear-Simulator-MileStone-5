package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;
import viewmodel.ViewModel;

public class MainWindowController implements Initializable, Observer {

	// --------------------------------------Parameters-------------------------------------------

	//ViewModel:
	public ViewModel vm;

	//joystick:
	private double sceneX, sceneY;
	private double translateX, translateY;
	public DoubleProperty aileronV, elevatorV;
	public StringProperty ipJoystick, portJoystick;
	public IntegerProperty isConnect;

	//Plane:
    public DoubleProperty airplaneX;
    public DoubleProperty airplaneY;
    public DoubleProperty startX;
    public DoubleProperty startY;
    public DoubleProperty markSceneX, markSceneY;
    public DoubleProperty offset;
    public DoubleProperty heading;
    public DoubleProperty aileron;
    public DoubleProperty elevator;

	//Map:
    public int mapData[][];
    private Image plane[];
    private Image mark;
    public double lastX;
    public double lastY;
    private String[] solution;
    private BooleanProperty path;
	public BooleanProperty isConnectedToSimulator;
	private boolean mapOn=false;
	public StringProperty ipPath;
	public StringProperty portPath;
	public boolean isConnenctCalcPath=false;

	//---------FXML---------:
	@FXML
	private Label connectLabel;
	@FXML
	private Canvas airplane;
	@FXML
	private Canvas markX;
	@FXML
	public MapDisplayer mapDisplayer;
	@FXML
	private RadioButton autoPilot;
	@FXML
	private TextArea textAreaBox;
	@FXML
	private Slider throttle;
	@FXML
	private Slider rudder;
	@FXML
	private Circle joystick;
	@FXML
	private Circle frameCircle;
	@FXML
	private RadioButton manual;
	@FXML
	private Label throttleLabel;
	@FXML
	private Label rudderLabel;


	// ---------------------------------------------------------Constructor---------------------------------------------------
	public MainWindowController()
	{
		ipJoystick = new SimpleStringProperty();
		portJoystick = new SimpleStringProperty();
		joystick = new Circle();
		aileronV = new SimpleDoubleProperty();
		elevatorV = new SimpleDoubleProperty();
		mapDisplayer = new MapDisplayer();
		startX = new SimpleDoubleProperty();
		startY = new SimpleDoubleProperty();
		offset = new SimpleDoubleProperty();
		isConnect = new SimpleIntegerProperty();
		connectLabel = new Label("");
		throttleLabel = new Label("");
		rudderLabel = new Label("");
		offset = new SimpleDoubleProperty();
		heading = new SimpleDoubleProperty();
		markSceneX = new SimpleDoubleProperty();
		markSceneY = new SimpleDoubleProperty();
		path = new SimpleBooleanProperty();
		airplaneX = new SimpleDoubleProperty();
		airplaneY = new SimpleDoubleProperty();
		startX = new SimpleDoubleProperty();
		startY = new SimpleDoubleProperty();
		isConnectedToSimulator = new SimpleBooleanProperty();
		ipPath =new SimpleStringProperty();
		portPath=new SimpleStringProperty();

		plane = new Image[8];
		try
		{
			plane[0] = new Image(new FileInputStream("./resources/planeImages/plane0.png"));
			plane[1] = new Image(new FileInputStream("./resources/planeImages/plane45.png"));
			plane[2] = new Image(new FileInputStream("./resources/planeImages/plane90.png"));
			plane[3] = new Image(new FileInputStream("./resources/planeImages/plane135.png"));
			plane[4] = new Image(new FileInputStream("./resources/planeImages/plane180.png"));
			plane[5] = new Image(new FileInputStream("./resources/planeImages/plane225.png"));
			plane[6] = new Image(new FileInputStream("./resources/planeImages/plane270.png"));
			plane[7] = new Image(new FileInputStream("./resources/planeImages/plane315.png"));
			mark = new Image(new FileInputStream("./resources/map/mark.png"));
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
	}

	// ------------------------------------------------------Functions-----------------------------------------------------------
	public void setVM(ViewModel vm)
	{
		this.vm = vm;
		throttle.valueProperty().bindBidirectional(vm.throttle);
		rudder.valueProperty().bindBidirectional(vm.rudder);
		ipJoystick.bindBidirectional(vm.ipJoystick);
		portJoystick.bindBidirectional(vm.portJoystick);
		aileronV.bindBidirectional(vm.aileronV);
		elevatorV.bindBidirectional(vm.elevatorV);
		elevatorV.bindBidirectional(vm.elevatorV);
		isConnect.bind(vm.isConnect);
		airplaneX.bindBidirectional(vm.airplaneX);
        airplaneY.bindBidirectional(vm.airplaneY);
        startX.bindBidirectional(vm.startX);
        startY.bindBidirectional(vm.startY);
        offset.bindBidirectional(vm.offset);
        heading.bindBidirectional(vm.heading);
        markSceneY.bindBidirectional(vm.markSceneY);
        markSceneX.bindBidirectional(vm.markSceneX);
        path.bindBidirectional(vm.path);
        path.setValue(false);
        isConnectedToSimulator.set(false);
        ipPath.bindBidirectional(vm.ipPath);
        portPath.bindBidirectional(vm.portPath);
	}

	@Override
	public void update(Observable o, Object args)
	{
		if (o == vm)
		{	//airPlane:
			if(args==null)
                drawAirplane();
            else //linePath:
            {
                solution=(String[])args;
                this.drawLine();
            }
		}
	}
	// --------------------------------------LeftSide-------------------------------------------

	// Connect Button:
	@FXML
	public void Connect()
	{
		if (isConnect.getValue() == 1)
			connectLabel.setText("You are already connected");
		else {
			// Create the custom dialog.
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Connect to simulator server");
			dialog.setHeaderText("Please enter your PORT & IP");

			// Set the icon (must be included in the project).
//	 		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

			// Set the button types.
			ButtonType SubmitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(SubmitButtonType, ButtonType.CANCEL);

			// Create the IP and PORT labels and fields.
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));

			TextField ip = new TextField();
			ip.setPromptText("127.0.0.1");
			TextField port = new TextField();
			port.setPromptText("5402");

			grid.add(new Label("IP:"), 0, 0);
			grid.add(ip, 1, 0);
			grid.add(new Label("PORT:"), 0, 1);
			grid.add(port, 1, 1);

			// Enable/Disable Submit button depending on whether a ip and port was entered.
			Node ipButton = dialog.getDialogPane().lookupButton(SubmitButtonType);
			ipButton.setDisable(true);

			// Do some validation (using the Java 8 lambda syntax).
			port.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!ip.getText().isEmpty())
					ipButton.setDisable(newValue.trim().isEmpty());
			});
			ip.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!port.getText().isEmpty())
					ipButton.setDisable(newValue.trim().isEmpty());
			});

			dialog.getDialogPane().setContent(grid);

			// Request focus on the IP field by default.
			// Platform.runLater(() -> ip.requestFocus());

			// Convert the result to a IP-PORT-pair when the login button is clicked.
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == SubmitButtonType) {
					return new Pair<>(ip.getText(), port.getText());
				}
				return null;
			});

			Optional<Pair<String, String>> result = dialog.showAndWait();

			String[] ipAndPort = new String[2];
			result.ifPresent(ipPort -> {
				ipAndPort[0] = ipPort.getKey();
				ipAndPort[1] = ipPort.getValue();
			});

			this.ipJoystick.setValue(ipAndPort[0]);
			this.portJoystick.setValue(ipAndPort[1]);
			if(ipJoystick.getValue()!=null && portJoystick.getValue()!=null)
				vm.connect();
			if (isConnect.getValue() == 1)
				connectLabel.setText("Connected");
			else
				connectLabel.setText("");
		}
	}

	// loadData:
	@FXML
	public void loadData()
	{
		connectLabel.setText("");
		FileChooser fc = new FileChooser();
		fc.setTitle("Load MAP");
		fc.setInitialDirectory(new File("./Resources/map"));
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fc.getExtensionFilters().add(fileExtensions);
		File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null)
        {
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            List<String[]> numbers = new ArrayList<>();
            try
            {

                br = new BufferedReader(new FileReader(selectedFile));
                String[] start=br.readLine().split(cvsSplitBy);
                startX.setValue(Double.parseDouble(start[0]));
                startY.setValue(Double.parseDouble(start[1]));
                start=br.readLine().split(cvsSplitBy);
                offset.setValue(Double.parseDouble(start[0]));

                while ((line = br.readLine()) != null)
                    numbers.add(line.split(cvsSplitBy));

                mapData = new int[numbers.size()][];

                for (int i = 0; i < numbers.size(); i++)
                {
                	mapData[i] = new int[numbers.get(i).length];
                    for (int j = 0; j < numbers.get(i).length; j++)
                    {
                        String tmp=numbers.get(i)[j];
                        mapData[i][j] = Integer.parseInt(tmp);
                    }
                }
                this.vm.setData(mapData);
                this.drawAirplane();
                if(!mapOn)
                	mapDisplayer.setMapData(mapData);
                mapOn=true;
            }
            catch (FileNotFoundException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
        }
	}

	//Connect to Solver and calculate the shortest path
    public void CalculatePath()
    {
    	if(mapOn)
    	{
			// Create the custom dialog.
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Connect to CalculatePath server");
			dialog.setHeaderText("Please enter your PORT & IP");

			// Set the icon (must be included in the project).
//	 		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

			// Set the button types.
			ButtonType SubmitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(SubmitButtonType, ButtonType.CANCEL);

			// Create the IP and PORT labels and fields.
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));

			TextField ip = new TextField();
			ip.setPromptText("127.0.0.1");
			TextField port = new TextField();
			port.setPromptText("6400");

			grid.add(new Label("IP:"), 0, 0);
			grid.add(ip, 1, 0);
			grid.add(new Label("PORT:"), 0, 1);
			grid.add(port, 1, 1);

			// Enable/Disable Submit button depending on whether a ip and port was entered.
			Node ipButton = dialog.getDialogPane().lookupButton(SubmitButtonType);
			ipButton.setDisable(true);

			// Do some validation (using the Java 8 lambda syntax).
			port.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!ip.getText().isEmpty())
					ipButton.setDisable(newValue.trim().isEmpty());
			});
			ip.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!port.getText().isEmpty())
					ipButton.setDisable(newValue.trim().isEmpty());
			});

			dialog.getDialogPane().setContent(grid);

			// Request focus on the IP field by default.
			// Platform.runLater(() -> ip.requestFocus());

			// Convert the result to a IP-PORT-pair when the login button is clicked.
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == SubmitButtonType) {
					return new Pair<>(ip.getText(), port.getText());
				}
				return null;
			});

			Optional<Pair<String, String>> result = dialog.showAndWait();

			String[] ipAndPort = new String[2];
			result.ifPresent(ipPort -> {
				ipAndPort[0] = ipPort.getKey();
				ipAndPort[1] = ipPort.getValue();
			});

			this.ipPath.setValue(ipAndPort[0]);
			this.portPath.setValue(ipAndPort[1]);

			double H = markX.getHeight();
			double W = markX.getWidth();
			double h = H / mapData.length;
			double w = W / mapData[0].length;
			if(ipPath.getValue()!=null && portPath.getValue()!=null)
			{
				vm.findPath(h, w);
				// boolean variable for indicates if this the first time you find the shortest path
				path.setValue(true);
			}
    	}
    	else
    		connectLabel.setText("Please load map first...");

    }

    public void drawAirplane()
    {
        if(airplaneX.getValue()!=null&&airplaneY.getValue()!=null)
        {
            double H = airplane.getHeight();
            double W = airplane.getWidth();
            double h = H / mapData.length;
            double w = W / mapData[0].length;
            GraphicsContext gc = airplane.getGraphicsContext2D();
            lastX=airplaneX.getValue();
            lastY=airplaneY.getValue()*-1;
            gc.clearRect(0,0,W,H);

            if(heading.getValue()>=0&&heading.getValue()<39)
                gc.drawImage(plane[0], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=39&&heading.getValue()<80)
                gc.drawImage(plane[1], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=80&&heading.getValue()<129)
                gc.drawImage(plane[2], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=129&&heading.getValue()<170)
                gc.drawImage(plane[3], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=170&&heading.getValue()<219)
                gc.drawImage(plane[4], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=219&&heading.getValue()<260)
                gc.drawImage(plane[5], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=260&&heading.getValue()<309)
                gc.drawImage(plane[6], w*lastX, lastY*h, 25, 25);
            if(heading.getValue()>=309)
                gc.drawImage(plane[7], w*lastX, lastY*h, 25, 25);
        }
    }
    //Draw the shortest path
    public void drawMark()
    {
        double H = markX.getHeight();
        double W = markX.getWidth();
        double h = H / mapData.length;
        double w = W / mapData[0].length;
        GraphicsContext gc = markX.getGraphicsContext2D();
        gc.clearRect(0,0,W,H);
        gc.drawImage(mark, markSceneX.getValue()-13,markSceneY.getValue() , 10, 10);
        if(path.getValue())
            vm.findPath(h,w);
    }
    //Draw the line from airplane to target
    public void drawLine()
    {
        double H = markX.getHeight();
        double W = markX.getWidth();
        double h = H / mapData.length;
        double w = W / mapData[0].length;
        GraphicsContext gc=markX.getGraphicsContext2D();
        
        String move=solution[1];
        double x= airplaneX.getValue()*w+10*w;
        double y=airplaneY.getValue()*-h+6*h;
        for(int i=2;i<solution.length;i++)
        {
            switch (move)
            {
                case "Right":
                    gc.setStroke(Color.BLACK.darker());
                    gc.strokeLine(x, y, x + w, y);
                    x +=  w;
                    break;
                case "Left":
                    gc.setStroke(Color.BLACK.darker());
                    gc.strokeLine(x, y, x -  w, y);
                    x -=  w;
                    break;
                case "Up":
                    gc.setStroke(Color.BLACK.darker());
                    gc.strokeLine(x, y, x, y - h);
                    y -=  h;
                    break;
                case "Down":
                    gc.setStroke(Color.BLACK.darker());
                    gc.strokeLine(x, y, x, y +  h);
                    y += h;
            }
            move=solution[i];
        }
    }
    
    //Event - pressing on the map
    EventHandler<MouseEvent> mapClick = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent e)
        {
        	if(mapOn) {
	            markSceneX.setValue(e.getX());
	            markSceneY.setValue(e.getY());
	            drawMark();
        	}
        	else
        		connectLabel.setText("Please Load map first...");
        }
    };






	// -------------------------------------------------------Middle---------------------------------------------------------------

	public void autoPilotlSelect()
	{
		if (manual.isSelected())
			manual.setSelected(false);
		autoPilot.setSelected(true);
		connectLabel.setText("");
	}

	// loadCommandsText:
	@FXML
	public void loadtxt()
	{
		try
		{
			if (autoPilot.isSelected())
			{
				FileChooser fc = new FileChooser();
				fc.setTitle("Load data file");
				fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
				fc.setInitialDirectory(new File("./resources/autoPilotCommands"));
				File chosen = fc.showOpenDialog(null);
				if (chosen != null)
				{
					textAreaBox.clear();
					BufferedReader file = new BufferedReader(new FileReader(new File(chosen.getPath())));
					Scanner scan = new Scanner(file);
					while (scan.hasNextLine())
						textAreaBox.appendText(scan.nextLine() + "\n");

					scan.close();
					file.close();
				}
			}
			else
				connectLabel.setText("Please select AutoPilot first...");
		}
		catch (IOException e) {/*e.printStackTrace();*/}
	}

	// Execute Commands:
	public void execute()
	{
		if (!textAreaBox.getText().isEmpty() && autoPilot.isSelected() && isConnect.getValue() == 1)
		{
			Scanner scan = new Scanner(textAreaBox.getText());
			int countLines = 0;
			while (scan.hasNext())
			{
				scan.nextLine();
				countLines++;
			}
			scan.close();
			Scanner scan2 = new Scanner(textAreaBox.getText());
			String[] lines = new String[countLines];
			for (int i = 0; scan2.hasNextLine(); i++)
				lines[i] = scan2.nextLine();
			scan2.close();
			this.vm.AutoPilot(lines);
		}
		else if (!autoPilot.isSelected())
			connectLabel.setText("Please select AutoPilot first...");
		else if (isConnect.getValue() == 0)
			connectLabel.setText("Please connect first...");
		else if (textAreaBox.getText().isEmpty())
			connectLabel.setText("Select or write commands");
	}

	// --------------------------------------------------------Right Side------------------------------------------------------------
	
	public void manualSelect()
	{
		if (autoPilot.isSelected())
			autoPilot.setSelected(false);
		manual.setSelected(true);
		connectLabel.setText("");
		this.vm.offAutoPilot();
	}
	//	Reset to zero the rudder when press on "R":
	public void resetRudder()
	{
		rudder.setValue(0.0);
	}
	//	Reset to zero the throttle when press on "R":
	public void resetThrottle()
	{
		throttle.setValue(0.0);
	}
	// When joystick pressed:
	@FXML
	private void joystickPressed(MouseEvent mouse)
	{
		if (manual.isSelected() && isConnect.getValue() == 1)
		{
			Circle circle = (Circle) mouse.getSource();
			sceneX = mouse.getSceneX();
			sceneY = mouse.getSceneY();
			translateX = circle.getTranslateX();
			translateY = circle.getTranslateY();
		}
		else if (!manual.isSelected())
			connectLabel.setText("Please select Manual...");
		else if (isConnect.getValue() == 0)
			connectLabel.setText("Please connect first...");

	}

	private double normalize(double value)
	{
		return Math.round(((value * 2) - 1) * 100.00) / 100.00;
	}

	private void changeJoystick(double x, double y)
	{
		if (manual.isSelected() && isConnect.getValue() == 1)
		{
			this.aileronV.setValue(x);
			this.elevatorV.setValue(y);
			this.vm.setJoystick();
		}
	}
	// When joystick dragged:
	@FXML
	private void joystickDragged(MouseEvent mouse)
	{
		if (manual.isSelected() && isConnect.getValue() == 1)
		{
			Circle circle = (Circle) mouse.getSource();
			double offsetX = mouse.getSceneX() - sceneX;
			double offsetY = mouse.getSceneY() - sceneY;
			double newTranslateX = translateX + offsetX;
			double newTranslateY = translateY + offsetY;
			double joystickCenterX = this.getCenter("x");
			double joystickCenterY = this.getCenter("y");
			double frameRadius = frameCircle.getRadius();
			double maxX = joystickCenterX + frameRadius;
			double contractionsCenterX = joystickCenterX - frameRadius;
			double maxY = joystickCenterY - frameRadius;
			double contractionsCenterY = joystickCenterY + frameRadius;

			double slant = Math
					.sqrt(Math.pow(newTranslateX - joystickCenterX, 2) + Math.pow(newTranslateY - joystickCenterY, 2));

			if (slant > frameRadius)
			{
				double alpha = Math.atan((newTranslateY - joystickCenterY) / (newTranslateX - joystickCenterX));
				if ((newTranslateX - joystickCenterX) < 0)
					alpha = alpha + Math.PI;

				newTranslateX = Math.cos(alpha) * frameRadius + translateX;
				newTranslateY = Math.sin(alpha) * frameRadius + translateY;
			}
			circle.setTranslateX(newTranslateX);
			circle.setTranslateY(newTranslateY);
			double xrange = (newTranslateX - contractionsCenterX) / (maxX - contractionsCenterX);
			double normalizedX = this.normalize(xrange);

			double yrange = (newTranslateY - contractionsCenterY) / (maxY - contractionsCenterY);
			double normalizedY = this.normalize(yrange);
			changeJoystick(normalizedX, normalizedY);
		}
	}
	// When joystick released:
	@FXML
	private void joystickReleased(MouseEvent mouse)
	{
		if (manual.isSelected() && isConnect.getValue() == 1)
		{
			Circle circle = (Circle) mouse.getSource();
			circle.setTranslateX(this.getCenter("x"));
			circle.setTranslateY(this.getCenter("y"));
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

		rudder.valueProperty().addListener((ChangeListener<Object>) (arg0, arg1, arg2) ->
		{
			rudderLabel.textProperty().setValue("Rudder: " + (Math.round((rudder.getValue() * 10.00))) / 10.00);
			if (manual.isSelected() && isConnect.getValue() == 1) 
			{
				vm.setRudder();
			}
		});
		throttle.valueProperty().addListener((ChangeListener<Object>) (arg0, arg1, arg2) ->
		{
			throttleLabel.textProperty().setValue("Throttle: " + (Math.round((throttle.getValue() * 10.00))) / 10.00);
			if (manual.isSelected() && isConnect.getValue() == 1)
			{
				vm.setThrottle();
			}
		});
        markX.setOnMouseClicked(mapClick);
	}

	private double getCenter(String target)
	{
		double circleRadius = frameCircle.getRadius();
		double joystickRadius = joystick.getRadius();
		double center;
		if (target.equals("x"))
			center = frameCircle.getTranslateX();
		else
			center = frameCircle.getTranslateY();
		if (target.equals("x"))
		{
			changeJoystick(0.0, 0.0);
			return center + circleRadius - joystickRadius - 54;
		}
		else
		{
			changeJoystick(0.0, 0.0);
			return center - circleRadius - joystickRadius + 146;
		}
	}
}
