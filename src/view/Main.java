package view;

import java.util.Observable;
import java.util.Observer;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.SimulatorModel;
import viewmodel.ViewModel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	SimulatorModel sm;
	ViewModel vm;
	@Override
	public void start(Stage primaryStage) {
		try {
			sm=new SimulatorModel();
			vm=new ViewModel(sm);
			sm.setVm(vm);
			vm.addObserver(sm);
			sm.addObserver(vm);


		    FXMLLoader fxml = new FXMLLoader();
			BorderPane root =fxml.load(getClass().getResource("MainWindow.fxml").openStream());
		    MainWindowController mwc = fxml.getController();//load the fxml file.
		    mwc.setVM(vm);
		    vm.addObserver(mwc);

		    //For run in eclipse:
//		    Server server = new MySerialServer();
//	        CacheManager CacheManager = new FileCacheManager();
//	        MyClientHandler ch = new MyClientHandler(CacheManager);
//	        server.open(6400, new ClientHandlerPath(ch));
		    vm.openServer();
		    
			Scene scene = new Scene(root,1000,400); //define the window size.
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //style of the window.
			primaryStage.setScene(scene);
			primaryStage.setTitle("Welcome to FlightGear Flight Simulator");
			primaryStage.show(); // open the window.
			primaryStage.setOnCloseRequest(event -> {
			 vm.closeServer();
			 System.out.println("Bye Bey! :)");
		    });
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public static void main(String[] args) {
		launch(args);
		Platform.exit();
		System.exit(0);
	}
}
