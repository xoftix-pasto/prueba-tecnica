// ElevatorModel.java
// Elevator simulation model with ElevatorShaft and two Floors
package model;

// Java core packages
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import controller.ElevatorConstants;
// Deitel packages
import event.BellEvent;
import event.BellListener;
import event.ButtonEvent;
import event.ButtonListener;
import event.DoorEvent;
import event.DoorListener;
import event.ElevatorModelListener;
import event.ElevatorMoveEvent;
import event.ElevatorMoveListener;
import event.LightEvent;
import event.LightListener;
import event.PersonMoveEvent;
import event.PersonMoveListener;

public class ElevatorModel implements ElevatorModelListener, ElevatorConstants {

	// declare two-Floor architecture in simulation
	private Floor firstFloor;
	private Floor secondFloor;

	// ElevatorShaft in simulation
	private ElevatorShaft elevatorShaft;

	// objects listening for events from ElevatorModel
	private Set<PersonMoveListener> personMoveListeners;
	private DoorListener doorListener;
	private ButtonListener buttonListener;
	private LightListener lightListener;
	private BellListener bellListener;
	private ElevatorMoveListener elevatorMoveListener;

	// cumulative number of people in simulation
	private int numberOfPeople = 0;

	// constructor instantiates ElevatorShaft and Floors
	public ElevatorModel() {
		// instantiate firstFloor and secondFloor objects
		firstFloor = new Floor(FIRST_FLOOR_NAME);
		secondFloor = new Floor(SECOND_FLOOR_NAME);

		// instantiate ElevatorShaft object
		elevatorShaft = new ElevatorShaft(firstFloor, secondFloor);

		// give elevatorShaft reference to first and second Floor
		firstFloor.setElevatorShaft(elevatorShaft);
		secondFloor.setElevatorShaft(elevatorShaft);

		// register for events from ElevatorShaft
		elevatorShaft.setDoorListener(this);
		elevatorShaft.setButtonListener(this);
		elevatorShaft.addElevatorMoveListener(this);
		elevatorShaft.setLightListener(this);
		elevatorShaft.setBellListener(this);

		// instantiate Set for ElevatorMoveListener objects
		personMoveListeners = new HashSet<PersonMoveListener>(1);

	} // end ElevatorModel constructor

	// return Floor with given name
	private Floor getFloor(String name) {
		if (name.equals(FIRST_FLOOR_NAME))
			return firstFloor;
		else

		if (name.equals(SECOND_FLOOR_NAME))
			return secondFloor;
		else
			return null;

	} // end method getFloor

	// add Person to Elevator Simulator
	public void placePersonOnFloor(String floorName) {
		// instantiate new Person and place on Floor
		Person person = new Person(numberOfPeople, getFloor(floorName));
		person.setName(Integer.toString(numberOfPeople));

		// register listener for Person events
		person.setPersonMoveListener(this);

		// start Person thread
		person.start();

		// increment number of Person objects in simulation
		numberOfPeople++;

	} // end method placePersonOnFloor

	// invoked when Elevator has departed from Floor
	public void elevatorDeparted(ElevatorMoveEvent moveEvent) {
		elevatorMoveListener.elevatorDeparted(moveEvent);
	}

	// invoked when Elevator has arrived at destination Floor
	public void elevatorArrived(ElevatorMoveEvent moveEvent) {
		elevatorMoveListener.elevatorArrived(moveEvent);
	}

	// send PersonMoveEvent to listener, depending on event type
	private void sendPersonMoveEvent(int eventType, PersonMoveEvent event) {
		Iterator<PersonMoveListener> iterator = personMoveListeners.iterator();

		while (iterator.hasNext()) {

			PersonMoveListener listener = (PersonMoveListener) iterator.next();

			// send Event to this listener, depending on eventType
			switch (eventType) {

			// Person has been created
			case Person.PERSON_CREATED:
				listener.personCreated(event);
				break;

			// Person arrived at Elevator
			case Person.PERSON_ARRIVED:
				listener.personArrived(event);
				break;

			// Person entered Elevator
			case Person.PERSON_ENTERING_ELEVATOR:
				listener.personEntered(event);
				break;

			// Person pressed Button object
			case Person.PERSON_PRESSING_BUTTON:
				listener.personPressedButton(event);
				break;

			// Person exited Elevator
			case Person.PERSON_EXITING_ELEVATOR:
				listener.personDeparted(event);
				break;

			// Person exited simulation
			case Person.PERSON_EXITED:
				listener.personExited(event);
				break;

			default:
				break;
			}
		}
	} // end method sendPersonMoveEvent

	// invoked when Person has been created in model
	public void personCreated(PersonMoveEvent moveEvent) {
		sendPersonMoveEvent(Person.PERSON_CREATED, moveEvent);
	}

	// invoked when Person has arrived at Floor's Button
	public void personArrived(PersonMoveEvent moveEvent) {
		sendPersonMoveEvent(Person.PERSON_ARRIVED, moveEvent);
	}

	// invoked when Person has pressed Button
	public void personPressedButton(PersonMoveEvent moveEvent) {
		sendPersonMoveEvent(Person.PERSON_PRESSING_BUTTON, moveEvent);
	}

	// invoked when Person has entered Elevator
	public void personEntered(PersonMoveEvent moveEvent) {
		sendPersonMoveEvent(Person.PERSON_ENTERING_ELEVATOR, moveEvent);
	}

	// invoked when Person has departed from Elevator
	public void personDeparted(PersonMoveEvent moveEvent) {
		sendPersonMoveEvent(Person.PERSON_EXITING_ELEVATOR, moveEvent);
	}

	// invoked when Person has exited Simulation
	public void personExited(PersonMoveEvent moveEvent) {
		sendPersonMoveEvent(Person.PERSON_EXITED, moveEvent);
	}

	// invoked when Door has opened
	public void doorOpened(DoorEvent doorEvent) {
		doorListener.doorOpened(doorEvent);
	}

	// invoked when Door has closed
	public void doorClosed(DoorEvent doorEvent) {
		doorListener.doorClosed(doorEvent);
	}

	// invoked when Button has been pressed
	public void buttonPressed(ButtonEvent buttonEvent) {
		buttonListener.buttonPressed(buttonEvent);
	}

	// invoked when Button has been reset
	public void buttonReset(ButtonEvent buttonEvent) {
		buttonListener.buttonReset(buttonEvent);
	}

	// invoked when Bell has rung
	public void bellRang(BellEvent bellEvent) {
		bellListener.bellRang(bellEvent);
	}

	// invoked when Light has turned on
	public void lightTurnedOn(LightEvent lightEvent) {
		lightListener.lightTurnedOn(lightEvent);
	}

	// invoked when Light has turned off
	public void lightTurnedOff(LightEvent lightEvent) {
		lightListener.lightTurnedOff(lightEvent);
	}

	// set listener for ElevatorModelListener
	public void setElevatorModelListener(ElevatorModelListener listener) {
		// ElevatorModelListener extends all interfaces below
		addPersonMoveListener(listener);
		setElevatorMoveListener(listener);
		setDoorListener(listener);
		setButtonListener(listener);
		setLightListener(listener);
		setBellListener(listener);
	}

	// set listener for PersonMoveEvents
	public void addPersonMoveListener(PersonMoveListener listener) {
		personMoveListeners.add(listener);
	}

	// set listener for DoorEvents
	public void setDoorListener(DoorListener listener) {
		doorListener = listener;
	}

	// set listener for ButtonEvents
	public void setButtonListener(ButtonListener listener) {
		buttonListener = listener;
	}

	// add listener for ElevatorMoveEvents
	public void setElevatorMoveListener(ElevatorMoveListener listener) {
		elevatorMoveListener = listener;
	}

	// set listener for LightEvents
	public void setLightListener(LightListener listener) {
		lightListener = listener;
	}

	// set listener for BellEvents
	public void setBellListener(BellListener listener) {
		bellListener = listener;
	}
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
