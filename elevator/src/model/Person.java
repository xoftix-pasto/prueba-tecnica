// Person.java
// Person riding the elevator
package model;

import event.DoorEvent;
import event.DoorListener;
import event.PersonMoveEvent;
import event.PersonMoveListener;

public class Person extends Thread implements DoorListener {

	// identification number
	private int ID = -1;

	// represents whether Person is moving or waiting
	private boolean moving;

	// reference to Location (either on Floor or in Elevator)
	private Location location;

	// listener object for PersonMoveEvents
	private PersonMoveListener personMoveListener;

	// time in milliseconds to walk to Button on Floor
	private static final int TIME_TO_WALK = 3000;

	// maximum time Person will wait for Elevator (10 minutes)
	private static final int TIME_WAITING = 10 * 60 * 1000;

	// types of messages Person may send
	public static final int PERSON_CREATED = 1;
	public static final int PERSON_ARRIVED = 2;
	public static final int PERSON_ENTERING_ELEVATOR = 3;
	public static final int PERSON_PRESSING_BUTTON = 4;
	public static final int PERSON_EXITING_ELEVATOR = 5;
	public static final int PERSON_EXITED = 6;

	// Person constructor set initial location
	public Person(int identifier, Location initialLocation) {
		super();

		ID = identifier; // assign unique identifier
		location = initialLocation; // set Floor Location
		moving = true; // start moving toward Button on Floor
	}

	// set listener for PersonMoveEvents
	public void setPersonMoveListener(PersonMoveListener listener) {
		personMoveListener = listener;
	}

	// invoked when Door has opened
	public void doorOpened(DoorEvent doorEvent) {
		// set Person on Floor where Door opened
		setLocation(doorEvent.getLocation());

		// interrupt Person's sleep method in run method and
		// Elevator's ride method
		interrupt();
	}

	// invoked when Door has closed
	public void doorClosed(DoorEvent doorEvent) {
	}

	// set Person Location
	private void setLocation(Location newLocation) {
		location = newLocation;
	}

	// get current Location
	private Location getLocation() {
		return location;
	}

	// get identifier
	public int getID() {
		return ID;
	}

	// set if Person should move
	public void setMoving(boolean personMoving) {
		moving = personMoving;
	}

	// get if Person should move
	public boolean isMoving() {
		return moving;
	}

	// Person either rides or waits for Elevator
	public void run() {
		sendPersonMoveEvent(PERSON_CREATED);

		// walk to Elevator
		pauseThread(TIME_TO_WALK);
		setMoving(false);

		// Person arrived at Floor Button
		sendPersonMoveEvent(PERSON_ARRIVED);

		// get current Door on Floor
		Door currentFloorDoor = location.getDoor();

		// determine if Door on Floor is open
		try {

			boolean doorOpen = currentFloorDoor.isDoorOpen();

			// if Door on Floor is closed
			if (!doorOpen) {

				// press Floor Button
				sendPersonMoveEvent(PERSON_PRESSING_BUTTON);
				pauseThread(1000);

				// register for Floor Door's doorOpen event
				currentFloorDoor.addDoorListener(this);

				// press Floor's Button to request Elevator
				Button floorButton = getLocation().getButton();
				floorButton.pressButton(getLocation());

				// wait for Floor's Door to open
				sleep(TIME_WAITING);

				// unregister with Floor's Door if too long
				currentFloorDoor.removeDoorListener(this);
			}

			// if Door on Floor is open, ride Eelevator
			else
				enterAndRideElevator();
		}

		// handle exception when interrupted from waiting
		catch (InterruptedException interruptedException) {

			// Person unregisters for Floor's Door doorOpen event
			currentFloorDoor.removeDoorListener(this);

			// enter and ride Elevator when Door on Floor opens,
			pauseThread(1000);
			enterAndRideElevator();
		}

		// waiting for Elevator's Door to open takes a second
		pauseThread(1000);

		// begin walking away from Elevator
		setMoving(true);

		// Person exits Elevator
		sendPersonMoveEvent(PERSON_EXITING_ELEVATOR);

		// walking from elevator takes five seconds
		pauseThread(2 * TIME_TO_WALK);

		// Person exits simulation
		sendPersonMoveEvent(PERSON_EXITED);

	} // end method run

	// Person enters Elevator
	private void enterAndRideElevator() {
		// Person enters Elevator
		sendPersonMoveEvent(PERSON_ENTERING_ELEVATOR);

		// set Person Location to Elevator
		Floor floorLocation = (Floor) getLocation();
		setLocation(floorLocation.getElevatorShaft().getElevator());

		// Person takes one second to enter Elevator
		pauseThread(1000);

		// register for Elevator's Door's doorOpen event
		Door elevatorDoor = getLocation().getDoor();
		elevatorDoor.addDoorListener(this);

		// pressing Elevator Button takes one second
		sendPersonMoveEvent(PERSON_PRESSING_BUTTON);
		pauseThread(1000);

		// get Elevator's Button
		Button elevatorButton = getLocation().getButton();

		// press Elevator's Button
		elevatorButton.pressButton(location);

		// Door closing takes one second
		pauseThread(1000);

		// ride in Elevator
		Elevator elevator = (Elevator) getLocation();
		elevator.ride();

		// Person finished riding Elevator

		// unregister for Elevator's Door's doorOpen event
		elevatorDoor.removeDoorListener(this);

	} // end method enterAndRideElevator

	// pause thread for desired number of milliseconds
	private void pauseThread(int milliseconds) {
		try {
			sleep(milliseconds);
		}

		// handle exception if interrupted when paused
		catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	} // end method pauseThread

	// send PersonMoveEvent to listener, depending on event type
	private void sendPersonMoveEvent(int eventType) {
		// create new event
		PersonMoveEvent event = new PersonMoveEvent(this, getLocation(), getID());

		// send Event to this listener, depending on eventType
		switch (eventType) {

		// Person has been created
		case PERSON_CREATED:
			personMoveListener.personCreated(event);
			break;

		// Person arrived at Elevator
		case PERSON_ARRIVED:
			personMoveListener.personArrived(event);
			break;

		// Person entered Elevator
		case PERSON_ENTERING_ELEVATOR:
			personMoveListener.personEntered(event);
			break;

		// Person pressed Button object
		case PERSON_PRESSING_BUTTON:
			personMoveListener.personPressedButton(event);
			break;

		// Person exited Elevator
		case PERSON_EXITING_ELEVATOR:
			personMoveListener.personDeparted(event);
			break;

		// Person exited simulation
		case PERSON_EXITED:
			personMoveListener.personExited(event);
			break;

		default:
			break;
		}
	} // end method sendPersonMoveEvent
}

/**************************************************************************
 * (C) Copyright 2002 by Deitel & Associates, Inc. and Prentice Hall. * All
 * Rights Reserved. * * DISCLAIMER: The authors and publisher of this book have
 * used their * best efforts in preparing the book. These efforts include the *
 * development, research, and testing of the theories and programs * to
 * determine their effectiveness. The authors and publisher make * no warranty
 * of any kind, expressed or implied, with regard to these * programs or to the
 * documentation contained in these books. The authors * and publisher shall not
 * be liable in any event for incidental or * consequential damages in
 * connection with, or arising out of, the * furnishing, performance, or use of
 * these programs. *
 *************************************************************************/
