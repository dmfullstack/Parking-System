EXTERNAL .JAR FILE DEPENDENCIES
-------------------------------
For database:
- mysql-connector-java-5.1.14-bin.jar


HOW TO RUN FROM COMMAND LINE
----------------------------
For the folder structure:

+ src
  + cs414
    + a5
      + fmaster
	    ...
	    ...
	    - .java files
+ lib
  - .jar files


1) cd to the main source dir 'src'.
cd src

2) Compile the code:
javac -cp "../lib/*" cs414/a5/fmaster/main/java/server/*.java cs414/a5/fmaster/main/java/server/database/*.java cs414/a5/fmaster/main/java/server/domain/*.java cs414/a5/fmaster/main/java/server/handler/*.java cs414/a5/fmaster/main/java/client/*.java cs414/a5/fmaster/main/java/client/admin/*.java cs414/a5/fmaster/main/java/client/enterparking/*.java cs414/a5/fmaster/main/java/client/exitparking/*.java cs414/a5/fmaster/main/java/client/ui/*.java cs414/a5/fmaster/main/java/client/ui/admin/*.java cs414/a5/fmaster/main/java/client/ui/enterparking/*.java cs414/a5/fmaster/main/java/client/ui/exitparking/*.java

Port number = 52365 works.
3) Run ParkingServer
java -cp "../lib/*:." cs414.a5.fmaster.main.java.server.ParkingServer <port_no>

4) Run EnterParking Client
java -cp "../lib/*:." cs414.a5.fmaster.main.java.client.enterparking.EnterParkingClient localhost <port_no>

5) Run ExitParkingClient
java -cp "../lib/*:." cs414.a5.fmaster.main.java.client.exitparking.ExitParkingClient localhost <port_no>

6) Run Admin Client
java -cp "../lib/*:." cs414.a5.fmaster.main.java.client.admin.AdminParkingClient localhost <port_no>


USERNAMES AND PASSWORDS
-----------------------
The system comes with a preloaded admin account.
Username: admin
Password: admin
Security Q: admin
Security A: admin


PATTERNS USED
-------------
- Observer
Observer pattern is used between ParkingServer and RemoteObservers.
- Singleton
Singleton pattern is used to get single instance of UI classes for each client. Also, to get single instance of Database Access class.
- Strategy
Strategy pattern is used for ViewRatesUIInterface. Each of the classes that implement it, have their own implementations of overridden methods.
- Proxy
Proxy pattern is used to wrap the Remote Observers or clients which can be of any type: Enter, Exit or Admin.
- Facade
A simple ParkingInterface is provided to the clients, so they do not know the complexities of the Server operations.
- Controller, Delegation, Indirection
The ParkingServerImpl acts as a controller delegating work to other Handlers.
- Creater
The MainUI's children such as EnterParkingMainUI creates the other closely related UI objects EnterParkingUI and EnterParkingViewRatesUI)
- High Cohesion, Information Expert
The Server Handlers have responsiblities that are strongly related and perform operations that are focused on particular processes.
- Low Coupling
The UI classes such as EnterParkingMainUI and ExitParkingMainUI do not depend on each other.
Also, the Server Handler have their own responsiblities and have low dependency on each other.
- Protected Variations
The RemoteObserver interface hides the Client type from the Server.
- Polymorphism
It is used with interfaces being implemented and superclasses being extended.


REFACTORINGS
------------
- Extract method
Methods extracted out in ActionListeners of the UI classes
- Pull up
Methods in MainUI superclass were pulled up from the subclasses
- Type generalization
Putting common methods in MainUI superclass, so all children UI can use them
- Push down
Methods in MainUI were pushed down because of different implementations for children.
- Extract Interface
Interface ViewRatesUIInterface is extracted from ViewRatesUI of different types.
- Extract Hierarchy, Superclass, Subclass
MainUI is extracted out as a superclass with EnterParkingMainUI, ExitParkingMainUI and AdminMainUI as subclasses.