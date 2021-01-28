PTM2-FlightGear-Simulator-MileStone-5

1.  Add the generic_small.xml file to the game installation folder in the following path:	.../FlightGear/data/Protocol
(You can find this file in the project folder at: resources/autoPilotCommands/generic_small.xml).
2. Run the FlightGear-Simulator.
3.  Write the following lines in the simulator settings at the bottom of the page:
     --telnet=socket,in,10,127.0.0.1,5402,tcp
     --generic=socket,out,10,127.0.0.1,5400,tcp,generic_small
4. In the main screen of the simulator select the map of honolulu.
5. I suggest you change as a default setting for morning time, you can do this in the environment tag in the main window of the simulator.
6. Open Server.bat file.
7. Open Client.bat file.
8. In the simulator, click fly. 
9. Once the simulator is loaded select AutoStart in the bar at the top of the simulator window, it is in one tag before the last.
10. Now in the joystick click on the connect button and write the ip and port written there (127.0.0.1 \ 5402). 
11. Now in the joystick click on the connect button and write the ip and port written there (127.0.0.1 \ 5402).
      You can now use the autopilot and joystick. (For the autopilot, click load and select which command file you want or write yourself according to the format written in the file, then click "execute").
12. In the button on the left you can load the map honolulu to see the location of the plane.
     And to find the cheapest route, select with the mouse in any area on the map and click calculate Path and write the ip and port written there (127.0.0.1 \ 6400).

