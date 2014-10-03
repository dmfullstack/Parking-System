Parking-System
==============

Parking System for CS414

1 Problem Statement: A Parking System
Suppose that a city is constructing a computerized parking garage. We will implement the parking system. We will start with a small set of features and over a couple of iterations, build a much larger system. The first three deliverables (A2, A3, and A4) are part of the the first iteration. The deliverable correspond to analysis, design, and implementation techniques. After the first iteration, we will have one more iteration with A5 as the deliverable.
Since you will not have actual hardware (e.g., card reader, ticket dispenser, garage gate, and garage signs) you will implement them in software (through user interfaces). The UI must be interactive, which means that the driver using it must be able to type or click on something to enter or exit through the gate, enter ticket information, etc.
Here we provide an overview of the parking system and list all the features that are of interest to us. Your job will be to flesh out the requirements. We will also provide a high level plan that will guide the two iterations.

1.1 First iteration

The city administrators will start with one garage that allows cars to park using parking tickets. We will start with one entry gate and one exit gate. Tickets are purchased at the entry gate and paid for at the time of exiting. The same ticket obtained while entering must be used to exit, i.e., bogus or fake tickets cannot be used to exit and cheat the system. You must allow multiple cars to enter or exit the garage.
The system keeps track of the current levels of occupancy in the garage and displays a sign showing whether the garage is full or not. The system also keeps tracks of payments and ticket sales, and collects occupancy summaries for each hour of the day. Garage administrators should be able to query and view garage usage in various forms (be creative here: hourly, daily, weekly, and monthly basis). The more flexible applications will allow the user to answer sophisticated questions such as, which hours are the most used on an average day over the last month.

1.2 Second iteration

We will extend the application to support multiple entry and exit gates. This brings up the distributed nature of the system. Each gate is connected to a central location for processing payments and collecting usage data. We will also implement a graphical user interface to support the old and new features.

2 Deliverables

The project will be submitted in stages. Submit an electronic copy of the deliverables at each stage. For A2 and A3, the deliverable will be a PDF document. For A4, you will submit both a PDF document and code as a jar file. For A5, you will turn in code as a single jar file.
You must embed all the images (i.e. jpg, bmp, etc.) in the PDF file as your submission. Any tool specific model files will NOT be accepted. Do not submit images as separate files. Please make sure that you include your name on the front page of all submitted documents.

A2. Use Case Analysis

Due: Monday, September 22, 2014, 11:59 PM via RamCT assignment submission.
20 points.
Tasks
Flesh out the requirements listed under iteration 1 above.
You must include possible erroneous situations for everything that is listed.
Identify the actors.
Think about use cases. Use cases must cover all general kinds of end-to-end uses. Follow the format used in Larman's text when you write them up.
Prepare a document containing the following:
Use case diagram showing the actors and the use cases. (5 points)
All use cases in fully-dressed essential form. (15 points)

A3. Domain Modeling

Due: Monday, September 29, 2014, 11:59 PM via RamCT assignment submission.
20 points.
Tasks
Based on the use cases identified in the previous assignment, model the domain concepts and their relationships.
Express a Domain Model as a UML class diagram. Follow the format used in the text. This is worth 15 points. The Domain Model should:
Be expressed at the problem domain level of abstraction; it should focus on the abstract concepts and not include design or implementation classes.
Include relevant and necessary associations and other links.
Attributes should be simple or "pure data elements" such as scalars. State that is complex should be represented as associations to concepts that represent the complex entity.
Do not worry about the direction (arrows) of the associations.
Include multiplicity if appropriate.
Use Generalization/Specialization (inheritance) sparingly, and only for true "is-a" relationships.
Do not include operations or methods in your conceptual model.
Include a brief glossary that defines each concept. This is worth 5 points.
Include the previous use case document after completing required updates. You may fix errors in them, but the previous documents will not be re-graded. The current document must be consistent with the previous document, i.e., your domain model must support the requirements and use cases that you listed or were asked to list in A2.

A4. Design and Implement Iteration 1

Due: Wednesday, October 29, 2014, 11:59 PM via RamCT assignment submission.
50 points.
Tasks
Use Subversion/GitHub/any repository based version control system. for all types of documents (design diagrams, tests, implementation code).
Prepare sequence diagrams as part of the design process. Submit three key/difficult/interesting ones.
Prepare a design class diagram.
Write up a system test plan.
Write JUnit test cases.
Implement the system for iteration 1. You must use the GRASP patterns that you studied in the class. It must have a user interface: text-based menus are acceptable. However, it should be possible to view what it happening to the garage and what it's current state is at any time. If your menus are long and/or nested, please design them to make them user-friendly.
Test your system.
Submission
Submit a PDF document containing the following:
Three sequence diagrams (9 points)
Class diagram (5 points)
System test plan (4 points)
Commit log showing checkins, checkouts, etc from your repository based version control system (3 points). If the document shows an inadequate amount of checkin and checkout, you will lose points.
Document describing how to run the system. The GTA will try to schedule a skype/google hangout/webex session with you for a demo of the system, but it's a good idea to have a reference anyway.
Submit the following code in a single jar file. Note that the code must implement the design you provided above. Points will be deducted if it doesn't.
JUnit test code (4 points)
System implementation broken down as follows:
Features: 20 points
Design quality: 3 points
Programming style: 2 points

A5. Design and Implement Iteration 2

Due: Friday, November 21, 2014, 11:59 PM via RamCT assignment submission.
60 points.
Tasks
After A4 is graded, you must browse the wrapup message for assignment A4 on the main discussion board, where we will post some suggestions for core functionality and user interface.
Refactor the code from iteration 1 to cleanly separate the user interface layer from the application and domain layers.
Allow for distribution. You will use Java RMI to enable distributed communication. You will now have a centralized server that handles calls from various clients that may be located at the gates of any of the garages or at the campus permit office. You will need to identify what functionality exist in the server and client sides. You will also define and implement new Remote interfaces.
Replace the text-based interface with a GUI. You must use Swing. No web-based user interfaces are allowed because this is a class on OO design. You will now be able to execute multiple GUI-based clients with a single server.
Use design patterns (e.g., Facade, Strategy and so on).
Submission: Submit the following in a single jar file.
System implementation Java source files. (56 points)
You can name subpackages as you like: test, ui, view, model, core, etc., but all code and data files should reside under the package cs414.a5.eid. and the source files must be in the folder hierarchy cs414/a5/eid/.
Features: 40 points
Design quality: 10 points
Programming style: 4 points
A text file named README, that 1) lists the external .jar file dependencies, if any, (database, user interface, logging, etc.), 2) shows how to run from command line, 3) gives user names and passwords if necessary, and 4) discusses what is especially strong, weak, or missing from the assignment, 5) what patterns and refactorings were used. (3 points)
Commit log (3 points). If the document shows an inadequate amount of checkin and checkout, you will lose points.
